package com.example.myapplication;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public class MemoContent extends AppCompatActivity {
    Memo memo;
    boolean saved = false;
    boolean editing = false;
    int position;
    EditText contentText;
    EditText titleText;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memo_content);


        position = getIntent().getExtras().getInt("pos");
        memo = MemoListActivity.mRealmResults.get(position);


        titleText = (EditText) findViewById(R.id.textView_memo_content_title);
        contentText = (EditText) findViewById(R.id.textView_memo_content);

        titleText.setText(memo.getTitle());
        contentText.setText(memo.getContent());

        Button buttonSave = (Button)findViewById(R.id.button_memo_content_save);
        final Button buttonEdit = (Button)findViewById(R.id.button_memo_content_edit);
        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                memoSave(MyApplication.memoConfig);
                memoEdit(buttonEdit, false);
                Toast.makeText(getApplicationContext(), "저장 완료", Toast.LENGTH_SHORT).show();
            }
        });

        buttonEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(editing){
                    editing = false;
                }
                else{
                    editing = true;
                }
                memoEdit(buttonEdit, editing);
            }
        });
    }
    @Override
    public void onBackPressed() {
//        if(!saved){
//            memoSave(MyApplication.memoConfig);
//        }
        super.onBackPressed();
    }

    public void memoSave(RealmConfiguration config){
        Realm realm = Realm.getInstance(config);
        realm.beginTransaction();
        memo.setTitle(titleText.getText().toString());
        memo.setContent(contentText.getText().toString());
        realm.commitTransaction();
        saved = true;
        MemoListActivity.mRealmList.set(position,new Memo(memo));
    }

    public void memoEdit(Button buttonEdit, boolean editing){
        if(editing){
            buttonEdit.setText("확인");
        }
        else{
            buttonEdit.setText("수정");
        }
        setEditableText(titleText,editing);
        setEditableText(contentText,editing);
    }

    public void setEditableText(EditText editText, boolean bool){
        Log.w("edit bool", Boolean.toString(bool));
        editText.setFocusableInTouchMode(bool);
        editText.setClickable(bool);
        editText.setFocusable(bool);
        editText.setCursorVisible(bool);
    }
}