package com.example.galleryapp;

public class ImageItem {
    private String imageUrl;
    private boolean isSelected;

    public ImageItem(String imageUrl,boolean isSelected){
        this.imageUrl = imageUrl;
        this.isSelected =isSelected;

    }

    public String getImageUrl() {
        return imageUrl;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
