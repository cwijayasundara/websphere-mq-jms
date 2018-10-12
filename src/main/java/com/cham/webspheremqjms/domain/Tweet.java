package com.cham.webspheremqjms.domain;

public class Tweet {

    private String user;
    private String tweetMessage;

    public Tweet(){}

    @Override
    public String toString() {
        return String.format("Tweet{user=%s, tweetMessage=%s}", getUser(), getTweetMessage());
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getTweetMessage() {
        return tweetMessage;
    }

    public void setTweetMessage(String tweetMessage) {
        this.tweetMessage = tweetMessage;
    }

    public Tweet(String user, String tweetMessage) {
        this.user = user;
        this.tweetMessage = tweetMessage;
    }
}
