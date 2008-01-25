//
//  AttachmentCell.m
//  CLManager
//
//  Created by Deuce on 1/24/08.
//  Copyright 2008 __MyCompanyName__. All rights reserved.
//

#import "AttachmentCell.h"

@implementation AttachmentCell

- (void)dealloc {
    [image release];
    image = nil;
    [super dealloc];
}

- copyWithZone:(NSZone *)zone
{
    AttachmentCell *cell = (AttachmentCell *)[super copyWithZone:zone];
    cell->image = [image retain];
    return cell;
}

- (void)setImage:(NSImage *)anImage
{
    if (anImage != image)
	{
        [image release];
        image = [anImage retain];
    }
}

- (NSImage *)image
{
	//if (image == nil) {
	//	image = [[NSImage alloc] initWithContentsOfFile:[self stringValue]];
	//	[image retain];
	//}
    return image;
}

- (NSRect)imageFrameForCellFrame:(NSRect)cellFrame
{
	NSImage* img = [self image];
    if (img != nil)
	{
        NSRect imageFrame;
        imageFrame.size = [img size];
        imageFrame.origin = cellFrame.origin;
        imageFrame.origin.x += 3;
        imageFrame.origin.y += ceil((cellFrame.size.height - imageFrame.size.height) / 2);
        return imageFrame;
    }
    else
        return NSZeroRect;
}

- (void)editWithFrame:(NSRect)aRect inView:(NSView *)controlView editor:(NSText *)textObj delegate:(id)anObject event:(NSEvent *)theEvent
{
	NSImage* img = [self image];
    NSRect textFrame, imageFrame;
    NSDivideRect (aRect, &imageFrame, &textFrame, 3 + [img size].width, NSMinXEdge);
    [super editWithFrame: textFrame inView: controlView editor:textObj delegate:anObject event: theEvent];
}

- (void)selectWithFrame:(NSRect)aRect inView:(NSView *)controlView editor:(NSText *)textObj delegate:(id)anObject start:(int)selStart length:(int)selLength
{
	NSImage* img = [self image];
    NSRect textFrame, imageFrame;
    NSDivideRect (aRect, &imageFrame, &textFrame, 3 + [img size].width, NSMinXEdge);
    [super selectWithFrame: textFrame inView: controlView editor:textObj delegate:anObject start:selStart length:selLength];
}

- (void)drawWithFrame:(NSRect)cellFrame inView:(NSView *)controlView
{
	NSImage* img = [self image];
    if (img != nil)
	{
        NSSize	imageSize;
        NSRect	imageFrame;

        imageSize = [img size];
        NSDivideRect(cellFrame, &imageFrame, &cellFrame, 3 + imageSize.width, NSMinXEdge);
        if ([self drawsBackground])
		{
            [[self backgroundColor] set];
            NSRectFill(imageFrame);
        }
        imageFrame.origin.x += 3;
        imageFrame.size = imageSize;

        if ([controlView isFlipped])
            imageFrame.origin.y += ceil((cellFrame.size.height + imageFrame.size.height) / 2);
        else
            imageFrame.origin.y += ceil((cellFrame.size.height - imageFrame.size.height) / 2);

        [img compositeToPoint:imageFrame.origin operation:NSCompositeSourceOver];
    }
    [super drawWithFrame:cellFrame inView:controlView];
}

- (NSSize)cellSize
{
	NSImage* img = [self image];
    NSSize cellSize = [super cellSize];
    cellSize.width += (img ? [img size].width : 0) + 3;
    return cellSize;
}

@end
