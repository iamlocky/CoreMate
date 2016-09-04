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
compile 'core.mate:core:1.0.3'
```

Eclipse用户的话可以将源码down下来手动导入成Lib工程，具体过程不再赘述。

之后在你的Application中进行初始化：

```
//使用Application的Context初始化CoreMate框架
Core.getInstance().init(this);
//开启debug日志输出
Core.getInstance().setDevModeEnable(true);
```

## 功能使用
你可以在源码中的app模块中查看如何使用本框架。

## 联系作者
QQ：178456643
邮箱：178456643@qq.com
