//
//  AttachmentTableView.h
//  CLManager
//
//  Created by Deuce on 12/29/07.
//  Copyright 2007 __MyCompanyName__. All rights reserved.
//

#import <Cocoa/Cocoa.h>


@interface AttachmentTableView : NSTableView {
	IBOutlet NSArrayController* attachments;
}

@end
