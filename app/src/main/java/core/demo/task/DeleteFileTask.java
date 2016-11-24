package core.demo.task;

import java.io.File;

import core.mate.async.CoreTask;
import core.mate.util.FileUtil;

/**
 * 删除文件的异步任务。
 *
 * @author DrkCore
 * @since 2016-09-04
 */
public class DeleteFileTask extends CoreTask<File[], Void, boolean[]> {
    @Override
    public boolean[] doInBack(File[] files) throws Exception {
        Thread.sleep(3000L);//为了演示这里阻塞3秒

        //使用FileUtil删除文件或者文件夹
        return FileUtil.deleteItems(files);
    }
}
