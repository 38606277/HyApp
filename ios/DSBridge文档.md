# 自用DSBridge文档

## 接入步骤

### 创建webView容器

```objective-c
DWKWebView * dwebview=[[DWKWebView alloc] initWithFrame:bounds];
[dwebview addJavascriptObject:[[JsApiTest alloc] init] namespace:nil];
```

### 目前规则时对不同功能模块进行分类处理，利用命名空间进行区分

例如：

```objective-c
// 数据库相关功能
[dwebview addJavascriptObject:[[MyDataBaseTest alloc] init] namespace:@"database"];
// 相机相册相关功能
[dwebview addJavascriptObject:[[ViewController alloc] init] namespace:@"photo"];
// 定位相关功能
[dwebview addJavascriptObject:[[ViewController alloc] init] namespace:@"my"];
```

### API定义规则

#### 同步API.

**`(id) handler:(id) msg`**

参数可以是任何类型, 但是返回值类型不能为 **void。** **如果不需要参数，也必须声明**，声明后不使用就行。

> 如果同步API返回值类型为void，调用时则会导致Crash，请务必遵守签名规范。

#### 异步 API.

**` (void) handler:(id)arg :(void (^)( id result,BOOL complete))completionHandler）`**

`JSCallback` 是一个block类型:

```objective-c
typedef void (^JSCallback)(NSString * _Nullable result,BOOL complete); 
```

> 注意：API名字**不能**以"init"开始，因为oc的类中是被预留的, 如果以"init"开始，执行结果将无法预期(很多时候会crash)。

例：

```objective-c
- (void)readDB:(NSString *)msg :(JSCallback)completion;
```

JS端调用示例：

```javascript
function callAsyn() {
        dsBridge.call("database.readDB","hello", function (v) {
            alert(v)
        })
    }
```

#### OC调用JS方法

```objective-c
/** 
callHandler JS方法名，命名规则为“空间名方法名” 没有空间名的直接使用"方法名"
arguments: 参数 数组传递
completionHandler： 回调结果
*/
[dwebview callHandler:@"delegate.addValue" arguments:@[@3,@4] completionHandler:^(NSNumber* value){
        dispatch_async(dispatch_get_main_queue(), ^{
            UIAlertView *v = [[UIAlertView alloc]initWithTitle:@"回调结果" message:value.stringValue delegate:self cancelButtonTitle:@"cancle" otherButtonTitles:nil, nil];
            [v show];
        });
    }];
```

**现发现系统代理中调用JS方法没有回调，如定位回调。未解决问题。目测可以使用`[webview evaluateJavaScript:completionHandler:]`处理**