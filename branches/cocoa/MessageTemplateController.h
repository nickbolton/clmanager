//
//  MessageTemplateController.h
//  CLManager
//
//  Created by Deuce on 12/27/07.
//  Copyright 2007 __MyCompanyName__. All rights reserved.
//

#import <Cocoa/Cocoa.h>


@interface MessageTemplateController : NSObject {
	IBOutlet NSTextField* statusMessage;
	IBOutlet NSWindow* messageTemplatesWindow;
	IBOutlet NSTableView* messageTemplateTable;
	IBOutlet NSArrayController* messageTemplates;
	IBOutlet NSCollectionView* photoView;
	IBOutlet NSObjectController* selectedAttachment;
	IBOutlet NSTextView* messageView;
	IBOutlet NSArrayController* posts;
	IBOutlet NSTableView* postsTable;
}

- (IBAction)addTemplate:(id)sender;
- (IBAction)removeTemplate:(id)sender;
- (IBAction)editTemplate:(id)sender;
- (IBAction)sendMessage:(id)sender;
- (IBAction)editSendMessage:(id)sender;
- (IBAction)okMessageTemplates:(id)sender;
- (IBAction)editSendMessage:(id)sender;
- (IBAction)sendMessage:(id)sender;
- (void)sheetDidEnd:(NSWindow *)sheet returnCode:(int)returnCode contextInfo:(void *)contextInfo;

@end
