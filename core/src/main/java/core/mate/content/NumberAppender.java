package core.mate.content;

import core.mate.util.AbsFileAppender;
import core.mate.util.FileUtil;
import core.mate.util.LogUtil;

/**
 * 使用数字作为tag的appender
 *
 * @author DrkCore
 * @since 2015年9月26日15:30:28
 */
public class NumberAppender extends AbsFileAppender {

    private static volatile NumberAppender instance = null;

    private NumberAppender() {
    }

    public static NumberAppender getInstance() {
        if (instance == null) {
            synchronized (NumberAppender.class) {
                if (instance == null) {
                    instance = new NumberAppender();
                }
            }
        }
        return instance;
    }

	/* 继承 */

    @Override
    protected String appendFile(String path) {
        String ext = FileUtil.getExt(path);
        if (ext != null) {// 拓展名存在
            String basePath = path.substring(0, path.length() - ext.length() - 1);
            return appendDir(basePath) + "." + ext;
        } else {// 拓展名不存在，逻辑等同于目录
            return appendDir(path);
        }
    }

    @Override
    protected String appendDir(String path) {
        StringBuilder builder = new StringBuilder(path);
        int len, left, right, num;
        // 初始化参数
        num = -1;// 置空数字标记

        len = path.length();
        left = path.lastIndexOf("(");
        right = path.lastIndexOf(")");
        if (left != -1 && right != -1) {// 左右括号存在
            if (right - left > 1 && right + 1 == len) {// 括号从左到右，且中间有位数，且右括号在末尾
                String numStr = path.substring(left + 1, right);
                try {
                    // 获取括号之间的数字
                    num = Integer.parseInt(numStr);
                } catch (NumberFormatException e) {
                    LogUtil.e(e);
                }
            }
        }

        if (num != -1) {// 存在括号之间的数字，增之，替换
            builder.delete(left, len);
            builder.append('(').append(num + 1).append(')');
        } else {// 暂无数据
            builder.append("(1)");
        }

        return builder.toString();
    }
}
