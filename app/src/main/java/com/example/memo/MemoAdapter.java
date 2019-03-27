package com.example.memo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;

import java.util.ArrayList;

public class MemoAdapter extends ArrayAdapter<Memo> {

    private ArrayList<Memo> items;
    private Context adapterContext;

    public MemoAdapter(Context context, ArrayList<Memo> items) {
        super(context, R.layout.activity_main, items);
        adapterContext = context;
        this.items = items;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        try {
            Memo memo = items.get(position);

            if (v == null) {
                LayoutInflater vi = (LayoutInflater) adapterContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                v = vi.inflate(R.layout.activity_main, null);
            }

            Button imp = (Button) v.findViewById(R.id.btnImp);
            Button date = (Button) v.findViewById(R.id.btnDate);

            imp.setVisibility(View.INVISIBLE);
            date.setVisibility(View.INVISIBLE);

        }
        catch (Exception e) {
            e.printStackTrace();
            e.getCause();
        }
        return v;
    }

}
