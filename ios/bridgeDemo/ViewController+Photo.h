//
//  ViewController+Photo.h
//  bridgeDemo
//
//  Created by huitong on 2019/7/26.
//  Copyright © 2019 duanfeng. All rights reserved.
//

#import "ViewController.h"
#import <dsBridge/dsBridge.h>

NS_ASSUME_NONNULL_BEGIN

@interface ViewController (Photo)
- (void)photo:(NSString *)msg :(JSCallback)comp;
@end

NS_ASSUME_NONNULL_END
