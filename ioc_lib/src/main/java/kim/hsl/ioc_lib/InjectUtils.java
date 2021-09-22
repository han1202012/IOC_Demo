package kim.hsl.ioc_lib;

import android.app.Activity;
import android.icu.lang.UProperty;
import android.view.View;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Map;

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

        // 注入事件
        injectEvents(activity);
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

    /**
     * 注入事件
     */
    private static void injectEvents(Activity activity) {
        // 获取 Class 字节码对象
        Class<? extends Activity> clazz = activity.getClass();
        // 获取所有方法
        Method[] methods = clazz.getDeclaredMethods();

        // 循环遍历类的方法
        for (int i = 0; i < methods.length; i ++) {
            // 获取方法的所有注解
            Annotation[] annotations = methods[i].getDeclaredAnnotations();

            // 遍历所有的注解
            for (int j = 0; j < annotations.length; j ++) {
                // 获取注解类型
                Class<? extends Annotation> annotationType = annotations[j].annotationType();
                // 获取 @EventBase 注解
                EventBase eventBase = annotationType.getAnnotation(EventBase.class);
                if (eventBase == null) {
                    // 如果没有获取到 EventBase 注解 , 执行下一次循环
                    continue;
                }

                // 如果获取到了 EventBase 注解 , 则开始获取事件注入的三要素
                /*
                通过反射执行下面的方法
                textView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });
                 */
                // 点击事件 View.setOnClickListener
                String listenerSetter = eventBase.listenerSetter();
                // 监听器类型 View.OnClickListener
                Class<?> listenerType = eventBase.listenerType();
                // 事件触发回调方法 public void onClick(View v)
                String callbackMethod = eventBase.callbackMethod();

                // 拦截 callbackMethod 方法 , 执行 method[i] 方法
                //      这个 method[i] 方法就是在 MainActivity 中用户自定义方法
                //      被 OnClick 注解修饰的方法
                //      将其封装到 Map 集合中
                Map<String, Method> methodMap = new HashMap<>();
                methodMap.put(callbackMethod, methods[i]);

                // 通过反射注入事件 , 设置组件的点击方法

                // 通过反射获取注解中的属性
                //      int[] value(); // 接收 int 类型数组
                try {
                    // 通过反射获取 OnClick 注解的 int[] value() 方法
                    Method valueMethod = annotationType.getDeclaredMethod("value");
                    // 调用 value() 方法 , 获取视图组件 ID 数组
                    int[] viewIds = (int[]) valueMethod.invoke(annotations[j]);

                    // 遍历 ID 数组
                    for (int k = 0; k < viewIds.length; k ++) {
                        // 获取组件实例对象
                        View view = activity.findViewById(viewIds[k]);
                        if (view == null) {
                            continue;
                        }

                        // 获取 View 视图组件的 listenerSetter 对应方法
                        //      这里是 View.setOnClickListener
                        //      参数一是方法名称 , 参数二是方法参数类型
                        Method listenerSetterMethod =
                                view.getClass().getMethod(listenerSetter, listenerType);

                        // 获取监听器 View.OnClickListener 接口的代理对象
                        EventInvocationHandler eventInvocationHandler =
                                new EventInvocationHandler(activity, methodMap);
                        Object proxy = Proxy.newProxyInstance(
                                listenerType.getClassLoader(),  // 类加载器
                                new Class<?>[]{listenerType},   // 接口数组
                                eventInvocationHandler);        // 调用处理程序

                        // 执行 View 的 setOnClickListener 方法, 为其设置点击事件
                        listenerSetterMethod.invoke(view, proxy);
                    }

                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }

            }
        }

    }
}