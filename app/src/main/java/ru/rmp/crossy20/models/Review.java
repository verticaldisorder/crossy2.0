package ru.rmp.crossy20.models;

public class Review {
    String content;
    String nickname;
    String reviewed;

    public Review(String content, String nickname, String reviewed) {
        this.content = content;
        this.nickname = nickname;
        this.reviewed = reviewed;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getReviewed() {
        return reviewed;
    }

    public void setReviewed(String reviewed) {
        this.reviewed = reviewed;
    }

    @Override
    public String toString() {
        return "Review{" +
                "content='" + content + '\'' +
                ", nickname='" + nickname + '\'' +
                ", reviewed='" + reviewed + '\'' +
                '}';
    }
}

