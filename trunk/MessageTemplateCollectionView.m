//
//  MessageTemplateCollectionView.m
//  CLManager
//
//  Created by Deuce on 1/24/08.
//  Copyright 2008 __MyCompanyName__. All rights reserved.
//

#import "MessageTemplateCollectionView.h"
#import "AttachmentImageView.h"

@implementation MessageTemplateCollectionView

- (void)awakeFromNib {
	NSLog(@"awakeFromNib");
}

- (NSArrayController*)messageTemplates {
	return messageTemplates;
}

@end
