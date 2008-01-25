//
//  BaseTableView.m
//  CLManager
//
//  Created by Deuce on 12/28/07.
//  Copyright 2007 __MyCompanyName__. All rights reserved.
//

#import "BaseTableView.h"


@implementation BaseTableView

-(void)mouseDown:(NSEvent *)theEvent {
	[super mouseDown:theEvent];
	NSLog(@"BaseTableView mouseDown");
	[statusMessage setStringValue:@""];	
}

@end
