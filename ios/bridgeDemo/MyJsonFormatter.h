//
//  MyJsonFormatter.h
//  bridgeDemo
//
//  Created by huitong on 2019/8/6.
//  Copyright © 2019 duanfeng. All rights reserved.
//

#import <Foundation/Foundation.h>

NS_ASSUME_NONNULL_BEGIN

@interface MyJsonFormatter : NSObject

+ (NSDictionary *)dictionaryWithJsonString:(NSString *)jsonString;
+ (NSString *)convertToJsonData:(NSDictionary *)dict;
@end

NS_ASSUME_NONNULL_END
