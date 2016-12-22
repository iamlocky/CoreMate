package core.mate.util;

/**
 * 简单的回调接口，用于异步编程。配合lambda表达式一同食用有奇效。
 * <p>
 * 如果你需要回调两个参数的话可以试试{@link android.support.v4.util.Pair}，
 * 超出两个的话建议使用数组或者定义一个Bean。
 *
 * @author DrkCore
 * @since 2016/12/22
 */
public interface Callback<Result> {

    void onCall(Result result);

}
