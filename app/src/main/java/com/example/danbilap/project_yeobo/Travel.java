package com.example.danbilap.project_yeobo;
import android.os.Parcel;
import android.os.Parcelable;

public class Travel implements Parcelable {
    int t_flag;
    int t_id;
    String t_title;
    String t_start;
    String t_end;
    String u_id;
    String url;

//    String imgurl;

    Travel(int flag, int t_id, String title,String start, String end,String u_id,String url){
        this.t_flag = flag;
        this.t_id = t_id;
        this.t_title = title;
        this.t_start = start;
        this.t_end = end;
        this.u_id=u_id;
        this.url=url;
 //       this.imgurl=imgurl;
    }

    protected Travel(Parcel in) {
        t_flag = in.readInt();
        t_id = in.readInt();
        t_title = in.readString();

        t_start = in.readString();
        t_end = in.readString();
        u_id=in.readString();
        url=in.readString();
   //     imgurl=in.readString();
    }

    public static final Creator<Travel> CREATOR = new Creator<Travel>() {
        @Override
        public Travel createFromParcel(Parcel in) {
            return new Travel(in);
        }

        @Override
        public Travel[] newArray(int size) {
            return new Travel[size];
        }
    };

    public int getT_flag() { return t_flag; }
    public String getT_title(){
        return t_title;
    }
    public String getT_start(){
        return t_start;
    }
    public int getT_id() {
        return t_id;
    }
    public String getT_end(){
        return t_end;
    }
    public String getU_id(){
        return u_id;
    }
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
        dest.writeInt(t_flag);
        dest.writeInt(t_id);
        dest.writeString(t_title);
        dest.writeString(t_start);
        dest.writeString(t_end);
        dest.writeString(u_id);
        dest.writeString(url);
  //      dest.writeString(imgurl);
    }
}

