package core.mate.view;

import android.support.v7.widget.RecyclerView;

/**
 * @author DrkCore
 * @since 2016-11-12
 */
public class RecyclerScrollBinder extends RecyclerView.OnScrollListener {
    
    private class Binder extends ScrollBinder {
        
        public Binder(Config config) {
            super(config);
        }
    }
    
    private Binder binder;
    
    public RecyclerScrollBinder(ScrollBinder.Config config) {
        this.binder = new Binder(config);
    }
    
    @Override
    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
        super.onScrollStateChanged(recyclerView, newState);
        if(newState== RecyclerView.SCROLL_STATE_IDLE){
            binder.onFinishScroll();
        }
    }
    
    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);
        binder.onScrollDelta(dy);
    }
    
}
