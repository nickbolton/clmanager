//
//  PostController.h
//  CLManager
//
//  Created by Deuce on 1/13/08.
//  Copyright 2008 __MyCompanyName__. All rights reserved.
//

#import <Cocoa/Cocoa.h>


@interface PostController : NSObject {
	IBOutlet NSArrayController* cities;
	IBOutlet NSArrayController* categories;
	IBOutlet NSArrayController* postController;
	IBOutlet NSPopUpButton* cityPopup;
	IBOutlet NSTableView* postTable;
	IBOutlet NSTextView* postContent;
	
	IBOutlet id searchField;
	IBOutlet id minAskField;
	IBOutlet id minAskStepper;
	IBOutlet id maxAskField;
	IBOutlet id maxAskStepper;
	IBOutlet id imagesOnlyCheckbox;
    IBOutlet id noFlaggedCheckbox;
	
	IBOutlet NSImageView* postImage1;
	IBOutlet NSImageView* postImage2;
	IBOutlet NSImageView* postImage3;
	IBOutlet NSImageView* postImage4;
	IBOutlet NSImageView* postImage5;
	IBOutlet NSImageView* postImage6;
	IBOutlet NSImageView* postImage7;
	IBOutlet NSImageView* postImage8;
	
	NSArray* citySortDescriptors;
	NSArray* categorySortDescriptors;
	NSArray* postSortDescriptors;
	
	NSManagedObjectContext* postContext;
	NSEntityDescription* postEntity;
	NSString* postBodyXPath;
	NSMutableDictionary* monthDict;
	NSString* searchValue;
	int minAsk;
	int maxAsk;
	BOOL imagesOnly;
    BOOL noFlagged;
	
	NSTimer* updatePostsTimer;
}

- (IBAction)citySelected:(id)sender;
- (IBAction)categorySelected:(id)sender;

- (NSArray*)citySortDescriptors;
- (NSArray*)categorySortDescriptors;
- (NSArray*)postSortDescriptors;

- (IBAction)searchValueAction:(id)sender;
- (IBAction)minAskAction:(id)sender;
- (IBAction)maxAskAction:(id)sender;
- (IBAction)imagesOnlyAction:(id)sender;
- (IBAction)minAskStepperAction:(id)sender;
- (IBAction)maxAskStepperAction:(id)sender;
- (IBAction)noFlaggedAction:(id)sender;

- (NSString*)searchValue;
- (void)setSearchValue:(NSString*)s;
- (int)minAsk;
- (void)setMinAsk:(int)i;
- (int)maxAsk;
- (void)setMaxAsk:(int)i;
- (BOOL)imagesOnly;
- (void)setImagesOnly:(BOOL)b;
- (BOOL)noFlagged;
- (void)setNoFlagged:(BOOL)b;

- (NSData*)postImageData1;
- (NSData*)postImageData2;
- (NSData*)postImageData3;
- (NSData*)postImageData4;
- (NSData*)postImageData5;
- (NSData*)postImageData6;
- (NSData*)postImageData7;
- (NSData*)postImageData8;

@end
