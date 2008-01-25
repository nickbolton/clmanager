//
//  MyImage.m
//  CLManager
//
//  Created by Deuce on 12/29/07.
//  Copyright 2007 __MyCompanyName__. All rights reserved.
//

#import "MessageTemplateImage.h"


@implementation MessageTemplateImage

- (id)initByReferencingFile:(NSString *)filename {
	[filename retain];
	_filename = filename;
	return [super initByReferencingFile:filename];
}

- (id)initByReferencingURL:(NSURL *)url {
	[url retain];
	_url = url;
	return [super initByReferencingURL:url];
}

- (id)initWithContentsOfFile:(NSString *)filename {
	[filename retain];
	_filename = filename;
	return [super initWithContentsOfFile:filename];
}

- (id)initWithContentsOfURL:(NSURL *)url {
	[url retain];
	_url = url;
	return [super initWithContentsOfURL:url];
}

- (NSURL*)url {
	return _url;
}

- (NSString*)filename {
	return _filename;
}

- (void)dealloc {
	[_url release];
	[_filename release];
	_url = nil;
	_filename = nil;
	[super dealloc];
}

@end
