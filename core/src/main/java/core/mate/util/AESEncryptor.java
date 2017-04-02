package core.mate.util;

import android.support.annotation.StringDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.nio.charset.Charset;
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

/**
 * @author DrkCore
 * @since 2016年2月24日22:40:31
 */
public class AESEncryptor extends AbsEncryptor {

    private String mode;
    private String padding;
    private byte[] password;

    public AESEncryptor(byte[] salt, Charset charset, String mode, String padding, byte[] password) {
        super(salt, charset);
        this.mode = mode;
        this.padding = padding;
        this.password = password;
    }

    /*Builder*/

    public static final String AES = "AES";

    public static final String MODE_CBC = "CBC";
    public static final String MODE_CFB = "CFB";
    public static final String MODE_ECB = "ECB";
    public static final String MODE_OFB = "OFB";
    public static final String MODE_PCBC = "PCBC";

    public static final String PADDING_ZERO = "ZeroBytePadding";
    public static final String PADDING_PKCS5 = "PKCS5Padding";
    public static final String PADDING_ISO10126 = "ISO10126Padding";

    public static final String SECURE_ALGORITHM_SHA1PRNG = "SHA1PRNG";
    public static final String SECURE_PROVIDER_CRYPTO = "Crypto";

    @StringDef({
            MODE_CBC,
            MODE_CFB,
            MODE_ECB,
            MODE_OFB,
            MODE_PCBC
    })
    @Retention(RetentionPolicy.SOURCE)
    public @interface Mode {
    }

    @StringDef({
            PADDING_ZERO,
            PADDING_PKCS5,
            PADDING_ISO10126
    })
    @Retention(RetentionPolicy.SOURCE)
    public @interface Padding {
    }

    public static class Builder {

        private String mode = MODE_ECB;
        private String padding = PADDING_ZERO;

        public Builder setMode(@Mode String mode) {
            this.mode = mode;
            return this;
        }

        public Builder setPadding(@Padding String padding) {
            this.padding = padding;
            return this;
        }

        private byte[] password;
        private byte[] salt;
        private Charset charset = Charset.defaultCharset();

        public Builder setPassword(byte[] password) {
            this.password = password;
            return this;
        }

        public Builder setPassword(String password) {
            this.password = password.getBytes(charset);
            return this;
        }

        public Builder setSalt(byte[] salt) {
            this.salt = salt;
            return this;
        }

        public Builder setSalt(String salt) {
            this.salt = !TextUtil.isEmpty(salt) ? salt.getBytes(charset) : null;
            return this;
        }

        public Builder setCharset(Charset charset) {
            if (password != null || salt != null) {
                throw new IllegalStateException("请在设置password和salt前设置编码格式");
            }

            charset = charset != null ? charset : Charset.defaultCharset();
            if (this.charset != charset) {
                this.charset = charset;
            }
            return this;
        }

        public AESEncryptor build() {
            return new AESEncryptor(salt, charset, mode, padding, password);
        }
    }

	/*Encrypt*/

    private Cipher encryptCipher;
    private Cipher decryptCipher;

    private void setup() throws Exception {
        if (encryptCipher == null || decryptCipher == null) {
            String cipherName = AES + '/' + mode + '/' + padding;

            //SecureRandom sr = new SecureRandom();// java pc版加密设置
            SecureRandom secureRandom = SecureRandom.getInstance(SECURE_ALGORITHM_SHA1PRNG, SECURE_PROVIDER_CRYPTO);
            secureRandom.setSeed(password);

            KeyGenerator keyGenerator = KeyGenerator.getInstance(AES);
            keyGenerator.init(256, secureRandom); // 192 and 256 bits may not be available
            SecretKey secretKey = keyGenerator.generateKey();
            byte[] encodedKey = secretKey.getEncoded();
            SecretKeySpec key = new SecretKeySpec(encodedKey, AES);

            //Cipher cipher = Cipher.getInstance(AES);// java pc版加密设置
            encryptCipher = Cipher.getInstance(cipherName);// Android版加密设置
            encryptCipher.init(Cipher.ENCRYPT_MODE, key);// 初始化

            //Cipher cipher = Cipher.getInstance(AES);// java pc版设置
            decryptCipher = Cipher.getInstance(cipherName);// Android版设置
            decryptCipher.init(Cipher.DECRYPT_MODE, key);// 初始化
        }
    }

    @Override
    protected byte[] doEncrypt(byte[] src) throws Exception {
        setup();
        return encryptCipher.doFinal(src);
    }

    @Override
    protected byte[] doDecrypt(byte[] cipherBytes) throws Exception {
        setup();
        return decryptCipher.doFinal(cipherBytes);
    }

}
