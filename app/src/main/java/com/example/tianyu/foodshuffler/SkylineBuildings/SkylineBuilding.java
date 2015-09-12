package com.example.tianyu.foodshuffler.SkylineBuildings;

import android.graphics.Canvas;
import android.graphics.Paint;

import com.example.tianyu.foodshuffler.SkylineView;

/**
 * Created by Billy on 9/12/2015.
 */
public abstract class SkylineBuilding {

    private float heightMultiplier;
    private float widthMultiplier;
    private float height;
    private float width;
    private Paint paint;

    private float rotation = SkylineView.ROTATION_START;

    public SkylineBuilding() {}

    public SkylineBuilding(int stdHeight, int stdWidth, Paint paint) {
        initDimen(stdHeight, stdWidth, paint);
    }

    public void initDimen(int stdHeight, int stdWidth, Paint paint) {
        height = stdHeight * getHeightMultiplier();
        width = stdWidth * getWidthMultiplier();
        this.paint = paint;
    }

    public abstract void draw(Canvas canvas, float xCenter, float yBottom);

    public float getHeight() {
        return height;
    }
    public float getWidth() {
        return width;
    }
    public Paint getPaint() {
        return paint;
    }
    public float getHeightMultiplier() {
        return heightMultiplier;
    }
    public float getWidthMultiplier() {
        return widthMultiplier;
    }

    public float getRotation() {
        return rotation;
    }
    public void incrementRotationbySpeed(float speed) {
        rotation += speed;
    }
    public float tailRotation(float offset, float offsetPadding) {
        return getRotation() - (getWidthMultiplier() * offset + offsetPadding);
    }
}
