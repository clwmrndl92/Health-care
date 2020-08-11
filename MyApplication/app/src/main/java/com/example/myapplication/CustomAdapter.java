package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import io.realm.RealmList;


public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.CustomViewHolder> {

    private RealmList<Memo> mList;

    public class CustomViewHolder extends RecyclerView.ViewHolder {
        protected TextView title;
        protected CheckBox check;


        public CustomViewHolder(View view) {
            super(view);
            this.title = (TextView) view.findViewById(R.id.main_listitem);
            this.check = (CheckBox) view.findViewById(R.id.check_box);
        }
    }


    public CustomAdapter(RealmList<Memo> list) {
        this.mList = list;
    }



    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_list, viewGroup, false);

        CustomViewHolder viewHolder = new CustomViewHolder(view);

        return viewHolder;
    }




    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder viewholder, int position) {

        viewholder.title.setTextSize(TypedValue.COMPLEX_UNIT_SP, 25);
        viewholder.title.setGravity(Gravity.CENTER);


        viewholder.title.setText(mList.get(position).getTitle());
        if(MemoListActivity.getSelecting()) {
            viewholder.check.setVisibility(View.VISIBLE);
            viewholder.check.setChecked(mList.get(position).getChecked());
        }
        else{
            viewholder.check.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return (null != mList ? mList.size() : 0);
    }

}