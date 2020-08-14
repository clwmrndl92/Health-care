package com.example.myapplication;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;


import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmResults;
import io.realm.Sort;

public class HealthActivity extends AppCompatActivity implements View.OnTouchListener{

    public static RealmResults<HealthRedButton> hRealmResults;
    public static RealmList<HealthRedButton> hRealmList;
    static Realm realm = Realm.getInstance(MyApplication.healthConfig);


    RelativeLayout.LayoutParams params;

    public static boolean moving = false;

    private RelativeLayout layout;
    float oldXvalue;
    float oldYvalue;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_health);
        layout = (RelativeLayout)findViewById(R.id.dynamic_button_layout);
        params = new RelativeLayout.LayoutParams(convertDpToPixel(20), convertDpToPixel(20));
        params.addRule(RelativeLayout.CENTER_IN_PARENT);

        final Realm healthRealm = Realm.getInstance(MyApplication.healthConfig);
        hRealmResults= healthRealm.where(HealthRedButton.class).findAll();
        hRealmList = new RealmList<>();

        for (HealthRedButton redButton : hRealmResults) {
            hRealmList.add(new HealthRedButton(redButton));
        }

    }

    public void pushButton(View view) {
        final Button dynamicButton = new Button(this);
//        HealthFlateActivity redButton = new HealthFlateActivity(getApplicationContext());;
        dynamicButton.setId(hRealmList.size());
        dynamicButton.setBackground(getDrawable(R.drawable.red_button));
        dynamicButton.setLayoutParams(params);
        dynamicButton.setOnTouchListener(this);
        layout.addView(dynamicButton);
//        final QuickAction quickAction = new QuickAction(this, QuickAction.VERTICAL);
//        ActionItem nextItem = new ActionItem(dynamicButton.getId(),"empty");
//        quickAction.setOnActionItemClickListener(new QuickAction.OnActionItemClickListener() {
//            @Override
//            public void onItemClick(QuickAction source, int pos, int actionId) {
//                //here we can filter which action item was clicked with pos or actionId parameter
//                ActionItem actionItem = quickAction.getActionItem(pos);
//
//                Toast.makeText(getApplicationContext(), actionItem.getTitle() + " selected", Toast.LENGTH_SHORT).show();
//            }
//        });
//        quickAction.addActionItem(nextItem);
//        dynamicButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if(!moving){
//                    quickAction.show(v);
//                }
//            }
//        });
    }


    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if(moving){
            int width = ((ViewGroup) v.getParent()).getWidth() - v.getWidth();
            int height = ((ViewGroup) v.getParent()).getHeight() - v.getHeight();

            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                oldXvalue = event.getX();
                oldYvalue = event.getY();
                //  Log.i("Tag1", "Action Down X" + event.getX() + "," + event.getY());
                Log.i("Tag1", "Action Down rX " + event.getRawX() + "," + event.getRawY());
            } else if (event.getAction() == MotionEvent.ACTION_MOVE) {
                v.setX(event.getRawX() - oldXvalue);
                v.setY(event.getRawY() - (oldYvalue + v.getHeight()));
                //  Log.i("Tag2", "Action Down " + me.getRawX() + "," + me.getRawY());
            } else if (event.getAction() == MotionEvent.ACTION_UP) {

                if (v.getX() > width && v.getY() > height) {
                    v.setX(width);
                    v.setY(height);
                } else if (v.getX() < 0 && v.getY() > height) {
                    v.setX(0);
                    v.setY(height);
                } else if (v.getX() > width && v.getY() < 0) {
                    v.setX(width);
                    v.setY(0);
                } else if (v.getX() < 0 && v.getY() < 0) {
                    v.setX(0);
                    v.setY(0);
                } else if (v.getX() < 0 || v.getX() > width) {
                    if (v.getX() < 0) {
                        v.setX(0);
                        v.setY(event.getRawY() - oldYvalue - v.getHeight());
                    } else {
                        v.setX(width);
                        v.setY(event.getRawY() - oldYvalue - v.getHeight());
                    }
                } else if (v.getY() < 0 || v.getY() > height) {
                    if (v.getY() < 0) {
                        v.setX(event.getRawX() - oldXvalue);
                        v.setY(0);
                    } else {
                        v.setX(event.getRawX() - oldXvalue);
                        v.setY(height);
                    }
                }
            }
        }
        return true;
    }

    public void MoveOnClick(View view){
        if(moving) {
            moving = false;
            view.setSelected(false);
        }
        else{
            moving = true;
            view.setSelected(true);
        }
    }

    public void RedButtonOnClick(View view){
        if(!moving) {
            moving = false;
            view.setSelected(false);
        }
    }

    public int convertDpToPixel(float dp){
        Resources resources = this.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        int px = (int) (dp * ((float)metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT));
        return px;
    }


}
