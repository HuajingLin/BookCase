package edu.temple.bookcase;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;


public class BookListFragment extends Fragment {

    private static final String BOOKS_KEY = "books";
    private ArrayList<String> bookList;

    private BookSelectedInterface fragmentParent;

    public BookListFragment() {
        bookList = new ArrayList<>();
    }

    public static BookListFragment newInstance(String[] bookTitles) {
        BookListFragment fragment = new BookListFragment();
        Bundle args = new Bundle();
        args.putStringArray(BOOKS_KEY, bookTitles);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            String[] books = getArguments().getStringArray(BOOKS_KEY);
            for (int i=0; i<books.length; i++){
                bookList.add(books[i]);
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_book_list, container, false);

        final ListView listView = (ListView)inflater.inflate(R.layout.fragment_book_list, container, false);

        ArrayAdapter adapter = new ArrayAdapter<String>((Context) fragmentParent, android.R.layout.simple_list_item_1, bookList);

        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                fragmentParent.bookSelected(parent.getItemAtPosition(position).toString());
                MainActivity.MyProperties.getInstance().currentIndex = position;
            }
        });

        return listView;

    }

    @Override
    public void onStart() {
        super.onStart();
        getView().postDelayed(new Runnable() {
            @Override
            public void run() {
                fragmentParent.bookSelected(bookList.get(MainActivity.MyProperties.getInstance().currentIndex));
            }
        }, 100);
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if(context instanceof BookSelectedInterface){
            fragmentParent = (BookSelectedInterface) context;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        fragmentParent = null;
    }

    //interface definition
    public interface BookSelectedInterface {
        void bookSelected(String bookTitle);
    }
}
