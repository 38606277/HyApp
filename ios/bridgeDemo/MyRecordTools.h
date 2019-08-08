//
//  MyRecordTools.h
//  bridgeDemo
//
//  Created by huitong on 2019/8/2.
//  Copyright © 2019 duanfeng. All rights reserved.
//

#import <UIKit/UIKit.h>

NS_ASSUME_NONNULL_BEGIN

@interface MyRecordTools : NSObject

+ (instancetype)shareTools;
- (void)recordingAction;
- (void)stopAction;
- (void)playAction;
@end

NS_ASSUME_NONNULL_END
