package ru.rmp.crossy20.models;

public class Book {
    private String author;
    private String title;
    private String genre;
    private String bookholderId;
    private String bookholderNickname;
    private boolean isCrossed;

    public Book(String author, String title, String genre, String bookholderId, String bookholderNickname, boolean isCrossed) {
        this.author = author;
        this.title = title;
        this.genre = genre;
        this.bookholderId = bookholderId;
        this.bookholderNickname = bookholderNickname;
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

    public String getBookholderId() {
        return bookholderId;
    }

    public void setBookholderId(String bookholder) {
        this.bookholderId = bookholder;
    }

    public boolean isCrossed() {
        return isCrossed;
    }

    public void setCrossed(boolean crossed) {
        isCrossed = crossed;
    }

    public String getBookholderNickname() {
        return bookholderNickname;
    }

    public void setBookholderNickname(String bookholderNickname) {
        this.bookholderNickname = bookholderNickname;
    }

    @Override
    public String toString() {
        return "Book{" +
                "author='" + author + '\'' +
                ", title='" + title + '\'' +
                ", genre='" + genre + '\'' +
                ", bookholderId='" + bookholderId + '\'' +
                ", bookholderNickname='" + bookholderNickname + '\'' +
                ", isCrossed=" + isCrossed +
                '}';
    }
}
