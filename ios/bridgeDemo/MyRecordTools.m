//
//  MyRecordTools.m
//  bridgeDemo
//
//  Created by huitong on 2019/8/2.
//  Copyright © 2019 duanfeng. All rights reserved.
//

#import "MyRecordTools.h"
#import <AVFoundation/AVFoundation.h>

@interface MyRecordTools ()<AVAudioRecorderDelegate,AVAudioPlayerDelegate>

@end

@implementation MyRecordTools{
    AVAudioRecorder *recorder;
    AVAudioPlayer *player;
    NSTimer *recordTimer;//录音计时器
    NSTimer *playTimer;//播放计时器
    NSInteger recordSecond;//录音时间
    UILabel *beginRecordLabel;//录音倒计时
    NSInteger playSecond;//播放时间
    UILabel *playLimitLabel;//播放倒计时
    NSURL *tmpUrl;
    NSURL *mp3Url;
    UIImageView *recordImg;
    UIImageView *stopImg;
    UIImageView *playImg;
}

+ (instancetype)shareTools{
    static dispatch_once_t onceToken;
    static MyRecordTools *instance;
    dispatch_once(&onceToken, ^{
        instance = [[self alloc]init];
    });
    return instance;
}

- (void)recordingAction {
    AVAudioSession *audioSession = [AVAudioSession sharedInstance];
    [audioSession setCategory:AVAudioSessionCategoryRecord error:nil];
    
    //录音设置
    NSMutableDictionary *recordSettings = [[NSMutableDictionary alloc] init];
    //录音格式 无法使用
    [recordSettings setValue :[NSNumber numberWithInt:kAudioFormatLinearPCM] forKey: AVFormatIDKey];
    //采样率
    [recordSettings setValue :[NSNumber numberWithFloat:11025.0] forKey: AVSampleRateKey];//44100.0
    //通道数
    [recordSettings setValue :[NSNumber numberWithInt:2] forKey: AVNumberOfChannelsKey];
    //线性采样位数
    //[recordSettings setValue :[NSNumber numberWithInt:16] forKey: AVLinearPCMBitDepthKey];
    //音频质量,采样质量
    [recordSettings setValue:[NSNumber numberWithInt:AVAudioQualityMin] forKey:AVEncoderAudioQualityKey];
    
    NSError *error = nil;
    NSString *recordUrl = [NSSearchPathForDirectoriesInDomains(NSDocumentDirectory, NSUserDomainMask, YES) objectAtIndex:0];
    tmpUrl = [NSURL URLWithString:[recordUrl stringByAppendingPathComponent:@"selfRecord.caf"]];
    
    recorder = [[AVAudioRecorder alloc]initWithURL:tmpUrl settings:recordSettings error:&error];
    
    if (recorder) {
        //启动或者恢复记录的录音文件
        if ([recorder prepareToRecord] == YES) {
            [recorder record];
            recordImg.hidden = YES;
            playImg.hidden = YES;
            stopImg.hidden = NO;
            beginRecordLabel.hidden = NO;
            playLimitLabel.hidden = YES;
            
            recordSecond = 0;
        }
    }else{
        NSLog(@"录音创建失败");
    }
}

- (void)stopAction {
    NSLog(@"停止录音");
    //停止录音
    [recorder stop];
    recorder = nil;
    [recordTimer invalidate];
    
    playLimitLabel.text = [NSString stringWithFormat:@"%lds",(long)recordSecond];
    playImg.hidden = NO;
    recordImg.hidden = NO;
    stopImg.hidden = YES;
    beginRecordLabel.hidden = YES;
    playLimitLabel.hidden = NO;
}

//播放录音
- (void)playAction {
    AVAudioSession *audioSession = [AVAudioSession sharedInstance];
    [audioSession setCategory:AVAudioSessionCategoryPlayback error:nil];
    NSError *playError;
    
    player = [[AVAudioPlayer alloc] initWithContentsOfURL:tmpUrl error:&playError];
    //当播放录音为空, 打印错误信息
    if (player == nil) {
        NSLog(@"Error crenting player: %@", [playError description]);
    }else {
        player.delegate = self;
        NSLog(@"开始播放");
        //开始播放
        playSecond = recordSecond;
        if ([player prepareToPlay] == YES) {
            playImg.userInteractionEnabled = NO;
            [player play];
            playTimer = [NSTimer scheduledTimerWithTimeInterval:1 target:self selector:@selector(playSecondChange) userInfo:nil repeats:YES];
            [playTimer fire];
        }
    }
}

//播放计时
- (void)playSecondChange {
    playSecond --;
    if (playSecond <= 0) {
        playSecond = 0;
        [playTimer invalidate];
    }
    playLimitLabel.text = [NSString stringWithFormat:@"%lds",(long)playSecond];
}

//当播放结束后调用这个方法
- (void)audioPlayerDidFinishPlaying:(AVAudioPlayer *)player successfully:(BOOL)flag{
    [playTimer invalidate];
    playLimitLabel.text = [NSString stringWithFormat:@"%lds",(long)recordSecond];
    playImg.userInteractionEnabled = YES;
}

@end
