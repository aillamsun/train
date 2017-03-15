package com.train;


import cn.easyproject.easyocr.EasyOCR;

import java.io.File;

/**
 * Created by sungang on 2017/3/14.
 */
public class TestOcr {

    public static void main(String[] args) {
        // System.setProperty("jna.library.path", "32".equals(System.getProperty("sun.arch.data.model")) ? "lib/win32-x86" : "lib/win32-x86-64");
        File imageFile = new File("/Users/sungang/Desktop/timg.jpeg");

        EasyOCR e=new EasyOCR();
        e.setTesseractPath("");
//直接识别图片内容
        System.out.println(e.discern("tesseract/images/timg.jpeg"));
    }
}
