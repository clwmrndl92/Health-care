package com.example.myapplication;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmResults;
import io.realm.Sort;


public class MemoListActivity extends AppCompatActivity {
    public static RealmResults<Memo> mRealmResults;
    public static RealmList<Memo> mRealmList;
    static Realm realm = Realm.getInstance(MyApplication.memoConfig);

    private static CustomAdapter mAdapter;
    int count = -1;
    private static boolean selecting = false;

    public static boolean getSelecting(){
        return selecting;
    }


    Button buttonInsert, buttonDelete, buttonCancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memo_list);

        buttonInsert = (Button) findViewById(R.id.button_memo_list_insert);
        buttonDelete = (Button) findViewById(R.id.button_memo_list_delete);
        buttonCancel = (Button) findViewById(R.id.button_memo_list_cancel);

        final Realm memoRealm = Realm.getInstance(MyApplication.memoConfig);
        mRealmResults= memoRealm.where(Memo.class).findAllSorted("time", Sort.DESCENDING);
        mRealmList = new RealmList<>();

        for (Memo memo : mRealmResults) {
            mRealmList.add(new Memo(memo));
        }

        RecyclerView mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview_memo_list);
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
                if(!selecting) {
                    Intent intent = new Intent(getBaseContext(), MemoContent.class);

                    intent.putExtra("pos", position);

                    startActivity(intent);
                }
                else{
                    memo.setMemoCheck(!memo.getChecked());
                    mAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onLongClick(View view, int position) {
                Memo memo = mRealmList.get(position);
                if(!selecting) {
                    selecting = true;
                    memo.setMemoCheck(true);
                    mAdapter.notifyDataSetChanged();
                    buttonDelete.setVisibility(View.VISIBLE);
                    buttonCancel.setVisibility(View.VISIBLE);
                    buttonInsert.setVisibility(View.GONE);
                }
            }
        }));

        buttonInsert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                count++;

                // Dictionary 생성자를 사용하여 RealmList에 삽입할 데이터를 만듭니다.
                Memo data = new Memo();

                realm.beginTransaction();
                realm.copyToRealm(data);
                realm.commitTransaction();

                mRealmList.add(0, data); //RecyclerView의 첫 줄에 삽입
                //mRealmList.add(data); // RecyclerView의 마지막 줄에 삽입

                mAdapter.notifyItemInserted(0);
                //mAdapter.notifyDataSetChanged(); //변경된 데이터를 화면에 반영

                //Toast.makeText(getApplicationContext(), memo.getTitle(), Toast.LENGTH_LONG).show();
                Intent intent = new Intent(getBaseContext(), MemoContent.class);

                intent.putExtra("pos", 0);

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
        private MemoListActivity.ClickListener clickListener;

        public RecyclerTouchListener(Context context, final RecyclerView recyclerView, final MemoListActivity.ClickListener clickListener) {
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

    public void deleteData(View view){
        for(int i = mRealmList.size()-1; i >= 0; --i){
            Memo memo = mRealmList.get(i);
            if(memo.getChecked()){
                mRealmList.remove(i);
                realm.beginTransaction();
                mRealmResults.deleteFromRealm(i);
                realm.commitTransaction();
            }
        }
        cancel(view);
    }
    public void cancel(View view){
        for(Memo memo:mRealmList){
            memo.setMemoCheck(false);
        }
        selecting = false;
        mAdapter.notifyDataSetChanged();

        buttonInsert.setVisibility(View.VISIBLE);
        buttonDelete.setVisibility(View.GONE);
        buttonCancel.setVisibility(View.GONE);
    }

}