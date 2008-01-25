//
//  PostImage.m
//  CLManager
//
//  Created by Deuce on 1/21/08.
//  Copyright 2008 __MyCompanyName__. All rights reserved.
//

#import "PostImage.h"


@implementation PostImage

- (void)fetchData {
	NSAutoreleasePool *pool =  [[NSAutoreleasePool alloc] init];
	NSURL* url = [NSURL URLWithString:[self valueForKey:@"url"]];
	NSData* data = [NSData dataWithContentsOfURL:url];
	[self setValue:data forKey:@"data"];
	[[self managedObjectContext] save:nil];
	[pool release];
}

- (NSData*)dataValue {
	NSData* value = [self valueForKey:@"data"];
	if (value == nil) {
		[self fetchData];
		value = [self valueForKey:@"data"];
	}
	return value;
}
@end
