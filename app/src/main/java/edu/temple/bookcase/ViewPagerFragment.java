package edu.temple.bookcase;
/*
    Author : Huajing Lin
    date: November 10, 2019
 */
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import java.util.ArrayList;

public class ViewPagerFragment extends Fragment {
    private static final String BOOKLIST_KEY = "booklist";

    ViewPager viewPager;
    private ArrayList<Book> bookList;
    BookListFragment.BookSelectedInterface parentActivity;

    public ViewPagerFragment() {}

    public static ViewPagerFragment newInstance(ArrayList<Book> bookList) {
        ViewPagerFragment fragment = new ViewPagerFragment();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_view_pager, container, false);

        viewPager = v.findViewById(R.id.viewPager);

        viewPager.setAdapter(new BookFragmentAdapter(getChildFragmentManager(), bookList));
        return v;
    }

    public ArrayList<Book> getBookList(){
        return bookList;
    }

    class BookFragmentAdapter extends FragmentStatePagerAdapter {

        ArrayList<Book> bookList;

        public BookFragmentAdapter(FragmentManager fm, ArrayList<Book> bookList) {
            super(fm);
            this.bookList = bookList;
        }

        @Override
        public Fragment getItem(int i) {
            return BookDetailsFragment.newInstance(bookList.get(i));
        }

        @Override
        public int getCount() {
            return bookList.size();
        }

        @Override
        public int getItemPosition(@NonNull Object object) {
            return PagerAdapter.POSITION_NONE;
        }
    }

}
