package com.smasher.caidlserver;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * @author warrior
 */
public class AIDLService extends Service {

    private final String TAG = "Server";

    private List<Book> bookList;

    public AIDLService() {
    }


    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate: ");
        bookList = new ArrayList<>();
        initData();
    }


    private void initData() {
        Book book1 = new Book("活着");
        Book book2 = new Book("或者");
        Book book3 = new Book("叶应是叶");
        Book book4 = new Book("https://github.com/leavesC");
        Book book5 = new Book("http://www.jianshu.com/u/9df45b87cfdf");
        Book book6 = new Book("http://blog.csdn.net/new_one_object");
        bookList.add(book1);
        bookList.add(book2);
        bookList.add(book3);
        bookList.add(book4);
        bookList.add(book5);
        bookList.add(book6);
    }


    @Override
    public IBinder onBind(Intent intent) {
        Log.d(TAG, "onBind: ");
        return stub;
    }


    private final BookController.Stub stub = new BookController.Stub() {
        @Override
        public List<Book> getBookList() throws RemoteException {
            Log.d(TAG, "getBookList: ");
            return bookList;
        }

        @Override
        public void addBookInOut(Book book) throws RemoteException {
            Log.d(TAG, "addBookInOut: ");
            if (book != null) {
                book.setName("服务器改了新书的名字 InOut");
                bookList.add(book);
            } else {
                Log.e(TAG, "接收到了一个空对象 InOut");
            }
        }
    };
}
