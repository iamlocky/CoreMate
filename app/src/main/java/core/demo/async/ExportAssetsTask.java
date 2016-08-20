package core.demo.async;

import java.io.File;

import core.demo.App;
import core.mate.async.CoreTask;
import core.mate.util.ResUtil;

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
        return ResUtil.exportAssetFile(params.fromAssets, params.toDir, true);
    }
}
