package com.example.android.bakeit;

import android.os.Parcel;
import android.os.Parcelable;

public class Steps implements Parcelable {
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(shortDesc);
        dest.writeString(description);
        dest.writeString(videoUrl);
        dest.writeString(thumbUrl);

    }
    public static final Parcelable.Creator<Steps> CREATOR
            = new Parcelable.Creator<Steps>() {
        public Steps createFromParcel(Parcel in) {
            return new Steps(in);
        }

        public Steps[] newArray(int size) {
            return new Steps[size];
        }
    };
    private Steps(Parcel in) {
        id=in.readInt();
        shortDesc=in.readString();
        description=in.readString();
        videoUrl=in.readString();
        thumbUrl=in.readString();
    }
}
