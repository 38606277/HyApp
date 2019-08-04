//
//  NetworkTools.h
//  bridgeDemo
//
//  Created by huitong on 2019/8/2.
//  Copyright © 2019 duanfeng. All rights reserved.
//

#import <Foundation/Foundation.h>
#import <Reachability.h>

NS_ASSUME_NONNULL_BEGIN

@interface NetworkTools : NSObject
+ (NetworkStatus)isConnectionAvailable;
@end

NS_ASSUME_NONNULL_END
