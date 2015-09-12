package com.example.tianyu.foodshuffler.SkylineBuildings;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;

/**
 * Created by Billy on 9/12/2015.
 */
public class WashMonumentBuilding extends SkylineBuilding {

    private float triangleHeight;

    public WashMonumentBuilding() {
        super();
    }

    public WashMonumentBuilding(int stdHeight, int stdWidth, Paint paint) {
        super(stdHeight, stdWidth, paint);
        triangleHeight = getWidth();
    }

    @Override
    public void initDimen(int stdHeight, int stdWidth, Paint paint) {
        super.initDimen(stdHeight, stdWidth, paint);
        triangleHeight = getWidth();
    }

    @Override
    public void draw(Canvas canvas, float xCenter, float yBottom) {
        float rectTop = yBottom - getHeight() + triangleHeight;
        float rectLeft = xCenter - getWidth()/2f;
        float rectRight = rectLeft + getWidth();
        canvas.drawRect(rectLeft, rectTop, rectRight, yBottom, getPaint());

        Path triangle = new Path();
        triangle.moveTo(rectLeft, rectTop + 1);
        triangle.lineTo(rectRight, rectTop + 1);
        triangle.lineTo(xCenter, rectTop - triangleHeight + 1);
        triangle.close();
        canvas.drawPath(triangle, getPaint());
    }

    @Override
    public float getHeightMultiplier() {
        return 1;
    }
    @Override
    public float getWidthMultiplier() {
        return 1;
    }
}
