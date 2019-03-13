package com.example.memo;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {
    private ListView listView;
    private Button btnAdd;
    private DBAccess databaseAccess;
    boolean isDeleting = false;
    private List<Memo> memos;
    MemoAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.databaseAccess = DBAccess.getInstance(this);

        this.listView = (ListView) findViewById(R.id.lvMemos);
        this.btnAdd = (Button) findViewById(R.id.btnAdd);

        this.btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onAddClicked();
            }
        });

        this.listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Memo memo = memos.get(position);
                TextView txtMemo = (TextView) view.findViewById(R.id.txtMemo);
                if (memo.isFullDisplayed()) {
                    txtMemo.setText(memo.getShortText());
                    memo.setFullDisplayed(false);
                } else {
                    txtMemo.setText(memo.getText());
                    memo.setFullDisplayed(true);
                }
            }
        });

        initSettings();
        initSortByClick();

    }

    private void initSettings() {
        String sortBy = getSharedPreferences("Memo", Context.MODE_PRIVATE).getString("sortfield","date");

        RadioButton rbDate = (RadioButton) findViewById(R.id.radioDate);
        RadioButton rbImp = (RadioButton) findViewById(R.id.radioImp);

        if (sortBy.equalsIgnoreCase("date")) {
            rbDate.setChecked(true);
        }
        else {
            rbImp.setChecked(true);
        }
    }

    private void initSortByClick() {
        RadioGroup rgSortBy = (RadioGroup) findViewById(R.id.radioGroupSort);
        rgSortBy.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup arg0, int arg1) {
                RadioButton rbDate = (RadioButton) findViewById(R.id.radioDate);
                RadioButton rbImp = (RadioButton) findViewById(R.id.radioImp);
                if (rbDate.isChecked()) {
                    getSharedPreferences("Memo", Context.MODE_PRIVATE).edit() .putString("sortfield", "date").commit();
                }
                else {
                    getSharedPreferences("Memo", Context.MODE_PRIVATE).edit().putString("sortfield", "importance").commit();
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        String sortBy = getSharedPreferences("Memo", Context.MODE_PRIVATE).getString("sortfield","memo");

        try {
            databaseAccess.open();
            memos = databaseAccess.getMemos(sortBy);
            databaseAccess.close();
            MemoAdapter adapter = new MemoAdapter(this, memos);
            ListView listView = (ListView) findViewById(R.id.lvMemos);
            listView.setAdapter(adapter);listView.setAdapter(adapter);
            this.listView.setAdapter(adapter);
        }
        catch (Exception e) {
            Toast.makeText(this, "Error retrieving memos", Toast.LENGTH_LONG).show();
        }
    }

    public void onAddClicked() {
        Intent intent = new Intent(this, EditActivity.class);
        startActivity(intent);
    }

    public void onDeleteClicked(Memo memo) {

        final ImageView deleteButton = (ImageView) findViewById(R.id.btnDelete);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (isDeleting) {
                    isDeleting = false;
                    adapter.notifyDataSetChanged();
                }
                else {
                    isDeleting = true;
                }
            }
        });

    }

    public void onEditClicked(Memo memo) {
        Intent intent = new Intent(this, EditActivity.class);
        intent.putExtra("MEMO", memo);
        startActivity(intent);
    }

    private class MemoAdapter extends ArrayAdapter<Memo> {


        public MemoAdapter(Context context, List<Memo> objects) {
            super(context, 0, objects);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = getLayoutInflater().inflate(R.layout.layout_list_item, parent, false);
            }

            ImageView btnEdit = (ImageView) convertView.findViewById(R.id.btnEdit);
            ImageView btnDelete = (ImageView) convertView.findViewById(R.id.btnDelete);
            TextView txtDate = (TextView) convertView.findViewById(R.id.txtDate);
            TextView txtMemo = (TextView) convertView.findViewById(R.id.txtMemo);

            final Memo memo = memos.get(position);
            memo.setFullDisplayed(false);
            txtDate.setText(memo.getDate());
            txtMemo.setText(memo.getShortText());
            btnEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onEditClicked(memo);
                }
            });
            btnDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onDeleteClicked(memo);
                }
            });
            return convertView;
        }
    }
}
