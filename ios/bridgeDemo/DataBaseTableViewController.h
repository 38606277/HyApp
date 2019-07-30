//
//  DataBaseTableViewController.h
//  bridgeDemo
//
//  Created by huitong on 2019/7/26.
//  Copyright © 2019 duanfeng. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "Person.h"

NS_ASSUME_NONNULL_BEGIN

@interface DataBaseTableViewController : UITableViewController


@property(strong,nonatomic) NSArray<Person *> *persons;

@end

NS_ASSUME_NONNULL_END
