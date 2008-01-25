//
//  MyTableView.m
//  CLManager
//
//  Created by Deuce on 12/27/07.
//  Copyright 2007 __MyCompanyName__. All rights reserved.
//

#import "MessageTemplateTableView.h"


@implementation MessageTemplateTableView

-(void)mouseDown:(NSEvent *)theEvent {
	[super mouseDown:theEvent];
	NSLog(@"MessageTemplateTableView mouseDown");
	BOOL doubleClick = ([theEvent clickCount] == 2);
	if (doubleClick) {
		[messageTemplateController editTemplate:self];
	}

}
@end
