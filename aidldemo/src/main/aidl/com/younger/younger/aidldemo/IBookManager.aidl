// IBookManager.aidl
package com.younger.younger.aidldemo;
import com.younger.younger.aidldemo.Book;
import com.younger.younger.aidldemo.IOnNewBookArrivedListener;
// Declare any non-default types here with import statements

interface IBookManager {


       List<Book> getNookList();
       void addBook(in Book book);

       void registerListener(IOnNewBookArrivedListener listenter);
       void unRegisterListener(IOnNewBookArrivedListener listenter);
}
