package com.artikov.photototext.utils;

import java.io.File;

/**
 * Date: 28/6/2016
 * Time: 12:35
 *
 * @author Artur Artikov
 */
public class FileNameUtils {
    static public String getFileNameWithoutExtension(String filePath) {
        int separatorIndex = filePath.lastIndexOf(File.separatorChar);
        String fileName = filePath.substring(separatorIndex + 1);
        int dotIndex = fileName.lastIndexOf('.');
        return dotIndex != -1 ? fileName.substring(0, dotIndex) : fileName;
    }
}
