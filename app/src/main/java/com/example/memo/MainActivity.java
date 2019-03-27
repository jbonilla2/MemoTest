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
    private Button btnImp;
    private Button btnDate;
    private DBAccess databaseAccess;
    /*boolean isDeleting = false;*/
    private ArrayList<Memo> memos;
    MemoAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.databaseAccess = DBAccess.getInstance(this);

        this.listView = findViewById(R.id.listView);
        this.btnAdd = findViewById(R.id.btnAdd);
        this.btnImp = findViewById(R.id.btnImp);
        this.btnDate = findViewById(R.id.btnDate);

        this.btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onAddClicked();
            }
        });

        /*btnImp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onImportanceClicked();
            }
        });

        btnDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onDateClicked();
            }
        });*/

        this.listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Memo memo = memos.get(position);
                TextView txtMemo = view.findViewById(R.id.txtMemo);
                if (memo.isFullDisplayed()) {
                    txtMemo.setText(memo.getShortText());
                    memo.setFullDisplayed(false);
                } else {
                    txtMemo.setText(memo.getText());
                    memo.setFullDisplayed(true);
                }
            }
        });

    }


    @Override
    protected void onResume() {
        super.onResume();

        String sortBy = getSharedPreferences("MyMemoPreferences", Context.MODE_PRIVATE).getString("sortfield", "importance");
        String sortOrder = getSharedPreferences("MyMemoPreferences", Context.MODE_PRIVATE).getString("sortorder", "ASC");

        try {

            if(sortBy.equalsIgnoreCase("date")) {
                databaseAccess.open();
                this.memos = databaseAccess.getAllMemos();
                databaseAccess.close();
            } else {
                databaseAccess.open();
                this.memos = databaseAccess.sortMemosByImp(sortBy, sortOrder);
                databaseAccess.close();
            }

            MemoAdapter adapter = new MemoAdapter(this, memos);
            this.listView.setAdapter(adapter);

        }
        catch (Exception e) {
            Toast.makeText(this, "Error retrieving memos", Toast.LENGTH_LONG).show();
        }
    }

    public void onImportanceClicked(Memo memo) {
        String sortBy = getSharedPreferences("MyMemoPreferences", Context.MODE_PRIVATE).getString("sortfield", "priority");
        String sortOrder = getSharedPreferences("MyMemoPreferences", Context.MODE_PRIVATE).getString("sortorder", "ASC");
        databaseAccess.open();
        databaseAccess.sortMemosByImp(sortBy, sortOrder);
  //   this is where you insert the sort query in DB
    }

    public void onDateClicked(Memo memo) {
        databaseAccess.open();
        databaseAccess.getAllMemos();
        //   this is where you insert the sort query in DB
    }

    public void onAddClicked() {
        Intent intent = new Intent(this, EditActivity.class);
        startActivity(intent);
    }


    public void onEditClicked(Memo memo) {
        Intent intent = new Intent(this, EditActivity.class);
        intent.putExtra("MEMO", memo);
        startActivity(intent);
    }

    public void onDeleteClicked(Memo memo) {
        databaseAccess.open();
        databaseAccess.delete(memo);
        databaseAccess.close();

        ArrayAdapter<Memo> adapter = (ArrayAdapter<Memo>) listView.getAdapter();
        adapter.remove(memo);
        adapter.notifyDataSetChanged();
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
            Button btnImp = (Button) convertView.findViewById(R.id.btnImp);
            Button btnDate = (Button) convertView.findViewById(R.id.btnDate);

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

            btnImp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onImportanceClicked(memo);
                }
            });

            btnDate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onDateClicked(memo);
                }
            });

            return convertView;
        }
    }
}
