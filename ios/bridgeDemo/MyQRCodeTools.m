//
//  MyQRCodeTools.m
//  bridgeDemo
//
//  Created by huitong on 2019/8/2.
//  Copyright © 2019 duanfeng. All rights reserved.
//

#import "MyQRCodeTools.h"
#import <AVFoundation/AVFoundation.h>
#import <dsBridge/dsBridge.h>
#import "QRCodeViewController.h"

@interface MyQRCodeTools ()<AVCaptureMetadataOutputObjectsDelegate, AVCaptureVideoDataOutputSampleBufferDelegate>
@property (nonatomic, strong) AVCaptureSession *session;
@property (nonatomic, strong) AVCaptureVideoDataOutput *videoDataOutput;
@property (nonatomic, strong) AVCaptureVideoPreviewLayer *videoPreviewLayer;
@property (nonatomic,strong) JSCallback qrBlock;
@property (nonatomic,strong) JSCallback qrBlock2;
@end

@implementation MyQRCodeTools

-(void)scanQrCode:(NSString *)msg :(JSCallback)compltetion{
    QRCodeViewController *vc = [QRCodeViewController new];
    vc.resultBlock = ^(id  _Nonnull result, BOOL b) {
        compltetion(result,YES);
    };
    [(UINavigationController *)[UIApplication sharedApplication].keyWindow.rootViewController pushViewController:vc animated:YES];
}

//-(void)startQRCode:(NSString *)msg :(JSCallback)compltetion{
//    self.qrBlock2 = compltetion;
//    // 1、获取摄像设备
//    AVCaptureDevice *device = [AVCaptureDevice defaultDeviceWithMediaType:AVMediaTypeVideo];
//
//    // 2、创建摄像设备输入流
//    AVCaptureDeviceInput *deviceInput = [AVCaptureDeviceInput deviceInputWithDevice:device error:nil];
//
//    // 3、创建元数据输出流
//    AVCaptureMetadataOutput *metadataOutput = [[AVCaptureMetadataOutput alloc] init];
//    [metadataOutput setMetadataObjectsDelegate:self queue:dispatch_get_main_queue()];
//
//    // 设置扫描范围（每一个取值0～1，以屏幕右上角为坐标原点）
//    // 注：微信二维码的扫描范围是整个屏幕，这里并没有做处理（可不用设置）;
//    // 如需限制扫描框范围，打开下一句注释代码并进行相应调整
//    //    metadataOutput.rectOfInterest = CGRectMake(0.05, 0.2, 0.7, 0.6);
//
//    // 4、创建会话对象
//    _session = [[AVCaptureSession alloc] init];
//    // 并设置会话采集率
//    _session.sessionPreset = AVCaptureSessionPreset1920x1080;
//
//    // 5、添加元数据输出流到会话对象
//    [_session addOutput:metadataOutput];
//
//    // 创建摄像数据输出流并将其添加到会话对象上,  --> 用于识别光线强弱
//    self.videoDataOutput = [[AVCaptureVideoDataOutput alloc] init];
//    [_videoDataOutput setSampleBufferDelegate:self queue:dispatch_get_main_queue()];
//    [_session addOutput:_videoDataOutput];
//
//    // 6、添加摄像设备输入流到会话对象
//    [_session addInput:deviceInput];
//
//    // 7、设置数据输出类型(如下设置为条形码和二维码兼容)，需要将数据输出添加到会话后，才能指定元数据类型，否则会报错
//    metadataOutput.metadataObjectTypes = @[AVMetadataObjectTypeQRCode, AVMetadataObjectTypeEAN13Code,  AVMetadataObjectTypeEAN8Code, AVMetadataObjectTypeCode128Code];
//
//    // 8、实例化预览图层, 用于显示会话对象
//    _videoPreviewLayer = [AVCaptureVideoPreviewLayer layerWithSession:_session];
//    // 保持纵横比；填充层边界
//    _videoPreviewLayer.videoGravity = AVLayerVideoGravityResizeAspectFill;
//    CGFloat x = 0;
//    CGFloat y = 0;
//    CGFloat w = [UIScreen mainScreen].bounds.size.width;
//    CGFloat h = [UIScreen mainScreen].bounds.size.height;
//    _videoPreviewLayer.frame = CGRectMake(x, y, w, h);
//    [[self topViewController].view.layer insertSublayer:_videoPreviewLayer atIndex:0];
//
//    // 9、启动会话
//    [_session startRunning];
//}

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

- (void)captureOutput:(AVCaptureOutput *)captureOutput didOutputMetadataObjects:(NSArray *)metadataObjects fromConnection:(AVCaptureConnection *)connection {
    NSLog(@"metadataObjects - - %@", metadataObjects);
    if (metadataObjects != nil && metadataObjects.count > 0) {
        AVMetadataMachineReadableCodeObject *obj = metadataObjects[0];
        NSLog(@"%@",[obj stringValue]);
        self.qrBlock([obj stringValue], YES);
        self.qrBlock2([obj stringValue], YES);
    } else {
        NSLog(@"暂未识别出扫描的二维码");
    }
}
@end
