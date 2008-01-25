//
//  CLManager_AppDelegate.h
//  CLManager
//
//  Created by Deuce on 1/13/08.
//  Copyright __MyCompanyName__ 2008 . All rights reserved.
//

#import <Cocoa/Cocoa.h>

@interface CLManager_AppDelegate : NSObject 
{
    IBOutlet NSWindow *window;
	IBOutlet NSWindow *preferencesWindow;
	IBOutlet NSWindow *messageTemplatesWindow;
    
    NSPersistentStoreCoordinator *persistentStoreCoordinator;
    NSManagedObjectModel *managedObjectModel;
    NSManagedObjectContext *managedObjectContext;
}

- (NSPersistentStoreCoordinator *)persistentStoreCoordinator;
- (NSManagedObjectModel *)managedObjectModel;
- (NSManagedObjectContext *)managedObjectContext;

- (IBAction)saveAction:sender;
- (IBAction)launchPreferences:(id)sender;
- (IBAction)okPreferences:(id)sender;

@end
