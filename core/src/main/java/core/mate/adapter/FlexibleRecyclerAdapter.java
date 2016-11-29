package core.mate.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * 灵活配置的RecyclerView的Adapter。
 *
 * @author DrkCore
 * @since 2016年1月31日23:40:34
 */
public class FlexibleRecyclerAdapter extends CoreRecyclerAdapter<Object, RecyclerView.ViewHolder> {
    
    private final List<AbsRecyclerItemType> itemTypes = new ArrayList<>();
    
    public final AbsRecyclerItemType getItemType(Class clz) {
        for (AbsRecyclerItemType itemType : itemTypes) {
            if (itemType.getClass() == clz) {
                return itemType;
            }
        }
        return null;
    }
    
    public FlexibleRecyclerAdapter() {
    }
    
    public FlexibleRecyclerAdapter(AbsRecyclerItemType<?, ?>[] itemTypes) {
        setTypes(itemTypes);
    }
    
    public FlexibleRecyclerAdapter(Collection<AbsRecyclerItemType<?, ?>> itemTypes) {
        setTypes(itemTypes);
    }
    
    public final FlexibleRecyclerAdapter setTypes(AbsRecyclerItemType<?, ?>[] itemTypes) {
        if (!this.itemTypes.isEmpty()) {
            throw new IllegalStateException("Types无法重新初始化");
        } else if (itemTypes == null || itemTypes.length == 0) {
            throw new IllegalArgumentException("Types不能为空");
        }
        
        Collections.addAll(this.itemTypes, itemTypes);
        return this;
    }
    
    public final FlexibleRecyclerAdapter setTypes(Collection<AbsRecyclerItemType<?, ?>> itemTypes) {
        if (!this.itemTypes.isEmpty()) {
            throw new IllegalStateException("Types无法重新初始化");
        } else if (itemTypes == null || itemTypes.isEmpty()) {
            throw new IllegalArgumentException("Types不能为空");
        }
        
        this.itemTypes.addAll(itemTypes);
        return this;
    }

	/*继承*/
    
    @NonNull
    @Override
    protected final RecyclerView.ViewHolder createViewHolder(LayoutInflater inflater, ViewGroup parent, int type) {
        AbsRecyclerItemType operator = itemTypes.get(type);
        return operator.createViewHolder(inflater, parent);
    }
    
    @Override
    protected final void bindViewData(RecyclerView.ViewHolder holder, int position, Object data, int viewType) {
        AbsRecyclerItemType operator = itemTypes.get(viewType);
        operator.bindViewData(holder, position, data);
    }
    
    @Override
    public final int getItemViewType(int position) {
        int size = itemTypes.size();
        if (size == 0) {
            throw new IllegalStateException("Types未初始化");
        }
        
        Object obj = getItem(position);
        for (int i = 0; i < size; i++) {
            if (itemTypes.get(i).canHandleObject(obj)) {
                return i;// 可处理
            }
        }
        // 用户填入了不可处理的类型
        throw new IllegalStateException("指定数据类型不存在可用的operator");
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
