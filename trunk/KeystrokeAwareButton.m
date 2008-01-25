//
//  KeystrokeAwareButton.m
//  CLManager
//
//  Created by Deuce on 12/30/07.
//  Copyright 2007 __MyCompanyName__. All rights reserved.
//

#import "KeystrokeAwareButton.h"


@implementation KeystrokeAwareButton

-(void)mouseDown:(NSEvent *)theEvent {
	NSLog(@"KeystrokeAwareButton mouseDown: %@", theEvent);	
	int modifiers = [theEvent modifierFlags];
	commandDown = (modifiers & NSCommandKeyMask) != 0;
	shiftDown = (modifiers & NSShiftKeyMask) != 0;
	controlDown = (modifiers & NSControlKeyMask) != 0;
	optionDown = (modifiers & NSAlternateKeyMask) != 0;
	[super mouseDown:theEvent];
}

- (BOOL)shiftDown {
	return shiftDown;
}

- (BOOL)commandDown {
	return commandDown;
}

- (BOOL)controlDown {
	return controlDown;
}

- (BOOL)optionDown {
	return optionDown;
}

@end
