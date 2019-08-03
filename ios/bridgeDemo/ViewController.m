//
//  ViewController.m
//  bridgeDemo
//
//  Created by huitong on 2019/7/26.
//  Copyright © 2019 duanfeng. All rights reserved.
//

#import "ViewController.h"
#import <dsBridge/dsBridge.h>
#import <FMDB/FMDB.h>
#import "Person.h"
#import "MyDataBaseTest.h"
#import <CoreLocation/CoreLocation.h>
#import "ViewController+Photo.h"
#import "MyQRCodeTools.h"

@interface ViewController ()<WKUIDelegate,CLLocationManagerDelegate>
    
@property (nonatomic, strong) CLLocationManager *locationManager;//设置manager
@property (nonatomic, strong) NSString *currentCity;
@property (nonatomic,strong) UIImageView *im;
@end

@implementation ViewController{
    DWKWebView * dwebview;
}

- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view.
    [self initBridge];
//    [self wirteDB];
////    [self DBaddData];
//    [self readDB];
    UIImageView *im = [[UIImageView alloc]initWithFrame:CGRectMake(0, 400, 100, 100)];
    im.contentMode = UIViewContentModeScaleAspectFill;
    [self.view addSubview:im];
    self.im = im;
    [[NSNotificationCenter defaultCenter] addObserver:self selector:@selector(showImage:) name:@"photo" object:nil];
}

- (void)showImage:(NSNotification *)noti{
    UIImage *image = noti.object;
    self.im.image = image;
}
    
- (void)requestLocation:(NSString *)msg :(JSCallback)completion{
    
    if ([CLLocationManager locationServicesEnabled]) {//监测权限设置
        self.locationManager = [[CLLocationManager alloc]init];
        self.locationManager.delegate = self;//设置代理
        self.locationManager.desiredAccuracy = kCLLocationAccuracyBest;//设置精度
        self.locationManager.distanceFilter = kCLDistanceFilterNone;//距离过滤
        [self.locationManager requestAlwaysAuthorization];//位置权限申请
        [self.locationManager startUpdatingLocation];//开始定位
    }
        completion(@"111",YES);
}
    
- (void)initBridge{
    CGRect bounds=self.view.bounds;
    dwebview=[[DWKWebView alloc] initWithFrame:CGRectMake(0, 25, bounds.size.width, bounds.size.height-25)];
    [self.view addSubview:dwebview];
    
    [dwebview addJavascriptObject:[[MyDataBaseTest alloc] init] namespace:@"database"];
    [dwebview addJavascriptObject:[[ViewController alloc] init] namespace:@"photo"];
    [dwebview addJavascriptObject:[[ViewController alloc] init] namespace:@"my"];
    [dwebview addJavascriptObject:[[MyQRCodeTools alloc] init] namespace:@"QRCode"];
    // open debug mode, Release mode should disable this.
    [dwebview setDebugMode:true];
    
//    [dwebview customJavascriptDialogLabelTitles:@{@"alertTitle":@"Notification",@"alertBtn":@"OK"}];
    
    dwebview.navigationDelegate=self;
    
    // load test.html
    NSString *path = [[NSBundle mainBundle] bundlePath];
    NSURL *baseURL = [NSURL fileURLWithPath:path];
    NSString * htmlPath = [[NSBundle mainBundle] pathForResource:@"test"
                                                          ofType:@"html"];
    NSString * htmlContent = [NSString stringWithContentsOfFile:htmlPath
                                                       encoding:NSUTF8StringEncoding
                                                          error:nil];
    [dwebview loadHTMLString:htmlContent baseURL:baseURL];
    
    
    UIButton *b = [UIButton buttonWithType:UIButtonTypeCustom];
    [b setTitle:@"调用h5" forState:UIControlStateNormal];
    b.backgroundColor = [UIColor blackColor];
    [b setTitleColor:[UIColor redColor] forState:UIControlStateNormal];
    [b addTarget:self action:@selector(hehehe:) forControlEvents:UIControlEventTouchUpInside];
    [self.view addSubview:b];
    b.frame = CGRectMake(100, 500, 100, 100);
    NSLog(@"加载完成");
}
    
- (void)hehehe:(UIButton *)sender{
    [dwebview callHandler:@"addValue" arguments:@[@3,@4] completionHandler:^(NSNumber* value){
        UIAlertView *v = [[UIAlertView alloc]initWithTitle:@"回调结果" message:value.stringValue delegate:self cancelButtonTitle:@"cancle" otherButtonTitles:nil, nil];
        [v show];
    }];
}
    
#pragma mark location代理
-(void)locationManager:(CLLocationManager *)manager didFailWithError:(NSError *)error {
//    UIAlertController *alert = [UIAlertController alertControllerWithTitle:@"提示" message:@"您还未开启定位服务，是否需要开启？" preferredStyle:UIAlertControllerStyleAlert];
//    UIAlertAction *cancel = [UIAlertAction actionWithTitle:@"取消" style:UIAlertActionStyleCancel handler:^(UIAlertAction * _Nonnull action) {
//    }];
//    UIAlertAction *queren = [UIAlertAction actionWithTitle:@"确定" style:UIAlertActionStyleDefault handler:^(UIAlertAction * _Nonnull action) {
//        NSURL *setingsURL = [NSURL URLWithString:UIApplicationOpenSettingsURLString];
//        [[UIApplication sharedApplication] openURL:setingsURL];
//    }];
//    [alert addAction:cancel];
//    [alert addAction:queren];
//    [self presentViewController:alert animated:YES completion:nil];
}
    
-(void)locationManager:(CLLocationManager *)manager didUpdateLocations:(NSArray<CLLocation *> *)locations {
    //[self.locationManager stopUpdatingLocation];//停止定位
    //地理反编码
    CLLocation *currentLocation = [locations lastObject];
    CLGeocoder *geoCoder = [[CLGeocoder alloc]init];
    //当系统设置为其他语言时，可利用此方法获得中文地理名称
    NSMutableArray
    *userDefaultLanguages = [[NSUserDefaults standardUserDefaults]objectForKey:@"AppleLanguages"];
    // 强制 成 简体中文
    [[NSUserDefaults standardUserDefaults] setObject:[NSArray arrayWithObjects:@"zh-hans", nil]forKey:@"AppleLanguages"];
    [geoCoder reverseGeocodeLocation:currentLocation completionHandler:^(NSArray<CLPlacemark *> * _Nullable placemarks, NSError * _Nullable error) {
        if (placemarks.count > 0) {
            CLPlacemark *placeMark = placemarks[0];
            NSString *city = placeMark.locality;
            if (!city) {
//                self.currentCity = @"⟳定位获取失败,点击重试";
            } else {
                self.currentCity = placeMark.locality ;//获取当前城市
                static dispatch_once_t onceToken;
                dispatch_once(&onceToken, ^{
                    UIAlertView *av = [[UIAlertView alloc]initWithTitle:@"native" message:self.currentCity delegate:self cancelButtonTitle:@"cancle" otherButtonTitles:nil, nil];
                    [av show];
                });
            }
            
        } else if (error == nil && placemarks.count == 0 ) {
        } else if (error) {
//            self.currentCity = @"⟳定位获取失败,点击重试";
        }
        // 还原Device 的语言
        [[NSUserDefaults
          standardUserDefaults] setObject:userDefaultLanguages
         forKey:@"AppleLanguages"];
    }];
    [dwebview callHandler:@"delegate.addValue" arguments:@[@3,@4] completionHandler:^(NSNumber* value){
        dispatch_async(dispatch_get_main_queue(), ^{
            UIAlertView *v = [[UIAlertView alloc]initWithTitle:@"回调结果" message:value.stringValue delegate:self cancelButtonTitle:@"cancle" otherButtonTitles:nil, nil];
            [v show];
        });
    }];
    [dwebview evaluateJavaScript:@"addValue(3,4)" completionHandler:^(id _Nullable result, NSError * _Nullable error) {
        NSLog(@"result:%@,error:%@",result,error);
    }];
    NSLog(@"%@",[NSThread currentThread]);
    NSLog(@"321321312");
    
}

@end
