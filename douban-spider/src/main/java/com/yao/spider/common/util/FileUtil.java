package com.yao.spider.common.util;

import com.yao.spider.exception.SpiderRuntimeException;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.maven.shared.utils.io.FileUtils;
import org.mule.config.i18n.MessageFactory;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * Created by shanyao on 2020/3/29
 */
public class FileUtil {
    public static void unZipFile() {
        File zip = new File("F:\\视频剪辑\\字幕\\1-2335\\[zmk.pw]0fae226f4186c7859dacbbc80f9cbb29.rar");
        File out = new File("F:\\视频剪辑\\字幕\\1-2335\\temp\\tst");
        try {
            String baseName = FilenameUtils.getBaseName("F:\\视频剪辑\\字幕\\1-2335\\temp\\tst\\tst.zip");
            System.out.println(baseName);
            String extension = FileUtils.extension("[zmk.pw]0b4f5c751ba4d3ba7370cdde1b5572c7.zip");
            System.out.println(extension);
            unzip(zip, out);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void unzip(File archive, File directory) throws IOException {
        ZipFile zip = null;

        if (directory.exists()) {
            if (!directory.isDirectory()) {
                throw new IOException("Directory is not a directory: " + directory);
            }
        } else if (!directory.mkdirs()) {
            throw new IOException("Could not create directory: " + directory);
        }

        try {
            zip = new ZipFile(archive, Charset.forName("GBK"));
            Enumeration entries = zip.entries();

            while(entries.hasMoreElements()) {
                ZipEntry entry = (ZipEntry)entries.nextElement();
                File f = newFile(directory, entry.getName());
                if (entry.isDirectory()) {
                    if (!f.exists() && !f.mkdirs()) {
                        throw new IOException("Could not create directory: " + f);
                    }
                } else {
                    File file = new File(directory, entry.getName());

                    if (!file.getParentFile().exists() && !file.getParentFile().mkdirs()) {
                        throw new IOException("Unable to create folders for zip entry: " + entry.getName());
                    }

                    InputStream is = zip.getInputStream(entry);
                    OutputStream os = new BufferedOutputStream(new FileOutputStream(f));
                    IOUtils.copy(is, os);
                    IOUtils.closeQuietly(is);
                    IOUtils.closeQuietly(os);
                }
            }
        } finally {
            if (zip != null) {
                zip.close();
            }

        }

    }

    public static File newFile(File parent, String child) {
        try {
            return (new File(parent, child)).getCanonicalFile();
        } catch (IOException var3) {
            throw new SpiderRuntimeException(MessageFactory.createStaticMessage("Unable to create a canonical file for parent: " + parent + " and child: " + child), var3);
        }
    }

    public static void main(String[] args) {
        unZipFile();
    }
}
