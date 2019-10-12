package com.mysoft.image_handle;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by Zourw on 2019/6/4.
 */
public class ImageUtils {
    private static final Bitmap.Config DEF_CONFIG = Bitmap.Config.RGB_565;

    public static Bitmap scale(File file, ImageProcessOption option) throws Exception {
        if (option.isKeepOrigin()) {
            Bitmap bitmap = null;
            try {
                bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
                return bitmap.copy(DEF_CONFIG, true);
            } finally {
                if (bitmap != null) {
                    bitmap.recycle();
                }
            }
        }

        int reqWidth = option.getWidth();
        int reqHeight = option.getHeight();
        BitmapFactory.Options options = new BitmapFactory.Options();

        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(file.getAbsolutePath(), options);

        if (options.outWidth > reqWidth || options.outHeight > reqHeight) {
            int suitedValue = Math.max(reqWidth, reqHeight);
            int widthRatio = options.outWidth / suitedValue;
            int heightRatio = options.outHeight / suitedValue;
            options.inSampleSize = Math.max(widthRatio, heightRatio);
        }

        Bitmap bitmap = null;
        try {
            options.inJustDecodeBounds = false;
            bitmap = BitmapFactory.decodeFile(file.getAbsolutePath(), options);

            if (options.outWidth > reqWidth || options.outHeight > reqHeight) {
                float scale = Math.min(reqWidth * 1.f / options.outWidth, reqHeight * 1.f / options.outHeight);

                Matrix matrix = new Matrix();
                matrix.setScale(scale, scale);

                return Bitmap.createBitmap(bitmap, 0, 0, options.outWidth, options.outHeight, matrix, true);
            } else {
                return bitmap.copy(DEF_CONFIG, true);
            }
        } finally {
            if (bitmap != null) {
                bitmap.recycle();
            }
        }
    }

    public static void compress(File srcFile, File destFile, Bitmap bitmap, ImageProcessOption option) {
        int maxQuality = 100;
        ByteArrayOutputStream baos = null;
        try {
            baos = new ByteArrayOutputStream();

            bitmap.compress(Bitmap.CompressFormat.JPEG, maxQuality, baos);
            while (baos.size() > srcFile.length()) {
                maxQuality -= 5;
                baos.reset();
                bitmap.compress(Bitmap.CompressFormat.JPEG, maxQuality, baos);
            }
            baos.reset();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (baos != null) {
                try {
                    baos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(destFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, (int) (maxQuality * option.getRatio()), fos);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (bitmap != null) {
                bitmap.recycle();
            }
        }
    }
}
