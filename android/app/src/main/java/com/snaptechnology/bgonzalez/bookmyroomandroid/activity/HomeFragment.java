package com.snaptechnology.bgonzalez.bookmyroomandroid.activity;

/**
 * Created by bgonzalez on 24/08/2016.
 */

import android.annotation.TargetApi;
import android.app.Activity;

import android.app.Fragment;
import android.content.Context;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.snaptechnology.bgonzalez.bookmyroomandroid.R;
import com.snaptechnology.bgonzalez.bookmyroomandroid.animation.Circle;
import com.snaptechnology.bgonzalez.bookmyroomandroid.animation.CircleAngleAnimation;


public class HomeFragment extends Fragment {

    public static int size;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);
        Circle circle = (Circle) rootView.findViewById(R.id.circle);

        final LinearLayout linearLayout = (LinearLayout) rootView.findViewById(R.id.linear_layout_main_circle);
        linearLayout.post(new Runnable()
        {

            @Override
            public void run()
            {
                Log.i("TEST", "Layout width : "+ linearLayout.getWidth());

            }
        });

        /*final LinearLayout linearLayout = (LinearLayout) rootView.findViewById(R.id.linear_layout_main_circle);
        linearLayout.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

            @Override
            public void onGlobalLayout() {
                // Ensure you call it only once :
                linearLayout.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                size = (int) (Math.round(linearLayout.getWidth())*0.77);
                Log.i("SQkdjiuw",Integer.toString(size ));

                // Here you can get the size :)
            }

        });



        circle.setSize(size);
        Circle circle2 = circle;*/

        CircleAngleAnimation animation = new CircleAngleAnimation(circle, 360);
        animation.setDuration(20000);
        circle.startAnimation(animation);

        // Inflate the layout for this fragment
        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }



}
