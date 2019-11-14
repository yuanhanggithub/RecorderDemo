//package com.etv.view;
//
//import android.content.Context;
//import android.graphics.Canvas;
//import android.graphics.Paint;
//import android.graphics.Path;
//import android.graphics.PixelFormat;
//import android.graphics.PorterDuff;
//import android.graphics.PorterDuffXfermode;
//import android.util.AttributeSet;
//import android.view.MotionEvent;
//import android.view.SurfaceHolder;
//import android.view.SurfaceView;
//
//public class PaintView extends SurfaceView implements SurfaceHolder.Callback, Runnable {
//
//    private Paint mPaint = null;
//    private Path mPath = null;
//    // 线程结束标志位
//    boolean mLoop = true;
//    SurfaceHolder mSurfaceHolder = null;
//    Canvas mCanvas;
//
//    public PaintView(Context context, AttributeSet arr) {
//        super(context, arr);
//        mSurfaceHolder = this.getHolder();//获取holder
//        mSurfaceHolder.addCallback(this);
//        mSurfaceHolder.setFormat(PixelFormat.OPAQUE);//不透明
//        this.setFocusable(true);
//        init();
//    }
//
//    private void init() {
//        mPaint = new Paint();
//        mPath = new Path();
//        mPaint.setAntiAlias(true);
//        mPaint.setStyle(Paint.Style.STROKE);
//        mPaint.setStrokeWidth(5);
//        mPaint.setColor(COLOR_BLUE);
//        mPaint.setStrokeCap(Paint.Cap.ROUND);  //圆头
//        mPaint.setDither(true);//消除拉动，使画面圓滑
//        mPaint.setStrokeJoin(Paint.Join.ROUND); //结合方式，平滑
//    }
//
//    @Override
//    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
//    }
//
//    @Override
//    public void surfaceCreated(SurfaceHolder holder) {
//        new Thread(this).start();//启动线程
//    }
//
//
//    @Override
//    public void surfaceDestroyed(SurfaceHolder holder) {
//        mLoop = false; //结束线程
//    }
//
//
//    @Override
//    public void run() {
//        while (mLoop == true) {
//            Draw();
//            try {
//                Thread.sleep(50);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }
//    }
//
//    //透明00FFFFFF
//    int COLOR_WHITE = 0x00FFFFFF;
//    int COLOR_BLUE = 0xff3F51B5;
//
//    // 绘图
//    private void Draw() {
//        try {
//            mCanvas = mSurfaceHolder.lockCanvas();
//            mCanvas.drawColor(COLOR_WHITE);
//            mCanvas.drawPath(mPath, mPaint);
//        } catch (Exception e) {
//        } finally {
//            if (mCanvas != null) {
//                mSurfaceHolder.unlockCanvasAndPost(mCanvas);
//            }
//        }
//    }
//
//    @Override
//    public boolean onTouchEvent(MotionEvent event) {
//        switch (event.getAction()) {
//            case MotionEvent.ACTION_DOWN:
//                touchDown(event);
//                break;
//            case MotionEvent.ACTION_MOVE:
//                touchMove(event);
//        }
//        //更新绘制
//        return true;
//    }
//
//    private float defaultX;
//    private float defaultY;
//
//    //手指点下屏幕时调用
//    private void touchDown(MotionEvent event) {
//        float xDown = event.getX();
//        float yDown = event.getY();
//        defaultX = xDown;
//        defaultY = yDown;
//        //mPath绘制的绘制起点
//        mPath.moveTo(xDown, yDown);
//    }
//
//    //手指在屏幕上滑动时调用
//    private void touchMove(MotionEvent event) {
//        float xUp = event.getX();
//        float yUp = event.getY();
//        float middleX = (xUp + defaultX) / 2;
//        float middleY = (yUp + defaultY) / 2;
//        mPath.quadTo(defaultX, defaultY, middleX, middleY);
//        defaultX = xUp;
//        defaultY = yUp;
//        invalidate();
//    }
//
//    private void setEraser() {
//        mPaint = new Paint();
//        mPath = new Path();
//        mPaint.setAntiAlias(true);
//        mPaint.setStyle(Paint.Style.STROKE);
//        mPaint.setStrokeWidth(50);
//        mPaint.setStrokeCap(Paint.Cap.ROUND);  //圆头
//        mPaint.setDither(true);//消除拉动，使画面圓滑
//        mPaint.setStrokeJoin(Paint.Join.ROUND); //结合方式，平滑
//        mPaint.setAlpha(0); ////
//        mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
//    }
//}
