package com.example.meena.robotrition;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class RecepiesFragment extends Fragment{
    Button goB;
    JustFo mCallback;
    public interface JustFo{
        void goBack();
    }
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mCallback = (JustFo) activity;
        }
        catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()+ " must implement OnHeadlineSelectedListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater,  ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_display_info,container,false);
        goB= (Button) view.findViewById(R.id.gob_id);
        goB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCallback.goBack();
            }
        });
        return view;
    }
}
