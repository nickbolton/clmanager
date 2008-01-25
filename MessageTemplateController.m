//
//  MessageTemplateController.m
//  CLManager
//
//  Created by Deuce on 12/27/07.
//  Copyright 2007 __MyCompanyName__. All rights reserved.
//

#import "MessageTemplateController.h"
#import <CSMail/CSMail.h>
#import "MessageTemplateImage.h"
#import "KeystrokeAwareButton.h"
#import "Post.h"

@implementation MessageTemplateController


- (void)awakeFromNib {
	
	// Force CSMailClient to load
	[CSMailClient class];
	
	NSUserDefaults* userDefaults = [NSUserDefaults standardUserDefaults];
	NSString* photoPath = [userDefaults objectForKey:@"messageTemplatePhotos"];
	if (photoPath == nil) {
		photoPath = @"~/Pictures";
	}
	NSArray* contents = [[NSFileManager defaultManager] directoryContentsAtPath:[[NSString stringWithFormat:photoPath] stringByExpandingTildeInPath]];
	int i;
	
	NSMutableArray* photoArr = [[NSMutableArray alloc] init];
	NSArray* photoTypes = [NSArray arrayWithObjects:@".jpg", @".jpeg", @".bmp", @".gif", @".png", @".tif", nil];
	for (i=0; i<[contents count]; i++) {
		NSString* fileName = [contents objectAtIndex:i];
		int j;
		for (j=0; j<[photoTypes count]; j++) {
			NSString* ext = [photoTypes objectAtIndex:j];
			int pos = [fileName rangeOfString:ext].location;
			int correctPos = [fileName length]-[ext length];
			if (pos == correctPos) {
				MessageTemplateImage* image = [[MessageTemplateImage alloc] initWithContentsOfFile:[[NSString stringWithFormat:@"%@/%@", photoPath, fileName] stringByExpandingTildeInPath]];
				NSLog(@"Adding image: %@", image);
				[photoArr addObject:image];
			}
		}
	}
	[photoView setContent:photoArr];
}

- (NSString*)mailSender {
	NSUserDefaults* defaults = [NSUserDefaults standardUserDefaults];
	return [defaults objectForKey:@"mailSender"];
}

- (NSString*)mailBcc {
	NSUserDefaults* defaults = [NSUserDefaults standardUserDefaults];
	return [defaults objectForKey:@"mailBcc"];
}

- (void)dealloc {
	[super dealloc];
}

- (IBAction)addAttachment:(id)sender {
	NSLog(@"addAttachment");
}

- (void)addAttachmentSheetDidEnd:(NSWindow *)sheet returnCode:(int)returnCode contextInfo:(void *)contextInfo
{
	if (returnCode == NSOKButton) {
		NSLog(@"contextInfo: %@", contextInfo);
	}
}

- (IBAction)removeAttachment:(id)sender {
}

- (void)launchMessageTemplates {
        NSLog(@"launchMessageTemplates");
        [NSApp beginSheet:messageTemplatesWindow modalForWindow:[NSApp mainWindow] modalDelegate:self
                didEndSelector:nil contextInfo:nil];
}

- (IBAction)okMessageTemplates:(id)sender {
        NSLog(@"okMessageTemplates");
		NSString* newMessage = [messageView string];
		NSManagedObject* template = [messageTemplates selection];
		NSString* message = [template valueForKey:@"message"];
		
		NSManagedObjectContext* context = [messageTemplates managedObjectContext];
		if ([message compare:newMessage] != NSOrderedSame) {
			[template setValue:newMessage forKey:@"message"];
		}
		[context save:nil];
		
        [NSApp endSheet:messageTemplatesWindow returnCode:NSOKButton];
        [messageTemplatesWindow orderOut:nil];
}

- (IBAction)addTemplate:(id)sender {
	NSManagedObjectContext* context = [messageTemplates managedObjectContext];
	NSEntityDescription* entity = [NSEntityDescription entityForName:@"MessageTemplate" inManagedObjectContext:context];
	NSManagedObject* template = [[NSManagedObject alloc] initWithEntity:entity insertIntoManagedObjectContext:context];
	[messageTemplates addObject:template];
	[selectedAttachment setContent:template];
	[self launchMessageTemplates];

}

- (IBAction)editTemplate:(id)sender {
	NSLog(@"editTemplate");
	int selectedRow = [messageTemplateTable selectedRow];
	if (selectedRow < 0) return;
	
	[messageTemplates setSelectionIndex:selectedRow];
	NSManagedObject* template = [messageTemplates selection];
	
	if (template != nil) {
		NSString* message = [template valueForKey:@"message"];
		[messageView setString:message];
		[self launchMessageTemplates];
	}
}

- (IBAction)removeTemplate:(id)sender {
	NSLog(@"removeTemplate");
}


- (NSAttributedString*)buildMessage:(NSString*)messageBody attachments:(NSSet*)attachments {

	NSMutableAttributedString *attribBody
		= [[[NSMutableAttributedString alloc] initWithString:messageBody] autorelease];
	if ([attachments count] == 0) return attribBody;
	
	unichar attachment[] = { NSAttachmentCharacter };
	NSFileWrapper *fileWrapper;
	NSTextAttachment *fileAttach;
	[attribBody beginEditing];
	int i;
	
	for (NSManagedObject* file in attachments) {
		fileWrapper = [[[NSFileWrapper alloc] initWithPath:[file valueForKey:@"path"]] autorelease];
	    fileAttach = [[[NSTextAttachment alloc] initWithFileWrapper:fileWrapper] autorelease];

		if (i>0) {
			[attribBody appendAttributedString:[[[NSAttributedString alloc] initWithString:@"\n"] autorelease]];
		}
		[attribBody appendAttributedString:
			[[[NSAttributedString alloc] initWithString:[NSString stringWithCharacters:attachment length:1]
                   attributes: [NSDictionary dictionaryWithObjectsAndKeys:fileAttach, NSAttachmentAttributeName,
                                   nil]] autorelease]];
	}
	[attribBody endEditing];
	
	return attribBody;
}

- (NSString*)resolveMessage:(NSString*)message post:(NSManagedObject*)post {
	NSMutableString* result = [NSMutableString stringWithString:message];
	[result replaceOccurrencesOfString:@"${identifier}" withString:[post valueForKey:@"id"] options:NSLiteralSearch range:NSMakeRange(0, [result length])];
	return result;
}

- (NSString*)formatSubject:(NSManagedObject*)post {
	return [NSString stringWithFormat:@"Re: %@ (CL:%@)", [[post valueForKey:@"title"] string], [post valueForKey:@"id"]];
}

- (void)sendOrConstructMessage:(BOOL)construct force:(BOOL)force {
	NSLog(@"sendMessage");
	[statusMessage setStringValue:@""];
	int messagedSelectedRow = [messageTemplateTable selectedRow];
	if (messagedSelectedRow < 0 || messagedSelectedRow >= [[messageTemplates arrangedObjects] count]) return;
	
	NSManagedObject* template = [[messageTemplates arrangedObjects] objectAtIndex:messagedSelectedRow];
	NSString* message = [template valueForKey:@"message"];
	NSSet* attachments = [template valueForKey:@"messagePhotos"];

	int postSelectedRow = [postsTable selectedRow];
    NSLog(@"postSelectedRow: %d", postSelectedRow);
	if (postSelectedRow < 0 || postSelectedRow >= [[posts arrangedObjects] count]) return;
	
	Post* post = [[posts arrangedObjects] objectAtIndex:postSelectedRow];
	
	NSNumber* responded = [post valueForKey:@"responded"];
	if (!force && [responded boolValue]) {
		[statusMessage setStringValue:@"Already responded to post"];
		NSLog(@"Already responded to post: %@", [post valueForKey:@"id"]);
		NSBeep();
		return;
	}
    
    if ([[post valueForKey:@"flagged"] boolValue]) {
    	[statusMessage setStringValue:@"Message was flagged"];
		NSLog(@"Message was flagged, post: %@", [post valueForKey:@"id"]);
		NSBeep();
		return;
    }
	
	NSLog(@"responding to email: %@", [post valueForKey:@"replyAddress"]);
	
	NSString* messageContent = [self resolveMessage:message post:post];
	NSString* subject = [self formatSubject:post];
	NSString* email = [post valueForKey:@"replyAddress"];
    //messageContent = [messageContent stringWithCanonicalLinebreaks];
    
	NSLog(@"subject: %@", subject);
	
    CSMailClient *client = [[CSMailClient installedClients] objectAtIndex:0];

	NSLog(@"mail app: %@", [client applicationName]);
	
	if (construct) {
		[client constructMessage:[self buildMessage:messageContent attachments:attachments]
			headers:[NSDictionary dictionaryWithObjectsAndKeys:
				email, @"To",
				[self mailBcc], @"Bcc",
				[self mailSender], @"From",
				[self mailSender], @"Sender",
				subject, @"Subject",
				nil]];
	} else {
		[client deliverMessage:[self buildMessage:messageContent attachments:attachments]
			headers:[NSDictionary dictionaryWithObjectsAndKeys:
				email, @"To",
				[self mailBcc], @"Bcc",
				[self mailSender], @"From",
				[self mailSender], @"Sender",
				subject, @"Subject",
				nil]];
	}

	[post setValue:[NSNumber numberWithBool:YES] forKey:@"responded"];
	[statusMessage setStringValue:@"Message sent"];
}

- (IBAction)editSendMessage:(id)sender {

	NSLog(@"editSendMessage: %@", sender);
	NSLog(@"sender: %@", [self mailSender]);
	NSLog(@"bcc   : %@", [self mailBcc]);
	KeystrokeAwareButton* b = sender;
	[self sendOrConstructMessage:YES force:[b commandDown]];
}

- (IBAction)sendMessage:(id)sender {
	KeystrokeAwareButton* b = sender;
	[self sendOrConstructMessage:NO force:[b commandDown]];
}

- (void)sheetDidEnd:(NSWindow *)sheet returnCode:(int)returnCode contextInfo:(void *)contextInfo
{
	//if (returnCode == NSOKButton)
	//	NSBeep();
}

@end
