package kim.hsl.ioc_lib;

import android.view.View;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 自定义注解
 *  用于依赖注入视图
 */
@Target(ElementType.METHOD)   // 该注解作用于方法上
@Retention(RetentionPolicy.RUNTIME)    // 注解保留到运行时
@EventBase(
        listenerSetter = "setOnClickListener",
        listenerType = View.OnClickListener.class,
        callbackMethod = "onClick")
public @interface OnClick {
    int[] value();    // 接收 int 类型数组
}
