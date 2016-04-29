package laoliu.neu.edu.threadservice;
//Activity销毁时,Service任继续运行,而且Service比Activity有更高的优先级
import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private final static String TAG = "Service";
    private  static Handler handler = new Handler();//建立一个静态的Handler实例
    private  static double randomDouble;
    private static TextView textView;

    //静态的公有界面更新函数
    //后台线程通过调用该函数,将后台产生的随机数传到该函数.
    public static void UpdateGUI(double refreshDouble){
        randomDouble = refreshDouble;
        handler.post(RefreshTextView);//将Runnable对象传给主线程的消息队列中
    }

    private static Runnable RefreshTextView = new Runnable() {
        @Override
        public void run() {
            textView.setText(String.valueOf(randomDouble));
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView = (TextView) findViewById(R.id.randomDouble_tv);
        //打印当前线程ID
        Log.i(TAG, "MainActivity currentThread id :　"+Thread.currentThread().getId());
        Log.i(TAG, "MAINACTIVITY onCreate");

    }


    public void onClick(View view){
        Intent myService = new Intent(this, MyService.class);
        switch (view.getId()){
            case R.id.start_service_btn:
                startService(myService);
                break;
            case R.id.stop_service_btn:
                stopService(myService);
                break;
            default:
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "MAINACTIVITY onDestroy");
    }
}
