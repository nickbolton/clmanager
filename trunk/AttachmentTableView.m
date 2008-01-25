//
//  AttachmentTableView.m
//  CLManager
//
//  Created by Deuce on 12/29/07.
//  Copyright 2007 __MyCompanyName__. All rights reserved.
//

#import "AttachmentTableView.h"


@implementation AttachmentTableView

- (void)awakeFromNib {
}

-(void)mouseDown:(NSEvent *)theEvent {
	[super mouseDown:theEvent];
		BOOL doubleClick = ([theEvent clickCount] == 2);
	if (doubleClick) {
		NSLog(@"AttachmentTableView double click");
		int selectedRow = [self selectedRow];
		[attachments removeObjectAtArrangedObjectIndex:selectedRow];
		//[super reloadData];
	}

}

- (void)dealloc {
	[super dealloc];
}


@end
