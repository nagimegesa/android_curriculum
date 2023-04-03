package com.xxzz.curriculum.join;
import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.Scanner;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
public class FileOperation {
    static void readfile(File file){
        try (Reader reader = new FileReader(file.getPath())) {
            // 按照字符来读.
            while (true) {
                char[] buffer = new char[1024];
                int len = reader.read(buffer);
                if (len == -1) {
                    break;
                }
                String s = new String(buffer, 0, len);
                System.out.println(s);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void scanDir(File rootFile, String toDeleteDir) throws IOException {
        File[] files = rootFile.listFiles();
        //判断目录是否为空
        if (files == null){
            return;
        }
        for (File f : files) {
            //先判断是否是文件
            if (f.isFile()){
                //包含文件名就删除, 不包含继续递归
                if (f.getName().contains(toDeleteDir)){
                    deleteDir(f);
                }
            }else if (f.isDirectory()){//如果是目录就递归
                scanDir(f, toDeleteDir);
            }
        }
    }

    private static void deleteDir(File f) throws IOException {

        //System.out.println(f.getCanonicalPath() + " 确认要删除吗? (Y/n)");
        //Scanner scanner = new Scanner(System.in);
        //String choice = scanner.next();
        //if (choice.equals("Y") || choice.equals("y")) {
            f.delete();
            //System.out.println("文件删除成功!");
       // } else {
          //  System.out.println("文件取消删除!");
       // }
    }
    private static void copyFileUsingStream(File source, File dest) throws IOException {
        InputStream is = null;
        OutputStream os = null;
        try {
            is = new FileInputStream(source);
            os = new FileOutputStream(dest);
            byte[] buffer = new byte[1024];
            int length;
            while ((length = is.read(buffer)) > 0) {
                os.write(buffer, 0, length);
            }
        } finally {
            is.close();
            os.close();
        }
    }
    public static void copyDir(String sourcePath,String newPath) {
        try {
            (new File(newPath)).mkdirs();
            // 与mkdir()都创建文件夹 ，mkdirs()如果父文件夹不存在也会创建
            File fileList = new File(sourcePath);
            String[] strName = fileList.list();
            File temp = null;//游标
            for (int i = 0; i < strName.length; i++) {
                // 如果源文件路径以分隔符File.separator /或者\结尾那就sourcePath
                if (sourcePath.endsWith(File.separator)) {
                    temp = new File(sourcePath + strName[i]);
                } else {
                    temp = new File(sourcePath + File.separator + strName[i]);
                }
                if (temp.isFile()) {
                    // 如果游标遇到文件
                    FileInputStream in = new FileInputStream(temp);
                    // 复制且改名
                    File file = new File(newPath + "/" + temp.getName().toString());
                    FileOutputStream out = new FileOutputStream(file);
                    byte[] buffer = new byte[1024 * 8];
                    int length;
                    while ((length = in.read(buffer)) != -1) {

                        out.write(buffer, 0, length);
                    }
                    out.flush();
                    out.close();
                    in.close();
                }
                // 如果游标遇到文件夹
                if (temp.isDirectory()) {
                    copyDir(sourcePath + "/" + strName[i], newPath + "/" + strName[i]);
                }
            }
        } catch (Exception e) {
            System.out.println("文件夹复制失败!");
        }
    }

}
