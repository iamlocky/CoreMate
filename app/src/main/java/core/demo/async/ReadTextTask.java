package core.demo.async;

import java.io.File;

import core.mate.async.CoreTask;
import core.mate.util.TextUtil;

public class ReadTextTask extends CoreTask<File, Void, String> {

    @Override
    public String doInBack(File file) throws Exception {
        return TextUtil.readText(file);
    }
}
