package core.mate.async;

import android.util.SparseArray;

import java.util.ArrayList;
import java.util.List;

import core.mate.common.Clearable;
import core.mate.common.ClearableHolder;
import core.mate.util.LogUtil;

/**
 * 用于串行执行异步的任务的辅助类。
 *
 * @author DrkCore
 * @since 2016年8月14日22:46:11
 */
public class AsyncManager implements Clearable {

    public static final int STATE_PRE = 0;
    public static final int STATE_START = 1;
    public static final int STATE_ERROR = 2;
    public static final int STATE_DONE = 3;

    private int state = STATE_PRE;

    public final int getState() {
        return state;
    }

    /*节点管理*/

    public interface Node<Depend, Params, Result> extends Clearable {

        void setupNode(AsyncManager asyncMgr, int idx);

        /**
         * 上个节点的结果将作为该节点的依赖。
         * 如果该方法返回非null的对象，当该节点对应的params为null时将会使用该对象作为params用于调用{@link #startWith(Object)}。
         *
         * @param depend
         * @return
         */
        Params dependOn(Depend depend);

        void startWith(Params params);

        void errorWith(Exception error);

        void successWith(Result result);

    }

    private final List<Class<? extends Node>> nodes = new ArrayList<>();
    private final SparseArray<Object> params = new SparseArray<>();

    public final int getNodeCount() {
        return nodes.size();
    }

    public final AsyncManager add(Class<? extends Node> node) {
        return add(node, null);
    }

    public final AsyncManager add(Class<? extends Node> node, Object params) {
        if (state != STATE_PRE) {
            throw new IllegalStateException("只允许在STATE_PRE阶段添加新的节点");
        }

        int idx = this.nodes.size();
        this.nodes.add(node);
        this.params.put(idx, params);
        return this;
    }

    public final void start() {
        start(null);
    }

    public final void start(Object firstDepend) {
        if (state != STATE_PRE) {
            throw new IllegalStateException("只允许在STATE_PRE状态下启动任务");
        } else if (nodes.isEmpty()) {
            throw new IllegalStateException("未添加任何节点，无法启动任务");
        }

        state = STATE_START;

        if (onAsyncListener != null) {
            onAsyncListener.onStart();
        }

        startNode(0, firstDepend);
    }

    protected final void startNode(int idx, Object depend) {
        //实例化节点
        Class<? extends Node> node = nodes.get(idx);
        Node instance;
        try {
            instance = node.newInstance();
        } catch (Exception e) {
            LogUtil.e(e);
            throw new IllegalStateException("无法使用默认构造方法创建Node实例");
        }

        if (onAsyncListener != null) {
            onAsyncListener.onPrepareNode(instance, idx);
        }

        clearableHolder.add(instance);

        //初始化节点参数和依赖
        instance.setupNode(this, idx);
        Object tmpParams = instance.dependOn(depend);
        Object saveParams = this.params.get(idx);

        //依赖转换
        if (saveParams == null && tmpParams != null) {
            saveParams = tmpParams;
        }

        try {//走你
            instance.startWith(saveParams);
        } catch (Exception e) {
            LogUtil.e(e);
            throw new IllegalStateException("无法使用" + params + "参数启动" + node + "节点");
        }
    }

    public final void onNodeError(Class<? extends Node> node, int idx, Exception e) {
        clear();

        int redirectIdx = onRedirectOnError(node, idx, e);
        if (redirectIdx >= 0) {
            Object params = this.params.get(redirectIdx);
            startNode(redirectIdx, params);
            return;
        }

        state = STATE_ERROR;

        if (onAsyncListener != null) {
            onAsyncListener.onError(node, idx, e);
        }
    }

    protected int onRedirectOnError(Class<? extends Node> node, int idx, Exception e) {
        return -1;
    }

    public final void onNodeResult(Class<? extends Node> node, int idx, Object result) {
        if (idx == nodes.size() - 1) {//最后一个任务完成了
            clear();
            state = STATE_DONE;

            if (onAsyncListener != null) {
                onAsyncListener.onFinalSuccess(result);
            }
        } else {//还有下个任务
            if (onAsyncListener != null) {
                onAsyncListener.onNodeSuccess(node, idx, result);
            }
            startNode(++idx, result);
        }
    }

    /*监听者*/

    public interface OnAsyncListener<Result> {

        void onStart();

        void onPrepareNode(Node node, int idx);

        void onError(Class<? extends Node> node, int idx, Exception e);

        void onNodeSuccess(Class<? extends Node> node, int idx, Object result);

        void onFinalSuccess(Result result);

    }

    private OnAsyncListener onAsyncListener;

    public final AsyncManager setOnAsyncListener(OnAsyncListener onAsyncListener) {
        this.onAsyncListener = onAsyncListener;
        return this;
    }

    /*Clearable*/

    private final ClearableHolder clearableHolder = new ClearableHolder();

    @Override
    public final boolean isCleared() {
        return clearableHolder.isCleared();
    }

    @Override
    public final void clear() {
        clearableHolder.clearAll();
    }
}
