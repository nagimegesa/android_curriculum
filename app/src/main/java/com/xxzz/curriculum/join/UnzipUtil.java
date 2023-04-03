package com.xxzz.curriculum.join;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class UnzipUtil {
    /**
     * 解压zip压缩文件到指定目录
     *
     * @param zipPath zip压缩文件绝对路径
     * @param descDir 指定的解压目录
     */
    public static void unzipFile(String zipPath, String descDir) throws IOException {
        try {
            File zipFile = new File(zipPath);
            if (!zipFile.exists()) {
                throw new IOException("要解压的压缩文件不存在");
            }
            File pathFile = new File(descDir);
            if (!pathFile.exists()) {
                pathFile.mkdirs();
            }
            InputStream input = new FileInputStream(zipPath);
            unzipWithStream(input, descDir);
        } catch (Exception e) {
            throw new IOException(e);
        }
    }

    /**
     * 解压
     *
     * @param inputStream
     * @param descDir
     */
    public static void unzipWithStream(InputStream inputStream, String descDir) {
        if (!descDir.endsWith(File.separator)) {
            descDir = descDir + File.separator;
        }
        try (ZipInputStream zipInputStream = new ZipInputStream(inputStream, Charset.forName("GBK"))) {
            ZipEntry zipEntry;
            while ((zipEntry = zipInputStream.getNextEntry()) != null) {
                String zipEntryNameStr = zipEntry.getName();
                String zipEntryName = zipEntryNameStr;
                if (zipEntryNameStr.contains("/")) {
                    String str1 = zipEntryNameStr.substring(0, zipEntryNameStr.indexOf("/"));
                    zipEntryName = zipEntryNameStr.substring(str1.length() + 1);
                }
                String outPath = (descDir + zipEntryName).replace("\\\\", "/");
                File outFile = new File(outPath.substring(0, outPath.lastIndexOf('/')));
                if (!outFile.exists()) {
                    outFile.mkdirs();
                }
                if (new File(outPath).isDirectory()) {
                    continue;
                }
                writeFile(outPath, zipInputStream);
                zipInputStream.closeEntry();
            }
            System.out.println("======解压成功=======");
        } catch (IOException e) {
            System.out.println("压缩包处理异常，异常信息{}" + e);
        }
    }

    //将流写到文件中
    public static void writeFile(String filePath, ZipInputStream zipInputStream) {
        try (OutputStream outputStream = new FileOutputStream(filePath)) {
            byte[] bytes = new byte[4096];
            int len;
            while ((len = zipInputStream.read(bytes)) != -1) {
                outputStream.write(bytes, 0, len);
            }
        } catch (IOException ex) {
            System.out.println("解压文件时，写出到文件出错");
        }
    }

    //测试方法
//    public static void main(String[] args) throws IOException {
//
//        String zipPath = "D:/test/测试文件.zip";
//        String descDir = "D:/test/解压/";
//
//        unzipFile(zipPath, descDir);
//    }
}


