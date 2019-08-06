//
//  LocationViewController.m
//  bridgeDemo
//
//  Created by huitong on 2019/8/5.
//  Copyright © 2019 duanfeng. All rights reserved.
//

#import "LocationViewController.h"
#import <AMapFoundationKit/AMapFoundationKit.h>
#import <AMapLocationKit/AMapLocationKit.h>
#import <dsBridge/dsBridge.h>

@interface LocationViewController ()

@property (nonatomic,strong) AMapLocationManager *locationManager;

@end

@implementation LocationViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view.
    
}

-(void)getLocationInfo:(NSString *)msg :(JSCallback)compltetion{
    [AMapServices sharedServices].apiKey =@"120593b180e6b7f9ae272d16c05f7170";
    // 带逆地理信息的一次定位（返回坐标和地址信息）
    [self.locationManager setDesiredAccuracy:kCLLocationAccuracyHundredMeters];
    //   定位超时时间，最低2s，此处设置为2s
    self.locationManager.locationTimeout =2;
    //   逆地理请求超时时间，最低2s，此处设置为2s
    self.locationManager.reGeocodeTimeout = 2;
    // 带逆地理信息的一次定位（返回坐标和地址信息）
    [self.locationManager setDesiredAccuracy:kCLLocationAccuracyBest];
    //   定位超时时间，最低2s，此处设置为10s
    self.locationManager.locationTimeout =2;
    //   逆地理请求超时时间，最低2s，此处设置为10s
    self.locationManager.reGeocodeTimeout = 2;
    [self.locationManager requestLocationWithReGeocode:NO completionBlock:^(CLLocation *location, AMapLocationReGeocode *regeocode, NSError *error) {
        
        if (error)
        {
            NSLog(@"locError:{%ld - %@};", (long)error.code, error.localizedDescription);
            
            if (error.code == AMapLocationErrorLocateFailed)
            {
                return;
            }
        }
        
        NSLog(@"location:%@", location);
        
        if (regeocode)
        {
            NSLog(@"reGeocode:%@", regeocode);
        }
    }];
}

/*
#pragma mark - Navigation

// In a storyboard-based application, you will often want to do a little preparation before navigation
- (void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender {
    // Get the new view controller using [segue destinationViewController].
    // Pass the selected object to the new view controller.
}
*/

@end
