package com.games.peter.project_live_football_tactics;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;

/**
 * Created by Peter on 22/4/2018.
 */


public class CustomView extends View {

    private int SCREEN_SIZE_WIDTH ;
    private int SCREEN_SIZE_HEIGHT ;
    private ArrayList<Rect> mRectSquares;
    private Paint mPaintSquare_Dark,mPaintSquare_Light,mPaintSquare_Borders;
    private int mSquareColor_Dark,mSquareColor_Light;
    private int mSquareSize;
    private int no_of_Rects;
    private Rect Pitch_Border;
    private Context context;
    public CustomView(Context context) {
        super(context);

        init(null,context);
    }

    public CustomView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs,context);
    }

    public CustomView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init(attrs,context);
    }

    public CustomView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);

        init(attrs,context);
    }
//    private int getNavBarHeight() {
//        int result = 0;
//        boolean hasMenuKey = ViewConfiguration.get(context).hasPermanentMenuKey();
//        boolean hasBackKey = KeyCharacterMap.deviceHasKey(KeyEvent.KEYCODE_BACK);
//
//        if(!hasMenuKey && !hasBackKey) {
//            //The device has a navigation bar
//            Resources resources = context.getResources();
//
//            int orientation = resources.getConfiguration().orientation;
//            int resourceId;
//            if (isTablet(context)){
//                resourceId = resources.getIdentifier(orientation == Configuration.ORIENTATION_PORTRAIT ? "navigation_bar_height" : "navigation_bar_height_landscape", "dimen", "android");
//            }  else {
//                resourceId = resources.getIdentifier(orientation == Configuration.ORIENTATION_PORTRAIT ? "navigation_bar_height" : "navigation_bar_width", "dimen", "android");
//            }
//
//            if (resourceId > 0) {
//                return resources.getDimensionPixelSize(resourceId);
//            }
//        }
//        return result;
//    }
//
//
//    private boolean isTablet(Context c) {
//        return (c.getResources().getConfiguration().screenLayout
//                & Configuration.SCREENLAYOUT_SIZE_MASK)
//                >= Configuration.SCREENLAYOUT_SIZE_LARGE;
//    }
    private void init(@Nullable AttributeSet set,Context context) {
        this.context=context;
        mRectSquares=new ArrayList<>();
        mPaintSquare_Borders = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaintSquare_Dark = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaintSquare_Light = new Paint(Paint.ANTI_ALIAS_FLAG);
        SCREEN_SIZE_WIDTH = Resources.getSystem().getDisplayMetrics().widthPixels;
        //SCREEN_SIZE_HEIGHT = Resources.getSystem().getDisplayMetrics().heightPixels;
        SCREEN_SIZE_HEIGHT = 1800;
        no_of_Rects=(int)Math.ceil(SCREEN_SIZE_HEIGHT/100);
        InitRects(no_of_Rects);
        if (set == null)
            return;

        TypedArray ta = getContext().obtainStyledAttributes(set, R.styleable.CustomView);

        mSquareColor_Dark = ta.getColor(R.styleable.CustomView_square_color, getResources().getColor(R.color.pitch_dark_green));
        mSquareColor_Light = ta.getColor(R.styleable.CustomView_square_color, getResources().getColor(R.color.pitch_light_green));
        mSquareSize = ta.getDimensionPixelSize(R.styleable.CustomView_square_size, SCREEN_SIZE_WIDTH);

        mPaintSquare_Dark.setColor(mSquareColor_Dark);
        mPaintSquare_Light.setColor(mSquareColor_Light);
        mPaintSquare_Borders.setColor(ta.getColor(R.styleable.CustomView_square_color, getResources().getColor(R.color.Transparent_white)));
        mPaintSquare_Borders.setStyle(Paint.Style.STROKE);
        mPaintSquare_Borders.setStrokeWidth(6);
        ta.recycle();
    }

    private void InitRects(int no_of_Rects){
        for (int i=0;i<no_of_Rects;i++){ //pitch grass
            mRectSquares.add(new Rect());
        }
        Pitch_Border = new Rect();
    }
    @Override
    protected void onDraw(Canvas canvas) {
        int top=0;
        float PENALTY_BOX_HEIGHT = (float) ((16.5*(SCREEN_SIZE_HEIGHT-40))/120);
        float PENALTY_BOX_WIDTH = (float)((40.3*(SCREEN_SIZE_WIDTH-40)/90));
        float SIX_YARD_HEIGHT = (float) ((PENALTY_BOX_HEIGHT)*5.5/16.5);
        float SIX_YARD_WIDTH = (float)((PENALTY_BOX_WIDTH)*(40.3-22)/40.3);
        float PENALTY_POINT_HEIGHT= (float)(PENALTY_BOX_HEIGHT*11/16.5);
        float PENALTY_POINT_RADIUS = (float) 2.5;
        float PENALTY_ARC_RADUIS = (float) (PENALTY_BOX_HEIGHT*9.15/16.5);
        float CORNER_ARC_RADIUS = (float) (PENALTY_BOX_HEIGHT*1/16.5);
        for (int i=0;i<mRectSquares.size();i++){
            mRectSquares.get(i).left = 0;
            mRectSquares.get(i).top = top;
            mRectSquares.get(i).right = mRectSquares.get(i).left + mSquareSize;
            mRectSquares.get(i).bottom = mRectSquares.get(i).top + 100;
            top=mRectSquares.get(i).bottom;
            if (i%2==0)
                canvas.drawRect(mRectSquares.get(i), mPaintSquare_Dark);
            else
            canvas.drawRect(mRectSquares.get(i), mPaintSquare_Light);
        }
        Pitch_Border.left = 20;
        Pitch_Border.top = 20;
        Pitch_Border.right = SCREEN_SIZE_WIDTH-20;
        Pitch_Border.bottom = SCREEN_SIZE_HEIGHT-20;
        //=====================================
        //PITCH BORDER , CENTER , CORNERS
        canvas.drawRect(Pitch_Border,mPaintSquare_Borders);//pitch border
        canvas.drawLine(20,SCREEN_SIZE_HEIGHT/2,SCREEN_SIZE_WIDTH-20,SCREEN_SIZE_HEIGHT/2+1,mPaintSquare_Borders);//center line
        canvas.drawCircle(SCREEN_SIZE_WIDTH/2,SCREEN_SIZE_HEIGHT/2,PENALTY_POINT_RADIUS,mPaintSquare_Borders);//Center Point
        canvas.drawCircle(SCREEN_SIZE_WIDTH/2,SCREEN_SIZE_HEIGHT/2,PENALTY_ARC_RADUIS,mPaintSquare_Borders);//Center Circle
        canvas.drawArc(20-CORNER_ARC_RADIUS,20-CORNER_ARC_RADIUS,20+CORNER_ARC_RADIUS,20+CORNER_ARC_RADIUS,0,90,false,mPaintSquare_Borders);//top left corner
        canvas.drawArc(SCREEN_SIZE_WIDTH-20-CORNER_ARC_RADIUS,20-CORNER_ARC_RADIUS,SCREEN_SIZE_WIDTH-20+CORNER_ARC_RADIUS,20+CORNER_ARC_RADIUS,90,90,false,mPaintSquare_Borders);//top right corner
        canvas.drawArc(20-CORNER_ARC_RADIUS,SCREEN_SIZE_HEIGHT-20-CORNER_ARC_RADIUS,20+CORNER_ARC_RADIUS,SCREEN_SIZE_HEIGHT-20+CORNER_ARC_RADIUS,270,90,false,mPaintSquare_Borders);//bottom left corner
        canvas.drawArc(SCREEN_SIZE_WIDTH-20-CORNER_ARC_RADIUS,SCREEN_SIZE_HEIGHT-20-CORNER_ARC_RADIUS,SCREEN_SIZE_WIDTH-20+CORNER_ARC_RADIUS,SCREEN_SIZE_HEIGHT-20+CORNER_ARC_RADIUS,180,90,false,mPaintSquare_Borders);//bottom right corner
        //=====================================
        //TOP PENALTY BOX
        canvas.drawRect(SCREEN_SIZE_WIDTH/2-SIX_YARD_WIDTH/2,20,SCREEN_SIZE_WIDTH/2+SIX_YARD_WIDTH/2,20+SIX_YARD_HEIGHT,mPaintSquare_Borders);//top 6yard area
        canvas.drawRect((SCREEN_SIZE_WIDTH/2-PENALTY_BOX_WIDTH/2),20,(SCREEN_SIZE_WIDTH/2+PENALTY_BOX_WIDTH/2), (20+PENALTY_BOX_HEIGHT),mPaintSquare_Borders);//top box area
        canvas.drawOval((SCREEN_SIZE_WIDTH/2-PENALTY_POINT_RADIUS),20+PENALTY_POINT_HEIGHT-PENALTY_POINT_RADIUS,(SCREEN_SIZE_WIDTH/2+PENALTY_POINT_RADIUS),20+PENALTY_POINT_HEIGHT+PENALTY_POINT_RADIUS,mPaintSquare_Borders);//top penalty point
        canvas.drawArc((SCREEN_SIZE_WIDTH/2-PENALTY_ARC_RADUIS),20+PENALTY_POINT_HEIGHT-PENALTY_ARC_RADUIS,(SCREEN_SIZE_WIDTH/2)+PENALTY_ARC_RADUIS,20+PENALTY_POINT_HEIGHT+PENALTY_ARC_RADUIS,35,110,false,mPaintSquare_Borders);//top penalty arc

        //=====================================
        //BOTTOM PENALTY BOX
        canvas.drawRect(SCREEN_SIZE_WIDTH/2-SIX_YARD_WIDTH/2,SCREEN_SIZE_HEIGHT-20-SIX_YARD_HEIGHT,SCREEN_SIZE_WIDTH/2+SIX_YARD_WIDTH/2,SCREEN_SIZE_HEIGHT-20,mPaintSquare_Borders);//bottom 6yard area
        canvas.drawRect((SCREEN_SIZE_WIDTH/2-PENALTY_BOX_WIDTH/2),(SCREEN_SIZE_HEIGHT-20-PENALTY_BOX_HEIGHT),(SCREEN_SIZE_WIDTH/2+PENALTY_BOX_WIDTH/2), (SCREEN_SIZE_HEIGHT-20),mPaintSquare_Borders);//bottom box area
        canvas.drawOval((SCREEN_SIZE_WIDTH/2-PENALTY_POINT_RADIUS),SCREEN_SIZE_HEIGHT-20-PENALTY_POINT_HEIGHT-PENALTY_POINT_RADIUS,(SCREEN_SIZE_WIDTH/2+PENALTY_POINT_RADIUS),SCREEN_SIZE_HEIGHT-20-PENALTY_POINT_HEIGHT+PENALTY_POINT_RADIUS,mPaintSquare_Borders);//bottom penalty point
        canvas.drawArc((SCREEN_SIZE_WIDTH/2-PENALTY_ARC_RADUIS),SCREEN_SIZE_HEIGHT-20-PENALTY_POINT_HEIGHT-PENALTY_ARC_RADUIS,(SCREEN_SIZE_WIDTH/2)+PENALTY_ARC_RADUIS,SCREEN_SIZE_HEIGHT-20-PENALTY_POINT_HEIGHT+PENALTY_ARC_RADUIS,215,110,false,mPaintSquare_Borders);//bottom penalty arc

        //=====================================
    }
}
