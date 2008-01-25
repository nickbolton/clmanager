//
//  Post.m
//  CLManager
//
//  Created by Deuce on 1/20/08.
//  Copyright 2008 __MyCompanyName__. All rights reserved.
//

#import "Post.h"
#import "PostImage.h"

@implementation Post
- (id) init
{
	self = [super init];
	if (self != nil) {
		//NSLog(@"Post: %@", self);
	}
	return self;
}

- (NSString*)favoriteValue {
	/*CFNumberRef rawValue = [self valueForKey:@"favorite"];
	int val;
	CFNumberGetValue(rawValue, kCFNumberIntType, &val);
	switch (val) {
		case 0: return @"";
		case 1: return @"F";
	}
	NSLog(@"invalid favorite value for entity(%@): %d", [self valueForKey:@"id"], val);
	*/
	return @"";

}

- (NSString*)flaggedValue {
	CFNumberRef rawValue = [self valueForKey:@"flagged"];
	int val;
	CFNumberGetValue(rawValue, kCFNumberIntType, &val);
	switch (val) {
		case 0: return @"";
		case 1: return @"F";
	}
	NSLog(@"invalid flagged value for entity(%@): %d", [self valueForKey:@"id"], val);
	return @"";
}

- (NSString*)respondedValue {
	CFNumberRef rawValue = [self valueForKey:@"responded"];
	int val;
	CFNumberGetValue(rawValue, kCFNumberIntType, &val);
	switch (val) {
		case 0: return @"";
		case 1: return @"R";
	}
	NSLog(@"invalid responded value for entity(%@): %d", [self valueForKey:@"id"], val);
	return @"";
}

- (NSString*)picValue {
	CFNumberRef rawValue = [self valueForKey:@"pic"];
	int val;
	CFNumberGetValue(rawValue, kCFNumberIntType, &val);
	switch (val) {
		case 0: return @"";
		case 1: return @"P";
	}
	NSLog(@"invalid pic value for entity(%@): %d", [self valueForKey:@"id"], val);
	return @"";

}

- (NSString*)replaceHtmlEntities:(NSString*)s {
	NSMutableString* result = [NSMutableString stringWithString:s];

	int i;
	for (i=32; i<127; i++) {
		NSString* source = [NSString stringWithFormat:@"&#%d;", i];
		NSString* target = [NSString stringWithFormat:@"%c", i];
		[result replaceOccurrencesOfString:source withString:target options:NSLiteralSearch range:NSMakeRange(0, [result length])];
	}
	[result replaceOccurrencesOfString:@"&quot;" withString:@"\"" options:NSLiteralSearch range:NSMakeRange(0, [result length])];
	[result replaceOccurrencesOfString:@"&amp;" withString:@"&" options:NSLiteralSearch range:NSMakeRange(0, [result length])];
	[result replaceOccurrencesOfString:@"&lt;" withString:@"<" options:NSLiteralSearch range:NSMakeRange(0, [result length])];
	[result replaceOccurrencesOfString:@"&gt;" withString:@">" options:NSLiteralSearch range: NSMakeRange(0, [result length])];

	return result;
}

- (NSString*)fetchPostContent {
	NSURL* url = [NSURL URLWithString:[self valueForKey:@"url"]];
	NSString* postContent = [NSString stringWithContentsOfURL:url];
	return postContent;
}

- (void)checkForFlagged:(NSString*)postContent {
	if ([postContent rangeOfString:@"The title on the listings page will be removed in just a few minutes"].location != NSNotFound) {
		[self setValue:[NSNumber numberWithBool:YES] forKey:@"flagged"];
		[[self managedObjectContext] save:nil];
	}
}

- (NSString*)fetchContent {
	NSString* postContent = [self fetchPostContent];
	if ([postContent rangeOfString:@"The title on the listings page will be removed in just a few minutes"].location != NSNotFound) {
		[self setValue:[NSNumber numberWithBool:YES] forKey:@"flagged"];
		[self setValue:@"" forKey:@"content"];
		[[self managedObjectContext] save:nil];
		return postContent;
	}
	
	NSString* replyAddress = nil;
	NSCharacterSet* emailStopCharacters = [NSCharacterSet characterSetWithCharactersInString:@"?\""];
	NSString* junk;
	NSScanner* scanner = [NSScanner scannerWithString:postContent];
	[scanner scanUpToString:@"mailto:" intoString:&junk];
	[scanner scanString:@"mailto:" intoString:&junk];
	[scanner scanUpToCharactersFromSet:emailStopCharacters intoString:&replyAddress];
	replyAddress = [self replaceHtmlEntities:replyAddress];
	[self setValue:replyAddress forKey:@"replyAddress"];
	
	NSString* content = nil;
	[scanner scanUpToString:@"<br>\n<br>\n<br>\n" intoString:&junk];
	[scanner scanString:@"<br>\n<br>\n<br>\n" intoString:&junk];
	[scanner scanUpToString:@"<table" intoString:&content];
	if (content != nil) {
		content = [content stringByReplacingOccurrencesOfString:@"<br>" withString:@"\n"];
	}
	[self setValue:content forKey:@"content"];
	
	NSString* imageUrl = nil;
	NSCharacterSet* doubleQuote = [NSCharacterSet characterSetWithCharactersInString:@"\""];
	NSMutableSet* imageSet = [self valueForKey:@"images"];
	NSEntityDescription* imageEntity = [NSEntityDescription entityForName:@"Image" inManagedObjectContext:[self managedObjectContext]];
	
	
	while ([scanner scanUpToString:@"<img src=" intoString:&imageUrl]) {
		if (![scanner scanString:@"<img src=\"" intoString:&imageUrl]) break;
		if (![scanner scanUpToCharactersFromSet:doubleQuote intoString:&imageUrl]) break;
		
		NSManagedObject* image = [[NSManagedObject alloc] initWithEntity:imageEntity
			insertIntoManagedObjectContext:[self managedObjectContext]];
		[image setValue:imageUrl forKey:@"url"];
		[image setValue:self forKey:@"post"];
        BOOL add = YES;
        for (PostImage* pi in imageSet) {
            add = [imageUrl compare:[pi valueForKey:@"url"]] != NSOrderedSame;
            if (!add) break;
        }
        if (add) {
            [imageSet addObject:image];
        }
	}

	[[self managedObjectContext] save:nil];
	return postContent;
}

- (id)valueForKey:(NSString *)key {
	NSAutoreleasePool *pool =  [[NSAutoreleasePool alloc] init];	
	id value = [super valueForKey:key];
	if ([key compare:@"content"] == NSOrderedSame) {
		NSString* postContent = nil;
		if (value == nil) {
			postContent = [self fetchContent];
		}
		
		if (![[super valueForKey:@"flagged"] boolValue]) {
			if (postContent == nil) {
				postContent = [self fetchPostContent];
			}
			[self checkForFlagged:postContent];
		}
		NSString* content = [super valueForKey:key];
		[pool release];
		return content;
		//return [[[NSAttributedString alloc] initWithString:[super valueForKey:key]
		//		attributes: [NSDictionary dictionaryWithObject: [NSFont systemFontOfSize: 16.0] forKey: NSFontAttributeName]] autorelease];
		
		
	} else if ([key compare:@"title"] == NSOrderedSame) {
		if ([[super valueForKey:@"viewed"] boolValue]) {
			[pool release];
			return [[[NSAttributedString alloc] initWithString:value
				attributes: [NSDictionary dictionaryWithObject: [NSFont systemFontOfSize: 13.0] forKey: NSFontAttributeName]] autorelease];
		}
		[pool release];
		return [[[NSAttributedString alloc] initWithString:value
			attributes: [NSDictionary dictionaryWithObject: [NSFont boldSystemFontOfSize: 13.0] forKey: NSFontAttributeName]] autorelease];
	}
	[pool release];
	return value;
}

@end

