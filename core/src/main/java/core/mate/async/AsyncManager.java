package core.mate.async;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import core.mate.common.Clearable;
import core.mate.common.ClearableHolder;
import core.mate.common.ITaskIndicator;
import core.mate.util.LogUtil;

/**
 * 用于处理任务调度的辅助类，你可以将之理解为状态机。
 * <p/>
 * 所有与状态相关的参数应该保存在状态机之中，任务节点应该在
 * {@link Node#startWith(AsyncManager)} 方法中获取参数并
 * 启动节点，最终将参数反馈到该状态机的实例中。
 * <p/>
 * 该类适用于将嵌套回调改写成线性的形式，比如某一个API依赖于
 * 前一个API的结果，或者需要线性处理异步任务等。
 *
 * @author DrkCore
 * @since 2016年8月14日22:46:11
 */
public abstract class AsyncManager implements Clearable {

    public static final int STATE_PRE = 0;
    public static final int STATE_START = 1;
    public static final int STATE_FINISH = 2;

    @IntDef({
            STATE_PRE,
            STATE_START,
            STATE_FINISH
    })
    @Retention(RetentionPolicy.SOURCE)
    public @interface AsyncManagerState {

    }

    private int state = STATE_PRE;

    public final int getState() {
        return state;
    }

    /*节点管理*/

    /**
     * 任务节点接口。在实现子类的时候你可以使用{@link NodeImpl}
     * 和{@link TaskNodeWrapper}的形式来简化任务节点的编写。
     * <p/>
     * 由于需要反射创建节点实例，因而节点类必须要有可用的无参构造函数。
     * <p/>
     * 你可以从该框架的demo工程中找到比较推荐的写法。
     *
     * @param <AsyncMgr>
     */
    public interface Node<AsyncMgr extends AsyncManager> {

        /**
         * 启动任务节点。你需要从asyncMgr实例中获取启动该节点
         * 的参数。
         *
         * @param asyncMgr
         */
        void startWith(AsyncMgr asyncMgr);

        /**
         * 任务节点完成时应该回调该方法。你需要在该方法中通过
         * {@link AsyncManager#commitNode(Object, Object, Exception)}
         * 方法将结果告知状态机。
         *
         * @param result
         * @param error
         */
        void endWith(Object result, Exception error);

    }

    public final void start() {
        if (state != STATE_PRE) {
            throw new IllegalStateException("AsyncManager不允许重复启动");
        }

        onStart();
        doStart();
    }

    protected final void startNode(Class node) {
        //实例化节点
        Node instance;
        try {
            instance = (Node) node.newInstance();
        } catch (Exception e) {
            LogUtil.e(e);
            throw new IllegalStateException("无法使用默认构造方法创建Node实例");
        }

        //添加clearable
        if (instance instanceof Clearable) {
            clearableHolder.add((Clearable) instance);
        }

        try {//走你
            onNodeStart(node, instance);
            instance.startWith(this);
        } catch (Exception e) {
            LogUtil.e(e);
            throw new IllegalStateException("无法使用" + getClass() + "启动" + node + "节点");
        }
    }

    /**
     * 提交任务节点的结果。
     * 如果e不为null则认定为任务失败。
     *
     * @param instance
     * @param result
     * @param e
     */
    public final void commitNode(Object instance, Object result, Exception e) {
        Class node = instance.getClass();
        onNodeEnd(node, instance, result, e);
        Class nextNode = prepareNextNode(node, instance, e == null);
        if (nextNode != null) {//存在下一节点
            startNode(nextNode);
        } else {//不存在下一节点，所有任务结束
            onFinish();
        }
    }

    /*内部回调*/

    protected void onStart() {
        state = STATE_START;

        if (indicator != null && !indicator.isProgressing()) {
            indicator.showProgress();
        }

        if (onAsyncListener != null) {
            onAsyncListener.onAsyncStateChanged(this, state);
        }
    }

    protected abstract void doStart();

    /**
     * 在创建节点实例之后，节点启动之前回调该方法。
     *
     * @param node
     * @param instance
     */
    protected void onNodeStart(Class node, Object instance) {
        if (onAsyncListener instanceof OnAsyncNodeListener) {
            OnAsyncNodeListener listener = (OnAsyncNodeListener) onAsyncListener;
            listener.onNodeStart(this, node, instance);
        }
    }

    protected void onNodeEnd(Class node, Object instance, Object result, Exception e) {
        if (onAsyncListener instanceof OnAsyncNodeListener) {
            OnAsyncNodeListener listener = (OnAsyncNodeListener) onAsyncListener;
            listener.onNodeEnd(this, node, instance, result, e);
        }
    }

    /**
     * 准备下一节点。如果返回null则认定所有任务结束。
     * 上一节点回调{@link #commitNode(Object, Object, Exception)}i时如果e为null则认定sSuccess为true。
     *
     * @param lastNode
     * @param lastInstance
     * @param isSuccess
     * @return
     */
    protected abstract Class prepareNextNode(Class lastNode, Object lastInstance, boolean isSuccess);

    protected void onFinish() {
        state = STATE_FINISH;

        if (indicator != null && indicator.isProgressing()) {
            indicator.hideProgress();
        }

        if (onAsyncListener != null) {
            onAsyncListener.onAsyncStateChanged(this, state);
        }
    }

    /*监听者*/

    public interface OnAsyncListener<AsyncMgr extends AsyncManager> {

        void onAsyncStateChanged(AsyncMgr asyncMgr, @AsyncManagerState int state);

    }

    public interface OnAsyncNodeListener<AsyncMgr extends AsyncManager> extends OnAsyncListener<AsyncMgr> {

        /**
         * 任务节点实例创建完毕，节点启动之前回调该方法。
         *
         * @param asyncMgr
         * @param node
         * @param instance
         */
        void onNodeStart(AsyncMgr asyncMgr, Class node, Object instance);

        void onNodeEnd(AsyncMgr asyncMgr, Class node, Object instance, Object result, Exception e);

    }

    private OnAsyncListener onAsyncListener;

    public final AsyncManager setOnAsyncListener(OnAsyncListener onAsyncListener) {
        this.onAsyncListener = onAsyncListener;
        return this;
    }

    /* 用户指示 */

    private ITaskIndicator indicator;

    public final void setIndicator(ITaskIndicator indicator) {
        this.indicator = indicator;
    }

    /*Clearable*/

    private final ClearableHolder clearableHolder = new ClearableHolder();

    @Override
    public final boolean isCleared() {
        return clearableHolder.isCleared();
    }

    @Override
    public final void clear() {
        if (indicator != null) {
            indicator.hideProgress();
            indicator = null;
        }
        clearableHolder.clear();
    }
}
