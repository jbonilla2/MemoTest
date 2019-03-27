package com.example.memo;

import android.*;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;


public class EditActivity extends AppCompatActivity {
    private EditText etText;
    private Button btnSave;
    private Button btnCancel;
    private Memo memo;

    int currentMemoId;
    Intent intent;
    String importance;
    RadioButton radioButtonLow;
    RadioButton radioButtonMedium;
    RadioButton radioButtonHigh;
    EditText eMemoMessage;
    String memoMessage;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        this.etText = (EditText) findViewById(R.id.etText);
        this.btnSave = (Button) findViewById(R.id.btnSave);
        this.btnCancel = (Button) findViewById(R.id.btnCancel);

        Bundle bundle = getIntent().getExtras();
        if(bundle != null) {
            memo = (Memo) bundle.get("MEMO");
            if(memo != null) {
                this.etText.setText(memo.getText());
            }
        }


        this.btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSaveClicked();

            }
        });

        this.btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onCancelClicked();
            }
        });
        initSortBy();
        initSortByClick();

    }

    public void onSaveClicked() {
        DBAccess databaseAccess = DBAccess.getInstance(this);
        databaseAccess.open();
        if(memo == null) {
            // Add new memo
            Memo temp = new Memo();
            temp.setText(etText.getText().toString());
            databaseAccess.save(temp);
        } else {
            // Update the memo
            memo.setText(etText.getText().toString());
            databaseAccess.update(memo);
        }
        databaseAccess.close();
        this.finish();



    }

    /* CLICKING THE IMPORTANCE BUTTON SHOULD TRIGGER THIS METHOD */
    /* This method saves and displays the user's (current) preferences */


    private void initSortBy() {
        String sortBy = getSharedPreferences("MyMemoPreferences", Context.MODE_PRIVATE).getString("sortfield","importance");

        RadioButton rbHigh = findViewById(R.id.radioHigh);
        RadioButton rbMed = findViewById(R.id.radioMed);
        RadioButton rbLow = findViewById(R.id.radioLow);
        if (sortBy.equalsIgnoreCase("low")) {
            rbLow.setChecked(true);
        }
        else if (sortBy.equalsIgnoreCase("med")) {
            rbMed.setChecked(true);
        }
        else {
            rbHigh.setChecked(true);
        }

    }

    private void initSortByClick() {
        RadioGroup rgSortBy = (RadioGroup) findViewById(R.id.radioGroupImp);
        rgSortBy.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup arg0, int arg1) {
                RadioButton rbLow = findViewById(R.id.radioLow);
                RadioButton rbMed = findViewById(R.id.radioMed);
                if (rbLow.isChecked()) {
                    getSharedPreferences("MyMemoPreferences", Context.MODE_PRIVATE).edit() .putString("sortfield", "low").commit();
                    memo.setPriority(3);
                }
                else if (rbMed.isChecked()) {
                    getSharedPreferences("MyMemoPreferences", Context.MODE_PRIVATE).edit().putString("sortfield", "med").commit();
                    memo.setPriority(2);
                }
                else {
                    getSharedPreferences("MyMemoPreferences", Context.MODE_PRIVATE).edit().putString("sortfield", "high").commit();
                    memo.setPriority(1);
                }
            }
        });
    }


public void onCancelClicked() {
        this.finish();
    }

}

  /*
        intent = getIntent();
        memoMessage = intent.getStringExtra("memo");
        importance = intent.getStringExtra("priority");
        currentMemoId = intent.getIntExtra("memoId",-1);


        eMemoMessage = (EditText) findViewById(R.id.etText);
        eMemoMessage.setText(memoMessage);
        eMemoMessage.setEnabled(true);

        radioButtonLow=(RadioButton)findViewById(R.id.radioLow);
        radioButtonMedium=(RadioButton)findViewById(R.id.radioMed);
        radioButtonHigh=(RadioButton)findViewById(R.id.radioHigh);

        if(importance.equals("Low")){
            radioButtonLow.setChecked(true);
        }

        else if(importance.equals("Med")) {
            radioButtonMedium.setChecked(true);
        }

        else {
            radioButtonHigh.setChecked(true);
        }
*/