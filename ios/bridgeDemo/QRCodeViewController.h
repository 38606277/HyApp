//
//  QRCodeViewController.h
//  bridgeDemo
//
//  Created by huitong on 2019/8/2.
//  Copyright © 2019 duanfeng. All rights reserved.
//

#import <UIKit/UIKit.h>


NS_ASSUME_NONNULL_BEGIN

@interface QRCodeViewController : UIViewController
@property (nonatomic,copy) void(^resultBlock)(id result , BOOL b);
@end

NS_ASSUME_NONNULL_END
