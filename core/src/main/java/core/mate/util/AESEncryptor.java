package core.mate.util;

import android.content.SharedPreferences;
import android.support.annotation.StringDef;
import android.text.TextUtils;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.nio.charset.Charset;
import java.security.SecureRandom;
import java.security.spec.KeySpec;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * @author DrkCore
 * @since 2016年2月24日22:40:31
 */
public class AESEncryptor extends AbsEncryptor {

    private final byte[] secureSalt;
    private final String mode;
    private final String padding;
    private final String password;

    private AESEncryptor(Charset charset, byte[] secureSalt, String mode, String padding, String password) {
        super(charset != null ? charset : Charset.defaultCharset());

        if (DataUtil.isEmpty(secureSalt)) {
            throw new IllegalArgumentException("请使用newSecureSaltHex()生成secureSalt后将之保存到本地，并在builder传入。\n" +
                    "如果解密时secureSalt和加密时的不一致，会导致解密失败。\n" +
                    "你可以使用builder.setSecureSalt(SharedPreferences, String)方法来简化这一过程。\n" +
                    "以上操作是为了兼容7.0以上的设备，更多细节请参阅：\n" +
                    "https://android-developers.googleblog.com/2016/06/security-crypto-provider-deprecated-in.html");
        }
        mode = !TextUtils.isEmpty(mode) ? mode : MODE_ECB;
        padding = !TextUtils.isEmpty(padding) ? padding : PADDING_ZERO;

        if (TextUtils.isEmpty(password)) {
            throw new IllegalArgumentException("password不能为空");
        }

        this.secureSalt = secureSalt;
        this.mode = mode;
        this.padding = padding;
        this.password = password;

        try {
            setup();
        } catch (Exception e) {
            LogUtil.e(e);
            throw new IllegalStateException("初始化加密设置失败");
        }
    }

    public static final int KEY_LENGTH = 256;
    public static final int ITERATION_COUNT = 1000;

    private void setup() throws Exception {
        KeySpec keySpec = new PBEKeySpec(password.toCharArray(), secureSalt,
                ITERATION_COUNT, KEY_LENGTH);
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(SECURE_ALGORITHM_PBKDF2WITHHMACSHA1);
        byte[] keyBytes = keyFactory.generateSecret(keySpec).getEncoded();
        SecretKey key = new SecretKeySpec(keyBytes, AES);

        String cipherName = AES + '/' + mode + '/' + padding;

        encryptCipher = Cipher.getInstance(cipherName);
        encryptCipher.init(Cipher.ENCRYPT_MODE, key);

        decryptCipher = Cipher.getInstance(cipherName);
        decryptCipher.init(Cipher.DECRYPT_MODE, key);
    }

    private Cipher encryptCipher;
    private Cipher decryptCipher;

    @Override
    protected byte[] doEncrypt(byte[] src) throws Exception {
        return encryptCipher.doFinal(src);
    }

    @Override
    protected byte[] doDecrypt(byte[] cipherBytes) throws Exception {
        return decryptCipher.doFinal(cipherBytes);
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

    public static final String DEPREACATED_SECURE_ALGORITHM_SHA1PRNG = "SHA1PRNG";
    public static final String DEPREACATED_SECURE_PROVIDER_CRYPTO = "Crypto";

    public static final String SECURE_ALGORITHM_PBKDF2WITHHMACSHA1 = "PBKDF2WithHmacSHA1";

    public static class Builder {

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

        private String mode;
        private String padding;

        public Builder setMode(@Mode String mode) {
            this.mode = mode;
            return this;
        }

        public Builder setPadding(@Padding String padding) {
            this.padding = padding;
            return this;
        }

        private String password;
        private byte[] secureSalt;
        private Charset charset;

        public Builder setPassword(String password) {
            this.password = password;
            return this;
        }

        public Builder setSecureSalt(SharedPreferences pref, String key) {
            return setSecureSalt(pref, key, false);
        }

        public Builder setSecureSalt(SharedPreferences pref, String key, boolean applyOrCommit) {
            setSecureSalt(getOrCreateSalt(pref, key, applyOrCommit));
            return this;
        }

        public Builder setSecureSalt(String secureSaltHex) {
            return setSecureSalt(EncodeUtil.hexToBytes(secureSaltHex));
        }

        public Builder setSecureSalt(byte[] secureSalt) {
            this.secureSalt = secureSalt;
            return this;
        }

        public Builder setCharset(Charset charset) {
            this.charset = charset;
            return this;
        }

        public AESEncryptor build() {
            return new AESEncryptor(charset, secureSalt, mode, padding, password);
        }
    }

    /*SecureSalt*/

    public static final int SALT_LENGTH = 32;

    public static String getOrCreateSalt(SharedPreferences pref, String key) {
        return getOrCreateSalt(pref, key, false);
    }

    public static String getOrCreateSalt(SharedPreferences pref, String key, boolean applyOrCommit) {
        String saltHex = pref.getString(key, null);
        if (TextUtils.isEmpty(saltHex)) {
            saltHex = AESEncryptor.newSecureSaltHex();
            SharedPreferences.Editor editor = pref.edit();
            editor.putString(key, saltHex);
            if (applyOrCommit) {
                editor.apply();
            } else {
                editor.commit();
            }
        }
        return saltHex;
    }

    public static String newSecureSaltHex() {
        return EncodeUtil.toHexString(newSecureSalt());
    }

    public static byte[] newSecureSalt() {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[SALT_LENGTH];
        random.nextBytes(salt);
        return salt;
    }
}
