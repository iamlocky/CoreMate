package core.mate.util;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import java.io.File;

import core.mate.Core;

/**
 * @author DrkCore
 * @since 2016年9月27日23:11:39
 */
public class IntentUtil {

    /*发送*/

	public static Intent getSendTextIntent(String title, String content) {
		Intent intent = new Intent(Intent.ACTION_SEND);
		intent.putExtra(Intent.EXTRA_TITLE, title);
		intent.putExtra(Intent.EXTRA_TEXT, content);
		intent.setType("text/plain");
		return intent;
	}

	public static Intent getSendMailIntent(String toMail, String title, String content) {
		Intent intent = new Intent(Intent.ACTION_SEND);
		intent.setType("text/plain");
		intent.putExtra(Intent.EXTRA_EMAIL, new String[]{toMail});
		intent.putExtra(Intent.EXTRA_SUBJECT, title);
		intent.putExtra(Intent.EXTRA_TEXT, content);
		return intent;
	}

	public static Intent getSendFileIntent(File file) {
		Intent intent = new Intent(Intent.ACTION_SEND);
		intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
		intent.setType("text/plain");
		return intent;
	}

	public static Intent getAppMarketIntent(String pkgName) {
		Uri uri = Uri.parse("market://details?id=" + pkgName);
		Intent intent = new Intent(Intent.ACTION_VIEW, uri);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		return intent;
	}

	public static Intent getChoseImageIntent() {
		Intent intent = new Intent(Intent.ACTION_GET_CONTENT);//ACTION_OPEN_DOCUMENT
		intent.addCategory(Intent.CATEGORY_OPENABLE);
		intent.setType("image/*");
		return intent;
	}

    /*其他*/

	public static Intent getInstallApkIntent(File apk) {
		return new Intent(Intent.ACTION_VIEW)
				.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
				.setDataAndType(Uri.parse("file://" + apk), "application/vnd.android.package-archive");
	}

	public static Intent getStartAppIntent(String pkgName) {
		Context app = Core.getInstance().getAppContext();
		return app.getPackageManager().getLaunchIntentForPackage(pkgName);
	}

	public static Intent getOpenUrlIntent(String url) {
		Uri uri = Uri.parse(url);
		return new Intent(Intent.ACTION_VIEW, uri);
	}
}
