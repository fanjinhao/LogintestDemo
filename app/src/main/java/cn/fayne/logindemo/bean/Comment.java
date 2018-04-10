package cn.fayne.logindemo.bean;

/**
 * Created by fan on 18-4-10.
 */

public class Comment {
    String mName;
    String mContent;

    public Comment() {

    }

    public Comment(String mName, String mContent) {
        this.mName = mName;
        this.mContent = mContent;
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
}
