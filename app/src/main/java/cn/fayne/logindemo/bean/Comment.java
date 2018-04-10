package cn.fayne.logindemo.bean;

/**
 * Created by fan on 18-4-10.
 */

public class Comment {
    String mName;
    String mContent;
    String mTime;

    public Comment() {

    }

    public Comment(String mName, String mContent, String time) {
        this.mName = mName;
        this.mContent = mContent;
        mTime = time;
    }

    public String getmName() {
        return mName;
    }

    public void setmName(String mName) {
        this.mName = mName;
    }

    public String getmContent() {
        return mContent;
    }

    public void setmContent(String mContent) {
        this.mContent = mContent;
    }

    public String getmTime() {
        return mTime;
    }

    public void setmTime(String mTime) {
        this.mTime = mTime;
    }
}
