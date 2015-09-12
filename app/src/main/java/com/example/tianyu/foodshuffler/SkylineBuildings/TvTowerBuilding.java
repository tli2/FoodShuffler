package com.example.tianyu.foodshuffler.SkylineBuildings;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;

/**
 * Created by Billy on 9/12/2015.
 */
public class TvTowerBuilding extends SkylineBuilding {

    private float ellipseHeight;
    private float ellipseWidth;

    public TvTowerBuilding() {
        super();
    }

    public TvTowerBuilding(int stdHeight, int stdWidth, Paint paint) {
        super(stdHeight, stdWidth, paint);
        ellipseWidth = getWidth() * 2;
        ellipseHeight = ellipseWidth * 3f/4f;
    }

    @Override
    public void initDimen(int stdHeight, int stdWidth, Paint paint) {
        super.initDimen(stdHeight, stdWidth, paint);
        ellipseWidth = getWidth() * 2;
        ellipseHeight = ellipseWidth * 3f/4f;
    }

    @Override
    public void draw(Canvas canvas, float xCenter, float yBottom) {
        float rectTop = yBottom - getHeight();
        float rectLeft = xCenter - getWidth()/2f;
        float rectRight = rectLeft + getWidth();
        Path triangle = new Path();
        triangle.moveTo(rectLeft, yBottom);
        triangle.lineTo(rectRight, yBottom);
        triangle.lineTo(xCenter, rectTop);
        triangle.close();
        canvas.drawPath(triangle, getPaint());

        float ovalLeft = xCenter - 0.5f*ellipseWidth;
        float ovalRight = ovalLeft + ellipseWidth;
        float ovalTop = rectTop + getHeight()/3.5f;
        float ovalBottom = ovalTop + ellipseHeight;
        Path ellipse = new Path();
        RectF ovalRect = new RectF(ovalLeft, ovalTop, ovalRight, ovalBottom);
        ellipse.addOval(ovalRect, Path.Direction.CCW);
        canvas.drawPath(ellipse, getPaint());
    }

    @Override
    public float getHeightMultiplier() {
        return 1.2f;
    }
    @Override
    public float getWidthMultiplier() {
        return 1;
    }

}
