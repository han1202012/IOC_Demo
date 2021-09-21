package kim.hsl.ioc_lib;

import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.Nullable;

public class BaseActivity extends Activity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 在此处注入布局
        //      此处传入的 Activity 参数是 MainActivity 子类对象
        InjectUtils.inject(this);
    }
}
