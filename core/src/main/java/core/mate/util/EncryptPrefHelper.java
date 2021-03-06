package core.mate.util;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * 带有加密功能的{@link SharedPreferences}的辅助类。
 * <p>
 * 虽然继承自{@link SyncPrefHelper} 但是默认情况下
 * 已关闭了线程安全的开关。
 *
 * @author DrkCore
 * @since 2016年2月18日17:56:06
 */
public class EncryptPrefHelper extends SyncPrefHelper {

	/*成员*/

    private final AbsEncryptor encryptor;
    private final boolean encryptKeyEnable;

    public boolean isEncryptKeyEnable() {
        return encryptKeyEnable;
    }

    public EncryptPrefHelper(String pref, AbsEncryptor encryptor) {
        this(pref, Context.MODE_PRIVATE, encryptor, true);
    }

    public EncryptPrefHelper(String pref, int mode, AbsEncryptor encryptor, boolean encryptKeyEnable) {
        super(pref, mode);
        this.encryptor = encryptor;
        this.encryptKeyEnable = encryptKeyEnable;
        setThreadSafe(false);
    }

    public EncryptPrefHelper(SharedPreferences pref, AbsEncryptor encryptor, boolean encryptKeyEnable) {
        super(pref);
        this.encryptor = encryptor;
        this.encryptKeyEnable = encryptKeyEnable;
        setThreadSafe(false);
    }

    /*字符串加解密*/

    private String getKey(String originKey) {
        return encryptKeyEnable ? encryptor.encryptHex(originKey) : originKey;
    }

    public final EncryptPrefHelper putEncryptedString(String key, String value) {
        key = getKey(key);
        value = value != null ? encryptor.encryptHex(value) : null;
        putString(key, value);
        return this;
    }

    public final String getDecryptedString(String key, String defValue) {
        key = getKey(key);
        String value = getString(key, null);
        return value != null ? encryptor.decryptHex(value) : defValue;
    }

    /*基础数据类型加解密*/

    public final EncryptPrefHelper putEncryptedBoolean(String key, boolean value) {
        String valueStr = String.valueOf(value);
        return putEncryptedString(key, valueStr);
    }

    public final EncryptPrefHelper putEncryptedInt(String key, int value) {
        String valueStr = String.valueOf(value);
        return putEncryptedString(key, valueStr);
    }

    public final EncryptPrefHelper putEncryptedLong(String key, long value) {
        String valueStr = String.valueOf(value);
        return putEncryptedString(key, valueStr);
    }

    public final EncryptPrefHelper putEncryptedFloat(String key, float value) {
        String valueStr = String.valueOf(value);
        return putEncryptedString(key, valueStr);
    }

    public final EncryptPrefHelper putEncryptedDouble(String key, double value) {
        String valueStr = String.valueOf(value);
        return putEncryptedString(key, valueStr);
    }

    public final boolean getDecryptedBoolean(String key, boolean defValue) {
        String valueStr = getDecryptedString(key, null);
        return valueStr != null ? Boolean.parseBoolean(valueStr) : defValue;
    }

    public final int getDecryptedInt(String key, int defValue) {
        String valueStr = getDecryptedString(key, null);
        return valueStr != null ? Integer.parseInt(valueStr) : defValue;
    }

    public final long getDecryptedLong(String key, long defValue) {
        String valueStr = getDecryptedString(key, null);
        return valueStr != null ? Long.parseLong(valueStr) : defValue;
    }

    public final float getDecryptedFloat(String key, float defValue) {
        String valueStr = getDecryptedString(key, null);
        return valueStr != null ? Float.parseFloat(valueStr) : defValue;
    }

    public final double getDecryptedDouble(String key, double defValue) {
        String valueStr = getDecryptedString(key, null);
        return valueStr != null ? Double.parseDouble(valueStr) : defValue;
    }

}
