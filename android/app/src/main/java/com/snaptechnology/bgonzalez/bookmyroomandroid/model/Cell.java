package com.snaptechnology.bgonzalez.bookmyroomandroid.model;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

/**
 * Created by bgonzalez on 14/09/2016.
 */
public class Cell extends TextView {
    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    private String start;

    public Cell(Context context) {
        super(context);
    }


}
