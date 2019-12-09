package com.younger.younger.aidldemo;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by Younger on 2019-12-09.
 */
public class BookManagerService extends Service {

    private static final String TAG = "BMS";
    private CopyOnWriteArrayList<Book> mBookList = new CopyOnWriteArrayList<>();

    private AtomicBoolean mIsServiceDestoryed = new AtomicBoolean(false);


    private CopyOnWriteArrayList<IOnNewBookArrivedListener> mListener = new CopyOnWriteArrayList<>();
    private Binder mBinder = new IBookManager.Stub() {
        @Override
        public List<Book> getNookList() throws RemoteException {
            return mBookList;
        }

        @Override
        public void addBook(Book book) throws RemoteException {
            mBookList.add(book);
        }

        @Override
        public void registerListener(IOnNewBookArrivedListener listenter) throws RemoteException {

            if (!mListener.contains(listenter)){
                mListener.add(listenter);
            }

            Log.e("======","=====registerListener ,size:"+mListener.size());
        }

        @Override
        public void unRegisterListener(IOnNewBookArrivedListener listenter) throws RemoteException {
            if (mListener.contains(listenter)){
                mListener.remove(listenter);
                Log.e("======","=====unregister, success");
            }
            Log.e("======","=====UNregisterListener ,size:"+mListener.size());
        }
    };


    @Override
    public void onCreate() {
        super.onCreate();
        mBookList.add(new Book(1,"Android"));
        mBookList.add(new Book(2,"iOS"));
        new Thread(new ServiceWorker()).start();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public void onDestroy() {
        mIsServiceDestoryed.set(true);
        super.onDestroy();
    }

    private class ServiceWorker implements Runnable{

        @Override
        public void run() {
            while (!mIsServiceDestoryed.get()){
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                int bookId = mBookList.size()+1;
                Book newBook = new Book(bookId,"new Book#"+bookId);

                try {
                    onNewBookArrived(newBook);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }

            }
        }
    }

    private void onNewBookArrived(Book newBook) throws RemoteException {
        mBookList.add(newBook);

        Log.e("======","=====onNewBookArrived,notify listeners:"+mListener.size());

        for (int i = 0; i < mListener.size(); i++) {
            IOnNewBookArrivedListener listener = mListener.get(i);
            Log.e("======","=====onNewBookArrived,notify listener:"+listener);
            listener.onNewBookArrived(newBook);
        }
    }
}
