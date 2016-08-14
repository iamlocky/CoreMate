package core.mate.async;

public abstract class OnAsyncListenerImpl<Result> implements AsyncManager.OnAsyncListener<Result> {

    @Override
    public void onStart() {

    }

    @Override
    public void onPrepareNode(AsyncManager.Node node, int idx) {

    }

    @Override
    public void onError(Class<? extends AsyncManager.Node> node, int idx, Exception e) {

    }

    @Override
    public void onNodeSuccess(Class<? extends AsyncManager.Node> node, int idx, Object result) {

    }
}
