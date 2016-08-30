package com.snaptechnology.bgonzalez.bookmyroomandroid.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.snaptechnology.bgonzalez.bookmyroomandroid.R;
import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;


public class CalendarFragment extends Fragment {

    MaterialBetterSpinner materialBetterSpinner ;
    String[] SPINNER_DATA = {"Room name 1","Room name 2","Room name 3"};


    public CalendarFragment() {
        // Required empty public constructor
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_calendar, container, false);



        final TableLayout table = (TableLayout) rootView.findViewById(R.id.table_calendar);

        for(int i = 0 ; i < table.getChildCount(); i++){

            final TableRow row = (TableRow) table.getChildAt(i);


            for( int j = 0 ; j < row.getChildCount(); j++){

                final TextView column = (TextView) row.getChildAt(j);

                column.setTag( ((TextView)((TableRow) table.getChildAt(0)).getChildAt(j)).getText()+ " " + ((TextView)row.getChildAt(0)).getText() );

                column.setOnClickListener(new View.OnClickListener(){

                    @Override
                    public void onClick(View v){
                        // TODO Auto-generated method stub
                        Log.d("TAG",v.getTag().toString());
                        // 1. Instantiate an AlertDialog.Builder with its constructor
                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                        // Get the layout inflater
                        LayoutInflater inflater = getActivity().getLayoutInflater();


                        View dialogView = inflater.inflate(R.layout.dialog_book_room,null);
                        String[] data =((String) v.getTag()).split(" ");
                        builder.setView(dialogView);


                        String[] time = {"15 min","30 min","45 min", "1 hour", "1 hour 15 min"};


                        MaterialBetterSpinner spinnerBook = (MaterialBetterSpinner)dialogView.findViewById(R.id.spinner_time_book);
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(dialogView.getContext(), android.R.layout.simple_dropdown_item_1line, time);

                        spinnerBook.setAdapter(adapter);


                        //ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_dropdown_item_1line, SPINNER_DATA);

                        //innerBook.setAdapter(adapter);


                        AlertDialog dialog = builder.create();
                        dialog.show();
                    }
                });
            }


        }


        /*
        for(int i = 0; i < table.getChildCount(); i++){
            TableRow row = (TableRow) table.getChildAt(i);

            for(int j= 0 ; j < row.getChildCount(); j++){
                TextView cell = (TextView)row.getChildAt(j);


                if(i < 0 && j < 0){
                    cell.setTag(((TextView) row.getChildAt(0)).getText() + "    "+ ((((TextView) ((TableRow)table.getChildAt(0)).getChildAt(j)).getText())));

                    cell.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Log.d("Tag", v.getTag().toString());
                            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

// 2. Chain together various setter methods to set the dialog characteristics
                            builder.setMessage("Test")
                                    .setTitle("test");

// 3. Get the AlertDialog from create()
                            AlertDialog dialog = builder.create();
                        }
                    });

                }
            }

        }

        */
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

