package com.github.guilin.config.prop;

import com.github.guilin.utils.RsaUtils;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.core.io.ClassPathResource;

import javax.annotation.PostConstruct;
import java.security.PrivateKey;
import java.security.PublicKey;

/**
 * 用于和配置文件中的RSA属性进行绑定
 *
 * @author CaoChenLei
 */
@Slf4j
@Data
@ConfigurationProperties(prefix = "rsa.key", ignoreInvalidFields = true)
public class RsaKeyProperties {
    private String publicKeyPath;
    private String privateKeyPath;

    private PublicKey publicKey;
    private PrivateKey privateKey;

    /**
     * 该方法用于初始化公钥和私钥的内容
     */
    @PostConstruct
    public void loadRsaKey() throws Exception {
        if (publicKeyPath != null) {
            publicKey = RsaUtils.getPublicKey(publicKeyPath);
            log.info("Load appoint {}", publicKeyPath);
        } else {
            //如果不写publicKeyPath，默认查找rsa/rsa_key.pub
            ClassPathResource classPathResource = new ClassPathResource("rsa/rsa_key.pub.txt");
            publicKey = RsaUtils.getPublicKey(classPathResource.getInputStream());
            log.info("Load default rsa/rsa_key.pub");
        }
        if (privateKeyPath != null) {
            privateKey = RsaUtils.getPrivateKey(privateKeyPath);
            log.info("Load appoint {}", privateKeyPath);
        } else {
            //如果不写privateKeyPath，默认查找rsa/rsa_key
            ClassPathResource classPathResource = new ClassPathResource("rsa/rsa_key.txt");
            privateKey = RsaUtils.getPrivateKey(classPathResource.getInputStream());
            log.info("Load default rsa/rsa_key");
        }
    }
}
