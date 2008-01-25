//
//  MyImageView.h
//  CLManager
//
//  Created by Deuce on 12/27/07.
//  Copyright 2007 __MyCompanyName__. All rights reserved.
//

#import <Cocoa/Cocoa.h>


@interface PostImageView : NSImageView {
	IBOutlet NSObjectController* enlargedPhotoController;
	IBOutlet NSWindow* enlargedPhotoWindow;
	IBOutlet id statusMessage;
}

- (IBAction)ok:(id)sender;

@end
