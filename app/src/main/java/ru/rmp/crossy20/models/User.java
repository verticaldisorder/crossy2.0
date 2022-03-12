package ru.rmp.crossy20.models;

import java.util.Objects;

public class User {
    String nickname;
    String address;
    boolean handOverPersonally;
    boolean handOnPost;

    public User(String nickname, String address, boolean handOverPersonally, boolean handOnPost) {
        this.nickname = nickname;
        this.address = address;
        this.handOverPersonally = handOverPersonally;
        this.handOnPost = handOnPost;
    }

    public User(){
        this.nickname = "example";
        this.address = "example";
        this.handOverPersonally = handOverPersonally;
        this.handOnPost = handOnPost;
    }

    public User(User user) {
        this.nickname = user.getNickname();
        this.address = user.getAddress();
        this.handOverPersonally = user.isHandOverPersonally();
        this.handOnPost = user.isHandOnPost();
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = new String(nickname);
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = new String(address);
    }

    public boolean isHandOverPersonally() {
        return handOverPersonally;
    }

    public void setHandOverPersonally(boolean handOverPersonally) {
        this.handOverPersonally = handOverPersonally;
    }

    public boolean isHandOnPost() {
        return handOnPost;
    }

    public void setHandOnPost(boolean handOnPost) {
        this.handOnPost = handOnPost;
    }

    @Override
    public String toString() {
        return "User{" +
                "nickname='" + nickname + '\'' +
                ", address='" + address + '\'' +
                ", handOverPersonally=" + handOverPersonally +
                ", handOnPost=" + handOnPost +
                '}';
    }

    public void copyUser(User coping) {
        this.setNickname(coping.getNickname());
        this.setAddress(coping.getAddress());
        this.setHandOverPersonally(coping.isHandOverPersonally());
        this.setHandOnPost(coping.isHandOnPost());
    }
}
