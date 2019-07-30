//
//  Person.h
//  bridgeDemo
//
//  Created by huitong on 2019/7/26.
//  Copyright © 2019 duanfeng. All rights reserved.
//

#import <Foundation/Foundation.h>

NS_ASSUME_NONNULL_BEGIN

@interface Person : NSObject

@property(assign,nonatomic) NSInteger identityCardID;
@property(copy,nonatomic) NSString *name;
@property(assign,nonatomic) NSInteger age;
@property(assign,nonatomic) NSInteger score;
    
@end

NS_ASSUME_NONNULL_END
