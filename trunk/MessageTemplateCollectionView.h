//
//  MessageTemplateCollectionView.h
//  CLManager
//
//  Created by Deuce on 1/24/08.
//  Copyright 2008 __MyCompanyName__. All rights reserved.
//

#import <Cocoa/Cocoa.h>


@interface MessageTemplateCollectionView : NSCollectionView {
	IBOutlet NSArrayController* messageTemplates;
}

- (NSArrayController*)messageTemplates;

@end
