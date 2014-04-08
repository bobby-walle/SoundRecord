package com.joyme.voice.utils;

/**
 * Created with IntelliJ IDEA.
 * User: bobzhai
 * Date: 13-12-11
 * Time: 下午3:57
 * To change this template use File | Settings | File Templates.
 */

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import java.io.*;

/**
 * 文件操作工具包
 * Created with IntelliJ IDEA.
 * User: bobzhai
 * Date: 13-11-6
 * Time: 下午4:03
 */
public class FileUtils {

    /**
     * 写文本文件
     * 在Android系统中，文件保存在 /data/data/PACKAGE_NAME/files 目录下
     *
     * @param context
     * @param fileName
     * @param content
     */
    public static void write(Context context, String fileName, String content) {
        FileOutputStream fos = null;
        try {
            fos = context.openFileOutput(fileName, Context.MODE_PRIVATE);
            fos.write(content.getBytes());

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeOutputStream(fos);
        }
    }

    /**
     * 读取文本文件
     *
     * @param context
     * @param fileName
     * @return
     */
    public static String read(Context context, String fileName) {
        FileInputStream fis = null;
        try {
            fis = context.openFileInput(fileName);
            return readInStream(fis);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeInputStream(fis);
        }
        return "";
    }

    public static String readFromFile(String path) {

        File file = new File(path);
        if (!file.exists())
            return "";
        try {
            return readInStream(new FileInputStream(file));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return "";
    }

    private static String readInStream(FileInputStream inStream) {
        ByteArrayOutputStream outStream = null;
        try {
            outStream = new ByteArrayOutputStream();
            byte[] buffer = new byte[512];
            int length = -1;
            while ((length = inStream.read(buffer)) != -1) {
                outStream.write(buffer, 0, length);
            }

            outStream.close();
            inStream.close();
            return outStream.toString();
        } catch (IOException e) {
            Log.i("FileTest", e.getMessage());
        } finally {
            closeOutputStream(outStream);
            closeInputStream(inStream);
        }

        return null;
    }

    /**
     * 获取文件大小
     *
     * @param filePath
     * @return
     */
    public static long getFileSize(String filePath) {
        long size = 0;

        File file = new File(filePath);
        if (file != null && file.exists()) {
            size = file.length();
        }
        return size;
    }

    /**
     * 获取文件大小
     *
     * @param size 字节
     * @return 0/M/K
     */
    public static String getFileSize(long size) {
        if (size <= 0) return "0";
        java.text.DecimalFormat df = new java.text.DecimalFormat("##.##");
        float temp = (float) size / 1024;
        if (temp >= 1024) {
            return df.format(temp / 1024) + "M";
        } else {
            return df.format(temp) + "K";
        }
    }

    /**
     * 获取目录文件个数
     *
     * @param dir
     * @return
     */
    public long getFileList(File dir) {
        long count = 0;
        File[] files = dir.listFiles();
        count = files.length;
        for (File file : files) {
            if (file.isDirectory()) {
                count = count + getFileList(file);//递归
                count--;
            }
        }
        return count;
    }

    /**
     * 输入流转为byte[]
     *
     * @param in
     * @return
     * @throws IOException
     */
    public static byte[] toBytes(InputStream in) throws IOException {
        ByteArrayOutputStream out = null;
        byte[] buffer;
        try {
            out = new ByteArrayOutputStream();
            int ch;
            while ((ch = in.read()) != -1) {
                out.write(ch);
            }
            buffer = out.toByteArray();
        } finally {
            closeOutputStream(out);
        }
        return buffer;
    }

    /**
     * 把二进制数据写入文件
     *
     * @param data 要写入的数据
     * @param file 目标文件
     */
    public static void writeToFile(byte[] data, File file) {


        FileOutputStream outputStream = null;
        try {
            if (!file.exists()) {
                File parentFile = file.getParentFile();
                if (!parentFile.exists()) {
                    parentFile.mkdirs();
                }
                file.createNewFile();
            }
            outputStream = new FileOutputStream(file);
            outputStream.write(data);

            outputStream.flush();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            closeOutputStream(outputStream);
        }

    }

    public static boolean isSdCardWrittenable() {

        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            return true;
        }
        return false;
    }

    /**
     * 检查是否安装SD卡
     *
     * @return
     */
    public static boolean checkSaveLocationExists() {
        String sDCardStatus = Environment.getExternalStorageState();
        boolean status;
        if (sDCardStatus.equals(Environment.MEDIA_MOUNTED)) {
            status = true;
        } else
            status = false;
        return status;
    }

    /**
     * 获取SDCard路径
     *
     * @return
     */
    public static String getSDCardPath() {
        String sDCardStatus = Environment.getExternalStorageState();
        String sDCardPath = "mnt/sdcard";
        if (sDCardStatus.equals(Environment.MEDIA_MOUNTED)) {
            sDCardPath = Environment.getExternalStorageDirectory().getPath();
        } else {
            sDCardPath = null;
        }
        return sDCardPath;
    }

    /**
     * 新建目录
     *
     * @param directoryName
     * @return
     */
    public static boolean createDirectory(String directoryName) {
        boolean status;
        if (!directoryName.equals("")) {
            File path = Environment.getExternalStorageDirectory();
            File newPath = new File(path.toString() + directoryName);
            status = newPath.mkdir();
            status = true;
        } else
            status = false;
        return status;
    }

    /**
     * 删除目录(包括：目录里的所有文件)
     *
     * @param fileName
     * @return
     */
    public static boolean deleteDirectory(String fileName) {
        boolean status;
        SecurityManager checker = new SecurityManager();

        if (!fileName.equals("")) {

            File path = Environment.getExternalStorageDirectory();
            File newPath = new File(path.toString() + fileName);
            checker.checkDelete(newPath.toString());
            if (newPath.isDirectory()) {
                String[] listfile = newPath.list();
                // delete all files within the specified directory and then
                // delete the directory
                try {
                    for (int i = 0; i < listfile.length; i++) {
                        File deletedFile = new File(newPath.toString() + "/"
                                + listfile[i].toString());
                        deletedFile.delete();
                    }
                    newPath.delete();
                    Log.i("DirectoryManager deleteDirectory", fileName);
                    status = true;
                } catch (Exception e) {
                    e.printStackTrace();
                    status = false;
                }

            } else
                status = false;
        } else
            status = false;
        return status;
    }

    /**
     * 删除文件
     *
     * @param fileName
     * @return
     */
    public static boolean deleteFile(String fileName) {
        boolean status;
        SecurityManager checker = new SecurityManager();

        if (!fileName.equals("")) {

            File path = Environment.getExternalStorageDirectory();
            File newPath = new File(path.toString() + fileName);
            checker.checkDelete(newPath.toString());
            if (newPath.isFile()) {
                try {
                    Log.i("DirectoryManager deleteFile", fileName);
                    newPath.delete();
                    status = true;
                } catch (SecurityException se) {
                    se.printStackTrace();
                    status = false;
                }
            } else
                status = false;
        } else
            status = false;
        return status;
    }

    /**
     * 关闭输入流
     *
     * @param inputSream
     */
    public static void closeInputStream(InputStream inputSream) {
        if (inputSream != null) {
            try {
                inputSream.close();
                inputSream = null;
            } catch (IOException e) {
            }
        }
    }

    /**
     * 关闭输出流
     *
     * @param outputStream
     */
    public static void closeOutputStream(OutputStream outputStream) {
        if (outputStream != null) {
            try {
                outputStream.close();
                outputStream = null;
            } catch (IOException e) {
            }
        }
    }
}
