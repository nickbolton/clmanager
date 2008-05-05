//
//  MyImageView.m
//  CLManager
//
//  Created by Deuce on 12/27/07.
//  Copyright 2007 __MyCompanyName__. All rights reserved.
//

#import "PostImageView.h"


@implementation PostImageView


-(void)mouseDown:(NSEvent *)theEvent {
	NSLog(@"mouseDown: %@", theEvent);
	//[statusMessage setStringValue:@""];
	BOOL doubleClick = ([theEvent clickCount] == 2);
	if (doubleClick) {
		NSImage* image = [self image];
		if (image != nil) {
			[enlargedPhotoController setContent:image];
			[NSApp beginSheet:enlargedPhotoWindow modalForWindow:[NSApp mainWindow] modalDelegate:self
					didEndSelector:nil contextInfo:nil];
		}
	}
}

- (IBAction)ok:(id)sender {
	[NSApp endSheet:enlargedPhotoWindow returnCode:NSOKButton];
	[enlargedPhotoWindow orderOut:nil];
}

- (void)dealloc {
	[super dealloc];
}

@end
