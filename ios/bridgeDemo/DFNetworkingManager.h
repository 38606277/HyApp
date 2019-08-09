//
//  DFNetworkingManager.h
//  kamehameWeibo
//
//  Created by 中财讯 on 2017/4/21.
//  Copyright © 2017年 duanfeng. All rights reserved.
//

#import <AFNetworking/AFNetworking.h>

@interface DFNetworkingManager : AFHTTPSessionManager

+ (instancetype)shareManager;

- (void)df_getUrlString:(NSString *)urlString parameters:(NSDictionary *)parameters complete:(void(^)(id responseResult,NSError *error))complete;

- (void)df_postUrlString:(NSString *)urlString parameters:(NSDictionary *)parameters complete:(void(^)(id responseResult,NSError *error))complete;

-(void)df_deleteUrlString:(NSString *)urlString parameters:(NSDictionary *)parameters complete:(void (^)(id, NSError *))complete;

@end
