package kim.hsl.ioc_lib;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 自定义注解
 *  用于依赖注入视图
 */
@Target(ElementType.FIELD)   // 该注解作用于字段上
@Retention(RetentionPolicy.RUNTIME)    // 注解保留到运行时
public @interface BindView {
    int value();    // 接收 int 类型值
}
