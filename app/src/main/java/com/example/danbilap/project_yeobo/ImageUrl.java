package com.example.danbilap.project_yeobo;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by user on 2016-11-09.
 */
public class ImageUrl implements Parcelable{

    String  url;

    ImageUrl(String url){

        this.url=url;
    }
    protected ImageUrl(Parcel in) {

        url=in.readString();
        //     imgurl=in.readString();
    }

    public static final Creator<ImageUrl> CREATOR = new Creator<ImageUrl>() {
        @Override
        public ImageUrl createFromParcel(Parcel in) {
            return new ImageUrl(in);
        }

        @Override
        public ImageUrl[] newArray(int size) {
            return new ImageUrl[size];
        }
    };

    public String getUrl(){
        return url;
    }
/*//    public String getImgurl(){
        return imgurl;
    }*/



    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(url);
    }
}
