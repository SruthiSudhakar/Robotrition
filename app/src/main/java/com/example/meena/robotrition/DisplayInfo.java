package com.example.meena.robotrition;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;


public class DisplayInfo extends Fragment {

    ListView listView;
    TextView textView;
    ArrayList<ArrayList> types;
    Context context;
    Button back;
    CustomAdapter adapter;
    NewChan mCallback;
    public interface NewChan{
        void goBack();
        void callFatGet(long l, Context c);
    }
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mCallback = (NewChan) activity;
        }
        catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()+ " must implement OnHeadlineSelectedListener");
        }
    }
    public DisplayInfo(Context context, ArrayList<ArrayList> types){
        this.types=types;
        this.context=context;
    }
    @Override
    public View onCreateView(LayoutInflater inflater,  ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_display_info,container,false);
        back=(Button) view.findViewById(R.id.backtwo_d);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCallback.goBack();
            }
        });
        adapter = new CustomAdapter(context, R.layout.layout_list, types);
        listView= (ListView) view.findViewById(R.id.listView_id);
        listView.setAdapter(adapter);
        textView= (TextView) view.findViewById(R.id.errorinserach);
        if (types.size()==0){
            textView.setText("Sorry, there are no results for this search. Please try again.");
        }
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mCallback.callFatGet(Long.decode(types.get(position).get(0).toString()),context);
            }
        });
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
            days.setText(list.get(position).get(1).toString());
            high.setText(list.get(position).get(2).toString());
            Log.d("DisplayInfoList",list+"");

            Log.d("DisplayInfoList",list.get(position)+"");
            return layoutView;
        }
    }
}
