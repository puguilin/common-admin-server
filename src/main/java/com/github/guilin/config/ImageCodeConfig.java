package com.github.guilin.config;

import com.google.code.kaptcha.Constants;
import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.google.code.kaptcha.util.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Properties;

@Configuration
public class ImageCodeConfig {
    @Bean
    public DefaultKaptcha getDefaultKaptcha() {
        Properties properties = new Properties();
        //验证码是否有边框
        properties.setProperty(Constants.KAPTCHA_BORDER, "yes");
        //边框颜色
        properties.setProperty(Constants.KAPTCHA_BORDER_COLOR, "192,192,192");
        //验证码图片宽度
        properties.setProperty(Constants.KAPTCHA_IMAGE_WIDTH, "150");
        //验证码图片高度
        properties.setProperty(Constants.KAPTCHA_IMAGE_HEIGHT, "47");
        //去掉干扰线
        properties.setProperty(Constants.KAPTCHA_NOISE_IMPL, "com.google.code.kaptcha.impl.NoNoise");
        //字体颜色
        properties.setProperty(Constants.KAPTCHA_TEXTPRODUCER_FONT_COLOR, "blue");
        //字体大小
        properties.setProperty(Constants.KAPTCHA_TEXTPRODUCER_FONT_SIZE, "32");
        //字体样式
        properties.setProperty(Constants.KAPTCHA_TEXTPRODUCER_FONT_NAMES, "楷体");
        //验证码位数
        properties.setProperty(Constants.KAPTCHA_TEXTPRODUCER_CHAR_LENGTH, "4");
        //图片效果
        properties.setProperty(Constants.KAPTCHA_OBSCURIFICATOR_IMPL, "com.google.code.kaptcha.impl.ShadowGimpy");
        DefaultKaptcha defaultKaptcha = new DefaultKaptcha();
        defaultKaptcha.setConfig(new Config(properties));
        return defaultKaptcha;
    }
}
