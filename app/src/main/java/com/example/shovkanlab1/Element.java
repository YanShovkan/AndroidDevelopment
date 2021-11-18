package com.example.shovkanlab1;

import android.os.Parcel;
import android.os.Parcelable;

public class Element implements Parcelable {

    int ID;
    String Name;
    String BoolFlag;

    protected Element(Parcel in) {
        ID = in.readInt();
        Name = in.readString();
        BoolFlag = in.readString();
    }

    public Element(int ID, String Name, String BoolFlag) {
        setID(ID);
        setName(Name);
        setBoolFlag(BoolFlag);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(ID);
        dest.writeString(Name);
        dest.writeString(BoolFlag);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Element> CREATOR = new Creator<Element>() {
        @Override
        public Element createFromParcel(Parcel in) {
            return new Element(in);
        }

        @Override
        public Element[] newArray(int size) {
            return new Element[size];
        }
    };

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getName() {
        return Name;
    }

    public void setName(String Name) {
        this.Name = Name;
    }

    public Boolean getBoolFlag() {
        return Boolean.parseBoolean(BoolFlag);
    }

    public void setBoolFlag(String BoolFlag) {
        this.BoolFlag = BoolFlag;
    }
}