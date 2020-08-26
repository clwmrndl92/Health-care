package com.example.myapplication;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Point;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmResults;
import newQuickAction.ActionItem;
import newQuickAction.QuickAction;

public class HealthActivity extends AppCompatActivity implements View.OnTouchListener{

    public static RealmResults<HealthRedButton> hRealmResults;
    public static RealmList<HealthRedButton> hRealmList;
    static Realm healthRealm = Realm.getInstance(MyApplication.healthConfig);

    static public int buttonCount = 0;

    QuickAction quickAction;
    public static class ActionEnum{
        public static final int health = 0;
        public static final int delete = 1;
    }

    RelativeLayout.LayoutParams params;

    public static boolean moving = false;
    public static Point ScreenSize = new Point();

    private RelativeLayout layout;
    float oldXvalue;
    float oldYvalue;



    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_health);
        layout = (RelativeLayout)findViewById(R.id.dynamic_button_layout);
        params = new RelativeLayout.LayoutParams(convertDpToPixel(20), convertDpToPixel(20));

        hRealmResults= healthRealm.where(HealthRedButton.class).findAllSorted("id");
        hRealmList = new RealmList<>();
        for (HealthRedButton redButton : hRealmResults) {
            hRealmList.add(new HealthRedButton(redButton));
        }
        if(hRealmList.size() != 0) {
            buttonCount = hRealmList.last().getId();
        }

        moving = false;
        Display display = getWindowManager().getDefaultDisplay();  // in Activity
        /* getActivity().getWindowManager().getDefaultDisplay() */ // in Fragment
        display.getRealSize(ScreenSize); // or getSize(size)



        quickAction = new QuickAction(this, QuickAction.VERTICAL);
        final ActionItem healthItem = new ActionItem(ActionEnum.health, "empty");
        ActionItem deleteItem = new ActionItem(ActionEnum.delete,"delete");
        quickAction.setOnActionItemClickListener(new QuickAction.OnActionItemClickListener() {
            @Override
            public void onItemClick(QuickAction source, int pos, int actionId) {
                //here we can filter which action item was clicked with pos or actionId parameter
                //healthItem.setTitle(redButton.getName());
                ActionItem actionItem = quickAction.getActionItem(pos);
                int location = searchItemById(quickAction.getHealthViewID());
                healthItem.setTitle(hRealmList.get(location).getName());
                if (actionItem.getActionId() == ActionEnum.health) {
                    Intent intent = new Intent(getBaseContext(), HealthContentActivity.class);
                    intent.putExtra("pos", location);
                    startActivity(intent);
                }
                else if(actionItem.getActionId() == ActionEnum.delete){
                    layout.removeView(findViewById(quickAction.getHealthViewID()));
                    hRealmList.remove(location);
                }
            }
        });
        quickAction.addActionItem(healthItem);
        quickAction.addActionItem(deleteItem);

        init();


    }

    void init(){
        for(HealthRedButton redButton : hRealmList){
            displayButton(redButton);
        }
    }

    public Button displayButton(final HealthRedButton redButton){
        final Button dynamicButton = new Button(this);
//        HealthFlateActivity redButton = new HealthFlateActivity(getApplicationContext());
        dynamicButton.setBackground(getDrawable(R.drawable.red_button));
        dynamicButton.setLayoutParams(params);
        dynamicButton.setOnTouchListener(this);
        dynamicButton.setId(redButton.getId());
        layout.addView(dynamicButton);
        dynamicButton.setX(redButton.getPositionX());
        dynamicButton.setY(redButton.getPositionY());
        dynamicButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), " button click", Toast.LENGTH_SHORT).show();
                if(!moving){
                    quickAction.show(v);
                }
            }
        });

        return dynamicButton;
    }

    HealthRedButton AddButton(){
        HealthRedButton redButton = new HealthRedButton();
        buttonCount++;
        hRealmList.add(redButton);
        return redButton;
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
                int pos = searchItemById(v.getId());
                hRealmList.get(pos).setPositionX(v.getX());
                hRealmList.get(pos).setPositionY(v.getY());
            }
            return true;
        }
        return false;
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

    public int convertDpToPixel(float dp){
        Resources resources = this.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        int px = (int) (dp * ((float)metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT));
        return px;
    }
    @Override
    public void onBackPressed() {
        saveHealth();
        super.onBackPressed();
    }


    public void pushButton(View v){
        displayButton(AddButton());
    }

    public void saveHealth(){
        healthRealm.beginTransaction();
        healthRealm.deleteAll();
        for(HealthRedButton redButton : hRealmList){
            healthRealm.copyToRealm(redButton);
        }
        healthRealm.commitTransaction();
    }

    public static int searchItemById(int id){
        int i, j;
        i = 0;
        j = hRealmList.size();
        int pos;
        pos = j/2;
        int val = 0;
        while(j != i){
            val = hRealmList.get(pos).getId();
            if(val < id){
                i = pos + 1;
                pos = (i + j) / 2;
            }
            else if (val > id){
                j = pos;
                pos = pos / 2;
            }
            else{
                return pos;
            }
        }
        return -1 * pos;
    }
}
