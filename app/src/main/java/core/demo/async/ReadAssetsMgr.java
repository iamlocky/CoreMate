package core.demo.async;

import java.io.File;

import core.mate.async.LinearAsyncManager;
import core.mate.async.TaskNodeWrapper;

public class ReadAssetsMgr extends LinearAsyncManager {

    public static class ExportAssetsTaskWrapper extends TaskNodeWrapper<ReadAssetsMgr, File> {

        @Override
        protected void doStart(ReadAssetsMgr readAssetsMgr) {
            ExportAssetsTask task = new ExportAssetsTask();
            task.addOnTaskListener(this);
            task.execute(new ExportAssetsTask.Params(readAssetsMgr.fromAsset, readAssetsMgr.toDir));
        }
    }

    public static class ReadTextTaskWrapper extends TaskNodeWrapper<ReadAssetsMgr, String> {

        @Override
        protected void doStart(ReadAssetsMgr readAssetsMgr) {
            ReadTextTask task = new ReadTextTask();
            task.addOnTaskListener(this);
            task.execute(readAssetsMgr.targetFile);
        }
    }

    private String fromAsset;
    private File toDir;

    public ReadAssetsMgr setUp(String fromAsset, File toDir) {
        this.fromAsset = fromAsset;
        this.toDir = toDir;
        return this;
    }

    private File targetFile;
    private String result;

    public String getResult() {
        return result;
    }

    public ReadAssetsMgr() {
        add(ExportAssetsTaskWrapper.class, ReadTextTaskWrapper.class);
    }

    @Override
    protected void onNodeEnd(Class node, Object instance, Object result, Exception e) {
        super.onNodeEnd(node, instance, result, e);
        if (node == ExportAssetsTaskWrapper.class && e == null) {
            this.targetFile = (File) result;
        } else if (node == ReadTextTaskWrapper.class && e == null) {
            this.result = (String) result;
        }
    }
}
