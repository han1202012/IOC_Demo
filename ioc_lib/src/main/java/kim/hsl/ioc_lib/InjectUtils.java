package kim.hsl.ioc_lib;

import android.app.Activity;

public class InjectUtils {
    /**
     * 为 Activity 注入布局
     * @param activity  该 Activity 是继承了 BaseActivity 的 MainActivity 实例对象
     */
    public static void inject(Activity activity) {
        // 获取 Class 字节码对象
        Class<? extends Activity> clazz = activity.getClass();
        // 反射获取类上的注解
        ContentView contentView = clazz.getAnnotation(ContentView.class);
        // 获取注解中的布局 ID
        int layoutId = contentView.value();
        // 为 Activity 设置显示的布局
        activity.setContentView(layoutId);
    }
}
