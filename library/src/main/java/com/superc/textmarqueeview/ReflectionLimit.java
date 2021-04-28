package com.superc.textmarqueeview;

import android.os.Build;

import java.lang.reflect.Method;

/**
 * @author: SuperChen
 * @last-modifier: SuperChen
 * @version: 1.0
 * @create-date: 2021/4/28 8:52
 * @last-modify-date: 2021/4/28 8:52
 * @description: 9.0反射限制问题
 */
public class ReflectionLimit {
    private static Object sVMRuntime;
    private static Method setHiddenApiExemptions;

    static {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            try {
                Method forName = Class.class.getDeclaredMethod("forName", String.class);
                Method getDeclaredMethod = Class.class.getDeclaredMethod("getDeclaredMethod", String.class, Class[].class);
                Class<?> vmRuntimeClass = (Class<?>) forName.invoke(null, "dalvik.system.VMRuntime");
                Method getRuntime = (Method) getDeclaredMethod.invoke(vmRuntimeClass, "getRuntime", null);
                setHiddenApiExemptions = (Method) getDeclaredMethod.invoke(vmRuntimeClass, "setHiddenApiExemptions", new Class[]{String[].class});
                setHiddenApiExemptions.setAccessible(true);
                sVMRuntime = getRuntime.invoke(null);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    //消除限制
    public static boolean clearLimit() {
        if (sVMRuntime == null || setHiddenApiExemptions == null) {
            return false;
        }
        try {
            setHiddenApiExemptions.invoke(sVMRuntime, new Object[]{new String[]{"L"}});
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
