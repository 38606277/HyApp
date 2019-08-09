package com.cannon.hy.utils;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;

import androidx.core.content.FileProvider;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import okio.BufferedSink;
import okio.BufferedSource;
import okio.Okio;

/**
 * 功能: 文件操作
 */
public final class FileUtils {

    private static final String AppFolderName = "HyApp";


    public static final String COMPRESS_IMAGE_FILE_DIR_PATH  = "/"+AppFolderName+"/compress/images";
    public static final String DOWNLOAD_IMAGE_FILE_DIR_PATH  = "/"+AppFolderName+"/download/images";
    public static final String DOWNLOAD_AUDIO_FILE_DIR_PATH  = "/"+AppFolderName+"/download/audio";

    /**
     * 录音文件缓存目录
     */
    public static final String CACHE_AUDIO_FILE_DIR_PATH  = "/"+AppFolderName+"/cache/audio";
    /**
     * 图片文件缓存目录
     */
    public static final String CACHE_IMAGE_FILE_DIR_PATH  = "/"+AppFolderName+"/cache/images";



    /**
     * 拷贝文件
     * @param istream
     * @param ostream
     * @return
     */
    public static boolean copyFile(InputStream istream, OutputStream ostream) {
        try {
            byte[] buffer = new byte[1024];
            int length;
            while ((length = istream.read(buffer)) > 0) {
                ostream.write(buffer, 0, length);
            }
            istream.close();
            ostream.close();
        } catch (Exception e) {
            try {
                if (istream != null)
                    istream.close();
                if (ostream != null)
                    ostream.close();
            } catch (Exception ee) {
                ee.printStackTrace();
            }
            return false;
        }
        return true;

    }



    /**
     * SD卡是否正常挂载
     * @return true:正常, false:不正常
     */
    private static boolean isMediaMounted() {
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }



    /**
     * 获取sdcard路径
     * @return
     */
    public static String getSdcardPath() {

        //LogUtils.d("aaabbbcc:"+Environment.getExternalStorageDirectory().getAbsolutePath());

        //return App.getContext().getCacheDir().getAbsolutePath();


        /*if (App.getContext().getExternalCacheDir() == null) {
            LogUtils.d("getSdcardPath目录：" + Environment.getDownloadCacheDirectory().getAbsolutePath());
            return Environment.getDownloadCacheDirectory().getAbsolutePath();
        } else {
            LogUtils.d("getSdcardPath目录2：" + App.getContext().getExternalCacheDir().getAbsolutePath());

            return App.getContext().getExternalCacheDir().getAbsolutePath();
        }*/

        return Environment.getExternalStorageDirectory().getPath();
        //return Environment.getExternalStorageDirectory().getAbsolutePath();
    }


    /**
     * 是否存在该目录
     * @param dirName : 目录名
     * @return true:存在, false:不存在
     */
    private static boolean isExistsDir(String dirName) {

        final String path = getSdcardPath() + "/" + dirName;
        final File fileDir = new File(path);
        return fileDir.exists();
    }


    /**
     * 创建目录
     */
    public static boolean createDir(String dirName) {

        if (!isExistsDir(dirName)) {
            // 如果不存在就创建
            final String path = getSdcardPath() + "/" + dirName;
            final File fileDir = new File(path);
            return fileDir.mkdirs();
        }
        return true;

    }


    /**
     * 删除文件
     * @param filePath
     * @return
     */
    public static boolean delFile(String filePath) {

        if (isExistsFilePath(filePath)) {
            final File file = new File(filePath);
            return file.delete();
        }
        return true;
    }




    /**
     * 把下载的文件流写入到某个文件里
     * @param file : 文件
     * @param bufferedSource : 下载的流
     * @return
     */
    public static boolean writeFile(File file, BufferedSource bufferedSource) {

        BufferedSink sink = null;
        try {
            sink = Okio.buffer(Okio.sink(file));
            sink.writeAll(bufferedSource);
            return true;
        } catch(Exception e) {
            LogUtils.d("FileUtils.writeFile方法发生了错误Exception：" + e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                if(sink != null) sink.close();
            } catch(IOException e) {
                LogUtils.d("FileUtils.writeFile方法发生了错误IOException：" + e.getMessage());
                e.printStackTrace();
            }
        }

        return false;
    }


    /**
     * 基础数据写入, 这里有问题发现如果创建了文件然后再向这个文件写入东西会写不进去,会有权限问题
     * @param dirName : 所在sdcard的目录路径 如aa,多级就用aa/bb/cc
     * @param fileName : 文件名 如xxx.jpg, xxx.db, xxx.txt
     * @param bufferedSource: 服务器下载的数据
     * @return true:写入成功, false:写入失败
     */
    public static boolean dbWriteFile(String dirName, String fileName, BufferedSource bufferedSource) {

        boolean isSuccess = true;

        // 检查sdcard卡是否正常
        if (isMediaMounted()) {


            // 第一步先创建db要存放在sdcard的文件夹
            if (!isExistsDir(dirName)) {
                isSuccess = createDir(dirName);
            }


            // 第二步创建要写入在sdcard的文件
            /*if (isSuccess) {

                // 文件夹创建成功了才可以来创建文件
                if (!isExistsFile(dirName, fileName)) {
                    isSuccess = createFile(dirName, fileName);
                }
            }*/

            // 文件路径
            final String filePath = getDirPath(dirName) + "/" + fileName;


            // 第二步如果有文件存在的话就先册掉去
            if (isSuccess) {

                if (isExistsFilePath(filePath)) {
                    final File file = getFile(dirName, fileName);
                    isSuccess = file.delete();
                }
            }



            // 第三步写入文件, 不能先创建文件然后再写入了这个文件中,这样会写不进的会报错
            if (isSuccess) {

                final File file = new File(filePath);

                if (file != null) {
                    isSuccess = writeFile(file, bufferedSource);
                } else {
                    isSuccess = false;
                }

            }


        }

        return isSuccess;



    }


    /**
     * 查看sdcard某个文件夹里面的文件是否存在
     * @param dirName : 文件所在的sdcard目录如xxx目录,如果多级就用aaa/bbb/ccc
     * @param fileName : 文件名如xxx.jpg,xxx.db
     * @return
     */
    public static boolean isExistsFile(String dirName, String fileName) {

        final String path = getSdcardPath() + "/" + dirName + "/" +fileName;
        final File file = new File(path);
        return file.exists();

    }

    /**
     * 查看sdcard某个文件夹里面的文件是否存在
     * @param filePath : 文件路径如data/data/xx/xxx.jpg
     * @return
     */
    public static boolean isExistsFilePath(String filePath) {

        final File file = new File(filePath);
        return file.exists();

    }

    /**
     * 得到sdcard某个文件夹里面的文件File对象
     * @param dirName : 文件所在的sdcard目录如xxx目录,如果多级就用aaa/bbb/ccc
     * @param fileName : 文件名如xxx.jpg,xxx.db
     * @return null:未得到, file:得到
     */
    public static File getFile(String dirName, String fileName) {

        final String path = getSdcardPath() + "/" + dirName + "/" +fileName;
        final File file = new File(path);
        if (file.exists()){
            return file;
        }
        return null;
    }


    /**
     * 得到sdcard某个文件夹目录
     * @param dirName : 文件所在的sdcard目录如xxx目录,如果多级就用aaa/bbb/ccc
     * @return null:未得到, file:得到
     */
    public static File getDir(String dirName) {

        final String path = getSdcardPath() + "/" + dirName;
        final File file = new File(path);
        if (file.isDirectory()){
            return file;
        }
        return null;
    }

    /**
     * 得到sdcard某个文件夹目录路径
     * @param dirName : 文件所在的sdcard目录如xxx目录,如果多级就用aaa/bbb/ccc
     * @return null:未得到, file:得到
     */
    public static String getDirPath(String dirName) {

        final String path = getSdcardPath() + "/" + dirName;
        final File file = new File(path);
        if (file.isDirectory()){
            return path;
        }
        return null;
    }


    /**
     * 得到sdcard某个文件夹里面的文件File路径
     * @param dirName : 文件所在的sdcard目录如xxx目录,如果多级就用aaa/bbb/ccc
     * @param fileName : 文件名如xxx.jpg,xxx.db
     * @return null:未得到, file:得到
     */
    public static String getFilePath(String dirName, String fileName) {

        final String path = getSdcardPath() + "/" + dirName + "/" +fileName;
        return path;
    }


    /**
     * 创建sdcard某个文件夹里面的文件
     * @param dirName : 文件所在的sdcard目录如xxx目录,如果多级就用aaa/bbb/ccc
     * @param fileName : 文件名如xxx.jpg,xxx.db
     * @return
     */
    public static boolean createFile(String dirName, String fileName) {


        if (isMediaMounted()) {

            // sdcard正常
            final String path = getSdcardPath() + "/" + dirName + "/" +fileName;
            final File file = new File(path);

            try {
                file.createNewFile();
                return true;
            } catch (IOException e) {
                return false;
            }

        }

        return false;

    }


    /**
     * 把资源文件夹assets里的文件拷到指定的目录文件里面
     * @param assetsFileName: assets目录里的文件名如:xxx.db
     * @param newFilePath: 要保存到新目录下里面的文件路径
     * @param context
     * @return true:成功, false:失败
     */
    private static boolean copyAssetsToFileSystem(String assetsFileName, String newFilePath, Context context) {

        InputStream inputStream = null;
        OutputStream outputStream = null;
        AssetManager assetManager = context.getAssets();

        try {
            inputStream = assetManager.open(assetsFileName);
            outputStream = new FileOutputStream(newFilePath);
            return FileUtils.copyFile(inputStream, outputStream);
        } catch (IOException e) {
            return false;
        }
    }


    /**
     * bitmap转化为file对象
     * @param bmp
     * @return
     */
    public static File bitmapToFile(Bitmap bmp) {

        // 首先保存图片
        File appDir = new File(Environment.getExternalStorageDirectory(), "upimagetemp");
        if (!appDir.exists()) {
            appDir.mkdir();
        }
        String fileName = System.currentTimeMillis() + ".jpg";
        File file = new File(appDir, fileName);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.JPEG, 80, fos); // 80可以压缩的蛮小的
            fos.flush();
            fos.close();
            return file;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * bitmap转化为file对象
     * @param bmp
     * @param tag : 有时上传图片要对应相应的参数, 如上传的客厅那么服务器上面保存到数据库也要对上客厅, 所以这里以上传的图片文件名来对应, 如下面加了_tag来做为额外参数
     * @return
     */
    public static File bitmapToFile(Bitmap bmp, String tag) {

        // 首先保存图片
        File appDir = new File(Environment.getExternalStorageDirectory(), "upimagetemp");
        if (!appDir.exists()) {
            appDir.mkdir();
        }
        String fileName = tag + "_" + System.currentTimeMillis() + ".jpg";
        File file = new File(appDir, fileName);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.JPEG, 80, fos); // 80可以压缩的蛮小的
            fos.flush();
            fos.close();
            return file;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取文件夹路径
     * @param typePath
     * @return
     */
    public static String getFileDirPathForType(String typePath){
        String path = Environment.getExternalStorageDirectory() + typePath;
        File file = new File(path);
        if (file.mkdirs()) {
            return path;
        }
        return path;
    }

    public static String getFilePathForType(String typePath,String fileName){
        String path = Environment.getExternalStorageDirectory() + typePath;
        File file = new File(path);
        if (file.mkdirs()) {
            return path + "/" +fileName;
        }
        return path + "/" +fileName;
    }



    /**
     * 获取压缩后的图片文件夹路径
     * @return
     */
    public static String  getCompressImageFileDirPath(){
        return getFileDirPathForType(COMPRESS_IMAGE_FILE_DIR_PATH);
    }

    /**
     * 获取保存图片的文件路劲
     * @param fileName
     * @return
     */
    public static String getDownloadImageFilePath(String fileName){
        return getDownloadImageFileDirPath() + "/" + fileName;
    }

    public static String getDownloadImageFileDirPath(){

        return getFileDirPathForType(DOWNLOAD_IMAGE_FILE_DIR_PATH);
    }


    /**
     * 获取保存音频的文件路劲
     * @param fileName
     * @return
     */
    public static String getDownloadAudioFilePath(String fileName){
        return getDownloadAudioFileDirPath() + "/" + fileName;
    }


    public static String getDownloadAudioFileDirPath(){
        return getFileDirPathForType(DOWNLOAD_AUDIO_FILE_DIR_PATH);
    }


    /**
     * 获取缓存文件的路径
     * @param fileName
     * @return
     */
    public static String getCacheAudioFilePath(String fileName){
        return getDownloadAudioFileDirPath() + "/" + fileName;
    }


    public static String getCacheAudioFileDirPath(){
        return getFileDirPathForType(CACHE_AUDIO_FILE_DIR_PATH);
    }


    public static Uri getFileUri(Context context,File file){
        if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.N) {
            return  FileProvider.getUriForFile(context, context.getApplicationContext().getPackageName() + ".provider", file);
        }else{
            return Uri.fromFile(file);
        }
    }


}
