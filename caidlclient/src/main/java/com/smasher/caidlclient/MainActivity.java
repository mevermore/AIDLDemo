package com.smasher.caidlclient;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.smasher.caidlserver.BookController;

import java.util.ArrayList;
import java.util.List;

import com.smasher.caidlserver.Book;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private final String TAG = "Client";

    private BookController bookController;

    private boolean connected;

    private List<Book> bookList;

    private Button add;

    private Button getList;

    private Button bindA;

    private Button bindB;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bindA = findViewById(R.id.bind_service_type_a);
        bindB = findViewById(R.id.bind_service_type_b);
        add = findViewById(R.id.btn_addBook_inOut);
        getList = findViewById(R.id.btn_getBookList);

        bindA.setOnClickListener(this);
        bindB.setOnClickListener(this);
        add.setOnClickListener(this);
        getList.setOnClickListener(this);
    }

    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            bookController = BookController.Stub.asInterface(iBinder);
            connected = true;
            Log.d(TAG, "onServiceConnected: ");
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            Log.d(TAG, "onServiceDisconnected: ");
            connected = false;
        }
    };

    private void bindServiceA() {
        Intent intent = new Intent();
        intent.setPackage("com.smasher.caidlserver");
        intent.setAction("com.smasher.caidlserver.action");
        bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
    }

    private void bindServiceB() {
        Intent intent = new Intent();
//        intent.setClass(this, AIDLService.class);
        intent.setAction("com.smasher.caidlserver.action");
        bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (connected) {
            unbindService(serviceConnection);
        }
    }

    @Override
    public void onClick(View v) {


        switch (v.getId()) {
            case R.id.bind_service_type_a:
                bindServiceA();
                break;
            case R.id.bind_service_type_b:
                bindServiceB();
                break;
            case R.id.btn_getBookList:
                getBookList();

                break;
            case R.id.btn_addBook_inOut:
                addBookInOut();
                break;
            default:
                break;
        }
    }


    private List<Book> getBookList() {

        if (bookController == null) {
            bookList = new ArrayList<>();
            Log.d(TAG, "onClick: controller is empty");
            return bookList;
        }

        try {
            bookList = bookController.getBookList();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        if (bookList != null) {
            Log.d(TAG, "onClick: " + bookList.size());
            for (Book book : bookList) {
                Log.d(TAG, "Book: " + book.getName());
            }
        }

        return bookList;
    }


    private void addBookInOut() {


        if (bookController == null) {
            Log.d(TAG, "onClick: controller is empty");
            return;
        }

        try {
            Book book = new Book("加入的新书《第一行代码》");
            bookController.addBookInOut(book);
            Log.d(TAG, "addBookInOut: a new book is add in");
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
}
