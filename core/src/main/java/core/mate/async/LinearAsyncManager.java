package core.mate.async;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 线性调度节点的状态机。调度中只要有一个节点失败则终止任务
 * 调度。
 *
 * @author DrkCore
 * @since 2016年8月20日21:24:12
 */
public class LinearAsyncManager extends AsyncManager {

    /*线性节点*/

    private final List<Class<? extends Node<? extends LinearAsyncManager>>> nodes = new ArrayList<>();

    public final LinearAsyncManager add(Class<? extends Node<? extends LinearAsyncManager>>... nodes) {
        Collections.addAll(this.nodes, nodes);
        return this;
    }

    /*继承*/

    @Override
    protected final void doStart() {
        if (nodes.isEmpty()) {
            throw new IllegalStateException("启动状态机前未填入任务节点");
        }
        startNode(nodes.get(0));
    }

    @Override
    protected Class prepareNextNode(Class lastNode, Object lastInstance, boolean isSuccess) {
        if (!isSuccess) {//只要某个节点出错，就终止线性执行
            return null;
        }
        //执行下一节点
        int nextIdx = nodes.indexOf(lastNode) + 1;
        return nextIdx < nodes.size() ? nodes.get(nextIdx) : null;
    }
}
