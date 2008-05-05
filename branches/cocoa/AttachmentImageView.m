//
//  MyImageCell.m
//  CLManager
//
//  Created by Deuce on 12/29/07.
//  Copyright 2007 __MyCompanyName__. All rights reserved.
//

#import "AttachmentImageView.h"
#import "MessageTemplateImage.h"

@implementation AttachmentImageView

-(void)mouseDown:(NSEvent *)theEvent {
	[super mouseDown:theEvent];
	BOOL doubleClick = ([theEvent clickCount] == 2);
	if (doubleClick) {
		NSLog(@"AttachmentImageView double click");
		NSView* view = [self superview];
		view = [view superview];
		NSArrayController* messageTemplates = [view messageTemplates];
		NSManagedObject* template = [messageTemplates selection];
		if (template != nil) {
			NSManagedObjectContext* context = [messageTemplates managedObjectContext];
			NSString* name = [template valueForKey:@"name"];
			NSPredicate * predicate = [NSPredicate predicateWithFormat:@"name = %@", name];
			NSFetchRequest * fetch = [[[NSFetchRequest alloc] init] autorelease];
			NSEntityDescription* entity = [NSEntityDescription entityForName:@"MessageTemplate" inManagedObjectContext:context];
			[fetch setEntity: entity];
			[fetch setPredicate: predicate];
			NSArray* result = [context executeFetchRequest:fetch error:nil];
			template = [result objectAtIndex:0];

			entity = [NSEntityDescription entityForName:@"MessagePhoto" inManagedObjectContext:context];
			NSMutableSet* attachments = [template valueForKey:@"messagePhotos"];
			
			NSManagedObject* messagePhoto = [[NSManagedObject alloc] initWithEntity:entity insertIntoManagedObjectContext:context];
			MessageTemplateImage* image = [self image];
			[messagePhoto setValue:[image filename] forKey:@"path"];
			[attachments addObject:messagePhoto];
			[messagePhoto setValue:template forKey:@"messageTemplate"];
			[context save:nil];
			
			result = [context executeFetchRequest:fetch error:nil];
			template = [result objectAtIndex:0];
			attachments = [template valueForKey:@"messagePhotos"];
			NSLog(@"attachments: %@", attachments);
		}
	}

}

- (void) dealloc
{
	[super dealloc];
}


@end
