// IOnNewBookArrivedListener.aidl
package com.younger.younger.aidldemo;
import com.younger.younger.aidldemo.Book;
// Declare any non-default types here with import statements

interface IOnNewBookArrivedListener {

    void onNewBookArrived(in Book newBook);
}
