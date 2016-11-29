# CoreMate

## 简介
该库是作者入坑Android这几年来积累下来的代码，主要包括：

  * Activity、Fragment的基类
  * 高度简化的Adapter
  * AsyncTask的再封装
  * 文件、加密、反射等一系列的实用工具类
  * SharedPreference的再封装，支持本地加密
  * 常用的一些资源

如你所见该库以轻量化为目标，主要的内容都是将Android提供的类进行再封装，适合作为项目的基础以省去大部分冗余代码。

你可以在本库的基础之上配合其他视图注入、HTTP、数据库和图片加载等框架来构建你的项目的整体框架。这里你可以看看作者在本库的基础上对xUtils3框架的封装：[xMate](https://github.com/DrkCore/xMate)。

开源才不久该库还有不少可以完善的地方，所以欢迎大家start/fork~

## 如何集成
Android Studio用户可有在模块的build.gradle中于dependencies下添加如下依赖：

```
compile 'core.mate:core:1.0.8'
```

Eclipse用户的话可以将源码down下来手动导入成Lib工程，具体过程不再赘述。

之后在你的Application中进行初始化：

```
//使用Application的Context初始化CoreMate库
Core.getInstance().init(this);
//开启debug日志输出
Core.getInstance().setDevModeEnable(true);
```

## 功能使用
你可以在源码的app模块中查看如何使用本库，这里只作简要介绍。

### 万能Adapter
创建Adapter实例
```
 //Adapter内部已经封装好了ViewHolder的逻辑，这里可以直接通过资源id或者控件类名来创建item
new SimpleAdapter<String>(android.R.layout.simple_list_item_1) {
    @Override
    protected void bindViewData(SimpleViewHolder<String> holder, int position, String data, int viewType) {
        //设置数据可以通过id直接setText
        holder.setText(android.R.id.text1, data);
        //也可以获取到控件后再设置
        //TextView textView = holder.getViewById(android.R.id.text1);
        //textView.setText(data);
    }
}
```
刷新数据：
```
//display方法有数组和Collection<>两个重载方法，这里是使用数组刷新
adapter.display(
    "\\(^o^)/",
    "b(￣▽￣)d",
    "ヾ(≧▽≦*)o",
    "(づ￣3￣)づ╭❤～",
    "_(:зゝ∠)_",
    "(◑▽◐)",
    "哼(ˉ(∞)ˉ)唧"
);
```

![Adapter使用截图](https://raw.githubusercontent.com/DrkCore/CoreMate/master/doc/img/Adapter使用.jpg)

以上是ListView的Adapter封装，如果是RecyclerView的话可以使用[SimpleRecyclerAdapter](https://github.com/DrkCore/CoreMate/blob/master/core/src/main/java/core/mate/adapter/SimpleRecyclerAdapter.java)，思想是一样的。

你可以在[core.mate.adapter](https://github.com/DrkCore/CoreMate/tree/master/core/src/main/java/core/mate/adapter)包下查看所有的Adapter以及继承逻辑。

### 如何封装一个异步任务
继承CoreTask来实现你的异步任务
```
public class ExportAssetsTask extends CoreTask<ExportAssetsTask.Params, Void, File> {

    public static class Params {

        public final String fromAssets;
        public final File toDir;

        public Params(String fromAssets, File toDir) {
            this.fromAssets = fromAssets;
            this.toDir = toDir;
        }
    }

    @Override
    public File doInBack(Params params) throws Exception {
        //ResUtil提供了获取资源的大部分方法，因为使用了初始化库时的Application，
        //所以你可以在任何地方获取资源
        return ResUtil.exportAssetFile(params.fromAssets, params.toDir, true);
    }
}
```
在doInBack方法中填写耗时操作的逻辑，如果没有抛出异常就认定为任务成功，否则就是失败。

在业务的代码中如下调用：
```
new ExportAssetsTask()
        //添加不可取消的转菊花对话框，阻塞用户操作
        .addIndicator(new ProgressDlgFrag().setFragmentManager(this))
        //添加回调
        .addOnTaskListener(new OnTaskListenerImpl<File>() {
            @Override
            public void onSuccess(File file) {
                ToastUtil.toastShort("成功导出到=" + file.getAbsolutePath());
            }
        })
        //将assets中的文件导出到/data/data/包名/files目录下
        .execute(new ExportAssetsTask.Params("test.txt", getContext().getFilesDir()));
```

CoreTask实现了Clearable接口，而在CoreActivity、CoreFrag、CoreDlgFrag均有如下方法用于清理Clearable：

```
public final void addClearable(Clearable clearable);
public final <T> T addClearableEx(T t);
public final void clearAllClearable();
```
在onDestroy方法中默认会调用clearAllClearable()方法清理所有对象。

因而，如果你担心Activity已经销毁后异步任务才执行完成的话可以在执行之前通过adClearable()标记要清理的异步任务。

### SharedPreference的封装和加密
```
建议将系统的配置封装成一个单独的类，如下：
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
    private static final EncryptPrefHelper PREF = new EncryptPrefHelper(
            //获取系统默认的SharePreference
            PreferenceManager.getDefaultSharedPreferences(App.getInstance()),
            //使用AES加密，你也可以使用自定义的设备唯一码来作为密码，或者自定义加密
            new AESEncryptor("123"),
            //是否连同key值一同加密
            false
    );

    /*配置*/

    /**
     * 对外部而言并不关心key值是什么，只要唯一即可，这里写成私有常量。
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
```
外部使用的的时候如下：
```
String text = editText.getText().toString();
ConfigHelper.setConfigParams(text);

String rawParams = ConfigHelper.getRawConfigParams();
String params = ConfigHelper.getConfigParams();

CharSequence result = TextUtils.concat("加密后的字符串 = \n", rawParams, "\n\n解密后的字符串 = \n", params);
textView.setText(result);
```
加密解密的过程对于外部而言是透明的。

![](https://raw.githubusercontent.com/DrkCore/CoreMate/master/doc/img/Pref加密解密.jpg)

### 工具类
我想大部分开发者写了几年代码多多少少都会积累下一套工具类，功能大同小异，却是开发中必不可少的部分。

本库提供的工具类可以在[core.mate.util](https://github.com/DrkCore/CoreMate/tree/master/core/src/main/java/core/mate/util)包下找到，
具体使用请参阅其中源码。

### 常用资源
本库带有一些基础的资源，并且都以“core_”打头，包括常见的anim、color、drawable等，具体内容请到源码中查看。

## 联系作者
QQ：178456643
邮箱：178456643@qq.com
