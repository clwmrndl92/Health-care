package com.example.myapplication;

import io.realm.RealmObject;

import static com.example.myapplication.HealthActivity.ScreenSize;

public class HealthRedButton extends RealmObject {
    float positionX, positionY;
    String name, content;
    int id;

    public HealthRedButton(){
        this.name = "empty";
        this.content = "content";
        this.id = HealthActivity.buttonCount;
        this.positionX = ScreenSize.x/2;
        this.positionY = ScreenSize.y/2;
    }

    public HealthRedButton(HealthRedButton healthRedButton){
        this.positionX = healthRedButton.getPositionX();
        this.positionY = healthRedButton.getPositionY();
        this.content = healthRedButton.content;
        this.name = healthRedButton.name;
        this.id = healthRedButton.id;
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

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    public int getId() {
        return id;
    }

}
