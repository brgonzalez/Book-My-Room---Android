package com.snaptechnology.bgonzalez.bookmyroomandroid.activity;

import android.view.View;

/**
 * Created by bgonzalez on 08/09/2016.
 */
public abstract class VisibleToggleClickListener implements View.OnClickListener {

    private boolean mVisible;

    @Override
    public void onClick(View v) {
        mVisible = !mVisible;
        changeVisibility(mVisible);
    }

    protected abstract void changeVisibility(boolean visible);

}
