#import "ImageHandlePlugin.h"

@implementation ImageHandlePlugin
+ (void)registerWithRegistrar:(NSObject<FlutterPluginRegistrar>*)registrar {
  FlutterMethodChannel* channel = [FlutterMethodChannel
      methodChannelWithName:@"image_handle"
            binaryMessenger:[registrar messenger]];
  ImageHandlePlugin* instance = [[ImageHandlePlugin alloc] init];
  [registrar addMethodCallDelegate:instance channel:channel];
}

- (void)handleMethodCall:(FlutterMethodCall*)call result:(FlutterResult)result {
  if ([@"imageCompress" isEqualToString:call.method]) {

      NSDictionary *args = call.arguments;
      NSString *path = args[@"path"];
      result([self imageCompress:path params:args]);
  } else {
    result(FlutterMethodNotImplemented);
  }
}

- (NSDictionary *)imageCompress:(NSString *)path params:(NSDictionary *)params{

    NSFileManager *fm = [NSFileManager defaultManager];
    UIImage *image = [UIImage imageWithContentsOfFile:path];
    NSDictionary *dic;
    if (![fm fileExistsAtPath:path] || !image) {

        dic = @{@"code":@(-1),@"msg":@"此路径下图片不存在"};
        return dic;
    }else{

        float ratio = 0.9;
        CGFloat scale = [UIScreen mainScreen].scale;
        CGFloat width = 0;
        CGFloat height = 0;
        CGSize targetSize = CGSizeZero;
        if ([params isKindOfClass:[NSDictionary class]]) {

            ratio = [params[@"ratio"] floatValue];
            width = [params[@"width"] floatValue]/scale;
            height = [params[@"height"] floatValue]/scale;
        }
        if (width > 0 && height > 0) {

            targetSize = (CGSize){width,height};
        }else{

            //没有设置宽高，则取默认宽高
            CGFloat bWid = CGRectGetWidth([UIScreen mainScreen].bounds);
            CGFloat bHeight = CGRectGetHeight([UIScreen mainScreen].bounds);
            targetSize = (CGSize){bWid,bHeight};
        }
        UIImage  *newImage;
        CGSize imageSize = (CGSize){image.size.width/scale,image.size.height/scale};
        //压缩尺寸和图片尺寸一样，或者传入宽高>图片真实宽高，直接返回原图,只允许缩小
        if (CGSizeEqualToSize(imageSize,targetSize) ||
            (imageSize.width < targetSize.width && imageSize.height < targetSize.height)) {

            newImage = image;
        }else{

            targetSize = imageSize;
            newImage = [self imageCompressForSize:image targetSize:targetSize];
        }
        NSData *imgData = UIImageJPEGRepresentation(newImage, ratio);
        BOOL success = [imgData writeToFile:path atomically:YES];
        if (success) {

            dic = @{@"code":@(0),@"msg":@"图片压缩成功"};
        }else{
            dic = @{@"code":@(-1),@"msg":@"图片压缩失败"};
        }
        return dic;
    }
}

-(UIImage *) imageCompressForSize:(UIImage *)sourceImage targetSize:(CGSize)size{

    @autoreleasepool {

        UIImage *tmpImage = sourceImage;
        CGSize imageSize = tmpImage.size;
        CGFloat width = imageSize.width;
        CGFloat height = imageSize.height;
        CGSize targetSize = size;
        CGFloat widthFactor = targetSize.width / width;
        CGFloat heightFactor = targetSize.height / height;
        CGFloat scaleFactor = MIN(widthFactor, heightFactor);
        CGSize newSize = CGSizeMake(width*scaleFactor, height*scaleFactor);

        UIGraphicsBeginImageContextWithOptions(newSize, NO, 0);
        [tmpImage drawInRect:CGRectMake(0,0,newSize.width,newSize.height)];
        UIImage* newImage = UIGraphicsGetImageFromCurrentImageContext();
        UIGraphicsEndImageContext();
        sourceImage = nil;
        return newImage;
    }
}

@end
