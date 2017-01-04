package core.mate.util;

import java.io.File;

/**
 * 用于向文件或者目录后添加尾缀以得到一个空的项目的处理工具
 */
public abstract class AbsFileAppender {

    /**
     * {@link #getNextFile(String)}
     *
     * @param baseFile
     * @return
     */
    public File getNextFile(File baseFile) {
        return getNextFile(baseFile.getAbsolutePath());
    }

    /**
     * 获取可用的文件占位
     *
     * @param basePath
     * @return 一个指向可用的空位置的File对象。
     */
    public File getNextFile(String basePath) {
        File availableFile = appendFile(basePath);
        if (!availableFile.exists()) {// 文件不存在，好样的
            return availableFile;
        } else {
            throw new IllegalStateException("appendTag所返回的File对象不允许指向一个已存在的项目");
        }
    }

    /**
     * {@link #getNextDir(String)}
     *
     * @param baseDir
     * @return
     */
    public File getNextDir(File baseDir) {
        return getNextDir(baseDir.getAbsolutePath());
    }

    /**
     * 获取可用的目录占位
     *
     * @param basePath
     * @return 一个指向可用的空位置的File对象。
     */
    public File getNextDir(String basePath) {
        File availableFile = appendDir(basePath);
        if (!availableFile.exists()) {// 文件不存在，好样的
            return availableFile;
        } else {
            throw new IllegalStateException("appendTag所返回的File对象不允许指向一个已存在的项目");
        }
    }

    /**
     * 根据基础的路径不断地拼接tag，直到得到一个不存在的文件的路径位置。
     *
     * @param basePath 文件的基本路径
     * @return 指向空位置的文件路径
     */
    protected abstract File appendFile(String basePath);

    /**
     * @param basePath 文件的基本路径
     * @return
     */
    protected abstract File appendDir(String basePath);
}
