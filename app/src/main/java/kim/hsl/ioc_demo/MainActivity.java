package kim.hsl.ioc_demo;

import kim.hsl.ioc_lib.BaseActivity;
import kim.hsl.ioc_lib.ContentView;

/**
 * 当该 MainActivity 启动时 , 调用 BaseActivity 的 onCreate 方法
 *      在 BaseActivity 的 onCreate 方法中注入布局
 */
@ContentView(R.layout.activity_main)
public class MainActivity extends BaseActivity {
}