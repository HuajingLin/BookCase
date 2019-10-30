package edu.temple.bookcase;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import android.content.res.Resources;
import android.os.Bundle;
import android.widget.Adapter;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements BookListFragment.BookSelectedInterface {

    BookDetailsFragment bookDetailsFragment;
    ArrayList<BookDetailsFragment> fragments;
    ViewPager viewPager;
    //TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        boolean singlePane = findViewById(R.id.viewPager) != null;
        String[] books = getResources().getStringArray(R.array.books);

        if(singlePane) {

            fragments = new ArrayList<>();

            for (int i = 0; i < books.length; i++) {
                fragments.add(BookDetailsFragment.newInstance(books[i]));
            }

            viewPager = findViewById(R.id.viewPager);

            MyFragmentAdapter adapter = new MyFragmentAdapter(getSupportFragmentManager(), fragments);
            viewPager.setAdapter(adapter);


            viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                }

                @Override
                public void onPageSelected(int position) {
                    fragments.get(position).displayTitle();
                }

                @Override
                public void onPageScrollStateChanged(int state) {

                }
            });

        }
        else{

            bookDetailsFragment = new BookDetailsFragment();

            BookListFragment bookListFragment = BookListFragment.newInstance(books);
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.container_1, bookListFragment)
                    .add(R.id.container_2, bookDetailsFragment)
                    .commit();
        }

    }

    //implement interface BookSelectedInterface
    @Override
    public void bookSelected(String bookTitle) {

        bookDetailsFragment.displayBook(bookTitle);
    }

    //
    class MyFragmentAdapter extends FragmentStatePagerAdapter{

        ArrayList<BookDetailsFragment> fragments;

        public MyFragmentAdapter(FragmentManager fm, ArrayList<BookDetailsFragment> fragments) {
            super(fm);
            this.fragments = fragments;
        }

        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }
    }
}
