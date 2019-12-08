package com.younger.younger.messengerdemo;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * Created by Younger on 2019-12-08.
 */
public class MessengerService extends Service {

    private static class MessengerHandler extends Handler{
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 1:
                    Log.i("=====","receive data from = "+ msg.getData().getString("msg"));

                    Messenger client = msg.replyTo;

                    Message relpyMessage = Message.obtain(null,2);
                    Bundle bundle = new Bundle();
                    bundle.putString("reply","i have already received your msg,tks");
                    relpyMessage.setData(bundle);

                    try {
                        client.send(relpyMessage);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }

                    break;
                    default:

                        super.handleMessage(msg);

                        break;
            }


        }
    }


    private final Messenger messenger = new Messenger(new MessengerHandler());
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return messenger.getBinder();
    }
}
