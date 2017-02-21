package com.wsns.lor.seller.utils;

/**
 * Created by 世杰 on 2016/4/2.
 */
public class StringDivide {
    String describe;
    String[] array = new String[3];

    public StringDivide(String describe) {
        this.describe = describe;
    }

    public int getCount() {

            array = describe.split(",");

        return array.length;
    }

    public String getItem(int position) {
        return array[position];
    }


    public String getFirstWord(int position) {


        return getItem(position).substring(0, 1);

    }

    public String getContent(int position) {


        return getItem(position).substring(1, getItem(position).length());

    }
}
