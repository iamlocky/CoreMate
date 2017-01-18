package core.mate.async;

import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.WorkerThread;
import android.util.Pair;

import java.util.ArrayList;
import java.util.List;

import core.mate.util.Callback;
import core.mate.util.LogUtil;
import core.mate.view.ITaskIndicator;

/**
 * 初步封装的异步任务基类。<br/>
 * <b>注意，当你实现该类的子类时请保留最后一个泛型参数为该异步任务的Result类型。
 * 否则将会因为在运行时无法确定Result类型而导致抛出异常。</b>
 *
 * @param <Param>
 * @param <Progress>
 * @param <Result>
 * @author DrkCore
 * @since 2015年9月29日10:28:34
 */
public abstract class CoreTask<Param, Progress, Result> extends AsyncTask<Param, Progress, CoreTask.ResultHolder<Param, Result>> implements Clearable {

    public static class ResultHolder<Param, Result> {

        public final Param param;
        public final Result result;
        public final Throwable e;

        private ResultHolder(Param param, Result result, Throwable e) {
            this.param = param;
            this.result = result;
            this.e = e;
        }

    }

	/* 继承 */

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        if (indicators != null) {
            for (ITaskIndicator indicator : indicators) {
                if (!indicator.isProgressing()) {
                    indicator.showProgress();
                }
            }
        }

        onStart();
    }

    @SuppressWarnings("unchecked")
    @Override
    protected ResultHolder<Param, Result> doInBackground(Param... params) {
        Param param = params != null && params.length >= 1 ? params[0] : null;

        try {
            Result result = doInBack(param);
            if (listeners != null) {
                for (OnTaskListener listener : listeners) {
                    listener.onPrepareResult(result);
                }
            }
            return new ResultHolder<>(param, result, null);
        } catch (Throwable e) {
            LogUtil.e(e);
            if (e instanceof RuntimeException) {
                // 运行时异常通常是逻辑的问题的问题，为了可维护性这里重新抛出
                throw (RuntimeException) e;
            }
            return new ResultHolder<>(param, null, e);
        }
    }

    @Override
    protected void onProgressUpdate(Progress... values) {
        super.onProgressUpdate(values);
        if (listeners != null) {
            for (OnTaskListener listener : listeners) {
                if (listener instanceof OnTaskProgressListener) {
                    OnTaskProgressListener<Progress, Result> progressListener = (OnTaskProgressListener<Progress, Result>) listener;
                    progressListener.onUpdateProgress(values != null && values.length > 0 ? values[0] : null);
                }
            }
        }
    }

    @Override
    protected void onCancelled() {
        super.onCancelled();
        onDone();
    }

    @Override
    protected void onPostExecute(ResultHolder<Param, Result> holder) {
        super.onPostExecute(holder);
        if (indicators != null) {
            for (ITaskIndicator indicator : indicators) {
                if (indicator.isProgressing()) {
                    indicator.hideProgress();
                }
            }
        }

        resultHolder = holder;

        // 检查任务可行性
        if (holder.e == null) {// 没有异常则认定为任务成功
            onSuccess(holder.result);
        } else {// 任务失败
            onFailure(holder.e);
        }

        onDone();
    }

	/* 内部回调 */

    protected void onStart() {
        // 处理回调
        if (listeners != null) {
            for (OnTaskListener<Result> onTaskListener : listeners) {
                onTaskListener.onStart();
            }
        }
    }

    /**
     * 异步调用的方法。
     *
     * @param param
     * @return
     * @throws Throwable 该方法抛出任何异常都视为这个异步任务失败。
     *                   但是如果抛出的异常是{@link RuntimeException}或者其子类，
     *                   则将该异常重新抛出使程序崩溃。
     */
    public abstract Result doInBack(Param param) throws Throwable;

    protected void onSuccess(Result result) {
        if (listeners != null) {
            for (OnTaskListener<Result> onTaskListener : listeners) {
                onTaskListener.onSuccess(result);
            }
        }
    }

    protected void onFailure(Throwable e) {
        if (listeners != null) {
            for (OnTaskListener<Result> onTaskListener : listeners) {
                onTaskListener.onFailure(e);
            }
        }
    }

    protected void onDone() {
        if (listeners != null) {
            for (OnTaskListener<Result> onTaskListener : listeners) {
                onTaskListener.onDone();
            }
        }

        if (clearAfterDone) {
            clear();
        }
    }

	/* 外部接口 */

    public interface OnTaskListener<Result> {

        /**
         * 在任务开始之前回调
         */
        void onStart();

        /**
         * 在{@link #doInBack(Object)}完成之后于异步线程之中回调。
         * 你可以在改回调之中处理一些耗时的操作。
         *
         * @param result
         */
        @WorkerThread
        void onPrepareResult(Result result);

        /**
         * 当任务成功时回调该方法
         *
         * @param result
         */
        void onSuccess(Result result);

        /**
         * 当判断任务失败时会调用该方法
         *
         * @param e 异步任务中抛出的异常，有可能为null
         */
        void onFailure(@Nullable Throwable e);

        /**
         * 当任务结束时回调该方法
         */
        void onDone();
    }

    public interface OnTaskProgressListener<Progress, Result> extends OnTaskListener<Result> {

        void onUpdateProgress(Progress progress);

    }

    private List<OnTaskListener<Result>> listeners;

    public OnTaskListener<Result> removeOnTaskListener(OnTaskListener<Result> listener) {
        return listeners != null && listeners.remove(listener) ? listener : null;
    }

    public CoreTask<Param, Progress, Result> addOnTaskListener(OnTaskListener<Result> listener) {
        if (listener != null) {
            if (this.listeners == null) {
                this.listeners = new ArrayList<>();
            }
            this.listeners.add(listener);
        }
        return this;
    }

	/*任务记录*/

    private ResultHolder<Param, Result> resultHolder;

    public Param getParam() {
        return resultHolder != null ? resultHolder.param : null;
    }

    public Result getResult() {
        return resultHolder != null ? resultHolder.result : null;
    }

    public void await(Callback<Pair<ResultHolder<Param, Result>, Exception>> callback) {
        if (callback == null) {
            return;
        }

        //避免完成后回调被清理
        clearAfterDone = false;

        if (resultHolder != null) {
            callback.onCall(new Pair<>(resultHolder, null));
        } else if (getStatus() == Status.FINISHED) {
            ResultHolder<Param, Result> holder = null;
            Exception exception = null;
            try {
                holder = get();
            } catch (Exception e) {
                LogUtil.e(e);
                exception = e;
            }
            callback.onCall(new Pair<>(holder, exception));
        } else {
            doAwait(callback);
        }
    }


    private Handler waitHandler;
    private final byte[] waitLock = new byte[0];

    private void doAwait(@NonNull Callback<Pair<ResultHolder<Param, Result>, Exception>> callback) {
        new Thread(() -> {
            ResultHolder<Param, Result> holder = null;
            Exception exception = null;
            try {
                holder = get();
            } catch (Exception e) {
                LogUtil.e(e);
                exception = e;
            }

            final ResultHolder<Param, Result> theHolder = holder;
            final Exception theException = exception;

            if (!isCancelled()) {
                synchronized (waitLock) {
                    if (!isCancelled()) {
                        if (waitHandler == null) {
                            waitHandler = new Handler(Looper.getMainLooper());
                        }
                        waitHandler.post(() -> callback.onCall(new Pair<>(theHolder, theException)));
                    }
                }
            }
        }).start();
    }

    public Throwable getException() {
        return resultHolder != null ? resultHolder.e : null;
    }

	/* 用户指示 */

    private List<ITaskIndicator> indicators;

    public CoreTask<Param, Progress, Result> addIndicator(ITaskIndicator indicator) {
        if (indicator != null) {
            if (this.indicators == null) {
                this.indicators = new ArrayList<>();
            }
            this.indicators.add(indicator);
        }
        return this;
    }

	/* 清空数据 */

    private boolean clearAfterDone = true;

    public CoreTask setClearAfterDone(boolean clearAfterDone) {
        this.clearAfterDone = clearAfterDone;
        return this;
    }

    public boolean isClearAfterDone() {
        return clearAfterDone;
    }

    @Override
    public boolean isCleared() {
        return isCancelled();
    }

    @Override
    public void clear() {
        clear(false);
    }

    /**
     * 清理外部的引用，包括{@link #listeners}和{@link #indicators}，
     * 然后取消任务。
     *
     * @param mayInterruptIfRunning
     */
    public void clear(boolean mayInterruptIfRunning) {
        if (indicators != null) {
            for (ITaskIndicator indicator : indicators) {
                if (indicator.isProgressing()) {
                    indicator.hideProgress();
                }
            }
            indicators.clear();
            indicators = null;
        }
        if (listeners != null) {
            listeners.clear();
            listeners = null;
        }

        if (!isCancelled()) {
            cancel(mayInterruptIfRunning);
        }


        synchronized (waitLock) {
            if (waitHandler != null) {
                waitHandler.removeCallbacksAndMessages(null);
            }
        }

    }

}