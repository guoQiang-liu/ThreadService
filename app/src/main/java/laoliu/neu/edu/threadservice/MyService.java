package laoliu.neu.edu.threadservice;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

/**
 * Created by liuguoqiang on 2016/4/27.
 */
 //1,实现Service类
//2,注册Service
//3,启动Service
public class MyService extends Service {
    private static final String TAG = "MyService";
    private Thread workThread;
    private Runnable backgroundWork = new Runnable() {
        @Override
        public void run() {
            Log.i(TAG, "child currentThread id : "+Thread.currentThread().getId());
            try { //一般情况下子线程需要无限运行
                while(!Thread.interrupted()){//判断线程是否应被中断
                    double randomDouble = Math.random();
                    Log.i(TAG, "wait 1000ms print"+"\n"+"get Random: "+String.valueOf(randomDouble));
                    MainActivity.UpdateGUI(randomDouble);
                    Thread.sleep(1000);//每隔一秒循环检测线程是否应被中断
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    };
    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(TAG,"MyService onCreate");
        //获取Service的线程ID,Service依赖于主线程
        Log.i(TAG, "MyService currentThread id : "+Thread.currentThread().getId());
        //Thread(ThreadGroup group(线程组), Runnable runnable, String threadName)
        workThread = new Thread(null, backgroundWork, "workThread"); //获取一个线程实例
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(TAG, "MyService onStartCommand");

        if(!workThread.isAlive()) { //如果子线程不活着
            workThread.start(); //启动子线程
        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "MyService onDestroy");
        workThread.interrupt();//通知线程准备终止
    }

    @Override //onBind 函数是在Service被绑定后调用的函数,返回Service的对象实例
    public IBinder onBind(Intent intent) {
        return null;
    }

}
