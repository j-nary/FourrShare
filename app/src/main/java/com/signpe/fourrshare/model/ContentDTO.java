package com.signpe.fourrshare.model;

import java.util.HashMap;
import java.util.Map;

public class ContentDTO {
    private String imageUri;
    private String uid;
    private String userId;
    private String timeStamp;
    private int likeCount;
    private Map<String,Boolean> likedPeople = new HashMap<>();

    public String getImageUri(){
        return imageUri;
    }

    public String getUid(){
        return uid;
    }

    public String getUserId(){
        return userId;
    }

    public String getTimeStamp(){
        return timeStamp;
    }

    public int getLikeCount(){
        return likeCount;
    }

    public Map<String,Boolean> getLikedPeople(){
        return likedPeople;
    }

    public void setImageUri(String imageUri){
        this.imageUri = imageUri;
    }

    public void setUid(String uid){
        this.uid=uid;
    }

    public void setUserId(String userId){
        this.userId=userId;
    }

    public void setTimeStamp(String timeStamp){
        this.timeStamp=timeStamp;
    }

    public void setLikeCount(int likeCount){
        this.likeCount=likeCount;
    }

    public void setLikedPeople(Map<String,Boolean> likedPeople){
        this.likedPeople=likedPeople;
    }
}
