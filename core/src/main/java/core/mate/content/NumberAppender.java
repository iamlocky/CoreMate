package core.mate.content;

import java.io.File;

import core.mate.util.AbsFileAppender;
import core.mate.util.FileUtil;
import core.mate.util.LogUtil;

/**
 * 使用数字作为tag的appender
 * 
 *@author DrkCore
 * @since 2015年9月26日15:30:28
 */
public class NumberAppender extends AbsFileAppender {
	
	private static final NumberAppender INSTANCE = new NumberAppender();
	
	public static NumberAppender getInstance() {
		return INSTANCE;
	}
	
	private NumberAppender() {
		
	}
	
	/* 继承 */
	
	@Override
	protected File appendFile(String basePath) {
		File dir = new File(basePath);
		if (dir.exists()) {// 重名文件已存在
			String type = FileUtil.getExt(basePath);
			if (type != null) {// 拓展名存在
				// 获取不带拓展名的的builder
				StringBuilder path = new StringBuilder(basePath.substring(0, basePath.length() - type.length() - 1));
				int len, left, right, num;
				do {
					// 初始化参数
					num = -1;// 置空数字标记
					
					len = path.length();
					left = path.lastIndexOf("(");
					right = path.lastIndexOf(")");
					if (left != -1 && right != -1) {// 左右括号存在
						if (right - left > 1 && right + 1 == len) {// 括号从左到右，之间有东西，且右括号在末尾
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
						path.delete(left, len);
						path.append('(').append(num + 1).append(')');
					} else {// 并没有，呵呵
						path.append("(1)");
					}
					
					// 最后创建的时候把拓展名加上去
					dir = new File(path.toString() + '.' + type);
				} while (dir.exists());
			} else {// 拓展名不存在
				StringBuilder path = new StringBuilder(basePath);
				int len, left, right, num;
				do {
					// 初始化参数
					num = -1;// 置空数字标记
					
					len = path.length();
					left = path.lastIndexOf("(");
					right = path.lastIndexOf(")");
					if (left != -1 && right != -1) {// 左右括号存在
						if (right - left > 1 && right + 1 == len) {// 括号从左到右，且之间有空，且右括号在末尾
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
						path.delete(left, len);
						path.append('(').append(num + 1).append(')');
					} else {// 并没有，呵呵
						path.append("(1)");
					}
					
					dir = new File(path.toString());
				} while (dir.exists());
			}
		}
		return dir;
	}
	
	@Override
	protected File appendDir(String basePath) {
		File dir = new File(basePath);
		if (dir.exists()) {// 重名文件已存在
			StringBuilder path = new StringBuilder(basePath);
			int len, left, right, num;
			do {
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
					path.delete(left, len);
					path.append('(').append(num + 1).append(')');
				} else {// 并没有，呵呵
					path.append("(1)");
				}
				
				dir = new File(path.toString());
			} while (dir.exists());
		}
		return dir;
	}
}
