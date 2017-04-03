package core.demo.helper;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import core.demo.App;
import core.mate.util.AESEncryptor;
import core.mate.util.EncryptPrefHelper;

/**
 * App全局的配置辅助类
 *
 * @author DrkCore
 * @since 2016-09-04
 */
public class ConfigHelper {

    /**
     * 因为是应用全局的配置，所以这里写成常量
     */
    private static final EncryptPrefHelper PREF;

    static {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(App.getInstance());
        PREF = new EncryptPrefHelper(
                //获取系统默认的SharePreference
                sp,
                //使用AES加密，你也可以使用自定义的设备唯一码来作为密码
                new AESEncryptor.Builder().setDerivedKeyInsecurely("123",AESEncryptor.KEY_BITS_LENGTH_256).buildQuietly(),
                //是否连同key值一同加密
                false
        );
    }

    /*测试*/

    /**
     * 对外部而言并不关系key值是什么，只要唯一即可，这里写成私有常量。
     */
    private static final String KEY_CONFIG_PARAS = "KEY_CONFIG_PARAS";

    /**
     * 将数据加密后保存到sp中
     * 对于外部的业务逻辑来说加密的过程是透明的
     *
     * @param params
     */
    public static void setConfigParams(String params) {

        PREF.putEncryptedString(KEY_CONFIG_PARAS, params);
    }

    /**
     * 获取解密后的字符串
     * 对于外部的业务逻辑来说解密的过程是透明的
     *
     * @return
     */
    public static String getConfigParams() {
        return PREF.getDecryptedString(KEY_CONFIG_PARAS, null);
    }

    /**
     * 获取未解密的字符串
     *
     * @return
     */
    public static String getRawConfigParams() {
        return PREF.getString(KEY_CONFIG_PARAS, null);
    }

}
