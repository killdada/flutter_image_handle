package com.mysoft.image_handle;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.MethodChannel.MethodCallHandler;
import io.flutter.plugin.common.MethodChannel.Result;
import io.flutter.plugin.common.PluginRegistry.Registrar;

/**
 * ImageHandlePlugin
 */
public class ImageHandlePlugin implements MethodCallHandler {
    /**
     * Plugin registration.
     */
    public static void registerWith(Registrar registrar) {
        final MethodChannel channel = new MethodChannel(registrar.messenger(), "image_handle");
        channel.setMethodCallHandler(new ImageHandlePlugin(registrar));
    }

    private Registrar registrar;

    public ImageHandlePlugin(Registrar registrar) {
        this.registrar = registrar;
    }

    @SuppressLint("StaticFieldLeak")
    @Override
    public void onMethodCall(MethodCall call, final Result result) {
        if (call.method.equals("imageCompress")) {
            new AsyncTask<Map, Void, String>() {
                @Override
                protected String doInBackground(Map... maps) {
                    Map map = maps[0];
                    String filepath = (String) map.get("path");
                    float ratio = map.containsKey("ration") ? (float) map.get("ratio") : 1.f;
                    int width = map.containsKey("width") ? (int) map.get("width") : 0;
                    int height = map.containsKey("height") ? (int) map.get("height") : 0;

                    ImageProcessOption option = new ImageProcessOption();
                    option.setRatio(ratio);
                    option.setWidth(width);
                    option.setHeight(height);

                    File srcFile = new File(Uri.parse(filepath).getPath());
                    File destFile = new File(registrar.context().getExternalCacheDir(), srcFile.getName());

                    try {
                        // 缩放
                        Bitmap bitmap = ImageUtils.scale(srcFile, option);

                        // 质量压缩
                        ImageUtils.compress(srcFile, destFile, bitmap, option);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    return destFile.getAbsolutePath();
                }

                @Override
                protected void onPostExecute(final String path) {
                    Map<String,Object> map = new HashMap<>();
                    map.put("code", 0);
                    map.put("path", path);
                    result.success(map);
                }
            }.execute((Map) call.arguments);
        } else {
            result.notImplemented();
        }
    }
}
