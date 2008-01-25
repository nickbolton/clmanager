//
//  KeystrokeAwareButton.h
//  CLManager
//
//  Created by Deuce on 12/30/07.
//  Copyright 2007 __MyCompanyName__. All rights reserved.
//

#import <Cocoa/Cocoa.h>


@interface KeystrokeAwareButton : NSButton {
	BOOL shiftDown;
	BOOL commandDown;
	BOOL controlDown;
	BOOL optionDown;
}

- (BOOL)shiftDown;
- (BOOL)commandDown;
- (BOOL)controlDown;
- (BOOL)optionDown;

@end
