package core.mate.util;

import java.io.File;

/**
 * 用于向文件或者目录后添加尾缀以得到一个空的项目的处理工具
 *
 * @author DrkCore
 * @since 2017年1月8日11:17:24
 */
public abstract class AbsFileAppender {

    public File nextFile(String path) {
        return nextFile(new File(path));
    }

    public File nextFile(File file) {
        return !file.exists() ? file : nextFile(appendFile(file.getAbsolutePath()));
    }

    public File nextDir(String path) {
        return nextDir(new File(path));
    }

    public File nextDir(File dir) {
        return !dir.exists() ? dir : nextDir(appendDir(dir.getAbsolutePath()));
    }

    protected abstract String appendFile(String path);

    protected abstract String appendDir(String path);
}
