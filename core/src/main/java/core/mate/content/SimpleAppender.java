package core.mate.content;

import android.support.v4.util.ArrayMap;

import java.util.Map;

import core.mate.util.AbsFileAppender;
import core.mate.util.FileUtil;
import core.mate.util.TextUtil;

/**
 * 简单的文件名称的tag添加工具。
 * 比如当路径为“/storage/sdcard0/1.txt”，处理后可以得到“/storage/sdcard0/1【tag...tag】.txt”
 * 类似的名称。<br/>
 * 如果路径类似“/storage/sdcard0/abc”，处理后可得到“/storage/sdcard0/abc【tag...tag】”类似的名称。<br/>
 * <p>
 * 即不影响文件的类型名。
 *
 * @author DrkCore
 * @since 2015年9月22日23:37:16
 */
public class SimpleAppender extends AbsFileAppender {

    private static final Map<String, SimpleAppender> cache = new ArrayMap<>();

    public static SimpleAppender getInstance(String tag) {
        SimpleAppender appender = cache.get(tag);
        if (appender == null) {
            appender = new SimpleAppender(tag);
            cache.put(tag, appender);
        }
        return appender;
    }

    private final String tag;

    private SimpleAppender(String tag) {
        if (tag == null || tag.equals("")) {
            throw new IllegalArgumentException("tag不可为空");
        } else if (tag.contains("\\") || tag.contains("/") || tag.contains(".")) {
            throw new IllegalArgumentException("tag中不允许带有非法字符");
        }
        this.tag = tag;
    }

    @Override
    protected String appendFile(String path) {
        String ext = FileUtil.getExt(path);
        if (ext != null) {// 拓展名存在
            return TextUtil.buildString(path.substring(0, path.length() - ext.length() - 1), tag, ".", ext);
        } else {// 拓展名不存在，逻辑等同于目录
            return appendDir(path);
        }
    }

    @Override
    protected String appendDir(String path) {
        return path + tag;
    }
}
