package com.example.myapplication;

import io.realm.RealmObject;

public class HealthRedButton extends RealmObject {
    float positionX, positionY;
    String name, context;

    public HealthRedButton(){
        this.name = "empty";
        this.name = "empty";
    }

    public HealthRedButton(HealthRedButton healthRedButton){
        this.positionX = healthRedButton.getPositionX();
        this.positionY = healthRedButton.getPositionY();
        this.context = healthRedButton.context;
        this.name = healthRedButton.name;
    }

    public void setPositionX(float positionX) {
        this.positionX = positionX;
    }

    public float getPositionX() {
        return positionX;
    }

    public void setPositionY(float positionY) {
        this.positionY = positionY;
    }

    public float getPositionY() {
        return positionY;
    }
}
