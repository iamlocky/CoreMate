package core.mate.util;

import android.support.annotation.IntDef;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * 用于组合多个接口实例的代理。
 *
 * @author DrkCore
 * @since 2017年1月6日11:26:45
 */
public class MultiProxy implements InvocationHandler {

    /**
     * 创建代理
     *
     * @param clz 由于使用动态代理机制clz必须为interface。
     * @return
     */
    @SuppressWarnings("unchecked")
    public static MultiProxy newInstance(Class clz) {
        MultiProxy proxy = new MultiProxy();
        Object obj = Proxy.newProxyInstance(clz.getClassLoader(), new Class[]{clz}, proxy);
        proxy.setProxy(obj);
        return proxy;
    }

    private Object proxy;
    private final List<Object> proxies = new ArrayList<>();

    private MultiProxy() {
    }

    /**
     * 获取被代理的接口。
     *
     * @param <T>
     * @return
     */
    public <T> T getProxy() {
        return (T) proxy;
    }

    private MultiProxy setProxy(Object proxy) {
        this.proxy = proxy;
        return this;
    }

    /**
     * 使用第一个被代理实例的返回值。这也是默认的模式。
     * <p>
     * 如果返回值为布尔类型，且{@link #booleanMergeMode}启用的话，该模式无效。
     */
    public static final int MODE_FIRST = 0;
    /**
     * 使用最后一个被代理实例的返回值。
     * <p>
     * 如果返回值为布尔类型，且{@link #booleanMergeMode}启用的话，该模式无效。
     */
    public static final int MODE_LAST = 1;

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({MODE_FIRST, MODE_LAST})
    @Target({ElementType.PARAMETER, ElementType.METHOD, ElementType.FIELD})
    public @interface ResultMode {
    }

    /**
     * 返回值选择模式。
     * <p>
     * 取值参阅{@link #MODE_FIRST}和{@link #MODE_LAST}。
     */
    @ResultMode
    private int resultMode = MODE_FIRST;

    /**
     * 获取返回值模式。默认为{@link #MODE_FIRST}。
     *
     * @return
     */
    @ResultMode
    public int getResultMode() {
        return resultMode;
    }

    /**
     * 设置返回值模式。
     * <p>
     * 如果返回值为布尔类型，且{@link #booleanMergeMode}启用的话，该模式无效。
     *
     * @param resultMode 取值参阅{@link #MODE_FIRST}、{@link #MODE_LAST}
     * @return
     */
    public MultiProxy setResultMode(@ResultMode int resultMode) {
        this.resultMode = resultMode;
        return this;
    }

    /**
     * 不启用布尔值选合并模式。这是默认的模式。
     */
    public static final int BOOL_MODE_DISABLE = 0;
    /**
     * 使用 <b>或</b> 逻辑合并所有布尔返回值。
     * <p>
     * 当返回值为布尔类型时该模式将覆盖{@link #resultMode}的效果。
     * <p>
     * 注意，如果是包装类的布尔类型也就是Boolean为null的话，认定为false。
     */
    public static final int BOOL_MODE_OR = 1;
    /**
     * 使用 <b>与</b> 逻辑合并所有布尔返回值。
     * <p>
     * 当返回值为布尔类型时该模式将覆盖{@link #resultMode}的效果。
     * <p>
     * * 注意，如果是包装类的布尔类型也就是Boolean为null的话，认定为false。
     */
    public static final int BOOL_MODE_AND = 2;

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({BOOL_MODE_DISABLE, BOOL_MODE_OR, BOOL_MODE_AND})
    @Target({ElementType.PARAMETER, ElementType.METHOD, ElementType.FIELD})
    public @interface BooleanMergeMode {
    }

    /**
     * 布尔返回值合并模式。
     * <p>
     * 取值参阅：
     * <li/>{@link #BOOL_MODE_DISABLE}，默认为该模式。
     * <li/>{@link #BOOL_MODE_OR}
     * <li/>{@link #BOOL_MODE_AND}
     */
    @BooleanMergeMode
    private int booleanMergeMode = BOOL_MODE_DISABLE;

    /**
     * 获取并布尔类型返回值模式。
     * <p>
     * 默认为{@link #BOOL_MODE_DISABLE}
     *
     * @return
     */
    @BooleanMergeMode
    public int getBooleanMergeMode() {
        return booleanMergeMode;
    }

    /**
     * 设置是否启用布尔类型返回值模式。
     * <p>
     * 如果被代理方法返回值是布尔类型且该模式启用，{@link #resultMode}效果将被忽略。
     *
     * @param booleanMergeMode 取值参阅{@link #BOOL_MODE_DISABLE}、{@link #BOOL_MODE_OR}、{@link #BOOL_MODE_AND}
     * @return
     */
    public MultiProxy setBooleanMergeMode(@BooleanMergeMode int booleanMergeMode) {
        this.booleanMergeMode = booleanMergeMode;
        return this;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Object result = null;
        Class<?> returnType = method.getReturnType();
        if (booleanMergeMode != BOOL_MODE_DISABLE && (returnType == Boolean.class || returnType == boolean.class)) {//布尔模式
            boolean bool = false, tmp;
            for (int i = 0, size = proxies.size(); i < size; i++) {
                tmp = Boolean.TRUE.equals(method.invoke(proxies.get(i), args));
                if (i == 0) {
                    bool = tmp;
                } else {
                    if (booleanMergeMode == BOOL_MODE_OR) {
                        bool |= tmp;
                    } else if (booleanMergeMode == BOOL_MODE_AND) {
                        bool &= tmp;
                    } else {
                        throw new IllegalStateException();
                    }
                }
            }
            result = bool;
        } else {//普通取值模式
            Object tmp;
            for (int i = 0, size = proxies.size(); i < size; i++) {
                tmp = method.invoke(proxies.get(i), args);
                if (result == null) {
                    if ((resultMode == MODE_FIRST && i == 0)/*选取首位*/ ||
                            (resultMode == MODE_LAST && i == size - 1)/*选取末位*/) {
                        result = tmp;
                    }
                }
            }
        }
        return result;
    }

    /*Delegate*/

    public boolean add(Object type) {
        return proxies.add(type);
    }

    public boolean addAll(Collection<? extends Object> c) {
        return proxies.addAll(c);
    }

    public boolean addAll(int index, Collection<? extends Object> c) {
        return proxies.addAll(index, c);
    }

    public void add(int index, Object element) {
        proxies.add(index, element);
    }

    public boolean remove(Object o) {
        return proxies.remove(o);
    }

    public boolean containsAll(Collection<?> c) {
        return proxies.containsAll(c);
    }

    public int size() {
        return proxies.size();
    }

    public boolean isEmpty() {
        return proxies.isEmpty();
    }

    public boolean contains(Object o) {
        return proxies.contains(o);
    }

    public boolean removeAll(Collection<?> c) {
        return proxies.removeAll(c);
    }

    public void clear() {
        proxies.clear();
    }

    public Object get(int index) {
        return proxies.get(index);
    }

    public Object set(int index, Object element) {
        return proxies.set(index, element);
    }

    public Object remove(int index) {
        return proxies.remove(index);
    }

    public int indexOf(Object o) {
        return proxies.indexOf(o);
    }

    public int lastIndexOf(Object o) {
        return proxies.lastIndexOf(o);
    }

    public void sort(Comparator<? super Object> c) {
        Collections.sort(proxies, c);
    }

}