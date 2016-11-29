package core.mate.adapter;

import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * 将对item的ViewHolder的创建和绑定数据解耦的适配器。
 * 你可以通过创建{@link AbsItemType#}的子类来灵活地创建item样式和行为。
 *
 * @author DrkCore
 * @since 2015年10月13日17:59:29
 */
public class FlexibleAdapter extends CoreAdapter<Object, AbsViewHolder<Object>> {
    
    private final List<AbsItemType> itemTypes = new ArrayList<>();
    
    public FlexibleAdapter() {
    }
    
    public FlexibleAdapter(AbsItemType<?, ?>[] itemTypes) {
        setTypes(itemTypes);
    }
    
    public FlexibleAdapter(Collection<AbsItemType> itemTypes) {
        setTypes(itemTypes);
    }
    
    public final FlexibleAdapter setTypes(AbsItemType<?, ?>[] itemTypes) {
        if (!this.itemTypes.isEmpty()) {
            throw new IllegalStateException("Types无法重新初始化");
        } else if (itemTypes == null || itemTypes.length == 0) {
            throw new IllegalArgumentException("Types不能为空");
        }
        
        Collections.addAll(this.itemTypes, itemTypes);
        return this;
    }
    
    public final FlexibleAdapter setTypes(Collection<AbsItemType> itemTypes) {
        if (!this.itemTypes.isEmpty()) {
            throw new IllegalStateException("Types无法重新初始化");
        } else if (itemTypes == null || itemTypes.isEmpty()) {
            throw new IllegalArgumentException("Types不能为空");
        }
        
        this.itemTypes.addAll(itemTypes);
        return this;
    }

	/* 继承 */
    
    @NonNull
    @SuppressWarnings("unchecked")
    @Override
    protected final AbsViewHolder<Object> createViewHolder(LayoutInflater inflater, ViewGroup parent, int viewType) {
        return (AbsViewHolder<Object>) itemTypes.get(viewType).createViewHolder(inflater, parent);
    }
    
    @SuppressWarnings("unchecked")
    @Override
    protected final void bindViewData(AbsViewHolder<Object> holder, int position, Object data, int viewType) {
        AbsItemType operator = itemTypes.get(viewType);
        operator.bindViewData(holder, position, data);
    }
    
    @Override
    public final int getItemViewType(int position) {
        Object obj = getItem(position);
        for (int i = 0, count = itemTypes.size(); i < count; i++) {
            if (itemTypes.get(i).canHandleObject(obj)) {
                return i;// 可处理
            }
        }
        // 用户填入了不可处理的类型
        throw new IllegalStateException("指定数据类型不存在可用的operator");
    }
    
    @Override
    public final int getViewTypeCount() {
        int size = itemTypes.size();
        if (size == 0) {
            throw new IllegalStateException("Types未初始化");
        }
        return size;
    }
    
    @Override
    public final void displayEx(Object... objects) {
        throw new IllegalStateException("为了避免数组对象引起歧义不建议在" + getClass() + "中使用不定参数的方法");
    }
    
    @Override
    public final void addEx(Object... objects) {
        throw new IllegalStateException("为了避免数组对象引起歧义不建议在" + getClass() + "中使用不定参数的方法");
    }
}
