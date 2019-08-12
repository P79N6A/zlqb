package com.nyd.capital.service.wsm;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * Cong Yuxiang
 * 2017/11/13
 **/
@Configuration
public @Data class WsmConfig {

    @Value("${wsm.sendOrderUrl}")
    private String sendOrderUrl;
    @Value("${wsm.checkOrderUrl}")
    private String checkOrderUrl;
    @Value("${wsm.clientkeystore}")
    private String clientkeystore;
    @Value("${wsm.clientkeystorePassword}")
    private String clientkeystorePassword;
    @Value("${wsm.mid}")
    private Integer mid;
    @Value("${wsm.cpm}")
    private Integer cpm;
    @Value("${wsm.shmyc}")
    private String shmyc;
    @Value("${wsm.csjmsy}")
    private String csjmsy;
    @Value("${wsm.threadNum}")
    private Integer threadNum;



    //订单内字段加密
    public String encrypt(String data,String key, String iv) throws Exception {
        try {
            Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");
            int blockSize = cipher.getBlockSize();

            byte[] dataBytes = data.getBytes("UTF-8");
            int plaintextLength = dataBytes.length;
            if (plaintextLength % blockSize != 0) {
                plaintextLength = plaintextLength + (blockSize - (plaintextLength % blockSize));
            }

            byte[] plaintext = new byte[plaintextLength];
            System.arraycopy(dataBytes, 0, plaintext, 0, dataBytes.length);

            SecretKeySpec keyspec = new SecretKeySpec(key.getBytes(), "AES");
            IvParameterSpec ivspec = new IvParameterSpec(iv.getBytes());

            cipher.init(Cipher.ENCRYPT_MODE, keyspec, ivspec);
            byte[] encrypted = cipher.doFinal(plaintext);
            String encryptedVal = new sun.misc.BASE64Encoder().encode(encrypted);
            return encryptedVal;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
