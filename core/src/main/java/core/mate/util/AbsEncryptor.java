package core.mate.util;

import java.nio.charset.Charset;

public abstract class AbsEncryptor {

    private final Charset charset;

    public Charset getCharset() {
        return charset;
    }

    public AbsEncryptor(Charset charset) {
        this.charset = charset != null ? charset : Charset.defaultCharset();
    }

    /*Impl*/

    public final byte[] encrypt(byte[] src) {
        try {
            return doEncrypt(src);
        } catch (Exception e) {
            LogUtil.e(e);
            throw new IllegalStateException();
        }
    }

    public final byte[] decrypt(byte[] cipher) {
        try {
            return doDecrypt(cipher);
        } catch (Exception e) {
            e.printStackTrace();
            throw new IllegalStateException();
        }
    }

    protected abstract byte[] doEncrypt(byte[] src) throws Exception;

    protected abstract byte[] doDecrypt(byte[] cipher) throws Exception;

	/*Ext*/

    public final String encryptHex(String src) {
        byte[] encrypted = encrypt(src.getBytes(charset));
        return EncodeUtil.encodeHex(encrypted);
    }

    public final String decryptHex(String hexStr) {
        byte[] cipher = EncodeUtil.decodeHex(hexStr);
        byte[] decrypted = decrypt(cipher);
        return new String(decrypted, charset);
    }

    public final String encryptBase64(String src) {
        byte[] encrypted = encrypt(src.getBytes(charset));
        return EncodeUtil.encodeBase64(encrypted);
    }

    public final String decryptBase64(String base64) {
        byte[] cipher = EncodeUtil.decodeBase64(base64);
        byte[] decrypted = decrypt(cipher);
        return new String(decrypted, charset);
    }

}
