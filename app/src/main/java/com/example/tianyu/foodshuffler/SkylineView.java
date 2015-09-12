package com.example.tianyu.foodshuffler;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.example.tianyu.foodshuffler.SkylineBuildings.SkylineBuilding;
import com.example.tianyu.foodshuffler.SkylineBuildings.TvTowerBuilding;
import com.example.tianyu.foodshuffler.SkylineBuildings.WashMonumentBuilding;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Billy on 9/12/2015.
 */
public class SkylineView extends View {

    private final String LOG_TAG = SkylineView.class.getSimpleName();

    private int height;
    private int width;
    private float earthCenterX;
    private float earthCenterY;
    private float earthSurfaceY;
    private Paint buildingPaint = new Paint();

    private float speed = 0.02f;
    private int stdHeight;
    private int stdWidth = 160;
    private int buildingPadding = 32; //padding between buildings
    private float unitRotationOffset;
    private float unitRotationOffsetPadding;

    private boolean measured = false;
    private static Random random = new Random();

    private ArrayList<Class<? extends SkylineBuilding>> availBuildings = new ArrayList<>();
    private ArrayList<SkylineBuilding> buildings = new ArrayList<>();

    public static final float ROTATION_START = -55;
    public static final float ROTATION_END = 55;

    public SkylineView(Context context, AttributeSet attrs) {
        super(context, attrs);
        buildingPaint.setColor(getResources().getColor(R.color.building_grey));
    }

    private void initAvailBuildings() {
        availBuildings.add(WashMonumentBuilding.class);
        availBuildings.add(TvTowerBuilding.class);
    }

    private void initBuildings() {
        addBuilding();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        height = h;
        width = w;
        Log.d(LOG_TAG, "canvas height: " + height + ", width: " + width);
    }

    private void getMeasurements() {
        earthCenterX = width / 2f;
        earthSurfaceY = height - (width/3.6f);
        earthCenterY = earthSurfaceY + width;
        measured = true;

        stdHeight = height / 2;
        unitRotationOffset = (float) (2 * Math.toDegrees(Math.atan2(stdWidth / 2, width)));
        unitRotationOffsetPadding = (float) (2 * Math.toDegrees(Math.atan2(buildingPadding,width)));

        initAvailBuildings();
        initBuildings();
    }

    private void drawBuilding(Canvas canvas, SkylineBuilding b) {
        b.incrementRotationbySpeed(speed);
        float rotation = b.getRotation();
        canvas.rotate(rotation, earthCenterX, earthCenterY);
        b.draw(canvas, earthCenterX, earthSurfaceY);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if(!measured) getMeasurements();

        canvas.drawColor(getResources().getColor(R.color.main_background));

        for(int i = 0; i < buildings.size(); i++) {
            canvas.save();
            SkylineBuilding b = buildings.get(i);
            drawBuilding(canvas, b);
            canvas.restore();
            if(i == 0 && b.tailRotation(unitRotationOffset, unitRotationOffsetPadding) >= ROTATION_END) {
                // this building is off screen, should be deleted
                buildings.remove(i);
                Log.d(LOG_TAG, "Building--: " + buildings.size());
            }
        }
        if (buildings.size() != 0 && buildings.get(buildings.size()-1)
                .tailRotation(unitRotationOffset, unitRotationOffsetPadding) >= ROTATION_START)
            addBuilding();

        invalidate();
    }

    private void addBuilding() {
        int index = random.nextInt(availBuildings.size());
        try {
            SkylineBuilding building = availBuildings.get(index).newInstance();
            building.initDimen(stdHeight, stdWidth, buildingPaint);
            buildings.add(building);
            Log.d(LOG_TAG, "Building++: " + buildings.size());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
