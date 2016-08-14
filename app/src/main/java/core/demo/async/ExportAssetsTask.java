package core.demo.async;

import java.io.File;

import core.demo.App;
import core.mate.async.CoreTask;
import core.mate.util.ResUtil;

public class ExportAssetsTask extends CoreTask<String, Void, File> {

    @Override
    public File doInBack(String s) throws Exception {
        File dir = App.getInstance().getFilesDir();
        return ResUtil.exportAssetFile(s, dir);
    }
}
