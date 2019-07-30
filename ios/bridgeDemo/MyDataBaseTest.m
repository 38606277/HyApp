//
//  MyDataBaseTest.m
//  bridgeDemo
//
//  Created by huitong on 2019/7/26.
//  Copyright © 2019 duanfeng. All rights reserved.
//

#import "MyDataBaseTest.h"
#import <FMDB/FMDB.h>
#import "Person.h"
#import <dsBridge/dsBridge.h>
#import "DataBaseTableViewController.h"

@implementation MyDataBaseTest{
    FMDatabase *db;
    NSString *dbPath;
}

- (void)wirteDB:(NSString *)msg :(JSCallback)completion{
    NSString *docuPath = NSSearchPathForDirectoriesInDomains(NSDocumentDirectory, NSUserDomainMask, YES)[0];
    dbPath = [docuPath stringByAppendingPathComponent:@"test.db"];
    db = [FMDatabase databaseWithPath:dbPath];
    [db open];
    if (![db open]) {
        NSLog(@"db open fail");
        return;
    }
    NSString *sql = @"create table if not exists t_person ('identityCardID' INTEGER PRIMARY KEY,'name' TEXT NOT NULL, 'age' INTEGER,'score' INTEGER)";
    BOOL result = [db executeUpdate:sql];
    if (result) {
//        NSLog(@"create table success");
        completion(@"create table success",YES);
    }else{
        completion(@"create table fail",NO);
    }
    [db close];
}
    
- (void)readDB:(NSString *)msg :(JSCallback)completion{
    
    NSDate *begin = [NSDate date];
    NSString *docuPath = NSSearchPathForDirectoriesInDomains(NSDocumentDirectory, NSUserDomainMask, YES)[0];
    dbPath = [docuPath stringByAppendingPathComponent:@"test.db"];
    db = [FMDatabase databaseWithPath:dbPath];
    [db open];
    NSMutableArray *arrayM = [NSMutableArray array];
    if ([db open]) {
        NSString *str = @"SELECT * FROM 't_person'";
        FMResultSet *result = [db executeQuery:str];
        while ([result next]) {
            Person *person = [Person new];
            person.identityCardID = [result intForColumn:@"identityCardID"];
            person.name = [result stringForColumn:@"name"];
            person.age = [result intForColumn:@"age"];
            person.score = [result intForColumn:@"score"];
            [arrayM addObject:person];
        }
        for (Person *person in arrayM) {
            NSLog(@"person:%@",person);
            completion(@"select table success",YES);
        }
        [db close];
    }else{
//        NSLog(@"readDB not open");
        completion(@"select table fail",NO);
    }
    DataBaseTableViewController *vc = [DataBaseTableViewController new];
    vc.persons = [arrayM copy];
    [(UINavigationController *)[UIApplication sharedApplication].keyWindow.rootViewController pushViewController:vc animated:YES];
    //            [[UIApplication sharedApplication].keyWindow.rootViewController presentViewController:vc animated:YES completion:nil];
    NSDate *end = [NSDate date];
    
    NSTimeInterval time = [end timeIntervalSinceDate:begin];
    NSLog(@"%f",time);
}
    
- (void)DBaddData:(NSString *)msg :(JSCallback)completion{
    NSString *docuPath = NSSearchPathForDirectoriesInDomains(NSDocumentDirectory, NSUserDomainMask, YES)[0];
    dbPath = [docuPath stringByAppendingPathComponent:@"test.db"];
    db = [FMDatabase databaseWithPath:dbPath];
    [db open];
    [db beginTransaction];
    BOOL rollBack = NO;
    @try {
        //2.在事务中执行任务
        for (int i = 0; i< 1000; i++) {
            
            BOOL result = [db executeUpdate:@"insert into 't_person'(identityCardID,name,age,score) values(?,?,?,?)" withArgumentsInArray:@[@(i),[NSString stringWithFormat:@"jack%d",i],@20,@(80 + i)]];
            if (result) {
                NSLog(@"在事务中insert success");
            }
        }
    }
    @catch(NSException *exception) {
        //3.在事务中执行任务失败，退回开启事务之前的状态
        rollBack = YES;
        [db rollback];
    }
    @finally {
        //4. 在事务中执行任务成功之后
        rollBack = NO;
        [db commit];
    }
    completion(@"success",YES);
}

@end
