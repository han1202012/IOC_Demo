package kim.hsl.ioc_demo;

import android.util.Log;
import android.widget.TextView;

import kim.hsl.ioc_lib.BaseActivity;
import kim.hsl.ioc_lib.BindView;
import kim.hsl.ioc_lib.ContentView;

/**
 * 当该 MainActivity 启动时 , 调用 BaseActivity 的 onCreate 方法
 *      在 BaseActivity 的 onCreate 方法中注入布局
 */
@ContentView(R.layout.activity_main)    // 布局注入
public class MainActivity extends BaseActivity {

    /**
     * 视图注入
     */
    @BindView(R.id.textView)
    private TextView textView;

    @Override
    protected void onResume() {
        super.onResume();
        // 验证 textView 是否注入成功
        Log.i("MainActivity", "textView : " + textView);
    }
}