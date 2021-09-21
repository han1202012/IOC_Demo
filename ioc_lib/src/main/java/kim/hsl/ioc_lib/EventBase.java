package kim.hsl.ioc_lib;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 自定义注解
 *  用于注解上的注解
 *  用于依赖注入视图
 */
@Target(ElementType.ANNOTATION_TYPE)   // 该注解作用于注解上
@Retention(RetentionPolicy.RUNTIME)    // 注解保留到运行时
public @interface EventBase {
    /**
     * 设置事件监听的方法
     * @return
     */
    String listenerSetter();

    /**
     * 设置监听器类型
     * @return
     */
    Class<?> listenerType();

    /**
     * 事件触发后的回调方法
     * @return
     */
    String callbackMethod();
}