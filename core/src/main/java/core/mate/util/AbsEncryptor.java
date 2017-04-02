package core.mate.util;

import android.text.TextUtils;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public abstract class AbsEncryptor {

    private final byte[] salt;
    private final Charset charset;

    public Charset getCharset() {
        return charset;
    }

    public AbsEncryptor() {
        this(null);
    }

    public AbsEncryptor(byte[] salt) {
        this(salt, null);
    }

    public AbsEncryptor(String salt, Charset charset) {
        this.charset = charset != null ? charset : Charset.defaultCharset();
        this.salt = !TextUtils.isEmpty(salt) ? salt.getBytes(this.charset) : null;
    }

    public AbsEncryptor(byte[] salt, Charset charset) {
        this.salt = salt != null ? salt.clone() : null;
        this.charset = charset != null ? charset : Charset.defaultCharset();
    }

    /*Impl*/

    public final byte[] encrypt(byte[] src) {
        try {
            if (src != null && !DataUtil.isEmpty(salt)) {
                src = DataUtil.concat(src, salt);
            }
            return doEncrypt(src);
        } catch (Exception e) {
            LogUtil.e(e);
            throw new IllegalStateException();
        }
    }

    public final byte[] decrypt(byte[] cipher) {
        try {
            byte[] src = doDecrypt(cipher);

            if (src != null && !DataUtil.isEmpty(salt)) {
                byte[] tmp = src;

                int len = src.length - salt.length;
                src = new byte[len];
                System.arraycopy(src, 0, tmp, 0, len);
            }
            return src;
        } catch (Exception e) {
            e.printStackTrace();
            throw new IllegalStateException();
        }
    }

    protected abstract byte[] doEncrypt(byte[] src) throws Exception;

    protected abstract byte[] doDecrypt(byte[] cipher) throws Exception;

	/*Ext*/

    public final String encryptToHex(String src) {
        byte[] encrypted = encrypt(src.getBytes(charset));
        return EncodeUtil.toHexString(encrypted);
    }

    public final String decryptHex(String hexStr) {
        byte[] cipher = EncodeUtil.toBytes(hexStr);
        byte[] decrypted = decrypt(cipher);
        return new String(decrypted, charset);
    }

    /**
     * 加密集合中的字符串。该方法不会修改原有的数据。
     *
     * @param values
     * @return
     */
    public final List<String> encrypt(Collection<String> values) {
        if (values == null) {
            return null;
        }

        List<String> list = new ArrayList<>(values.size());
        if (!values.isEmpty()) {
            for (String v : values) {
                list.add(encryptToHex(v));
            }
        }
        return list;
    }

    /**
     * 加密数组中的字符串。该方法不会修改原有数据。
     *
     * @param values
     * @return
     */
    public final String[] encrypt(String... values) {
        if (values == null) {
            return null;
        }

        int len = values.length;
        String[] encryptValues = new String[len];
        for (int i = 0; i < len; i++) {
            encryptValues[i] = encryptToHex(values[i]);
        }
        return encryptValues;
    }

    /**
     * 解密集合中的字符串。该方法不会修改原有数据。
     *
     * @param values
     * @return
     */
    public final List<String> decrypt(Collection<String> values) {
        if (values == null) {
            return null;
        }

        List<String> list = new ArrayList<>(values.size());
        if (!values.isEmpty()) {
            for (String v : values) {
                list.add(decryptHex(v));
            }
        }
        return list;
    }

    /**
     * 解密数组中的字符串。该方法不会修改原有数据。
     *
     * @param values
     * @return
     */
    public final String[] decrypt(String... values) {
        if (values == null) {
            return null;
        }

        int len = values.length;
        String[] decryptValues = new String[len];
        if (len > 0) {
            for (int i = 0; i < len; i++) {
                decryptValues[i] = decryptHex(values[i]);
            }
        }
        return decryptValues;
    }

}
