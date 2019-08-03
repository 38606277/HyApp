//
//  ViewController+Photo.m
//  bridgeDemo
//
//  Created by huitong on 2019/7/26.
//  Copyright © 2019 duanfeng. All rights reserved.
//

#import "ViewController+Photo.h"
#import "ImageViewController.h"

@interface ViewController ()<UIImagePickerControllerDelegate, UINavigationControllerDelegate>

@end

@implementation ViewController (Photo)
    
- (void)photo:(NSString *)msg :(JSCallback)comp{
    UIAlertController *a = [UIAlertController alertControllerWithTitle:@"相机相册" message:@"" preferredStyle:UIAlertControllerStyleActionSheet];
    UIAlertAction *first = [UIAlertAction actionWithTitle:@"相机" style:UIAlertActionStyleDefault handler:^(UIAlertAction * _Nonnull action) {
        [self camera:UIImagePickerControllerSourceTypeCamera];
    }];
    UIAlertAction *second = [UIAlertAction actionWithTitle:@"相册" style:UIAlertActionStyleDefault handler:^(UIAlertAction * _Nonnull action) {
        [self camera:UIImagePickerControllerSourceTypePhotoLibrary];
    }];
    [a addAction:first];
    [a addAction:second];
    [[UIApplication sharedApplication].keyWindow.rootViewController presentViewController:a animated:YES completion:nil];
//    comp(@"com",YES);
}
    
- (void)camera:(UIImagePickerControllerSourceType)type{
// 创建UIImagePickerController实例
    UIImagePickerController *imagePickerController = [[UIImagePickerController alloc] init];
    // 设置代理
    imagePickerController.delegate = self;
    // 是否显示裁剪框编辑（默认为NO），等于YES的时候，照片拍摄完成可以进行裁剪
    imagePickerController.allowsEditing = YES;
    // 设置照片来源为相机
    imagePickerController.sourceType = type;
    // 设置进入相机时使用前置或后置摄像头
    if (type == UIImagePickerControllerSourceTypeCamera) {
        imagePickerController.cameraDevice = UIImagePickerControllerCameraDeviceRear;
    }
    // 展示选取照片控制器
    //    [self presentViewController:imagePickerController animated:YES completion:nil];
    [[UIApplication sharedApplication].keyWindow.rootViewController presentViewController:imagePickerController animated:YES completion:nil];
}
    
#pragma mark - UIImagePickerControllerDelegate
    // 完成图片的选取后调用的方法
- (void)imagePickerController:(UIImagePickerController *)picker didFinishPickingMediaWithInfo:(NSDictionary *)info {
    // 选取完图片后跳转回原控制器
    [picker dismissViewControllerAnimated:YES completion:nil];
    /* 此处参数 info 是一个字典，下面是字典中的键值 （从相机获取的图片和相册获取的图片时，两者的info值不尽相同）
     * UIImagePickerControllerMediaType; // 媒体类型
     * UIImagePickerControllerOriginalImage; // 原始图片
     * UIImagePickerControllerEditedImage; // 裁剪后图片
     * UIImagePickerControllerCropRect; // 图片裁剪区域（CGRect）
     * UIImagePickerControllerMediaURL; // 媒体的URL
     * UIImagePickerControllerReferenceURL // 原件的URL
     * UIImagePickerControllerMediaMetadata // 当数据来源是相机时，此值才有效
     */
    // 从info中将图片取出，并加载到imageView当中
    UIImage *image = [info objectForKey:UIImagePickerControllerOriginalImage];
    
    [[NSNotificationCenter defaultCenter] postNotificationName:@"photo" object:image];
}
    
    // 取消选取调用的方法
- (void)imagePickerControllerDidCancel:(UIImagePickerController *)picker {
    [[UIApplication sharedApplication].keyWindow.rootViewController dismissViewControllerAnimated:YES completion:nil];
}
    
@end
