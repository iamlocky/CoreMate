package core.mate.app;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.List;

import core.mate.util.LogUtil;

/**
 * 封装了Fragment操作的辅助类
 *
 * @author DrkCore
 * @since 2015年11月9日14:41:58
 */
public class FragHelper {

    private final FragmentManager fragMgr;
    
    public FragmentManager getManager() {
        return fragMgr;
    }
    
    public FragHelper(FragmentManager fragMgr) {
        this.fragMgr = fragMgr;
    }

	/* 获取或添加 */

    public Fragment findFrag(Class clz) {
        return clz != null ? fragMgr.findFragmentByTag(clz.getCanonicalName()) : null;
    }

    public Fragment findFrag(String tag) {
        return tag != null ? fragMgr.findFragmentByTag(tag) : null;
    }

    public Fragment findOrAddFrag(int containerId, Class fragClazz) {
        return findOrAddFrag(containerId, fragClazz, null);
    }

    public Fragment findOrAddFrag(int containerId, Class clz, String tag) {
        Fragment frag = !TextUtils.isEmpty(tag) ? findFrag(tag) : findFrag(clz);
        if (frag == null) {
            try {
                frag = (Fragment) clz.newInstance();
            } catch (Exception e) {
                LogUtil.e(e);
                throw new IllegalArgumentException("指定的clz必须存在可见的默认构造函数");
            }
            fragMgr.beginTransaction().add(containerId, frag, tag).commit();
        }
        return frag;
    }

	/* 切换 */

    public Fragment switchFrag(int containerId, Fragment curFrag, Class nextFragClass) {
        return switchFrag(containerId, curFrag, null, nextFragClass, nextFragClass.getCanonicalName());
    }

    public Fragment switchFrag(int containerId, Fragment curFrag, Class nextFragClass, String fragTag) {
        return switchFrag(containerId, curFrag, null, nextFragClass, fragTag);
    }

    public Fragment switchFrag(int containerId, Fragment curFrag, Fragment nextFrag) {
        return switchFrag(containerId, curFrag, nextFrag, null, nextFrag.getClass().getCanonicalName());
    }

    public Fragment switchFrag(int containerId, Fragment curFrag, Fragment nextFrag, String fragTag) {
        return switchFrag(containerId, curFrag, nextFrag, null, fragTag);
    }

    private Fragment switchFrag(int containerId, Fragment curFrag, Fragment nextFrag, Class nextFragClass, String fragTag) {
        if (TextUtils.isEmpty(fragTag)) {
            throw new IllegalArgumentException("fragTag不允许为null或者空字符串");
        } else if (nextFrag == null && nextFragClass == null) {
            throw new IllegalArgumentException("nextFrag和nextFragClass不允许同时为null");
        }

        FragmentTransaction fragTran = fragMgr.beginTransaction();

        // 隐藏当前的frag
        if (curFrag != null) {
            if (curFrag == nextFrag || curFrag.getClass() == nextFragClass) {
                fragTran.show(curFrag).commit();
                return curFrag;
            } else {
                fragTran.hide(curFrag);
            }
        }

        // 查找或者创建nextFrag的实例
        if (nextFrag == null) {
            nextFrag = fragMgr.findFragmentByTag(fragTag);
        }
        if (nextFrag == null) {
            try {
                nextFrag = (Fragment) nextFragClass.newInstance();
            } catch (Exception e) {
                LogUtil.e(e);
                throw new IllegalArgumentException("指定的nextFragClass必须存在可见的默认构造函数");
            }
        }

        // 切换到nextFrag
        if (nextFrag.isAdded()) {
            fragTran.show(nextFrag);
        } else {
            fragTran.add(containerId, nextFrag, fragTag);
        }
        commitTran(fragTran);

        return nextFrag;
    }

    private boolean commitAllowingStateLossEnable = true;

    public FragHelper setCommitAllowingStateLossEnable(boolean commitAllowingStateLossEnable) {
        this.commitAllowingStateLossEnable = commitAllowingStateLossEnable;
        return this;
    }

    public boolean isCommitAllowingStateLossEnable() {
        return commitAllowingStateLossEnable;
    }

    private void commitTran(FragmentTransaction fragTran) {
        if (commitAllowingStateLossEnable) {
            fragTran.commitAllowingStateLoss();
        } else {
            fragTran.commit();
        }
    }

	/* 显示或者隐藏 */

    @SuppressWarnings("unchecked")
    public boolean hideFrag(Class... fragClasses) {
        int len = fragClasses.length;
        List<Fragment> frags = new ArrayList<>(len);
        Fragment frag;
        for (Class fragClass : fragClasses) {
            frag = fragMgr.findFragmentByTag(fragClass.getCanonicalName());
            if (frag != null) {
                frags.add(frag);
            }
        }

        return hideFrag(frags.toArray(new Fragment[frags.size()]));
    }

    public boolean hideFrag(String... fragTags) {
        int len = fragTags.length;
        List<Fragment> frags = new ArrayList<>(len);
        Fragment frag;
        for (String fragTag : fragTags) {
            frag = fragMgr.findFragmentByTag(fragTag);
            if (frag != null) {
                frags.add(frag);
            }
        }

        return hideFrag(frags.toArray(new Fragment[frags.size()]));
    }

    private boolean hideFrag(Fragment... frags) {
        if (frags.length > 0) {
            FragmentTransaction fragTran = fragMgr.beginTransaction();
            for (Fragment frag : frags) {

                fragTran.hide(frag);
            }
            commitTran(fragTran);
            return true;
        }
        return false;
    }

}
