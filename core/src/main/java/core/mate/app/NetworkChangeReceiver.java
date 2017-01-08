package core.mate.app;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;

import core.mate.util.DeviceUtil;

/**
 * @author DrkCore
 * @since 2017/1/3
 */
public class NetworkChangeReceiver extends CoreReceiver {

    private static final String ACTION = ConnectivityManager.CONNECTIVITY_ACTION;
    private static final boolean LOCAL = false;

    public NetworkChangeReceiver() {
        super(LOCAL, ACTION);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        int netState = DeviceUtil.getNetState();
        onNetworkChanged(netState);
    }

    public void onNetworkChanged(int netState) {
        switch (netState) {
            case DeviceUtil.STATE_WIFI:
                onWiFi();
                break;

            case DeviceUtil.STATE_WAP:
                onWap();
                break;

            case DeviceUtil.STATE_CMNET:
                onCmnet();
                break;

            case DeviceUtil.STATE_NONE:
            default:
                onNone();
                break;
        }
    }

    public void onWiFi() {

    }

    public void onWap() {

    }

    public void onCmnet() {

    }

    public void onNone() {

    }

}
