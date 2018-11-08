package diary.beautiful.sync;

import android.annotation.SuppressLint;
import android.util.Log;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.zip.Zip64Mode;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveInputStream;
import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * 一个单例模式的工具类，用于打包与解压文件。
 * @author bsz
 *
 */

public class ZipTool {
    private volatile static ZipTool tool;
    private ZipTool (){}

    /**
     * @description 用于获取实例
     * @return 一个类的实例
     * @date 2018/11/8
    */
    public static ZipTool getTool() {
        if (tool == null) {
            synchronized (ZipTool.class) {
                if (tool == null) {
                    tool = new ZipTool();
                }
            }
        }
        return tool;
    }

    /**
     * @description ZipTool.getTool().doZip("E:\\log","E:\\log.zip");
     * @param dir  待压缩文件夹路径
     * @param zipPath zip文件路径
     * @date 2018/11/8
    */
    public void doZip(String dir , String zipPath){
        List<String> paths = getFiles(dir);
        compressFilesZip(paths.toArray(new String[paths.size()]),zipPath,dir	);
    }
    /**
     * 递归取到当前目录所有文件
     * @param dir 带压缩文件夹路径
     * @return 文件名列表
     */
    private List<String> getFiles(String dir){
        List<String> lstFiles =  new ArrayList<>();
        File file = new File(dir);
        File [] files = file.listFiles();
        for(File f : files){
            if(f.isDirectory()){
                lstFiles.add(f.getAbsolutePath());
                lstFiles.addAll(getFiles(f.getAbsolutePath()));
            }else{
                String str =f.getAbsolutePath();
                lstFiles.add(str);
            }
        }
        return lstFiles;
    }

    /**
     * 文件名处理
     * @param dir 文件所在文件夹
     * @param path 文件路径
     * @return 文件名
     */
    private String getFilePathName(String dir, String path){
        String p = path.replace(dir+File.separator, "");
        p = p.replace("\\", "/");
        return p;
    }


    @SuppressLint("LongLogTag")
    private void compressFilesZip(String[] files, String zipFilePath, String dir) {
        if(files == null || files.length <= 0) {
            return ;
        }
        ZipArchiveOutputStream zaos = null;
        try {
            File zipFile = new File(zipFilePath);
            zaos = new ZipArchiveOutputStream(zipFile);
            zaos.setUseZip64(Zip64Mode.AsNeeded);
            //将每个文件用ZipArchiveEntry封装
            //再用ZipArchiveOutputStream写到压缩文件中
            for(String strfile : files) {
                File file = new File(strfile);
                String name = getFilePathName(dir,strfile);
                ZipArchiveEntry zipArchiveEntry  = new ZipArchiveEntry(file,name);
                zaos.putArchiveEntry(zipArchiveEntry);
                if(file.isDirectory()){
                    continue;
                }
                try (InputStream is = new BufferedInputStream(new FileInputStream(file))) {
                    byte[] buffer = new byte[1024];
                    int len = -1;
                    while ((len = is.read(buffer)) != -1) {
                        //把缓冲区的字节写入到ZipArchiveEntry
                        zaos.write(buffer, 0, len);
                    }
                    zaos.closeArchiveEntry();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }

            }
            zaos.finish();
        }catch(Exception e){
            throw new RuntimeException(e);
        }

        //最后关闭ZipArchiveOutputStream
        try {
            zaos.close();
        } catch (IOException e) {
            Log.e("ZipTool.compressFilesZip", "compressFilesZip: runtimeException");
            throw new RuntimeException(e);
        }

    }


    /**
     * 把zip文件解压到指定的文件夹
     * @param zipFilePath zip文件路径, 如 "D:/test/aa.doZip"
     * @param saveFileDir 解压后的文件存放路径, 如"D:/test/" ()
     */
    public  void unZip(String zipFilePath, String saveFileDir) {
        if(!saveFileDir.endsWith("\\") && !saveFileDir.endsWith("/") ){
            saveFileDir += File.separator;
        }
        File dir = new File(saveFileDir);
        if(!dir.exists()){
            dir.mkdirs();
        }
        File file = new File(zipFilePath);
        if (file.exists()) {
            InputStream is;
            ZipArchiveInputStream zais;
            try {
                is = new FileInputStream(file);
                zais = new ZipArchiveInputStream(is);
                ArchiveEntry archiveEntry;
                while ((archiveEntry = zais.getNextEntry()) != null) {
                    // 获取文件名
                    String entryFileName = archiveEntry.getName();
                    // 构造解压出来的文件存放路径
                    String entryFilePath = saveFileDir + entryFileName;
                    OutputStream os = null;
                    try {
                        // 把解压出来的文件写到指定路径
                        File entryFile = new File(entryFilePath);
                        if(entryFileName.endsWith("/")){
                            entryFile.mkdirs();
                        }else{
                            os = new BufferedOutputStream(new FileOutputStream(
                                    entryFile));
                            byte[] buffer = new byte[1024 ];
                            int len = -1;
                            while((len = zais.read(buffer)) != -1) {
                                os.write(buffer, 0, len);
                            }
                        }
                    } catch (IOException e) {
                        throw new IOException(e);
                    } finally {
                        if (os != null) {
                            os.flush();
                            os.close();
                        }
                    }

                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

            
            //最后资源回收
            try {
                zais.close();
                if (null != is) {
                    is.close();
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}


