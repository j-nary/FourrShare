package com.signpe.fourrshare;

public class GalleryItem {
    private int image;
    private String imageTitle;

    public GalleryItem(int image, String imageTitle) {
        this.image = image;
        this.imageTitle = imageTitle;
    }

    public int getImage() {
        return image;
    }

    public String getImageTitle() {
        return imageTitle;
    }
}
