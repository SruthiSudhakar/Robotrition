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


public class TodaysHistory extends Fragment {
    ListView listView;
    Context context;
    ArrayList<ArrayList> list;
    TextView textView;
    CustomAdapter adapter;
    ArrayList foodNames;
    ArrayList foodIDs;
    NewTwoChan mCallback;
    TextView tt2;
    TextView tt7;
    TextView tt5;
    TextView food_label;
    TextView name_label;
    Button removItem;
    double cals=0;
    double fats=0;
    double sugs=0;
    TextView total_cals;
    TextView total_fats;
    TextView total_sug;
    TextView serv_laebel;
    int removes=0;
    public interface NewTwoChan{
        void callFatGet(long l, Context c);
        void removeItem(int position);
    }
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mCallback = (NewTwoChan) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement OnHeadlineSelectedListener");
        }
    }
    public TodaysHistory(Context context, ArrayList<ArrayList> list, ArrayList list2,ArrayList foodIDs,int removeOnne){
        this.context=context;
        this.list=list;
        this.foodNames=list2;
        this.foodIDs=foodIDs;
        this.removes=removeOnne;
    }
    @Override
    public View onCreateView(LayoutInflater inflater,  ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_todays_history,container,false);
        listView= (ListView) view.findViewById(R.id.log_listview);
        adapter = new CustomAdapter(context, R.layout.layout_list, list);
        textView=  (TextView) view.findViewById(R.id.favories_title);
        name_label= (TextView) view.findViewById(R.id.name_label);
        food_label= (TextView) view.findViewById(R.id.calorie_label);
        total_cals=  (TextView) view.findViewById(R.id.total_cals);//1
        total_fats=  (TextView) view.findViewById(R.id.total_fat);//4
        total_sug=  (TextView) view.findViewById(R.id.total_Sugar);//12
        serv_laebel= (TextView) view.findViewById(R.id.serv_amount_label);
        removItem= (Button) view.findViewById(R.id.edit_id);
        tt2= (TextView) view.findViewById(R.id.tt2);
        tt7=  (TextView) view.findViewById(R.id.tt7);//1
        tt5=  (TextView) view.findViewById(R.id.tt5);//4
        listView.setAdapter(adapter);
        if (removes==1)
        {
            listView.setBackgroundColor(Color.LTGRAY);
            removItem.setText("Done");
        }
        removItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (removes==0) {
                    removes = 1;
                    removItem.setText("Done");
                    listView.setBackgroundColor(Color.LTGRAY);
                }
                else {
                    removes=0;
                    removItem.setText("Edit");
                    listView.setBackgroundColor(Color.WHITE);

                }
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(removes==0)
                mCallback.callFatGet(Long.decode(foodIDs.get(position).toString()),context);
                if (removes==1){
                    Toast.makeText(context,"Removed "+foodNames.get(position).toString()+" from history",Toast.LENGTH_LONG).show();
                    mCallback.removeItem(position);
                }
            }
        });
        if (list.size()==0){
            textView.setText("No Food Logged For Today");
            removItem.setEnabled(false);
            removItem.setText("Edit");
            listView.setBackgroundColor(Color.WHITE);
            removes=0;
        }
        else{
            removItem.setEnabled(true);
            name_label.setText("Food Name");
            food_label.setText("Calories");
            serv_laebel.setText("Serving Amount");
            tt2.setText("Total Sugar:");
            tt7.setText("Total Fat:");
            tt5.setText("Total Calories:");
            for (int x=0;x<list.size();x++){
                try {cals += Double.parseDouble(list.get(x).get(1).toString());
                } catch (Exception e){};
                try{fats+=Double.parseDouble(list.get(x).get(4).toString());
                } catch (Exception e){}
                try {
                    sugs += Double.parseDouble(list.get(x).get(12).toString());
                } catch (Exception e){};
            }

            total_cals.setText(""+cals);
            total_fats.setText(""+fats+"g");
            total_sug.setText(""+sugs+"g");

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
            TextView servAmount= (TextView) layoutView.findViewById(R.id.serving_amount);
            //ImageView image= (ImageView) layoutView.findViewById(R.id.imageView2);
            //image.setImageResource(R.drawable.cloudy);
            notifyDataSetChanged();
            high.setText(list.get(position).get(1).toString());
            days.setText(foodNames.get(position).toString());
            servAmount.setText(list.get(position).get(10).toString()+list.get(position).get(15).toString());
            return layoutView;
        }
    }
}
