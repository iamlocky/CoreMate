package core.mate.async;

import android.support.annotation.Nullable;

public abstract class TaskNodeWrapper<AsyncMgr extends AsyncManager, Result> extends NodeImpl<AsyncMgr> implements CoreTask.OnTaskListener<Result> {

    @Override
    public void onStart() {

    }

    @Override
    public void onPrepareResult(Result result) {

    }

    @Override
    public void onFailure(@Nullable Exception e) {
        endWith(null, e);
    }

    @Override
    public void onSuccess(Result result) {
        endWith(result, null);
    }

    @Override
    public void onDone() {

    }
}
