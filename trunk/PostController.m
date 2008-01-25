//
//  PostController.m
//  CLManager
//
//  Created by Deuce on 1/13/08.
//  Copyright 2008 __MyCompanyName__. All rights reserved.
//

#import "PostController.h"
#import "FavoriteValueTransformer.h"
#import "RespondedValueTransformer.h"
#import "PicValueTransformer.h"
#import "Post.h"
#import "PostImage.h"

@implementation PostController

- (NSDictionary*)searchValueDefaults:(NSString*)key {
	NSUserDefaults* userDefaults = [NSUserDefaults standardUserDefaults];
	NSDictionary* categorySearchValues = [userDefaults objectForKey:@"categorySearchValues"];
	return [categorySearchValues objectForKey:key];
}

- (NSURL*)buildSearchQuery:(NSString*)cityUrl categoryName:(NSString*)categoryName query:(NSString*)query {
	if (query == nil || [query compare:@""] == NSOrderedSame) {
		query = @"+";
	}
	return [NSURL URLWithString:[NSString stringWithFormat:@"%@/search/%@/?query=%@&srchType=T&minAsk=min&maxAsk=max", cityUrl, categoryName, query]];
}

- (NSManagedObject*)getCurrentCity {
	NSUserDefaults* defaults = [NSUserDefaults standardUserDefaults];
	int selectedIndex = [defaults integerForKey:@"currentCity"];
	NSManagedObject* city = nil;
	NSArray* arr = [cities content];
	if (selectedIndex >= 0 && arr != nil && [arr count] > 0) {
		city = [arr objectAtIndex:selectedIndex];
	}
	return city;
}

- (NSManagedObject*)getCurrentCategory {
	NSUserDefaults* defaults = [NSUserDefaults standardUserDefaults];
	int selectedIndex = [defaults integerForKey:@"currentCategory"];
	NSManagedObject* category = nil;
	NSArray* arr = [categories content];
	if (selectedIndex >= 0 && arr != nil && [arr count] > 0) {
		category = [arr objectAtIndex:selectedIndex];
	}
	return category;
}

- (NSString*)searchValueDefaultsKey {
	NSManagedObject* city = [self getCurrentCity];
	NSManagedObject* category = [self getCurrentCategory];
	return [NSString stringWithFormat:@"%@-%@", [city valueForKey:@"name"], [category valueForKey:@"name"]];
}

- (void)updateSearchValueDefaults {
	NSString* key = [self searchValueDefaultsKey];
	NSDictionary* dict = [NSDictionary dictionaryWithObjectsAndKeys:
		searchValue, @"searchValue",
		[NSString stringWithFormat:@"%d", minAsk], @"minAsk",
		[NSString stringWithFormat:@"%d", maxAsk], @"maxAsk",
		[NSString stringWithFormat:@"%d", imagesOnly], @"imagesOnly",
		[NSString stringWithFormat:@"%d", noFlagged], @"noFlagged",
		nil];
	NSUserDefaults* userDefaults = [NSUserDefaults standardUserDefaults];
	NSMutableDictionary* categorySearchValues = [userDefaults objectForKey:@"categorySearchValues"];
	if (categorySearchValues == nil) {
		categorySearchValues = [NSDictionary dictionaryWithObjectsAndKeys:dict, key, nil];
	} else {
		categorySearchValues = [NSMutableDictionary dictionaryWithDictionary:categorySearchValues];
		[categorySearchValues setObject:dict forKey:key];
	}
	[userDefaults setObject:categorySearchValues forKey:@"categorySearchValues"];
	[userDefaults synchronize];
}

- (Post*)fetchPost:(NSString*)postId {
    NSPredicate * predicate;
    predicate = [NSPredicate predicateWithFormat:@"id = %@", postId];
	
    NSFetchRequest * fetch = [[NSFetchRequest alloc] init];
    [fetch setEntity: postEntity];
    [fetch setPredicate: predicate];
	NSArray* result = [postContext executeFetchRequest:fetch error:nil];
	if ([result count] > 1) {
		NSLog(@"Multiple posts exists with ID: %@", postId);
	}
	if ([result count] > 0) {
		return [result objectAtIndex:0];
	}
	return nil;
}

- (void)updatePosts:(NSTimer*)theTimer {
	NSAutoreleasePool *pool =  [[NSAutoreleasePool alloc] init];
	NSError *err=nil;
		
	for (NSManagedObject* city in [cities arrangedObjects]) {
		for (NSManagedObject* category in [categories arrangedObjects]) {
			NSString* categoryName = [category valueForKey:@"name"];
			NSString* query = [category valueForKey:@"query"];
			
			NSURL* url = [self buildSearchQuery:[city valueForKey:@"url"] categoryName:categoryName query:query];
			NSLog(@"search url: %@", url);
			NSXMLDocument* doc = [[NSXMLDocument alloc] initWithContentsOfURL:url options:NSXMLDocumentTidyXML error:&err];
			NSArray* nodes = [doc nodesForXPath:postBodyXPath error:&err];
			int nodeCount = [nodes count];
			if (nodeCount > 0) {
				NSDate* date = nil;
				int i;
				BOOL cont = YES;
				for (i = 0; cont && i < nodeCount; i++) {
					NSXMLElement* p = (NSXMLElement*)[nodes objectAtIndex:i];

					if ([[p elementsForName:@"a"] count] <= 0) {
							cont = NO;
							continue;
					}

					NSArray* dateAttributes = [[p stringValue] componentsSeparatedByString:@" "];
					NSString* month = [monthDict objectForKey:[dateAttributes objectAtIndex:0]];
					int monthInt = [month intValue];
					NSString* day = [dateAttributes objectAtIndex:1];
					NSCalendar* cal = [NSCalendar currentCalendar];
					NSInteger year = [[cal components:NSYearCalendarUnit fromDate:[NSDate date]] year];
					NSInteger todayMonth = [[cal components:NSMonthCalendarUnit fromDate:[NSDate date]] month];

					if (monthInt == 12 && monthInt != todayMonth) {
							year--;
					}

					date = [NSDate dateWithString:[NSString stringWithFormat:@"%d-%@-%@ 00:00:00 +0000", year, month, day]];

					NSXMLElement* a = [[p elementsForName:@"a"] objectAtIndex:0];
					NSXMLElement* font = nil;
					if ([[p elementsForName:@"font"] count] > 0) {
						font = [[p elementsForName:@"font"] objectAtIndex:0];
					}

					NSString* href = [[a attributeForName:@"href"] stringValue];
					NSString* ident = [[[[href componentsSeparatedByString:@"/"] objectAtIndex:2] componentsSeparatedByString:@".html"] objectAtIndex:0];

					NSString* loc = nil;
					if (font != nil) {
						loc = [[font stringValue] stringByTrimmingCharactersInSet:[NSCharacterSet whitespaceCharacterSet]];
					} else {
						loc = @"";
					}
					NSString* age = @"";
					NSString* title = [a stringValue];
					NSArray* components = [title componentsSeparatedByString:@" - "];
					if ([components count] > 0) {
						NSString* potentialAge = [components objectAtIndex:([components count]-1)];
						if ([potentialAge intValue] > 0) {
							age = potentialAge;
							int ageSepLoc = [title rangeOfString:[NSString stringWithFormat:@" - %@", age]].location;
							if (ageSepLoc != NSNotFound) {
								title = [title substringToIndex:ageSepLoc];
							}
						}
					} 
								NSXMLElement* pic = nil;
					BOOL hasPic = NO;
					if ([[p elementsForName:@"span"] count] > 0) {
						pic = [[p elementsForName:@"span"] objectAtIndex:0];
						if (pic != nil) {
							hasPic = [[[pic stringValue] stringByTrimmingCharactersInSet:[NSCharacterSet whitespaceCharacterSet]] compare:@"pic"] == NSOrderedSame;
						}
					}
					
					Post* post = [self fetchPost:ident];
					if (post == nil) {
						post = [[Post alloc] initWithEntity:postEntity insertIntoManagedObjectContext:postContext];
						[post setValue:ident forKey:@"id"];
						NSLog(@"setting date: %@", date);
						[post setValue:date forKey:@"date"];
						[post setValue:title forKey:@"title"];
						[post setValue:age forKey:@"age"];
						[post setValue:[NSNumber numberWithBool:NO] forKey:@"responded"];
						[post setValue:loc forKey:@"location"];
						[post setValue:[NSString stringWithFormat:@"%@/%@", [city valueForKey:@"url"], href] forKey:@"url"];
						[post setValue:[NSNumber numberWithBool:NO] forKey:@"flagged"];
						[post setValue:[NSNumber numberWithBool:hasPic] forKey:@"pic"];
						[post setValue:city forKey:@"city"];
						[post setValue:category forKey:@"category"];
						NSMutableSet* postSet = [city valueForKey:@"posts"];
						[postSet addObject:post];
						postSet = [category valueForKey:@"posts"];
						[postSet addObject:post];
						[post valueForKey:@"content"];
						NSSet* images = [post valueForKey:@"images"];
						for (PostImage* image in images) {
							[image dataValue];
						}
					} else if (![[post valueForKey:@"flagged"] boolValue]) {
                        //NSLog(@"fetchContent");
                        //[post fetchContent];
                    }
				}
			}
		}
	}
	[postContext save:nil];
			
	[pool release];
}

- (void)setFilterValues {
	NSDictionary* searchValues = [self searchValueDefaults:[self searchValueDefaultsKey]];
	if (searchValues == nil) {
		minAsk = 0;
		maxAsk = 99;
		searchValue = @"";
		imagesOnly = NO;
        noFlagged = NO;
	} else {
		minAsk = [[searchValues objectForKey:@"minAsk"] intValue];
		maxAsk = [[searchValues objectForKey:@"maxAsk"] intValue];
		imagesOnly = [[searchValues objectForKey:@"imagesOnly"] boolValue];
        noFlagged = [[searchValues objectForKey:@"noFlagged"] boolValue];
		searchValue = [searchValues objectForKey:@"searchValue"];
		if (searchValue == nil) {
			searchValue = @"";
		}
	}

	[minAskField setStringValue:[NSString stringWithFormat:@"%d", minAsk]];
	[minAskStepper setIntValue:minAsk];
	[maxAskField setStringValue:[NSString stringWithFormat:@"%d", maxAsk]];
	[maxAskStepper setIntValue:maxAsk];
	[imagesOnlyCheckbox setState:imagesOnly];
    [noFlaggedCheckbox setState:noFlagged];
}

- (void)tableViewSelectionDidChange:(NSNotification *)aNotification {
	int selectedIndex = [postTable selectedRow];
	if (selectedIndex >= 0) {
		
		Post* post = [[postController arrangedObjects] objectAtIndex:selectedIndex];
		NSString* content = [post valueForKey:@"content"];
		[postContent setEditable:YES];
		[postContent setString:content];
		[postContent setEditable:NO];
		[post setValue:[NSNumber numberWithBool:YES] forKey:@"viewed"];
		
		NSData* data = [self postImageData1];
		NSImage* image = [[NSImage alloc] initWithData:data];
		[image autorelease];
		[postImage1 setImage:image];
		
		data = [self postImageData2];
		image = [[NSImage alloc] initWithData:data];
		[image autorelease];
		[postImage2 setImage:image];
		
		data = [self postImageData3];
		image = [[NSImage alloc] initWithData:data];
		[image autorelease];
		[postImage3 setImage:image];
		
		data = [self postImageData4];
		image = [[NSImage alloc] initWithData:data];
		[image autorelease];
		[postImage4 setImage:image];
		
		data = [self postImageData5];
		image = [[NSImage alloc] initWithData:data];
		[image autorelease];
		[postImage5 setImage:image];
		
		data = [self postImageData6];
		image = [[NSImage alloc] initWithData:data];
		[image autorelease];
		[postImage6 setImage:image];
		
		data = [self postImageData7];
		image = [[NSImage alloc] initWithData:data];
		[image autorelease];
		[postImage7 setImage:image];
		
		data = [self postImageData8];
		image = [[NSImage alloc] initWithData:data];
		[image autorelease];
		[postImage8 setImage:image];
	}
}

- (void)updatePostsFilter {
    NSManagedObject* city = [self getCurrentCity];
    NSManagedObject* category = [self getCurrentCategory];
    NSMutableString* ps = [NSMutableString stringWithFormat:@"city.name = '%@' and category.name = '%@'", [city valueForKey:@"name"], [category valueForKey:@"name"]];
	
	if (searchValue != nil && [searchValue length] > 0) {
		[ps appendFormat:@" and title.string like[cd] '%@'", searchValue];
	}
	if (minAsk > 18) {
		[ps appendFormat:@" and age >= '%d'", minAsk];
	}
	if (maxAsk < 99) {
		[ps appendFormat:@" and age =< '%d'", maxAsk];
	}
	if (imagesOnly) {
		[ps appendFormat:@" and pic = true"];
	}
    if (noFlagged) {
		[ps appendFormat:@" and flagged = false"];
    }
    NSLog(@"ps: %@", ps);
    NSPredicate* predicate = [NSPredicate predicateWithFormat:ps];
    [postController setFilterPredicate:predicate];
}

- (void)awakeFromNib {
	NSError *error;
	postContext = [postController managedObjectContext];
	postEntity = [NSEntityDescription entityForName:@"Post" inManagedObjectContext:postContext];
	[postContext retain];
	[postEntity retain];
	
	[cities fetchWithRequest:nil merge:NO error:&error];
	[categories fetchWithRequest:nil merge:NO error:&error];
	[self setFilterValues];
	[self updatePostsFilter];
	[self updatePosts:nil];
	
	//[postController fetchWithRequest:nil merge:NO error:&error];
	[postTable selectRow:0 byExtendingSelection:NO];
	
	updatePostsTimer = [NSTimer scheduledTimerWithTimeInterval:10 target:self selector:@selector(updatePosts:) userInfo:nil repeats:YES];
    [updatePostsTimer fire];
}

- (IBAction)searchValueAction:(id)sender {
	[self setSearchValue:[sender stringValue]];
	[self updatePostsFilter];
	[self updateSearchValueDefaults];
}

- (IBAction)minAskAction:(id)sender {
	[self setMinAsk:[sender intValue]];
	[minAskStepper setIntValue:minAsk];
	[self updatePostsFilter];
	[self updateSearchValueDefaults];
}

- (IBAction)maxAskAction:(id)sender {
	[self setMaxAsk:[sender intValue]];
	[maxAskStepper setIntValue:maxAsk];	[self updatePostsFilter];
	[self updatePostsFilter];
	[self updateSearchValueDefaults];
}

- (IBAction)minAskStepperAction:(id)sender {
	[self setMinAsk:[sender intValue]];
	[minAskField setIntValue:minAsk];
	[self updatePostsFilter];
	[self updateSearchValueDefaults];
}

- (IBAction)maxAskStepperAction:(id)sender {
	[self setMaxAsk:[sender intValue]];
	[maxAskField setIntValue:maxAsk];
	[self updatePostsFilter];
	[self updateSearchValueDefaults];
}

- (IBAction)imagesOnlyAction:(id)sender {
	[self setImagesOnly:[sender state]];
	[self updatePostsFilter];
	[self updateSearchValueDefaults];
}

- (IBAction)noFlaggedAction:(id)sender {
    [self setNoFlagged:[sender state]];
	[self updatePostsFilter];
	[self updateSearchValueDefaults];
}

- (NSString*)searchValue {
	return searchValue;
}

- (void)setSearchValue:(NSString*)s {
	[s retain];
	[searchValue release];
	searchValue = s;
}

- (int)minAsk {
	return minAsk;
}

- (void)setMinAsk:(int)i {
	minAsk = i;
}

- (int)maxAsk {
	return maxAsk;
}

- (void)setMaxAsk:(int)i {
	maxAsk = i;
}

- (BOOL)imagesOnly {
	return imagesOnly;
}

- (void)setImagesOnly:(BOOL)b {
	imagesOnly = b;
}

- (BOOL)noFlagged {
    return noFlagged;
}

- (void)setNoFlagged:(BOOL)b {
    noFlagged = b;
}

- (IBAction)citySelected:(id)sender {
	[self updatePosts:nil];
	[self setFilterValues];
	[self updatePostsFilter];
}

- (IBAction)categorySelected:(id)sender {
	[self updatePosts:nil];
	[self setFilterValues];
	[self updatePostsFilter];
}

- (PostImage*)lookupImage:(NSSet*)set index:(int)index {
	int i=0;
	for (PostImage* image in set) {
		if (i++==index) return image;
	}
	return nil;
}

- (NSData*)postImageData:(int)index {
	int selectedIndex = [postTable selectedRow];
	if (selectedIndex < 0) return nil;
	
	Post* post = [[postController arrangedObjects] objectAtIndex:selectedIndex];
	if (post == nil) return nil;
	
	NSSet* images = [post valueForKey:@"images"];
	
	if ([images count] < 1) return nil;

	PostImage* image = [self lookupImage:images index:index];
	if (image == nil) return nil;
	
	return [image dataValue];
}

- (NSData*)postImageData1 {
	return [self postImageData:0];
}

- (NSData*)postImageData2 {
	return [self postImageData:1];
}

- (NSData*)postImageData3 {
	return [self postImageData:2];
}

- (NSData*)postImageData4 {
	return [self postImageData:3];
}

- (NSData*)postImageData5 {
	return [self postImageData:4];
}

- (NSData*)postImageData6 {
	return [self postImageData:5];
}

- (NSData*)postImageData7 {
	return [self postImageData:6];
}

- (NSData*)postImageData8 {
	return [self postImageData:7];
}

- (NSArray*)citySortDescriptors {
	return citySortDescriptors;
}

- (NSArray*)categorySortDescriptors {
	return categorySortDescriptors;
}

- (NSArray*)postSortDescriptors {
	return postSortDescriptors;
}

- (id) init
{
	self = [super init];
	if (self != nil) {
		NSSortDescriptor* nameSorter = [[NSSortDescriptor alloc] initWithKey:@"name" ascending:YES];
		citySortDescriptors = [NSArray arrayWithObject:nameSorter];
		[citySortDescriptors retain];
		categorySortDescriptors = [NSArray arrayWithObject:nameSorter];
		[categorySortDescriptors retain];
		
		NSSortDescriptor* postSorter = [[NSSortDescriptor alloc] initWithKey:@"id" ascending:NO];
		postSortDescriptors = [NSArray arrayWithObject:postSorter];
		
		postBodyXPath = [[NSUserDefaults standardUserDefaults] objectForKey:@"postBodyXPath"];
        if (postBodyXPath == nil) {
            postBodyXPath = @"//p";
        }
		
		monthDict = [[NSMutableDictionary alloc] init];
        [monthDict setObject:@"01" forKey:@"Jan"];
        [monthDict setObject:@"02" forKey:@"Feb"];
        [monthDict setObject:@"03" forKey:@"Mar"];
        [monthDict setObject:@"04" forKey:@"Apr"];
        [monthDict setObject:@"05" forKey:@"May"];
        [monthDict setObject:@"06" forKey:@"Jun"];
        [monthDict setObject:@"07" forKey:@"Jul"];
        [monthDict setObject:@"08" forKey:@"Aug"];
        [monthDict setObject:@"09" forKey:@"Sep"];
        [monthDict setObject:@"10" forKey:@"Oct"];
        [monthDict setObject:@"11" forKey:@"Nov"];
        [monthDict setObject:@"12" forKey:@"Dec"];
	}
	return self;
}


- (void) dealloc
{
	[citySortDescriptors release];
	citySortDescriptors = nil;
	[categorySortDescriptors release];
	categorySortDescriptors = nil;
	[postSortDescriptors release];
	postSortDescriptors = nil;
	[postBodyXPath release];
	postBodyXPath = nil;
	[monthDict release];
	monthDict = nil;
	[postEntity release];
	postEntity = nil;
	[postContext release];
	postContext = nil;
	[updatePostsTimer release];
    updatePostsTimer = nil;
	[super dealloc];
}

@end
