package edu.temple.bookcase;
/*
    Author : Huajing Lin
    date: November 10, 2019
 */
import android.content.Context;
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

    private static final String BOOKLIST_KEY = "booklist";

    ListView listView;
    private ArrayList<Book> bookList;
    BookSelectedInterface parentActivity;

    public BookListFragment() {}

    public static BookListFragment newInstance(ArrayList<Book> bookList) {
        BookListFragment fragment = new BookListFragment();
        Bundle args = new Bundle();
        args.putParcelableArrayList(BOOKLIST_KEY, bookList);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            bookList = getArguments().getParcelableArrayList(BOOKLIST_KEY);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof BookSelectedInterface)
            parentActivity = (BookSelectedInterface) context;
        else
            throw new RuntimeException("Please implement BookSelectedInterface");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        parentActivity = null;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_book_list, container, false);
        listView = layout.findViewById(R.id.listView);

        final String[] titles = new String[bookList.size()];
        for (int i=0; i<bookList.size(); i++){
            titles[i] = bookList.get(i).getTitle();
        }

        listView.setAdapter(new ArrayAdapter<String>((Context) parentActivity, android.R.layout.simple_list_item_1, titles));

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                parentActivity.bookSelected(bookList.get(position));
            }
        });

        return layout;
    }

    public ArrayList<Book> getBookList(){
        return bookList;
    }

    interface BookSelectedInterface {
        void bookSelected(Book book);
    }
}
