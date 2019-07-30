//
//  Person.m
//  bridgeDemo
//
//  Created by huitong on 2019/7/26.
//  Copyright © 2019 duanfeng. All rights reserved.
//

#import "Person.h"

@implementation Person

    
- (NSString *)description{
   return [NSString stringWithFormat:@"id:%zd,name:%@,age:%zd,score:%zd",self.identityCardID,self.name,self.age,self.score];
}
@end
