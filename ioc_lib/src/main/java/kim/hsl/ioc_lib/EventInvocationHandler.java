package kim.hsl.ioc_lib;

import android.app.Activity;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Map;

public class EventInvocationHandler implements InvocationHandler {
    /**
     * 客户端 Activity
     */
    private Activity activity;

    /**
     * 拦截 callbackMethod 方法 , 执行 method[i] 方法
     *      这个 method[i] 方法就是在 MainActivity 中用户自定义方法
     *      被 OnClick 注解修饰的方法
     *      将其封装到 Map 集合中
     */
    private Map<String, Method> methodMap;

    public EventInvocationHandler(Activity activity, Map<String, Method> methodMap) {
        this.activity = activity;
        this.methodMap = methodMap;
    }

    /**
     * 拦截方法 , 并使用自己的方法替换
     *      如 : 发现是 onClick 方法 , 则替换成用户自定义的方法 (被 @OnClick 注解修饰的方法)
     * @param proxy
     * @param method
     * @param args
     * @return
     * @throws Throwable
     */
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        // 获取回调的方法名称, 该方法是 onClick 或者 onLongClick 或者 onTouch 等方法
        String name = method.getName();
        // 获取对应的被调用方法
        Method method1 = methodMap.get(name);

        // 如果被调用的方法 需要被拦截 , 则能获取到被拦截后替换的方法
        if (method1 != null) {
            // 执行用户 Activity 中的相应方法
            return method1.invoke(activity, args);
        }

        // 其它方法正常执行
        return method.invoke(proxy, args);
    }
}
