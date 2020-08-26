package com.example.myapplication;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class HealthContentActivity extends AppCompatActivity {
    HealthRedButton item;

    EditText nameText;
    EditText contentText;

    int pos;
    boolean editing = false;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_health_content);

        pos = getIntent().getExtras().getInt("pos");

        item = new HealthRedButton(HealthActivity.hRealmList.get(pos));

        editing = false;

        nameText = (EditText) findViewById(R.id.textView_health_content_name);
        contentText = (EditText) findViewById(R.id.textView_health_content);
        nameText.setText(item.getName());
        contentText.setText(item.getContent());

        final Button buttonEdit = (Button) findViewById(R.id.button_health_content_edit);
        buttonEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editing) {
                    editing = false;
                } else {
                    editing = true;
                }
                memoEdit(buttonEdit, editing);
            }
        });
    }

    @Override
    public void onBackPressed() {
        memoSave();
        super.onBackPressed();
    }

    public void memoSave() {
        item.setName(nameText.getText().toString());
        item.setContent(contentText.getText().toString());
        HealthActivity.hRealmList.set(pos, item);

        HealthActivity.healthRealm.beginTransaction();
        HealthActivity.healthRealm.copyToRealm(HealthActivity.hRealmList.get(pos));
        HealthActivity.healthRealm.commitTransaction();
    }

    public void memoEdit(Button buttonEdit , boolean editing) {
        if (editing) {
            buttonEdit.setText("확인");
        } else {
            buttonEdit.setText("수정");
        }
        setEditableText(nameText, editing);
        setEditableText(contentText, editing);
    }

    public void setEditableText(EditText editText, boolean bool) {
        editText.setFocusableInTouchMode(bool);
        editText.setClickable(bool);
        editText.setFocusable(bool);
        editText.setCursorVisible(bool);
    }
}