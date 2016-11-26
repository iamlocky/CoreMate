package core.demo.task;

import java.io.File;

import core.mate.async.CoreTask;
import core.mate.util.TextUtil;

/**
 * 读取文本的异步任务
 */
public class ReadTextTask extends CoreTask<File, Void, String> {

    @Override
    public String doInBack(File file) throws Throwable {
        Thread.sleep(3000L);//为了演示这里阻塞3秒

        //使用TextUtil读取文本
        return TextUtil.readText(file);
    }
}
