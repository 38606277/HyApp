//
//  QRCodeViewController.m
//  bridgeDemo
//
//  Created by huitong on 2019/8/2.
//  Copyright © 2019 duanfeng. All rights reserved.
//

#import "QRCodeViewController.h"
#import <AVFoundation/AVFoundation.h>

#define kScreenW [UIScreen mainScreen].bounds.size.width
#define kScreenH [UIScreen mainScreen].bounds.size.height
@interface QRCodeViewController ()<AVCaptureMetadataOutputObjectsDelegate,AVCaptureVideoDataOutputSampleBufferDelegate,CAAnimationDelegate>

/** 回话对象属性*/
@property (nonatomic, strong) AVCaptureSession *session;
/** 摄像头预览图层*/
@property (nonatomic, strong) AVCaptureVideoPreviewLayer *previewLayer;
@property (nonatomic,strong) AVCaptureVideoDataOutput *videoDataOutput;
@property (nonatomic,strong) CALayer *scanLineLayer;

@end

@implementation QRCodeViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    self.view.backgroundColor = [UIColor whiteColor];
    // Do any additional setup after loading the view.
    // 创建回话对象
    self.session = [[AVCaptureSession alloc] init];
    
    // 设置回话对象图像采集率
    [self.session setSessionPreset:AVCaptureSessionPresetHigh];
    
    // 获取摄像头设备
    AVCaptureDevice *device = [AVCaptureDevice defaultDeviceWithMediaType:AVMediaTypeVideo];
    
    // 创建回话输入对象
    NSError *inputError = nil;
    AVCaptureDeviceInput *input = [AVCaptureDeviceInput deviceInputWithDevice:device error:&inputError];
    
    // 添加会话输入
    [self.session addInput:input];
    // 创建输出对象
    AVCaptureMetadataOutput *output = [[AVCaptureMetadataOutput alloc] init];
    self.videoDataOutput = [[AVCaptureVideoDataOutput alloc] init];
    [_videoDataOutput setSampleBufferDelegate:self queue:dispatch_get_main_queue()];
    [_session addOutput:_videoDataOutput];
    // 设置输出对象代理
    [output setMetadataObjectsDelegate:self queue:dispatch_get_main_queue()];
    // 设置扫描范围（每一个取值0～1，以屏幕右上角为坐标原点）
    // 注：微信二维码的扫描范围是整个屏幕，这里并没有做处理（可不用设置）;
    // 如需限制扫描框范围，打开下一句注释代码并进行相应调整
//    output.rectOfInterest = CGRectMake(0.05, 0.2, 0.7, 0.6);
//    output.rectOfInterest = CGRectMake((self.view.center.x - 100) / kScreenW, (self.view.center.y - 100) / self.view.bounds.size.height, 200 / kScreenW, 200 / self.view.bounds.size.height);
    // 添加会话输出
    [self.session addOutput:output];
    
    // 设置输出数据类型,需要将元数据输出添加到回话后,才能制定元数据,否则会报错
    // 二维码和条形码可以一起设置
    output.metadataObjectTypes = @[AVMetadataObjectTypeQRCode, AVMetadataObjectTypeEAN13Code,  AVMetadataObjectTypeEAN8Code, AVMetadataObjectTypeCode128Code];
    
    // 创建摄像头预览图层
    self.previewLayer = [AVCaptureVideoPreviewLayer layerWithSession:self.session];
    
    self.previewLayer.videoGravity = AVLayerVideoGravityResizeAspectFill;
    
    self.previewLayer.frame = self.view.frame;
    
    // 将图层插入当前图层
    [self.view.layer insertSublayer:self.previewLayer atIndex:0];
    
    // 启动会话
    [self.session startRunning];
    
    // 扫描线+动画
    self.scanLineLayer = [CALayer layer];
    self.scanLineLayer.backgroundColor = [UIColor redColor].CGColor;
    self.scanLineLayer.bounds = CGRectMake(0, 0, 200, 1);
    self.scanLineLayer.position = self.view.center;
    [self.previewLayer addSublayer:self.scanLineLayer];
    
    CABasicAnimation *animation = [CABasicAnimation animationWithKeyPath:@"position"];
    animation.duration = 2;
    animation.delegate = self;
    animation.fromValue = @(CGPointMake(self.view.center.x, self.view.center.y - 100));
    animation.toValue = @(CGPointMake(self.view.center.x, self.view.center.y + 100));
    animation.timingFunction = [CAMediaTimingFunction functionWithName:kCAMediaTimingFunctionLinear];
    animation.repeatCount = MAXFLOAT;
    [self.scanLineLayer addAnimation:animation forKey:@"scanLineLayerPosition"];
}

- (void)captureOutput:(AVCaptureOutput *)output didOutputMetadataObjects:(NSArray<__kindof AVMetadataObject *> *)metadataObjects fromConnection:(AVCaptureConnection *)connection {
    
    // 1、如果扫描完成，停止会话
    [self.session stopRunning];
    
    // 2、删除预览图层
    [self.previewLayer removeFromSuperlayer];
    
    // 3、设置界面显示扫描结果
    if (metadataObjects && metadataObjects.count > 0) {
        AVMetadataMachineReadableCodeObject *obj = metadataObjects.firstObject;
        // 取出二维码扫描到的内容
        NSLog(@"二维码内容：：：%@",obj.stringValue);
//        [[NSNotificationCenter defaultCenter] postNotificationName:@"QRCode" object:obj.stringValue];
        self.resultBlock(obj.stringValue, YES);
        [self.navigationController popViewControllerAnimated:YES];
    }
}

//- (void)captureOutput:(AVCaptureOutput *)captureOutput didOutputSampleBuffer:(CMSampleBufferRef)sampleBuffer fromConnection:(AVCaptureConnection *)connection {
//    // 这个方法会时时调用，但内存很稳定
//    CFDictionaryRef metadataDict = CMCopyDictionaryOfAttachments(NULL,sampleBuffer, kCMAttachmentMode_ShouldPropagate);
//    NSDictionary *metadata = [[NSMutableDictionary alloc] initWithDictionary:(__bridge NSDictionary*)metadataDict];
//    CFRelease(metadataDict);
//    NSDictionary *exifMetadata = [[metadata objectForKey:(NSString *)kCGImagePropertyExifDictionary] mutableCopy];
//    float brightnessValue = [[exifMetadata objectForKey:(NSString *)kCGImagePropertyExifBrightnessValue] floatValue];
//    NSLog(@"%f",brightnessValue);
//    if (brightnessValue < - 1) {
//        [self.view addSubview:self.lightBtn];
//    } else {
//        if (self.isSelectedFlashlightBtn == NO) {
//            [self removeFlashlightBtn];
//        }
//    }
//}

//- (void)lightBtnAction:(UIButton *)button {
//    if (button.selected == NO) {
//        /** 打开手电筒 */
//        AVCaptureDevice *captureDevice = [AVCaptureDevice defaultDeviceWithMediaType:AVMediaTypeVideo];
//        NSError *error = nil;
//        if ([captureDevice hasTorch]) {
//            BOOL locked = [captureDevice lockForConfiguration:&error];
//            if (locked) {
//                captureDevice.torchMode = AVCaptureTorchModeOn;
//                [captureDevice unlockForConfiguration];
//            }
//        }
//        self.isSelectedFlashlightBtn = YES;
//        button.selected = YES;
//    } else {
//        dispatch_after(dispatch_time(DISPATCH_TIME_NOW, (int64_t)(0.2 * NSEC_PER_SEC)),                  dispatch_get_main_queue(), ^{
//            AVCaptureDevice *device = [AVCaptureDevice defaultDeviceWithMediaType:AVMediaTypeVideo];
//            if ([device hasTorch]) {
//                [device lockForConfiguration:nil];
//                [device setTorchMode: AVCaptureTorchModeOff];
//                [device unlockForConfiguration];
//            }
//            self.isSelectedFlashlightBtn = NO;
//            self.flashlightBtn.selected = NO;
//            [self.flashlightBtn removeFromSuperview];
//        });
//
//    }
//}

@end
