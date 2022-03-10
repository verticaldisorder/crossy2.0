package ru.rmp.crossy20.models;

public class Book {
    private String author;
    private String title;
    private String genre;
    private String bookholder;
    private boolean isCrossed;

    public Book(String author, String title, String genre, String bookholder, boolean isCrossed) {
        this.author = author;
        this.title = title;
        this.genre = genre;
        this.bookholder = bookholder;
        this.isCrossed = isCrossed;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getBookholder() {
        return bookholder;
    }

    public void setBookholder(String bookholder) {
        this.bookholder = bookholder;
    }

    public boolean isCrossed() {
        return isCrossed;
    }

    public void setCrossed(boolean crossed) {
        isCrossed = crossed;
    }

    @Override
    public String toString() {
        return "Book{" +
                "author='" + author + '\'' +
                ", title='" + title + '\'' +
                ", genre='" + genre + '\'' +
                ", bookholder='" + bookholder + '\'' +
                ", isCrossed=" + isCrossed +
                '}';
    }
}
