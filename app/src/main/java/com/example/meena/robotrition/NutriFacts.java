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
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.security.spec.ECField;
import java.util.ArrayList;


public class NutriFacts extends Fragment {
     TextView measure;
     TextView calorie;
     TextView sugar;
     TextView protien;
     TextView fat;
     TextView nameofit;
    String string6;
    String food_id;
    Button back;
    Button add_favs;
    Button nutriaddButton;
    ArrayList list;
    Context context;
    EditText nutriServingAmountTaken;
    BackNewChan mCallback;

    public interface BackNewChan{
        void goBack();
        void addToFavs(ArrayList list1,String s,String i);
        void addedItem(ArrayList list,String string,String fId);
    }
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mCallback = (BackNewChan) activity;
        }
        catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()+ " must implement OnHeadlineSelectedListener");
        }
    }
    public NutriFacts(ArrayList list, String food_name, String food_id, Context context){
        this.list=list;
        string6=food_name;
        this.food_id=food_id;
        this.context=context;
    }

    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.nutri_facts,container,false);
        back= (Button) view.findViewById(R.id.back_id);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCallback.goBack();
            }
        });
        measure= (TextView) view.findViewById(R.id.servs);
        calorie= (TextView) view.findViewById(R.id.cal);
        sugar= (TextView) view.findViewById(R.id.sugar);
        protien= (TextView) view.findViewById(R.id.prot);
        fat = (TextView) view.findViewById(R.id.fat);
        nameofit= (TextView) view.findViewById(R.id.nameOfit);
        add_favs= (Button) view.findViewById(R.id.add_to_favs);
        nutriaddButton= (Button) view.findViewById(R.id.nutri_facts_add_button);
        nutriServingAmountTaken= (EditText) view.findViewById(R.id.nutri_facts_serving_edit_text);
        nutriaddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int servingAmount;
                try {
                    if (Double.parseDouble(nutriServingAmountTaken.getText().toString())>0) {
                        Log.d("ServingAmount", nutriServingAmountTaken.getText() + "-serving amount");
                        Log.d("ServingAmount", list+"");
                        for (int startX=0;startX<list.size();startX++){
                            try{
                                list.set(startX,Double.parseDouble(list.get(startX).toString())*Double.parseDouble(nutriServingAmountTaken.getText().toString()));
                            }
                            catch (Exception e){Log.d("ServingAmount","Error: "+e.getMessage());};
                            };
                        mCallback.addedItem(list, string6, food_id);
                    }
                    else {Toast.makeText(context,"Please Enter A Serving Amount Based On Above Facts",Toast.LENGTH_LONG).show();};
                }
                catch (Exception e){
                    Log.d("ServingAmount","Error: "+e.getMessage());
                    Toast.makeText(context,"Please Enter A Serving Amount Based On Above Facts",Toast.LENGTH_LONG).show();
                };
            }
        });
        add_favs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCallback.addToFavs(list,string6,food_id);
            }
        });
        measure.setText(list.get(10).toString()+list.get(15).toString());
        calorie.setText(list.get(1).toString());
        sugar.setText(list.get(12).toString()+"g");
        protien.setText(list.get(8).toString()+"g");
        fat.setText(list.get(4).toString()+"g");
        nameofit.setText(string6);

        return view;
    }
}
