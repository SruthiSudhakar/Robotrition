package com.example.meena.robotrition;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by meena on 10/8/2017.
 */

public class ProfileFrag extends Fragment {
    ProfileChange mCallback;
    Context context;
    Button save_prof_frag;
    EditText height_prof_frag;
    EditText name_prof_frag;
    EditText age_prof_frag;
    ArrayList<String> profile_info_frag;
    Button cancel_profile_fragment;
    Spinner gender_spinner_frag;
    Spinner activity_spinner_frag;
    EditText weight_prof_frag;
    public interface ProfileChange{
        void goalSet(ArrayList<String> infomration);
        void goBack();
    }
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mCallback = (ProfileChange) activity;
        }
        catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()+ " must implement OnHeadlineSelectedListener");
        }
    }
    public ProfileFrag(Context context,ArrayList<String> profile_info_frag){
        this.context=context;
        this.profile_info_frag=profile_info_frag;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.profile_lay,container,false);
        save_prof_frag= (Button) view.findViewById(R.id.save_prof_lay);
        height_prof_frag= (EditText) view.findViewById(R.id.calorie_goal_profile);
        name_prof_frag= (EditText) view.findViewById(R.id.name_profile);
        age_prof_frag= (EditText) view.findViewById(R.id.age_profile);
        cancel_profile_fragment= (Button) view.findViewById(R.id.cancel_prof_frag);
        gender_spinner_frag= (Spinner) view.findViewById(R.id.gender_spin_id);
        activity_spinner_frag= (Spinner) view.findViewById(R.id.activity_id);
        weight_prof_frag= (EditText) view.findViewById(R.id.weight_prof_id);
        addItemsOnSpinner2();
        Log.d("profile_frag_info",profile_info_frag+"");
        if (profile_info_frag.size()!=0){
            height_prof_frag.setText(profile_info_frag.get(2));
            name_prof_frag.setText(profile_info_frag.get(0));
            age_prof_frag.setText(profile_info_frag.get(1));
            weight_prof_frag.setText(profile_info_frag.get(5));
        }

        save_prof_frag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int count=0;
                if ((!name_prof_frag.getText().toString().equals(""))){
                        count++;
                    if (!age_prof_frag.getText().toString().equals("")){
                        if (Integer.parseInt(age_prof_frag.getText().toString()) > 15) {
                            count++;
                            if (!height_prof_frag.getText().toString().equals("")){
                                if (Integer.parseInt(height_prof_frag.getText().toString()) > 24) {
                                    count++;
                                    if(gender_spinner_frag.getSelectedItem()!=null) {
                                        count++;
                                        if (activity_spinner_frag.getSelectedItem()!=null) {
                                            count++;
                                            if (!weight_prof_frag.getText().toString().equals("")) {
                                                if (Integer.parseInt(weight_prof_frag.getText().toString()) > 49) {
                                                    count++;
                                                } else {
                                                    Toast.makeText(context, "Your weight must be greater than 50 pounds.", Toast.LENGTH_LONG).show();
                                                }
                                            } else {
                                                Toast.makeText(context, "Your weight must be greater than 50 pounds.", Toast.LENGTH_LONG).show();
                                            }
                                        }
                                        else {
                                            Toast.makeText(context, "Select Your Activity Level", Toast.LENGTH_LONG).show();
                                        }
                                    }else {
                                        Toast.makeText(context, "Select Your Gender", Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    Toast.makeText(context, "Enter A Valid Calorie Goal. Must be greater than 1000.", Toast.LENGTH_SHORT).show();
                                }
                            } else  Toast.makeText(context, "Enter A Valid Calorie Goal. Must be greater than 1000.", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(context, "Enter A Valid Age. You must be greater than 10 years old to use this feature of the App.", Toast.LENGTH_SHORT).show();
                        }
                    } else  Toast.makeText(context, "Enter A Valid Age. You must be greater than 10 years old to use this feature of the App.", Toast.LENGTH_SHORT).show();

                }
                    else  Toast.makeText(context, "Enter A Name", Toast.LENGTH_SHORT).show();
                if (count == 6) {
                    if (profile_info_frag.size()==0)
                        profile_info_frag.add(name_prof_frag.getText().toString());
                    profile_info_frag.set(0,name_prof_frag.getText().toString());
                    if (profile_info_frag.size()==1)
                        profile_info_frag.add(age_prof_frag.getText().toString());
                    profile_info_frag.set(1,age_prof_frag.getText().toString());
                    if (profile_info_frag.size()==2)
                        profile_info_frag.add(height_prof_frag.getText().toString());
                    profile_info_frag.set(2,height_prof_frag.getText().toString());
                    if (profile_info_frag.size()==3)
                        profile_info_frag.add(gender_spinner_frag.getSelectedItem().toString());
                    profile_info_frag.set(3,gender_spinner_frag.getSelectedItem().toString());
                    if (profile_info_frag.size()==4)
                        profile_info_frag.add(activity_spinner_frag.getSelectedItem().toString());
                    profile_info_frag.set(4,activity_spinner_frag.getSelectedItem().toString());
                    if (profile_info_frag.size()==5)
                        profile_info_frag.add(weight_prof_frag.getText().toString());
                    profile_info_frag.set(5,weight_prof_frag.getText().toString());
                    mCallback.goalSet(profile_info_frag);
                    mCallback.goBack();
                }
            }
        });
        cancel_profile_fragment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCallback.goBack();
            }
        });
        return view;
    }
    public void addItemsOnSpinner2() {
        List<String> list = new ArrayList<String>();
        list.add("Male");
        list.add("Female");
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(context,
                android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        gender_spinner_frag.setAdapter(dataAdapter);
        gender_spinner_frag.setPrompt("Select A Gender");
        if (profile_info_frag.size()!=0){
            for (int x=0;x<list.size();x++)
            {
                if (list.get(x).equals(profile_info_frag.get(3)))
                    gender_spinner_frag.setSelection(x);
            }
        }

        List<String> list2 = new ArrayList<String>();
        list2.add("Sedentary");
        list2.add("Light Activity");
        list2.add("Moderate Activity");
        list2.add("Very Activity");
        list2.add("Extra Activity");
        ArrayAdapter<String> dataAdapter2 = new ArrayAdapter<String>(context,
                android.R.layout.simple_spinner_item, list2);
        dataAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        activity_spinner_frag.setAdapter(dataAdapter2);
        activity_spinner_frag.setPrompt("Select Activity Level");

        if (profile_info_frag.size()!=0){
            for (int x=0;x<list2.size();x++)
            {
                if (list2.get(x).equals(profile_info_frag.get(4)))
                    activity_spinner_frag.setSelection(x);
            }
        }

    }
}
