package kim.hsl.ioc_lib;

import android.app.Activity;
import android.view.View;

import java.lang.reflect.Field;

public class InjectUtils {
    /**
     * 为 Activity 注入布局
     * @param activity  该 Activity 是继承了 BaseActivity 的 MainActivity 实例对象
     */
    public static void inject(Activity activity) {
        // 注入布局文件
        injectLayout(activity);

        // 注入视图组件
        injectViews(activity);
    }

    /**
     * 注入布局文件
     */
    private static void injectLayout(Activity activity) {
        // 获取 Class 字节码对象
        Class<? extends Activity> clazz = activity.getClass();
        // 反射获取类上的注解
        ContentView contentView = clazz.getAnnotation(ContentView.class);
        // 获取注解中的布局 ID
        int layoutId = contentView.value();
        // 为 Activity 设置显示的布局
        activity.setContentView(layoutId);
    }

    /**
     * 注入视图组件
     */
    private static void injectViews(Activity activity) {
        // 获取 Class 字节码对象
        Class<? extends Activity> clazz = activity.getClass();
        // 获取类的属性字段
        Field[] fields = clazz.getDeclaredFields();

        // 循环遍历类的属性字段
        for (int i = 0; i < fields.length; i ++) {
            // 获取字段
            Field field = fields[i];
            // 属性有可能是私有的, 这里设置可访问性
            field.setAccessible(true);
            // 获取字段上的注解, @BindView 注解
            //  注意不是所有的属性字段都有 @BindView 注解
            BindView bindView = field.getAnnotation(BindView.class);
            if (bindView == null) {
                // 如果没有获取到 BindView 注解 , 执行下一次循环
                continue;
            }
            // 获取注入的视图组件
            int viewId = bindView.value();
            // 根据组件 id 获取对应组件对象
            View view = activity.findViewById(viewId);
            try {
                // 通过反射设置 Activity 的对应属性字段的值
                field.set(activity, view);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }
}