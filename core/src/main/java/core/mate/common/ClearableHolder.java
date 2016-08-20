package core.mate.common;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public class ClearableHolder implements Clearable {

    private List<WeakReference<Clearable>> clearables;

    public void add(Clearable clearable) {
        if (this.clearables == null) {
            this.clearables = new ArrayList<>();
        }
        this.clearables.add(new WeakReference<>(clearable));
    }

    @Override
    public boolean isCleared() {
        return clearables == null || clearables.isEmpty();
    }

    @Override
    public void clear() {
        if (isCleared()) {
            return;
        }
        Clearable tmp;
        for (WeakReference<Clearable> clearable : clearables) {
            tmp = clearable.get();
            if (tmp != null && !tmp.isCleared()) {
                tmp.clear();
            }
        }
        clearables.clear();
    }

}
