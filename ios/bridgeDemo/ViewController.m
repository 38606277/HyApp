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
#import "NetworkTools.h"
#import <Reachability.h>
#import <ContactsUI/ContactsUI.h>
#import "MyRecordTools.h"
#import "MyLocationTest.h"
#import "MyQRCodeTools.h"
#import "LocationViewController.h"
#import "DFNetworkingManager.h"
#import "AppDelegate.h"

@interface ViewController ()<WKUIDelegate,CLLocationManagerDelegate,CNContactPickerDelegate,CNContactViewControllerDelegate,WKNavigationDelegate>
    
@property (nonatomic, strong) CLLocationManager *locationManager;//设置manager
@property (nonatomic, strong) NSString *currentCity;
@property (nonatomic,strong) UIImageView *im;
@property (nonatomic,strong) UILabel *l;
@property (nonatomic,strong) NSDictionary *pushDic;
@property (nonatomic,strong) UIImageView *launchScreenImageView;
@end

@implementation ViewController{
    
}

- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view.
    [self initBridge];
    [self addLaunch];
    UIImageView *im = [[UIImageView alloc]initWithFrame:CGRectMake(0, 400, 100, 100)];
    im.contentMode = UIViewContentModeScaleAspectFill;
    [self.view addSubview:im];
    self.im = im;
    UILabel *l = [[UILabel alloc]initWithFrame:CGRectMake(0, 500, 375, 100)];
    self.l = l;
    l.numberOfLines = 0;
    l.textColor = [UIColor blackColor];
    [self.view addSubview:l];
    [[NSNotificationCenter defaultCenter] addObserver:self selector:@selector(pushAction:) name:DemoPushNotifacationName object:nil];
}

- (void)pushAction:(NSNotification *)noti{
    self.pushDic = noti.userInfo;
    // webview已经加载完成直接callJS 未加载完成走完成的webview代理  未验证
    NSValue *first = self.pushDic[@"first"];
    NSValue *second = self.pushDic[@"second"];
        if (first != nil && second != nil) {
            [self.dwebview callHandler:@"addValue" arguments:@[first,second] completionHandler:^(id  _Nullable value) {
                UIAlertView *v = [[UIAlertView alloc]initWithTitle:@"notifation" message:value delegate:self cancelButtonTitle:@"cancle" otherButtonTitles:nil, nil];
                [v show];
            }];
        }
}

- (void)checkNet:(NSString *)msg :(JSCallback)completion{
    NetworkStatus status = [NetworkTools isConnectionAvailable];
    NSString *str;
    switch (status) {
        case NotReachable:
            str = @"没网";
            break;
        case ReachableViaWiFi:
            str = @"wifi";
            break;
        case ReachableViaWWAN:
            str = @"WWAN";
            break;
    }
    UIAlertView *v = [[UIAlertView alloc]initWithTitle:@"回调结果" message:str delegate:self cancelButtonTitle:@"cancle" otherButtonTitles:nil, nil];
    [v show];
}

- (void)callContact:(NSString *)msg :(JSCallback)completion{
    [self checkContact];
}

- (void)checkContact{
    CNContactPickerViewController *nav = [[CNContactPickerViewController alloc] init];
    nav.delegate = self;
    [[self topViewController] presentViewController:nav animated:YES completion:nil];
    
    CNAuthorizationStatus status = [CNContactStore authorizationStatusForEntityType:CNEntityTypeContacts];
    if (status == CNAuthorizationStatusAuthorized) {
        
    }else{
        return;
    }
}

- (UIViewController *)topViewController {
    UIViewController *topVC;
    topVC = [self getTopViewController:[[UIApplication sharedApplication].keyWindow rootViewController]];
    while (topVC.presentedViewController) {
        topVC = [self getTopViewController:topVC.presentedViewController];
    }
    return topVC;
}

- (UIViewController *)getTopViewController:(UIViewController *)vc {
    if (![vc isKindOfClass:[UIViewController class]]) {
        return nil;
    }
    if ([vc isKindOfClass:[UINavigationController class]]) {
        return [self getTopViewController:[(UINavigationController *)vc topViewController]];
    } else if ([vc isKindOfClass:[UITabBarController class]]) {
        return [self getTopViewController:[(UITabBarController *)vc selectedViewController]];
    } else {
        return vc;
    }
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
}


    
- (void)initBridge{
    CGRect bounds=self.view.bounds;
    self.dwebview=[[DWKWebView alloc] initWithFrame:CGRectMake(0, 25, bounds.size.width, bounds.size.height-25)];
    [self.view addSubview:self.dwebview];
    
    [self.dwebview addJavascriptObject:[[MyDataBaseTest alloc] init] namespace:@"dbApi"];
    [self.dwebview addJavascriptObject:[[ViewController alloc] init] namespace:@"photo"];
    [self.dwebview addJavascriptObject:[[ViewController alloc] init] namespace:@"my"];
    [self.dwebview addJavascriptObject:[[MyQRCodeTools alloc] init] namespace:@"cameraApi"];
    [self.dwebview addJavascriptObject:[[MyLocationTest alloc] init] namespace:@"locationApi"];
//    [self.dwebview addJavascriptObject:[[DFNetworkingManager alloc] init] namespace:@"net"];
//    [dwebview addJavascriptObject:[[LocationViewController alloc] init] namespace:@"locationApi"];
    // open debug mode, Release mode should disable this.
    self.dwebview.allowsBackForwardNavigationGestures = YES;
    [self.dwebview setDebugMode:true];
    
//    [dwebview customJavascriptDialogLabelTitles:@{@"alertTitle":@"Notification",@"alertBtn":@"OK"}];
    
    self.dwebview.navigationDelegate=self;
    
    // load test.html
    NSString *path = [[NSBundle mainBundle] pathForResource:@"hhhhhBD" ofType:@"bundle"];
    NSURL *baseURL = [NSURL fileURLWithPath:path];
    NSBundle *bundle = [NSBundle bundleWithPath:path];
    NSString * htmlPath = [bundle pathForResource:@"index" ofType:@"html"];
    NSString * htmlContent = [NSString stringWithContentsOfFile:htmlPath
                                                       encoding:NSUTF8StringEncoding
                                                          error:nil];
    [self.dwebview loadHTMLString:htmlContent baseURL:baseURL];
    //
//    NSString *path = [[NSBundle mainBundle] bundlePath];
//    NSURL *baseURL = [NSURL fileURLWithPath:path];
//    NSBundle *bundle = [NSBundle bundleWithPath:path];
//    NSString * htmlPath = [bundle pathForResource:@"test" ofType:@"html"];
//    NSString * htmlContent = [NSString stringWithContentsOfFile:htmlPath
//                                                       encoding:NSUTF8StringEncoding
//                                                          error:nil];
//    [self.dwebview loadHTMLString:htmlContent baseURL:baseURL];
}

- (void)addLaunch{
    [[UIApplication sharedApplication].keyWindow addSubview:self.launchScreenImageView];
}
    
- (void)hehehe:(UIButton *)sender{
    [self.dwebview callHandler:@"addValue" arguments:@[@3,@4] completionHandler:^(NSNumber* value){
        UIAlertView *v = [[UIAlertView alloc]initWithTitle:@"回调结果" message:value.stringValue delegate:self cancelButtonTitle:@"cancle" otherButtonTitles:nil, nil];
        [v show];
    }];
}

- (void)callPhone:(NSString *)msg :(JSCallback)comp{
    [[UIApplication sharedApplication] openURL:[NSURL URLWithString:@"telprompt://10010"]];
}

- (void)startRecord:(NSString *)msg :(JSCallback)comp{
    [[MyRecordTools shareTools] recordingAction];
}

- (void)endRecord:(NSString *)msg :(JSCallback)comp{
    [[MyRecordTools shareTools] stopAction];
}

- (void)playRecord:(NSString *)msg :(JSCallback)comp{
    [[MyRecordTools shareTools] playAction];
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
                    UIAlertView *av = [[UIAlertView alloc]initWithTitle:@"native" message:[NSString stringWithFormat:@"当前地点%@%@%@%@",placeMark.locality,placeMark.subLocality,placeMark.thoroughfare,placeMark.name] delegate:self cancelButtonTitle:@"cancle" otherButtonTitles:nil, nil];
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
    [self.dwebview callHandler:@"delegate.addValue" arguments:@[@3,@4] completionHandler:^(NSNumber* value){
        dispatch_async(dispatch_get_main_queue(), ^{
            UIAlertView *v = [[UIAlertView alloc]initWithTitle:@"回调结果" message:value.stringValue delegate:self cancelButtonTitle:@"cancle" otherButtonTitles:nil, nil];
            [v show];
        });
    }];
    [self.dwebview evaluateJavaScript:@"addValue(3,4)" completionHandler:^(id _Nullable result, NSError * _Nullable error) {
        NSLog(@"result:%@,error:%@",result,error);
    }];
}

- (void)webView:(WKWebView *)webView didFinishNavigation:(WKNavigation *)navigation{
    [self.launchScreenImageView removeFromSuperview];
    NSNumber *first = self.pushDic[@"first"];
    NSNumber *second = self.pushDic[@"second"];
    if ([webView isKindOfClass:[DWKWebView class]]) {
        DWKWebView *wv = (DWKWebView*)webView;
        if (first != nil && second != nil) {
            [wv callHandler:@"addValue" arguments:@[first,second] completionHandler:^(id  _Nullable value) {
                UIAlertView *v = [[UIAlertView alloc]initWithTitle:@"didfinish" message:value delegate:self cancelButtonTitle:@"cancle" otherButtonTitles:nil, nil];
                [v show];
            }];
        }
    }
}

- (UIImageView *)launchScreenImageView{
    if (!_launchScreenImageView) {
        _launchScreenImageView = [[UIImageView alloc]initWithFrame:self.view.frame];
        _launchScreenImageView.image = [UIImage imageNamed:@"timg"];
        _launchScreenImageView.contentMode = UIViewContentModeScaleAspectFill;
    }
    return _launchScreenImageView;
}

@end
