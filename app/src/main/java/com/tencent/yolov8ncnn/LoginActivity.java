package com.tencent.yolov8ncnn;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends Activity {

    private static final String VALID_USERNAME = "admin";
    private static final String VALID_PASSWORD = "123456";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // 获取界面中的控件
        final EditText editTextUsername = findViewById(R.id.editText_username);
        final EditText editTextPassword = findViewById(R.id.editText_password);
        Button buttonLogin = findViewById(R.id.button_login);

        // 设置登录按钮的点击事件
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = editTextUsername.getText().toString().trim();
                String password = editTextPassword.getText().toString().trim();

                // 检查用户名和密码是否为空
                if (TextUtils.isEmpty(username)) {
                    Toast.makeText(LoginActivity.this, "请输入用户名", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(LoginActivity.this, "请输入密码", Toast.LENGTH_SHORT).show();
                    return;
                }

                // 验证用户名和密码
                if (isValid(username, password)) {
                    // 登录成功后执行的操作
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish(); // 结束当前登录Activity
                } else {
                    // 登录失败后提示用户
                    Toast.makeText(LoginActivity.this, "未查询到账户信息", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    // 验证用户名和密码的函数
    private boolean isValid(String username, String password) {
        return VALID_USERNAME.equals(username) && VALID_PASSWORD.equals(password);
    }
}
