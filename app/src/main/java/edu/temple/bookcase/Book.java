package edu.temple.bookcase;

import android.os.Parcel;
import android.os.Parcelable;

public class Book implements Parcelable {
    private int id;
    private String title;
    private String author;
    private int published; //The year the book was published
    private String coverURL;//the image representing the book cover

    public Book(int id, String title, String author, int published, String coverURL){
        this.id = id;
        this.title = title;
        this.author = author;
        this.published = published;
        this.coverURL =coverURL;
    }

    protected Book(Parcel in) {
        id = in.readInt();
        title = in.readString();
        author = in.readString();
        published = in.readInt();
        coverURL = in.readString();
    }

    public static final Creator<Book> CREATOR = new Creator<Book>() {
        @Override
        public Book createFromParcel(Parcel in) {
            return new Book(in);
        }

        @Override
        public Book[] newArray(int size) {
            return new Book[size];
        }
    };

    public int getId(){
        return id;
    }
    
    public String getTitle(){
        return title;
    }
    
    public String getAuthor(){
        return author;
    }

    public int getPublished(){
        return published;
    }

    public String getCoverURL(){
        return  coverURL;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setTitle(String ti){
        title = ti;
    }

    public void setAuthor(String au){
        this.author = au;
    }

    public void setPublished(int year){
        published = year;
    }

    public void setCoverURL(String url){
        coverURL = url;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(title);
        parcel.writeString(author);
        parcel.writeInt(published);
        parcel.writeString(coverURL);
    }
}
