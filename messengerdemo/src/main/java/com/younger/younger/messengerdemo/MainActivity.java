package com.younger.younger.messengerdemo;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

public class MainActivity extends AppCompatActivity {

    private Messenger mService;

    private Messenger mGetRelpyMessenger = new Messenger(new MessengerHandler());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = new Intent(this,MessengerService.class);
        bindService(intent,mConnection,Context.BIND_AUTO_CREATE);
    }

    private ServiceConnection mConnection = new ServiceConnection(){

        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {


            mService = new Messenger(iBinder);
            Message msg = Message.obtain(null,1);
            Bundle data = new Bundle();
            data.putString("msg","hello , this is younger");
            msg.setData(data);
            msg.replyTo = mGetRelpyMessenger;
            try {
                mService.send(msg);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {

        }
    };


    private static class MessengerHandler extends Handler{
        @Override
        public void handleMessage(Message msg) {

            switch (msg.what){
                case 2:
                    Log.e("=======","=======哥收到回信了="+msg.getData().getString("reply"));
                    break;
                    default:
                        super.handleMessage(msg);
            }

        }
    }

    @Override
    protected void onDestroy() {
        unbindService(mConnection);
        super.onDestroy();
    }
}
