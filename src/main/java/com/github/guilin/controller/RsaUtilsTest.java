package com.github.guilin.controller;

import com.github.guilin.utils.RsaUtils;

/**
 * @description: 添加公私钥
 * @author: puguilin
 * @date: 2023/1/28
 * @version: 1.0
 */

public class RsaUtilsTest {
    private static  String publicFile = "E:\\studyCode\\auth_key\\rsa_key.pub";
    private static String privateFile = "E:\\studyCode\\auth_key\\rsa_key";
    private static String secret = "123456789abcdefg";

    public static void main(String[] args) throws Exception {
        RsaUtils.generateKey(publicFile, privateFile, secret, 2048);
    }

}
