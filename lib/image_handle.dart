import 'dart:async';

import 'package:flutter/services.dart';

class ImageHandle {
  static const MethodChannel _channel = const MethodChannel('image_handle');

  static Future<Map> imageCompress(Map params) async {
    assert(params != null);
    final Map result = await _channel.invokeMethod('imageCompress', params);
    if (!result.containsKey('path')) {
      result['path'] = params['path'];
    }
    return result;
  }
}
