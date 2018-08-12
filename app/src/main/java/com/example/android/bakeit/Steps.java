package com.example.android.bakeit;

public class Steps {
    int id;
    String shortDesc;
    String description;
    String videoUrl;
    String thumbUrl;

    public Steps(int id, String shortDesc, String description, String videoUrl, String thumbUrl) {
        this.id = id;
        this.shortDesc = shortDesc;
        this.description = description;
        this.videoUrl = videoUrl;
        this.thumbUrl = thumbUrl;
    }
}
