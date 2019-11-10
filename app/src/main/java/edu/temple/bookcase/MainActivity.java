package edu.temple.bookcase;
/*
    Author : Huajing Lin
    date: November 10, 2019
 */
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements BookListFragment.BookSelectedInterface {

    EditText etSearch;
    Button btnSearch;
    FragmentManager fm;
    BookDetailsFragment bookDetailsFragment;
    boolean onePane;
    boolean firstLoad = true;
    boolean bSearch = false;

    Handler bookHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message message) {
            ArrayList<Book> bookList = new ArrayList<>();
            try {
                JSONArray jsonArray = new JSONArray(message.obj.toString());
                if (jsonArray != null) {
                    for (int i=0;i<jsonArray.length();i++){
                        JSONObject bookObject = new JSONObject(jsonArray.getString(i));

                        if(bookObject != null) {

                            int id = Integer.parseInt(bookObject.getString("book_id"));
                            String title = bookObject.getString("title");
                            String author = bookObject.getString("author");
                            int published = Integer.parseInt(bookObject.getString("published"));
                            String cover_url = bookObject.getString("cover_url");
                            //System.out.printf("====%d, %s, %s, %d, %s\n", id, title, author,published,cover_url);
                            Book book = new Book(id, title, author,published,cover_url);
                            if(book != null)
                                bookList.add(book);
                        }
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            createViews(bookList);
            return true;
        }
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(firstLoad) {
            downloadData("https://kamorris.com/lab/audlib/booksearch.php");
            firstLoad = false;
        }else{
            createViews(null);
        }

        //seach books
        etSearch = (EditText) findViewById(R.id.etSearch);
        btnSearch=(Button)findViewById(R.id.btnSearch);
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String strURL = "https://kamorris.com/lab/audlib/booksearch.php?search=";
                strURL += etSearch.getText().toString();
                bSearch = true;
                downloadData(strURL);
                Context context = getApplicationContext();
                Toast.makeText(context, "seach books: "+ etSearch.getText().toString(),Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void downloadData(final String strURL){
        new Thread() {
            @Override
            public void run() {
                URL url = null;
                try {
                    url = new URL(strURL);
                    BufferedReader reader = new BufferedReader(
                            new InputStreamReader(url.openStream()));
                    StringBuilder builder = new StringBuilder();
                    String response;

                    while ((response = reader.readLine()) != null) {
                        builder.append(response);
                    }

                    Message msg = Message.obtain();
                    msg.obj = builder.toString();
                    bookHandler.sendMessage(msg);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }
    private void createViews(ArrayList<Book> books){
        fm = getSupportFragmentManager();

        onePane = findViewById(R.id.container_2) == null;
        // Check for fragments in both containers
        Fragment current1 = fm.findFragmentById(R.id.container_1);
        Fragment current2 = fm.findFragmentById(R.id.container_2);

        ArrayList<Book> bookList = null;// = new ArrayList<>();
        //Collections.addAll(bookList, getResources().getStringArray(R.array.books));

        if(bSearch) {
            Context context = getApplicationContext();
            Toast.makeText(context, "Get books: " + String.valueOf(books.size()), Toast.LENGTH_LONG).show();
            //System.out.printf("===== book List: %d\n", books.size());
            fm.beginTransaction()
                    .remove(current1)
                    .commit();
            current1 = null;
            if(current2 != null){
                fm.beginTransaction()
                        .remove(current2)
                        .commit();
            }
            bSearch = false;
        }

        // If there are no fragments at all (first time starting activity)
        if (current1 == null) {
            bookList = books;
            if (onePane) {
                fm.beginTransaction()
                        .add(R.id.container_1, ViewPagerFragment.newInstance(bookList))
                        .commit();
            } else {
                bookDetailsFragment = new BookDetailsFragment();
                fm.beginTransaction()
                        .add(R.id.container_1, BookListFragment.newInstance(bookList))
                        .add(R.id.container_2, bookDetailsFragment)
                        .commit();
            }
        } else {
            // Fragments already exist (activity was restarted)
            if (onePane) {
                if (current1 instanceof BookListFragment) {

                    BookListFragment bookListFragment = (BookListFragment) current1;
                    bookList = bookListFragment.getBookList();

                    // If we have the wrong fragment for this configuration, remove it and add the correct one
                    fm.beginTransaction()
                            .remove(current1)
                            .add(R.id.container_1, ViewPagerFragment.newInstance(bookList))
                            .commit();
                }
            } else {
                int currentIndex = 0;
                if (current1 instanceof ViewPagerFragment) {

                    ViewPagerFragment viewPagerFragment = (ViewPagerFragment) current1;
                    bookList = viewPagerFragment.getBookList();
                    currentIndex = viewPagerFragment.viewPager.getCurrentItem();

                    fm.beginTransaction()
                            .remove(current1)
                            .add(R.id.container_1, BookListFragment.newInstance(bookList))
                            .commit();
                }
                if (current2 instanceof BookDetailsFragment) {

                    bookDetailsFragment = (BookDetailsFragment) current2;
                }else {
                    bookDetailsFragment = new BookDetailsFragment();
                    fm.beginTransaction()
                            .add(R.id.container_2, bookDetailsFragment)
                            .commit();
                }


            }

            //bookDetailsFragment = (BookDetailsFragment) current2;

        }
    }


    @Override
    public void bookSelected(Book book) {
        if (bookDetailsFragment != null) {
            bookDetailsFragment.changeBook(book);
        }
    }
}
