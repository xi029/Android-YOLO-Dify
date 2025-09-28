package com.tencent.yolov8ncnn;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Switch;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import com.bumptech.glide.Glide;

public class MainActivity extends Activity implements SurfaceHolder.Callback {

    public static final int REQUEST_CAMERA = 100;
    private Yolov8Ncnn yolov8ncnn = new Yolov8Ncnn();
    private int facing = 0;
    private Spinner spinnerModel;
    private Spinner spinnerCPUGPU;
    private int current_model = 0;
    private int current_cpugpu = 0;

    private SurfaceView cameraView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 检查用户是否登录
        if (!isUserLoggedIn()) {
            // 如果用户未登录，跳转到LoginActivity
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();  // 结束当前Activity，避免用户回退到MainActivity
            return;
        }

        // 如果已登录，加载主界面
        setContentView(R.layout.main);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        cameraView = findViewById(R.id.cameraview);
        cameraView.getHolder().setFormat(PixelFormat.RGBA_8888);
        cameraView.getHolder().addCallback(this);

        Button buttonSwitchCamera = findViewById(R.id.buttonSwitchCamera);
        buttonSwitchCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                int new_facing = 1 - facing;
                yolov8ncnn.closeCamera();
                yolov8ncnn.openCamera(new_facing);
                facing = new_facing;
            }
        });

        ImageView sea = findViewById(R.id.sea);
        ImageView imageView = findViewById(R.id.imageView);
        Switch switch2GIF = findViewById(R.id.switch2GIF);

        // 设置初始状态，显示静态图片，隐藏GIF
        sea.setVisibility(View.GONE);
        imageView.setVisibility(View.VISIBLE);

        switch2GIF.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // 选中状态 显示GIF
                    sea.setVisibility(View.VISIBLE);
                    Glide.with(MainActivity.this).load(R.drawable.sea).into(sea);
                    imageView.setVisibility(View.GONE);
                } else {
                    // 未选中状态 隐藏GIF 显示静态图片
                    sea.setVisibility(View.GONE);
                    imageView.setImageResource(R.drawable.logo_sea);
                    imageView.setVisibility(View.VISIBLE);
                }
            }
        });

        Button buttonWebview = findViewById(R.id.button1);
        buttonWebview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, WebviewActivity.class);
                startActivity(intent);
            }
        });

        spinnerModel = findViewById(R.id.spinnerModel);
        spinnerModel.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long id) {
                if (position != current_model) {
                    current_model = position;
                    reload();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });

        spinnerCPUGPU = findViewById(R.id.spinnerCPUGPU);
        spinnerCPUGPU.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long id) {
                if (position != current_cpugpu) {
                    current_cpugpu = position;
                    reload();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });

        reload();
    }

    private void reload() {
        boolean ret_init = yolov8ncnn.loadModel(getAssets(), current_model, current_cpugpu);
        if (!ret_init) {
            Log.e("MainActivity", "yolov8ncnn loadModel failed");
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        yolov8ncnn.setOutputWindow(holder.getSurface());
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {}

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {}

    @Override
    public void onResume() {
        super.onResume();

        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA);
        }

        yolov8ncnn.openCamera(facing);
    }

    @Override
    public void onPause() {
        super.onPause();
        yolov8ncnn.closeCamera();
    }

    // 模拟用户登录状态的检查方法 (可以根据实际逻辑实现)
    private boolean isUserLoggedIn() {
        // 这里应该根据实际登录状态检查用户是否已经登录，比如通过SharedPreferences或其他登录状态
        // 例如返回 true 表示用户已经登录，返回 false 表示需要跳转到登录界面
        return true;  // 这里默认返回true用于测试，实际代码中应做出真实的登录状态检查
    }
}
