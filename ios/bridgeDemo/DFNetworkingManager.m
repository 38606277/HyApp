//
//  DFNetworkingManager.m
//  kamehameWeibo
//
//  Created by 中财讯 on 2017/4/21.
//  Copyright © 2017年 duanfeng. All rights reserved.
//

#import "DFNetworkingManager.h"
#import <dsBridge/dsBridge.h>
#import "MyJsonFormatter.h"

@interface DFNetworkingManager ()<NSCopying>

@end

@implementation DFNetworkingManager

+(instancetype)shareManager{
    static DFNetworkingManager *instance;
    static dispatch_once_t onceToken;
    dispatch_once(&onceToken, ^{
        instance = [[self alloc]init];
        instance.requestSerializer = [AFJSONRequestSerializer serializer];
        instance.requestSerializer = [AFHTTPRequestSerializer serializer];
        instance.responseSerializer = [AFHTTPResponseSerializer serializer];
        instance.responseSerializer = [AFJSONResponseSerializer serializer];
//        instance.requestSerializer = [AFJSONRequestSerializer new];
        instance.responseSerializer.acceptableContentTypes = [NSSet setWithObjects:@"application/json", @"text/json", @"text/javascript", @"text/html", @"application/xml" , @"text/plain",nil];
    });
    return instance;
}

+ (id)allocWithZone:(struct _NSZone *)zone
{
    return [DFNetworkingManager shareManager];
}

- (id) copyWithZone:(struct _NSZone *)zone
{
    return [DFNetworkingManager shareManager];
}



- (void)requestPost:(NSString *)msg :(JSCallback)completion{
    NSDictionary *dic = [MyJsonFormatter dictionaryWithJsonString:msg];
    [self df_postUrlString:dic[@"url"] parameters:dic[@"parameters"] complete:^(id responseResult, NSError *error) {
        completion(responseResult,YES);
    }];
}

-(void)df_deleteUrlString:(NSString *)urlString parameters:(NSDictionary *)parameters complete:(void (^)(id, NSError *))complete{
    [self DELETE:urlString parameters:parameters success:^(NSURLSessionDataTask * _Nonnull task, id  _Nullable responseObject) {
        complete(responseObject,nil);
    } failure:^(NSURLSessionDataTask * _Nullable task, NSError * _Nonnull error) {
        complete(nil,error);
    }];
}

-(void)df_getUrlString:(NSString *)urlString parameters:(NSDictionary *)parameters complete:(void (^)(id, NSError *))complete{
    [self GET:urlString parameters:parameters progress:nil success:^(NSURLSessionDataTask * _Nonnull task, id  _Nullable responseObject) {
        complete(responseObject,nil);
    } failure:^(NSURLSessionDataTask * _Nullable task, NSError * _Nonnull error) {
        complete(nil,error);
    }];
}

-(void)df_postUrlString:(NSString *)urlString parameters:(NSDictionary *)parameters complete:(void (^)(id, NSError *))complete{
    [self POST:urlString parameters:parameters progress:nil success:^(NSURLSessionDataTask * _Nonnull task, id  _Nullable responseObject) {
        complete(responseObject,nil);
    } failure:^(NSURLSessionDataTask * _Nullable task, NSError * _Nonnull error) {
        complete(nil,error);
    }];
}

@end
