//
//  MyQRCodeTools.h
//  bridgeDemo
//
//  Created by huitong on 2019/8/2.
//  Copyright © 2019 duanfeng. All rights reserved.
//

#import <UIKit/UIKit.h>
#import <dsBridge/dsBridge.h>

NS_ASSUME_NONNULL_BEGIN

@interface MyQRCodeTools : NSObject

-(void)startQRCode:(NSString *)msg :(JSCallback)compltetion;

@end

NS_ASSUME_NONNULL_END
