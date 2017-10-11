package com.example.meena.robotrition;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;


public class SearchBar extends Fragment {
    Button search;
    EditText editText;
    CommChan mCallback;
    Button searchBack;
    public interface CommChan{
        void giveNameToSearch(String string);
        void goBack();
    }
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mCallback = (CommChan) activity;
        }
        catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()+ " must implement OnHeadlineSelectedListener");
        }
    }
    public View onCreateView(LayoutInflater inflater,  ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.search_bar,container,false);
        search=(Button) view.findViewById(R.id.serach);
        editText= (EditText) view.findViewById(R.id.foodName);
        searchBack= (Button) view.findViewById(R.id.search_bar_back_button);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCallback.giveNameToSearch(editText.getText().toString());
            }
        });
        searchBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCallback.goBack();
            }
        });
        return view;
    }

}