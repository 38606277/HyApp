//
//  MyDataBaseTest.h
//  bridgeDemo
//
//  Created by huitong on 2019/7/26.
//  Copyright © 2019 duanfeng. All rights reserved.
//

#import <Foundation/Foundation.h>
#import <dsBridge/dsBridge.h>

NS_ASSUME_NONNULL_BEGIN

@interface MyDataBaseTest : NSObject
- (void)wirteDB:(NSString *)msg :(JSCallback)completion;
- (void)DBaddData:(NSString *)msg :(JSCallback)completion;
- (void)readDB:(NSString *)msg :(JSCallback)completion;
@end

NS_ASSUME_NONNULL_END
