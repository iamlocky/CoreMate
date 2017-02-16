package core.mate.app;

import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

import core.mate.util.BroadcastUtil;
import core.mate.util.DataUtil;

/**
 * @author DrkCore
 * @since 2017/2/16
 */
public class ReceiverHelper {

    private List<CoreReceiver> fullReceivers;
    private List<CoreReceiver> resumeReceivers;

    public void onResume() {
        for (int i = 0, len = DataUtil.getSize(resumeReceivers); i < len; i++) {
            registerReceiver(resumeReceivers.get(i));
        }
    }

    public void onPause() {
        for (int i = 0, len = DataUtil.getSize(resumeReceivers); i < len; i++) {
            unregisterReceiver(resumeReceivers.get(i));
        }
    }

    public void onDestroy() {
        for (int i = 0, len = DataUtil.getSize(fullReceivers); i < len; i++) {
            unregisterReceiver(fullReceivers.get(i));
        }
    }

    /**
     * 注册长时间监听的广播。该广播会在调用该方法时自动注册，并在{@link #onDestroy()}中注销。
     *
     * @param receiver
     */
    public void addFullReceiver(CoreReceiver receiver) {
        if (fullReceivers == null) {
            fullReceivers = new ArrayList<>();
        }
        fullReceivers.add(receiver);
        registerReceiver(receiver);
    }

    /**
     * 注册只在{@link #onResume()}和{@link #onPause()}之间的窗口启用的广播。
     * <p>
     * 注意，如果在窗口期内调用该方法的话只会在下个窗口期开始时真正注册到上下文中，
     * 因而只建议在{@link #onCreate(Bundle)}中使用该方法。
     *
     * @param receiver
     */
    public void addResumeReceiver(CoreReceiver receiver) {
        if (resumeReceivers == null) {
            resumeReceivers = new ArrayList<>();
        }

        resumeReceivers.add(receiver);
    }

    public void registerReceiver(CoreReceiver receiver) {
        BroadcastUtil.getManager(receiver.isLocal()).register(receiver, receiver.getFilter());
    }

    public void unregisterReceiver(CoreReceiver receiver) {
        BroadcastUtil.getManager(receiver.isLocal()).unregister(receiver);
    }

}
