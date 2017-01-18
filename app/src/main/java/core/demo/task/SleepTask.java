package core.demo.task;

import core.mate.async.CoreTask;
import core.mate.util.LogUtil;

/**
 * 删除文件的异步任务。
 *
 * @author DrkCore
 * @since 2016-09-04
 */
public class SleepTask extends CoreTask<Long, Void, Void> {
    @Override
    public Void doInBack(Long aLong) throws Throwable {
        LogUtil.d("开始睡眠"+aLong);
        Thread.sleep(aLong);
        return null;
    }
}
