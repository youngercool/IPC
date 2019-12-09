package com.younger.younger.aidldemo;

import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.util.List;

/**
 * @author younger
 */
public class MainActivity extends AppCompatActivity {

    private static final int MESSAGE_NEW_BOOK_ARRIVED = 1;

    private IBookManager mRemoteBookManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = new Intent(this,BookManagerService.class);
        bindService(intent,mConnection,Context.BIND_AUTO_CREATE);
    }


    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){

                case MESSAGE_NEW_BOOK_ARRIVED:
                    Log.e("======","===receive new book :"+msg.obj);
                    break;
                    default:
                        super.handleMessage(msg);
            }
        }
    };

    private ServiceConnection mConnection = new ServiceConnection(){


        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            IBookManager bookManager = IBookManager.Stub.asInterface(iBinder);
            mRemoteBookManager = bookManager;
            try {
                List<Book> list = bookManager.getNookList();
                Log.e("=======","===query book list , list type:"+list.getClass().getCanonicalName());

                Log.e("=======","====query book list:"+list.toString());

                Book newBook = new Book(3,"Android开发艺术探索");
                bookManager.addBook(newBook);

                List<Book> newList = bookManager.getNookList();

                Log.e("=======","===query book list , list type:"+newList.toString());
                bookManager.registerListener(mOnNewBookArrivedListener);

            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            mRemoteBookManager = null;
        }
    };



    private IOnNewBookArrivedListener mOnNewBookArrivedListener = new IOnNewBookArrivedListener.Stub() {
        @Override
        public void onNewBookArrived(Book newBook) throws RemoteException {
            mHandler.obtainMessage(MESSAGE_NEW_BOOK_ARRIVED,newBook).sendToTarget();
        }
    };
    @Override
    protected void onDestroy() {
        if (mRemoteBookManager !=null && mRemoteBookManager.asBinder().isBinderAlive()){

            try {
                Log.e("=======","======unregister listener: "+ mOnNewBookArrivedListener);
                mRemoteBookManager.unRegisterListener(mOnNewBookArrivedListener);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }


        unbindService(mConnection);
        super.onDestroy();

    }
}
