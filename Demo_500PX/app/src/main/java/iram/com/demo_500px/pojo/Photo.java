package iram.com.demo_500px.pojo;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;
import static iram.com.demo_500px.extras.Keys.DESCRIPTION;
import static iram.com.demo_500px.extras.Keys.ID;
import static iram.com.demo_500px.extras.Keys.NAME;
import static iram.com.demo_500px.extras.Keys.URL;

public class Photo implements Parcelable {
    @SerializedName(ID)
    int id;
    @SerializedName(NAME)
    String name;
    @SerializedName(DESCRIPTION)
    String description;
    @SerializedName(URL)
    String thumburl;
    @SerializedName(URL)
    String imgurl;

    public Photo() {
    }

    protected Photo(Parcel in) {
        id = in.readInt();
        name = in.readString();
        description = in.readString();
        thumburl = in.readString();
        imgurl = in.readString();
    }

    public static final Creator<Photo> CREATOR = new Creator<Photo>() {
        @Override
        public Photo createFromParcel(Parcel in) {
            return new Photo(in);
        }

        @Override
        public Photo[] newArray(int size) {
            return new Photo[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getThumburl() {
        return thumburl;
    }

    public void setThumburl(String thumburl) {
        this.thumburl = thumburl;
    }

    public String getImgurl() {
        return imgurl;
    }

    public void setImgurl(String imgurl) {
        this.imgurl = imgurl;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(name);
        dest.writeString(description);
        dest.writeString(thumburl);
        dest.writeString(imgurl);
    }

    @Override
    public String toString() {
        return "Photo{" +
                "id=" + id +
                ", name=" + name +
                ", description='" + description + '\'' +
                ", thumburl=" + thumburl +
                ", imgurl=" + imgurl + '\'' +
                '}';
    }
}
