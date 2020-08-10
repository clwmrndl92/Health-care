package com.example.myapplication;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmResults;


public class MainActivity extends AppCompatActivity {
    public static RealmResults<Memo> mRealmResults;
    public static RealmList<Memo> mRealmList;

    private CustomAdapter mAdapter;
    int count = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Realm memoRealm = Realm.getInstance(MyApplication.memoConfig);
        mRealmResults= memoRealm.where(Memo.class).findAll();
        mRealmList = new RealmList<>();

        for (Memo memo : mRealmResults) {
            mRealmList.add(new Memo(memo.getTime(), memo.getTitle()));
        }

        RecyclerView mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview_main_list);
        LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);


        mAdapter = new CustomAdapter( mRealmList);
        mRecyclerView.setAdapter(mAdapter);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mRecyclerView.getContext(),
                mLinearLayoutManager.getOrientation());
        mRecyclerView.addItemDecoration(dividerItemDecoration);



        mRecyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), mRecyclerView, new ClickListener() {
            @Override
            public void onClick(View view, int position) {
                Memo memo = mRealmList.get(position);
                //Toast.makeText(getApplicationContext(), memo.getTitle(), Toast.LENGTH_LONG).show();
                Intent intent = new Intent(getBaseContext(), ResultActivity.class);

                intent.putExtra("pos", position);

                startActivity(intent);
            }

            @Override
            public void onLongClick(View view, int position) {
            }
        }));


        Button buttonInsert = (Button)findViewById(R.id.button_main_insert);
        buttonInsert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                count++;

                // Dictionary 생성자를 사용하여 RealmList에 삽입할 데이터를 만듭니다.
                Memo data = new Memo();

                mRealmList.add(0, data); //RecyclerView의 첫 줄에 삽입
                //mRealmList.add(data); // RecyclerView의 마지막 줄에 삽입

                //mAdapter.notifyItemInserted(0);
                mAdapter.notifyDataSetChanged(); //변경된 데이터를 화면에 반영

                //Toast.makeText(getApplicationContext(), memo.getTitle(), Toast.LENGTH_LONG).show();
                Intent intent = new Intent(getBaseContext(), ResultActivity.class);

                intent.putExtra("pos", mRealmList.size());

                startActivity(intent);
            }
        });

    }

    @Override
    protected void onResume() {
        mAdapter.notifyDataSetChanged();
        super.onResume();
    }

    public interface ClickListener {
        void onClick(View view, int position);

        void onLongClick(View view, int position);
    }

    public static class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {

        private GestureDetector gestureDetector;
        private MainActivity.ClickListener clickListener;

        public RecyclerTouchListener(Context context, final RecyclerView recyclerView, final MainActivity.ClickListener clickListener) {
            this.clickListener = clickListener;
            gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }

                @Override
                public void onLongPress(MotionEvent e) {
                    View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
                    if (child != null && clickListener != null) {
                        clickListener.onLongClick(child, recyclerView.getChildAdapterPosition(child));
                    }
                }
            });
        }

        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
            View child = rv.findChildViewUnder(e.getX(), e.getY());
            if (child != null && clickListener != null && gestureDetector.onTouchEvent(e)) {
                clickListener.onClick(child, rv.getChildAdapterPosition(child));
            }
            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {
        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {
        }
    }


}