package com.github.guilin.utils;

import org.apache.commons.lang3.StringUtils;

import java.util.UUID;

/**
 * 简单文件工具类
 *
 * @author CaoChenLei
 */
public class FileUtils {
    /**
     * 获取文件随机名
     *
     * @return
     */
    public static String getRandomFileName() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    /**
     * 获取文件拓展名
     *
     * @param fileName
     * @return
     */
    public static String getFileExtension(String fileName) {
        if (StringUtils.isEmpty(fileName)) return "";
        int lastIndexOf = fileName.lastIndexOf(".");
        if (lastIndexOf == -1) return "";
        return fileName.substring(lastIndexOf + 1).toLowerCase();
    }
}
