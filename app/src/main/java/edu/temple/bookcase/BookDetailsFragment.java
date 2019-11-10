package edu.temple.bookcase;
/*
    Author : Huajing Lin
    date: November 10, 2019
 */
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;


public class BookDetailsFragment extends Fragment {

    private static final String BOOK_KEY = "bookKey";
    private Book book;

    ImageView bookPicture = null;
    TextView bookTitle = null;
    TextView bookAuthor = null;
    TextView bookPublished = null;

    public BookDetailsFragment() {}

    public static BookDetailsFragment newInstance(Book book) {
        BookDetailsFragment fragment = new BookDetailsFragment();
        Bundle args = new Bundle();
        args.putParcelable(BOOK_KEY, (Parcelable) book);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            book = getArguments().getParcelable(BOOK_KEY);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_book_details, container, false);

        bookPicture = v.findViewById(R.id.imageView);
        bookTitle = v.findViewById(R.id.tvTitle);
        bookAuthor = v.findViewById(R.id.tvAuthor);
        bookPublished = v.findViewById(R.id.tvPublished);

        if(book != null) {
            changeBook(book);
        }
        return v;
    }

    public void changeBook(Book book) {

        bookTitle.setText(book.getTitle());
        bookAuthor.setText(book.getAuthor());
        bookPublished.setText(Integer.toString(book.getPublished()));
        Picasso.get().load(book.getCoverURL()).into(bookPicture);
    }

}
