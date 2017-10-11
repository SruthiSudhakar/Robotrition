package com.example.meena.robotrition;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;


public class FavsFrags extends Fragment {
    ListView listView;
    Context context;
    ArrayList<ArrayList> list;
    TextView textView;
    CustomAdapter adapter;
    ArrayList foodNames;
    ArrayList foodIDs;
    NewThreeChan mCallback;
    TextView name_label;
    TextView food_label;
    Button removeButton;
    Button favgoback;
    int removes=0;

    public interface NewThreeChan{
        void callFatGet(long l, Context c);
        void removeFav(int position);
        void goBack();
    }
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mCallback = (NewThreeChan) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement OnHeadlineSelectedListener");
        }
    }
    public FavsFrags(Context context, ArrayList<ArrayList> list, ArrayList list2,ArrayList foodIDs, int removeOnneeee){
        this.context=context;
        this.list=list;
        this.foodNames=list2;
        this.foodIDs=foodIDs;
        this.removes=removeOnneeee;
    }
    @Override
    public View onCreateView(LayoutInflater inflater,  ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_favs_frags,container,false);
        listView= (ListView) view.findViewById(R.id.favo_listview);
        favgoback= (Button) view.findViewById(R.id.fav_frag_back_button);
        adapter = new CustomAdapter(context, R.layout.layout_list, list);
        textView=  (TextView) view.findViewById(R.id.favories_title);
        removeButton= (Button) view.findViewById(R.id.editit_id);
        name_label= (TextView) view.findViewById(R.id.fav_labe);
        food_label= (TextView) view.findViewById(R.id.fav_val_label);
        listView.setAdapter(adapter);

        if (removes==1)
        {
            listView.setBackgroundColor(Color.LTGRAY);
            removeButton.setText("Done");
        }
        removeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (removes==0) {
                    removes = 1;
                    removeButton.setText("Done");
                    listView.setBackgroundColor(Color.LTGRAY);
                }
                else {
                    removes=0;
                    removeButton.setText("Edit");
                    listView.setBackgroundColor(Color.WHITE);

                }
            }
        });
        favgoback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCallback.goBack();
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(removes==0)
                    mCallback.callFatGet(Long.decode(foodIDs.get(position).toString()),context);
                if (removes==1){
                    Toast.makeText(context,"Removed "+foodNames.get(position).toString()+" from favorites",Toast.LENGTH_LONG).show();
                    mCallback.removeFav(position);
                }
            }
        });
        if (list.size()==0){
            textView.setText("No Favorites Added Yet");
            removeButton.setEnabled(false);
            removeButton.setText("Edit");
            listView.setBackgroundColor(Color.WHITE);
        }
        else{
            removeButton.setEnabled(true);
            name_label.setText("Name of Food");
            food_label.setText("Calories in Food");
        }

        return view;
    }
    public class CustomAdapter extends ArrayAdapter<ArrayList> {
        ArrayList<ArrayList> list;
        Context mainContext;
        int reasource;


        public CustomAdapter(Context context, int resource, ArrayList<ArrayList> objects)
        {
            super (context,resource,objects);
            list=objects;
            mainContext=context;
            reasource=resource;


        }

        public View getView(int position, View convertView, ViewGroup parent)
        {

            LayoutInflater inflater= (LayoutInflater)mainContext.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            View layoutView = inflater.inflate(reasource,null);
            TextView days= (TextView) layoutView.findViewById(R.id.days);
            TextView high= (TextView) layoutView.findViewById(R.id.high);
            //ImageView image= (ImageView) layoutView.findViewById(R.id.imageView2);
            //image.setImageResource(R.drawable.cloudy);
            notifyDataSetChanged();
            high.setText(list.get(position).get(1).toString());
            days.setText(foodNames.get(position).toString());

            return layoutView;
        }
    }
}
