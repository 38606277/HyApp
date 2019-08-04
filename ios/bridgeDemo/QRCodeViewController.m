//
//  QRCodeViewController.m
//  bridgeDemo
//
//  Created by huitong on 2019/8/2.
//  Copyright © 2019 duanfeng. All rights reserved.
//

#import "QRCodeViewController.h"
#import "MyQRCodeTools.h"
#import <AVFoundation/AVFoundation.h>

@interface QRCodeViewController ()
/** 回话对象属性*/
@property (nonatomic, strong) AVCaptureSession *session;

/** 摄像头预览图层*/
@property (nonatomic, strong) AVCaptureVideoPreviewLayer *previewLayer;
@end

@implementation QRCodeViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    self.view.backgroundColor = [UIColor whiteColor];
    // Do any additional setup after loading the view.
    MyQRCodeTools *tools = [[MyQRCodeTools alloc]init];
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
    
    // 设置输出对象代理
    [output setMetadataObjectsDelegate:self queue:dispatch_get_main_queue()];
    
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
        [[NSNotificationCenter defaultCenter] postNotificationName:@"QRCode" object:obj.stringValue];
        [self.navigationController popViewControllerAnimated:YES];
    }
}
// 播放音效成功回调
void soundCompleteCallback (){
    
}

@end
