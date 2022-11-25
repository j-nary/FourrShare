package com.signpe.fourrshare.model;

import java.util.HashMap;
import java.util.Map;

public class ImageDTO {
    private String imageUri;
    private String uid;
    private String userId;
    private String userNickname;
    private String timeStamp;
    private boolean isUpload;
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

    public String getUserNickname() { return userNickname; }

    public boolean getIsUpload() { return isUpload; }

    public String getTimeStamp(){ return timeStamp; }

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

    public void setUserNickname(String userNickname) { this.userNickname=userNickname; }

    public void setTimeStamp(String timeStamp){
        this.timeStamp=timeStamp;
    }

    public void setIsUpload(boolean isUpload) { this.isUpload = isUpload; }

    public void setLikeCount(int likeCount){
        this.likeCount=likeCount;
    }

    public void setLikedPeople(Map<String,Boolean> likedPeople){
        this.likedPeople=likedPeople;
    }
}
