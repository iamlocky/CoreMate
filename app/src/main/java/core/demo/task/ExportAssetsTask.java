package core.demo.task;

import java.io.File;

import core.mate.async.CoreTask;
import core.mate.util.ContextUtil;

/**
 * 从Assets导出文件的异步任务
 */
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
        Thread.sleep(3000L);//为了演示这里阻塞3秒

        //ResUtil提供了获取资源的大部分方法，因为使用了初始化框架时的Application，
        //所以你可以在任何地方获取资源
        return ContextUtil.exportAssetFile(params.fromAssets, params.toDir, true);
    }
}
