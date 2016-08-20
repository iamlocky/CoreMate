package core.mate.async;

public abstract class NodeImpl<AsyncMgr extends AsyncManager> implements AsyncManager.Node<AsyncMgr>{

    /*任务节点*/

    private AsyncMgr asyncMgr;

    @Override
    public final void startWith(AsyncMgr asyncMgr) {
        this.asyncMgr = asyncMgr;
        doStart(asyncMgr);
    }

    protected abstract void doStart(AsyncMgr asyncMgr);

    @Override
    public final void endWith(Object result, Exception error) {
        asyncMgr.commitNode(this, result, error);
    }
}
