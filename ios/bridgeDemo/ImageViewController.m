//
//  ImageViewController.m
//  bridgeDemo
//
//  Created by huitong on 2019/8/2.
//  Copyright © 2019 duanfeng. All rights reserved.
//

#import "ImageViewController.h"

@interface ImageViewController ()

@end

@implementation ImageViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view.
    UIImageView *imageView = [[UIImageView alloc]initWithFrame:self.view.frame];
    imageView.contentMode = UIViewContentModeScaleAspectFill;
    imageView.image = self.image;
    [self.view addSubview:imageView];
}

//- (void)showImage:(NSNotification *)noti{
//    [[NSNotificationCenter defaultCenter] addObserver:self selector:@selector(showImage:) name:@"photo" object:nil];
//    UIImage *image = noti.object;
//    
//}
/*
#pragma mark - Navigation

// In a storyboard-based application, you will often want to do a little preparation before navigation
- (void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender {
    // Get the new view controller using [segue destinationViewController].
    // Pass the selected object to the new view controller.
}
*/

@end
