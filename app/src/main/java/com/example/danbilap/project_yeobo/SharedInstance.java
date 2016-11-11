package com.example.danbilap.project_yeobo;

import android.graphics.Bitmap;

/**
 * Created by Gawon on 2016-06-18.
 */
public class SharedInstance {

    String sharedUrl=null;
    String sharedCategory = null;
    Bitmap sharedBitmap=null;
    String sharedDescription = null;
    String sharedTitle= null;

    public SharedInstance() {
    }

    //웹페이지 공유할 때 생성할 객체.
    public SharedInstance(String sharedUrl,
                          String sharedDescription, String sharedTitle) {
        this.sharedUrl = sharedUrl;
       /* this.sharedCategory = sharedCategory;
        this.sharedBitmap = sharedBitmap;*/
        this.sharedDescription = sharedDescription;
        this.sharedTitle = sharedTitle;
    }

    public SharedInstance(Bitmap sharedBitmap) {
        this.sharedBitmap = sharedBitmap;
    }

    public String getSharedUrl() {
        return sharedUrl;
    }

    public void setSharedUrl(String sharedUrl) {
        this.sharedUrl = sharedUrl;
    }

    public String getSharedCategory() {
        return sharedCategory;
    }

    public void setSharedCategory(String sharedCategory) {
        this.sharedCategory = sharedCategory;
    }

    public Bitmap getSharedBitmap() {
        return sharedBitmap;
    }

    public void setSharedBitmap(Bitmap sharedBitmap) {
        this.sharedBitmap = sharedBitmap;
    }

    public String getSharedDescription() {
        return sharedDescription;
    }

    public void setSharedDescription(String sharedDescription) {
        this.sharedDescription = sharedDescription;
    }

    public String getSharedTitle() {
        return sharedTitle;
    }

    public void setSharedTitle(String sharedTitle) {
        this.sharedTitle = sharedTitle;
    }

}
