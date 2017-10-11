package com.example.meena.robotrition;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by meena on 10/10/2017.
 */

public class RecFrag extends Fragment {
    RecInterface mCallback;
    Context context;
    ArrayList<String> profile_info_rec;
    Button back_button_about;
    TextView cal_rec_frag;

    int startCalorie=-84;

    public interface RecInterface{
        void goBack();
    }
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mCallback = (RecInterface) activity;
        }
        catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()+ " must implement OnHeadlineSelectedListener");
        }
    }
    public RecFrag(Context context, ArrayList<String> info){
        profile_info_rec=info;
        this.context=context;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.rec_lay,container,false);
        back_button_about= (Button) view.findViewById(R.id.rec_back_button);
        cal_rec_frag= (TextView) view.findViewById(R.id.rec_cal_id);
        back_button_about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCallback.goBack();
            }
        });
        Log.d("RecFragProfileInfo",findCalories()+"");
        cal_rec_frag.setText(findCalories());
        return view;
    }
    public String findCalories(){
        int age = 0;
        int height=0;
        int weight=0;
        double heightIncrease=19;
        double weightIncrease=5.5;
        double ageIncrease=-6;
        String activity="";
        int calories=startCalorie;
        if (profile_info_rec.size()!=0) {
            age = Integer.parseInt(profile_info_rec.get(1));
            height = Integer.parseInt(profile_info_rec.get(2));
            weight = Integer.parseInt(profile_info_rec.get(5));
            activity= profile_info_rec.get(4);
            switch (activity){
                case "Light Activity": ageIncrease=-7;
                    weightIncrease=6;
                    heightIncrease=22;
                    break;
                case "Moderate Activity": weightIncrease=7;
                    heightIncrease=25;
                    ageIncrease=-8;
                    break;
                case "Very Activity": weightIncrease=8;
                    heightIncrease=27.333;
                    ageIncrease=-8.333;
                    break;
                case "Extra Activity": weightIncrease=8.5;
                    heightIncrease=30;
                    ageIncrease=-9.5;
                    break;
            }
            calories+= height*heightIncrease;
            calories+= weight*weightIncrease;
            calories+= (age-15)*ageIncrease;
            Toast.makeText(context,calories+"",Toast.LENGTH_LONG).show();
            return "Recommended calories consumption a day: "+Integer.toString(calories);
        }
        //else Toast.makeText(context,"Fill out profile before using this app",Toast.LENGTH_LONG).show();

        return "Fill out profile before requesting recommendations";
    }
}
