package com.hjq.demo.helper;

import android.app.Activity;
import android.app.Application;

import androidx.collection.ArrayMap;

/**
 *    author : Android 轮子哥
 *    github : https://github.com/getActivity/AndroidProject
 *    time   : 2018/11/18
 *    desc   : SocialActivity 栈管理
 */
public final class ActivityStackManager {

    private static volatile ActivityStackManager sInstance;

    private final ArrayMap<String, Activity> mActivitySet = new ArrayMap<>();

    /** 当前 SocialActivity 对象标记 */
    private String mCurrentTag;

    private ActivityStackManager() {}

    public static ActivityStackManager getInstance() {
        // 加入双重校验锁
        if(sInstance == null) {
            synchronized (ActivityStackManager.class) {
                if(sInstance == null){
                    sInstance = new ActivityStackManager();
                }
            }
        }
        return sInstance;
    }

    /**
     * 获取 Application 对象
     */
    public Application getApplication() {
        return getTopActivity().getApplication();
    }

    /**
     * 获取栈顶的 SocialActivity
     */
    public Activity getTopActivity() {
        return mActivitySet.get(mCurrentTag);
    }

    /**
     * 销毁所有的 SocialActivity
     */
    public void finishAllActivities() {
        finishAllActivities((Class<? extends Activity>) null);
    }

    /**
     * 销毁所有的 SocialActivity，除这些 Class 之外的 SocialActivity
     */
    @SafeVarargs
    public final void finishAllActivities(Class<? extends Activity>... classArray) {
        String[] keys = mActivitySet.keySet().toArray(new String[]{});
        for (String key : keys) {
            Activity activity = mActivitySet.get(key);
            if (activity != null && !activity.isFinishing()) {
                boolean whiteClazz = false;
                if (classArray != null) {
                    for (Class<? extends Activity> clazz : classArray) {
                        if (activity.getClass() == clazz) {
                            whiteClazz = true;
                        }
                    }
                }
                // 如果不是白名单上面的 SocialActivity 就销毁掉
                if (!whiteClazz) {
                    activity.finish();
                    mActivitySet.remove(key);
                }
            }
        }
    }

    /**
     * SocialActivity 同名方法回调
     */
    public void onCreated(Activity activity) {
        mCurrentTag = getObjectTag(activity);
        mActivitySet.put(getObjectTag(activity), activity);
    }

    /**
     * SocialActivity 同名方法回调
     */
    public void onDestroyed(Activity activity) {
        mActivitySet.remove(getObjectTag(activity));
        // 如果当前的 SocialActivity 是最后一个的话
        if (getObjectTag(activity).equals(mCurrentTag)) {
            // 清除当前标记
            mCurrentTag = null;
        }
    }

    /**
     * 获取一个对象的独立无二的标记
     */
    private static String getObjectTag(Object object) {
        // 对象所在的包名 + 对象的内存地址
        return object.getClass().getName() + Integer.toHexString(object.hashCode());
    }
}