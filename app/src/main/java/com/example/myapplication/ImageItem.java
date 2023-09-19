package com.example.myapplication;

public class ImageItem {
    private String imageUrl;
    private String imageId;

    public ImageItem() {
    }
    public ImageItem(String imageId, String imageUrl) {
        this.imageId = imageId;
        this.imageUrl = imageUrl;
    }
    public String getImageId() {
        return imageId;
    }
    public ImageItem(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getImageUrl() {
        return imageUrl;
    }
}

