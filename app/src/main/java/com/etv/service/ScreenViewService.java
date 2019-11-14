package com.etv.service;

import android.Manifest;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.media.AudioManager;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Binder;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.storage.StorageManager;
import android.support.v4.app.ActivityCompat;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.android.internal.telephony.ITelephony;
import com.etv.config.AppInfo;
import com.etv.util.Biantai;
import com.etv.util.FileUtil;
import com.etv.util.MyLog;
import com.etv.util.PlayUtil;
import com.etv.util.SharedPerManager;
import com.etv.util.ViewSizeChange;
import com.etv.util.system.SystemManagerUtil;
import com.ys.etv.R;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import static com.etv.config.AppInfo.SD_INSERT;
import static com.etv.config.AppInfo.SD_PULLOUT;

/***
 * 录像悬浮窗
 * 1：屏幕变小，开始录像
 * 2：开始打电话，停止录像，压缩时视频
 */

public class ScreenViewService extends Service implements SurfaceHolder.Callback {

    private static final String TAG = "ScreenViewService";
    private static final int EXIT_APP =0X11 ;
    private static String FILEA_PATH;
    private String url_file;
    private WindowManager mWindowManager;
    private WindowManager.LayoutParams mWmParams;
    private boolean isCallIng = false;   //是否正在通话
    private boolean isCallFrom = false;  //来电
    public static final int TYPE = 1;
    private Camera mCamera;
    private AudioManager audioManager;

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            MyLog.cdl("==========BroadcastReceiver======" + action);
            if (action.equals(AppInfo.ONE_KEY_POLICE_BROAD)) {  //一键报警功能
                if (Biantai.isFourClick()) {
                    dealCallPolice();
                }
            } else if (action.equals(AppInfo.HAND_UP_PHONE)) {  //挂断电话
                setViewChange(false);
                isCallIng = false;
                isCallFrom = false;
            } else if (action.equals(AppInfo.CALL_FROM_THER)) { //来电
                isCallIng = true;
                isCallFrom = true;
                updateCallView();
            } else if (action.equals(AppInfo.CALL_TO_PHONE_RING)) { //去电
                isCallIng = true;
            }else if (action.equals(AppInfo.HAND_UP_LONG_PHONE)) { //长按挂掉电话
                Log.i(TAG,"HAND_UP_LONG_PHONE=="+isCallIng);
                if (isCallIng){
                    setViewChange(false);
                    clickBunNum = 0;
                    endPhone(ScreenViewService.this,(TelephonyManager) context.getSystemService(Service.TELEPHONY_SERVICE));
                }
//                else {
//                    setViewChange(false);
//                    isCallIng = false;
//                    isCallFrom = false;
//                    stopRecorder();
//                    Toast.makeText(ScreenViewService.this,"视频录制停止",Toast.LENGTH_SHORT).show();
//                }
            }else if (action.equals(SD_PULLOUT)){
            Log.i("BootBroadcastReceiver","action========"+SD_PULLOUT);
            if (!FileUtil.getPath(FILEA_PATH))
                openCamera();
            }else if (action.equals(SD_INSERT)){
                if (mRecorder==null) {
                    mCamera.release();
                    mCamera = null;
                    startRecord();
                }
        }
        }
    };

    /**
     * 更新来电
     */
    private void updateCallView() {
        setViewChange(true);
        tv_desc_show.setText("来电:110");
    }

    PlayUtil playUtil;

    private static final int NUM_DEFAULT = 0;  //空闲状态
    private static final int NUM_PLAY_NOTIFY = 1;  //播放提示音状态
    private static final int NUM_PLAY_CALLING = 2;  //拨打电话状态

    int clickBunNum = NUM_DEFAULT;

    private void dealCallPolice() {
//        if (Biantai.isFourClick()) {
//            Log.i("yuanhang","isOneClick==");
//            return;
//        }
        if (isCallFrom) {
            MyLog.cdl("===========000=====来电，接听电话");
            acceptCall();
            clickBunNum = NUM_PLAY_CALLING;
            return;
        }
        if (playUtil == null) {
            playUtil = new PlayUtil(ScreenViewService.this);
        }
        if (clickBunNum == NUM_DEFAULT) {  //播放提示音
            setViewChange(true);
            playUtil.playMusic();
                tv_desc_show.setText("确认报警,请再按一次");
            startTimer();
            clickBunNum++;
        } else if (clickBunNum == NUM_PLAY_NOTIFY) { //开始拨号
            boolean ishasSimCard = SystemManagerUtil.ishasSimCard(ScreenViewService.this);
            if (!ishasSimCard) {
                tv_desc_show.setText("当前没有SIM卡信息!");
                cacelTimer();
                if (isSizeBig) {
                    handler.sendEmptyMessageDelayed(MESSAGE_CACEL_POLICE, 3000);
                }
                return;
            }
            playUtil.stopMusic();
            tv_desc_show.setText("正在呼叫: 110");
            makePhoneToPolice();
            clickBunNum++;
        }
//        else if (clickBunNum == NUM_PLAY_CALLING) {
//            handUpCallPhone();
//            setViewChange(false);
//            clickBunNum = 0;
//        }
    }

    public void onCreate() {
        super.onCreate();
        createFloatView();
        initReceiver();
    }

    View viewPop;

    private void createFloatView() {
        mWmParams = new WindowManager.LayoutParams();
        mWindowManager = ((WindowManager) getApplication().getSystemService(Context.WINDOW_SERVICE));
        mWmParams.type = WindowManager.LayoutParams.TYPE_PHONE;
        mWmParams.format = WindowManager.LayoutParams.LAYOUT_CHANGED;
        mWmParams.flags = WindowManager.LayoutParams.FORMAT_CHANGED;
//        mWmParams.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL;
        mWmParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
        mWmParams.height = ViewGroup.LayoutParams.MATCH_PARENT;
        viewPop = LayoutInflater.from(getApplication()).inflate(R.layout.view_camera_pop, null);
        audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
        initView(viewPop);
//        viewPop.setOnKeyListener(new View.OnKeyListener() {
//            @Override
//            public boolean onKey(View view, int i, KeyEvent keyEvent) {
//                Log.i(TAG,"keyenent="+i);
//                return false;
//            }
//        });
//        viewPop.setFocusableInTouchMode(true);
        mWindowManager.addView(viewPop, mWmParams);
        viewPop.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED), View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
    }

    private SurfaceView mSurfaceview;
    private MediaRecorder mRecorder;
    private SurfaceHolder mSurfaceHolder;
    private RelativeLayout rela_view, rela_bgg;
    private TextView tv_desc_show;

    private void initView(View viewPop) {
        tv_desc_show = (TextView) viewPop.findViewById(R.id.tv_desc_show);
        rela_bgg = (RelativeLayout) viewPop.findViewById(R.id.rela_bgg);
        rela_view = (RelativeLayout) viewPop.findViewById(R.id.rela_view);
//        setViewChange(true);
        rela_bgg.setVisibility(View.GONE);
        ViewSizeChange.setGridViewSizeSmall(rela_view);
        mSurfaceview = (SurfaceView) viewPop.findViewById(R.id.surfaceview);
        SurfaceHolder holder = mSurfaceview.getHolder();// 取得holder
        holder.addCallback(this); // holder加入回调接口
        // setType必须设置，要不出错.
        holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
//        if (mRecorder == null) {
//            mRecorder = new MediaRecorder(); // Create MediaRecorder
//        }
    }

    boolean isSizeBig = true;

    public void setViewChange(boolean isBig) {
        isSizeBig = isBig;
        if (isBig) {
            rela_bgg.setVisibility(View.VISIBLE);
            ViewSizeChange.setGridViewSizeBig(ScreenViewService.this,rela_view);
            int max = audioManager.getStreamMaxVolume( AudioManager.STREAM_MUSIC );
            int current = audioManager.getStreamVolume( AudioManager.STREAM_MUSIC );
            Log.i("yuanhang","max="+max+"/"+"current="+current);
            SharedPerManager.setNumber(current);
            audioManager.setStreamVolume(AudioManager.STREAM_MUSIC,max,0);
        } else {
            Intent intent = new Intent();
            intent.setAction(AppInfo.SHRINK_WINDOW);
            sendBroadcast(intent);
            audioManager.setStreamVolume(AudioManager.STREAM_MUSIC,SharedPerManager.getNumber(),0);
            Log.i("yuanhang","SharedPerManager.getNumber()="+SharedPerManager.getNumber());
            handler.sendEmptyMessageDelayed(VIEW_CHANGE_SMALL_POSYLY, 2000);
        }
    }

    private void stopRecorder() {
        try {
            mRecorder.stop();
            mRecorder.reset();
            mRecorder = null;
        } catch (Exception e) {
            MyLog.e("cdl", "====停止录像失败==" + e.toString());
            e.printStackTrace();
        }
    }

    public void startRecord() {
        try {
            if (getSDCardPaths(getApplicationContext()) != null) {
//                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");// HH:mm:ss
//                Date date = new Date(System.currentTimeMillis());
//                String data = simpleDateFormat.format(date);
                File file = new File(FILEA_PATH + "/videokit/");
                Log.i("yuanang","FILEA_PATH=="+FILEA_PATH);
                FileUtil.execFor7("chmod 777 "+FILEA_PATH);
                if (!file.exists()) {
                    file.mkdirs();
                }
                String size = FileUtil.readSDCard(FILEA_PATH, true);
                Double filesize = Double.parseDouble(size.substring(0, size.length() - 2).trim());
                Log.i(TAG, "filesize:=" + filesize);
                if (filesize < 700) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            Date date = new Date();
                            FileUtil.delete(FILEA_PATH + "/videokit", date, TYPE);
                        }
                    }).start();
                }
//            Date d = new Date();
//            String timestamp = String.valueOf(d.getTime());
                long l = System.currentTimeMillis();
                String time = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss").format(Long.valueOf(l));
//        url_file = Environment.getExternalStorageDirectory().getPath() + "/videoKit" + timestamp + ".mp4";
//        url_file = "/mnt/sdcard/videokit/in.mp4";
                url_file = FILEA_PATH + "/videokit/" + time + ".mp4";
                File file1 = new File(url_file);
                if (file1.exists()) {
                    file1.delete();
                }

                file1.createNewFile();
            } else {
                Toast.makeText(getApplicationContext(), "SD卡未插入，请插入SD卡后在录制视频", Toast.LENGTH_LONG).show();
//                handler.sendEmptyMessageDelayed(EXIT_APP,3000);
                openCamera();
                return;
            }
            initCamdera();
                       // Recording is now started
        } catch (Exception e) {
            MyLog.e("cdl", "====录制异常===" + e.toString());
            e.printStackTrace();
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width,
                               int height) {
        mSurfaceHolder = holder;
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        mSurfaceHolder = holder;
        startRecord();
        handler.sendEmptyMessageDelayed(START_KINESCOPE, 1000 * 60*60);
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        mSurfaceview = null;
        mSurfaceHolder = null;
        if (mRecorder != null) {
            mRecorder.release(); // Now the object cannot be reused
            mRecorder = null;
        }
    }

    private final IBinder mBinder = new LocalBinder();

    @Override
    public void onDestroy() {
        super.onDestroy();
        try {
            cacelTimer();
            stopRecorder();
            mWindowManager.removeView(viewPop);
            if (receiver != null) {
                unregisterReceiver(receiver);
            }
        } catch (Exception e) {
            MyLog.e("cdl", "===移除View error==" + e.toString());
        }
    }

    public class LocalBinder extends Binder {
        public ScreenViewService getService() {
            return ScreenViewService.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    private void initReceiver() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(AppInfo.ONE_KEY_POLICE_BROAD);
        filter.addAction(AppInfo.HAND_UP_LONG_PHONE);
        filter.addAction(AppInfo.HAND_UP_PHONE);
        filter.addAction(AppInfo.CALL_FROM_THER);
        filter.addAction(AppInfo.CALL_TO_PHONE_RING);
        registerReceiver(receiver, filter);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(AppInfo.SD_PULLOUT);
        intentFilter.addAction(AppInfo.SD_INSERT);
        intentFilter.addDataScheme("file");
        registerReceiver(receiver,intentFilter);
    }

    private void makePhoneToPolice() {
        try {
            cacelTimer();  //取消计时，停止缩放界面
            String str = "tel:" + SharedPerManager.getPhoneNum();
            Intent intent = new Intent(Intent.ACTION_CALL);
            Uri data = Uri.parse(str);
            intent.setData(data);
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            startActivity(intent);
        } catch (Exception e) {
            MyLog.cdl("=====通话异常===" + e.toString());
            e.printStackTrace();
        }
    }

    public void acceptCall() {
        try {
            Method method = Class.forName("android.os.ServiceManager")
                    .getMethod("getService", String.class);
            IBinder binder = (IBinder) method.invoke(null, new Object[]{Context.TELEPHONY_SERVICE});
            ITelephony telephony = ITelephony.Stub.asInterface(binder);
            telephony.answerRingingCall();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 挂断电话
     */
    public void handUpCallPhone() {
        try {
            // 首先拿到TelephonyManager
            TelephonyManager telMag = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
            Class<TelephonyManager> c = TelephonyManager.class;
            // 再去反射TelephonyManager里面的私有方法 getITelephony 得到 ITelephony对象
            Method mthEndCall = c.getDeclaredMethod("getITelephony", (Class[]) null);
            //允许访问私有方法
            mthEndCall.setAccessible(true);
            final Object obj = mthEndCall.invoke(telMag, (Object[]) null);
            // 再通过ITelephony对象去反射里面的endCall方法，挂断电话
            Method mt = obj.getClass().getMethod("endCall");
            //允许访问私有方法
            mt.setAccessible(true);
            mt.invoke(obj);
            MyLog.cdl("挂断电话");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static final int MESSAGE_CACEL_POLICE = 5467;
    private static final int VIEW_CHANGE_SMALL_POSYLY = 5468;
    private static final int START_KINESCOPE = 0x10;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            handler.removeMessages(msg.what);
            switch (msg.what) {
                case VIEW_CHANGE_SMALL_POSYLY:
                    clickBunNum = 0;
                    if (playUtil != null) {
                        playUtil.stopMusic();
                    }
                    rela_bgg.setVisibility(View.GONE);
                    ViewSizeChange.setGridViewSizeSmall(rela_view);
                    break;
                case MESSAGE_CACEL_POLICE:
                    if (!isCallIng) {
                        clickBunNum = 0;
                        setViewChange(false);

                    }
                    break;
                case START_KINESCOPE:
                    stopRecorder();
                    startRecord();
                    handler.sendEmptyMessageDelayed(START_KINESCOPE, 1000 * 60*60);
                    break;
                case EXIT_APP:
                    sendBroadcast(new Intent("com.ys.exit.videoapp"));
                    break;
            }
        }
    };

    private void startTimer() {
        cacelTimer();
        timer = new Timer(true);
        task = new MyTask();
        timer.schedule(task, 15 * 1000);
    }

    private void cacelTimer() {
        if (timer != null) {
            timer.cancel();
        }
        if (task != null) {
            task.cancel();
        }
    }

    private Timer timer;
    private MyTask task;

    private class MyTask extends TimerTask {
        @Override
        public void run() {
            handler.sendEmptyMessage(MESSAGE_CACEL_POLICE);
        }
    }

//    public static String getSDCardPaths(Context context) {
//        try {
//            Class class_StorageManager = StorageManager.class;
//            Method method_getVolumeList = class_StorageManager.getMethod("getVolumeList");
//            Method method_getVolumeState = class_StorageManager.getMethod("getVolumeState", String.class);
//            StorageManager sm = (StorageManager) context.getSystemService(Context.STORAGE_SERVICE);
//            Class class_StorageVolume = Class.forName("android.os.storage.StorageVolume");
//            Method method_getPath = class_StorageVolume.getMethod("getPath");
//            Method method_mDescription = class_StorageVolume.getMethod("getDescription");
//            Object[] objArray = (Object[]) method_getVolumeList.invoke(sm);
//            if (objArray.length<2)
//                return null;
//            if ((method_getVolumeState.invoke(sm, method_getPath.invoke(objArray[1]))).equals("mounted")) {
//                String mDescription = (String) method_mDescription.invoke(class_StorageVolume,context);
//                if (mDescription.contains("SD")){
//                    FILEA_PATH = (String) method_getPath.invoke(objArray[1]);
//                }
//            } else if ((method_getVolumeState.invoke(sm, method_getPath.invoke(objArray[2]))).equals("mounted")) {
//                FILEA_PATH = (String) method_getPath.invoke(objArray[2]);
//            }
//            return FILEA_PATH;
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return null;
//    }

    public static StorageManager getStorageManager(Context context) {
        return (StorageManager) context.getSystemService(Context.STORAGE_SERVICE);
    }

    /**
     * 获取外置SD卡根目录
     *
     * @param context
     * @return
     */
    private static String getSDCardPaths(Context context) {
        StorageManager storageManager = getStorageManager(context);
        Class<?> volumeInfoClazz = null;
        Class<?> diskInfoClazz = null;
        try {
            diskInfoClazz = Class.forName("android.os.storage.DiskInfo");
            Method isSd = diskInfoClazz.getMethod("isSd");
            volumeInfoClazz = Class.forName("android.os.storage.VolumeInfo");
            Method getType = volumeInfoClazz.getMethod("getType");
            Method getDisk = volumeInfoClazz.getMethod("getDisk");
            Field path = volumeInfoClazz.getDeclaredField("path");
            Method getVolumes = storageManager.getClass().getMethod("getVolumes");
            List<Class<?>> result = (List<Class<?>>) getVolumes.invoke(storageManager);
            for (int i = 0; i < result.size(); i++) {
                Object volumeInfo = result.get(i);
                if ((int) getType.invoke(volumeInfo) == 0) {
                    Object disk = getDisk.invoke(volumeInfo);
                    if (disk != null) {
                        if ((boolean) isSd.invoke(disk)) {
                            FILEA_PATH = (String) path.get(volumeInfo);
                            return FILEA_PATH;
                        }
                    }
                }
            }
            return null ;
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
        return null;
    }

    //自动挂断
    public static void endPhone(Context c,TelephonyManager tm) {
        try {
            Log.i(TAG, "endPhone");
            ITelephony iTelephony;
            Method getITelephonyMethod = TelephonyManager.class
                    .getDeclaredMethod("getITelephony", (Class[]) null);
            getITelephonyMethod.setAccessible(true);
            iTelephony = (ITelephony) getITelephonyMethod.invoke(tm,
                    (Object[]) null);
            // 挂断电话
            iTelephony.endCall();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    //检查设备是否有摄像头
    private boolean hasCamera(Context context) {
        Log.i(TAG, "hasCamera=============");
        if (context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
            return true;
        } else {
            return false;
        }
    }

    private static boolean cameraFront = false;
    private void initCamdera() {
        if (!hasCamera(getApplicationContext())) {
            //这台设备没有发现摄像头
            Toast.makeText(getApplicationContext(), R.string.dont_have_camera_error
                    , Toast.LENGTH_SHORT).show();
//            setResult(MainActivity.RESULT_CODE_FOR_RECORD_VIDEO_FAILED);
            Log.i("finish", "onResume=====");
        }
        if (mCamera == null) {
//            final boolean frontal = cameraFront;

            int cameraId = findFrontFacingCamera();
            if (cameraId < 0) {
                //前置摄像头不存在
                //尝试寻找后置摄像头
                cameraId = findBackFacingCamera();
            } else if (!cameraFront) {
                cameraId = findBackFacingCamera();
            }
            if (cameraId==-1){
                Toast.makeText(getApplicationContext(), "没有找到摄像头，请连接好摄像头再进行录制", Toast.LENGTH_LONG).show();
                handler.sendEmptyMessageDelayed(EXIT_APP,3000);
                return;
            }else {
                try {
                String path = "/sdcard/"+System.currentTimeMillis()+".mp4";
                File fileVideo = new File(path);
                if (fileVideo.exists()) {
                    fileVideo.delete();
                }
                    fileVideo.createNewFile();
                MyLog.e("cdl", "====开始录制===");
                if (mRecorder == null) {
                    mRecorder = new MediaRecorder(); // Create MediaRecorder
                }
                mRecorder.setAudioSource(MediaRecorder.AudioSource.CAMCORDER);
                mRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);
                mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
                mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
                mRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.H263);
                mRecorder.setVideoSize(640, 480);  //客户长形得摄像头
//            mRecorder.setVideoSize(320, 240);
//            mRecorder.setVideoFrameRate(10);
                mRecorder.setPreviewDisplay(mSurfaceHolder.getSurface());
                mRecorder.setOutputFile(url_file);
                mRecorder.prepare();
                mRecorder.start();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 找前置摄像头,没有则返回-1
     *
     * @return cameraId
     */
    private int findFrontFacingCamera() {
        int cameraId = -1;
        //获取摄像头个数
        int numberOfCameras = Camera.getNumberOfCameras();
        for (int i = 0; i < numberOfCameras; i++) {
            Camera.CameraInfo info = new Camera.CameraInfo();
            Camera.getCameraInfo(i, info);
            if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
                cameraId = i;
                cameraFront = true;
                break;
            }
        }
        Log.i(TAG, "findFrontFacingCamera============="+cameraId);
        if (cameraId==-1) {
        }
        return cameraId;
    }
    /**
     * 找后置摄像头,没有则返回-1
     *
     * @return cameraId
     */
    private int findBackFacingCamera() {
        int cameraId = -1;
        //获取摄像头个数
        int numberOfCameras = Camera.getNumberOfCameras();
        for (int i = 0; i < numberOfCameras; i++) {
            Camera.CameraInfo info = new Camera.CameraInfo();
            Camera.getCameraInfo(i, info);
            if (info.facing == Camera.CameraInfo.CAMERA_FACING_BACK) {
                cameraId = i;
                cameraFront = false;
                break;
            }
        }
        Log.i(TAG, "findBackFacingCamera============="+cameraId);
        return cameraId;
    }

    private void openCamera(){
        stopRecorder();
        mCamera = android.hardware.Camera.open(0);//创建照相机对象
//                mCamera.setDisplayOrientation(90);//调整方向
        SurfaceHolder sh = mSurfaceview.getHolder();

        try {
            android.hardware.Camera.Parameters parameters = mCamera.getParameters();//取照相参数
            parameters.setPreviewSize(800, 600);//设置预览尺寸
            parameters.setPreviewFpsRange(4, 10);//设置预览FPS幅度
//                    parameters.setPictureFormat(ImageFormat.JPEG);//设置相片格式
            parameters.set("jpeg-quality", 85);
            parameters.setPictureSize(800, 600);//设置相片尺寸

            mCamera.setPreviewDisplay(sh);//设置预览显示对象
            mCamera.startPreview();//开始预览
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}