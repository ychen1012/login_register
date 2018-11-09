package diary.beautiful.userManager;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.IOException;
import java.util.LinkedHashMap;

import diary.beautiful.R;
import diary.beautiful.model.RegisterResult;
import diary.beautiful.util.HttpUtil;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * created by yangchen on 21:40
 */
public class LoginActivity extends Activity {
    private Button loginButton;
    private EditText emailEditText;
    private EditText passwordText;


    String emailValid;

    void initComponents() {
        loginButton = findViewById(R.id.login_button);
        emailEditText = findViewById(R.id.login_email);
        passwordText = findViewById(R.id.login_password);

    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        initComponents();
        setListeners();

    }


    void setListeners() {
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(LoginActivity.this, "!!!!!!", Toast.LENGTH_SHORT).show();
                login();

            }
        });
    }

    private void login() {
        String email = emailEditText.getText().toString();
        String password = passwordText.getText().toString();
        String checkMsg = checkDataValid(email, password);
        if (checkMsg.length() <= 0) {
            return;
        }
        if (!checkMsg.equals("succ")) {
            return;
        }
        loginButton.setEnabled(true);
        LinkedHashMap<String, String> param = new LinkedHashMap<>();
        param.put("email", email);
        param.put("password", password);
        HttpUtil.get("http://192.168.31.49:4396/user/login", param, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(LoginActivity.this, "netWork error", Toast.LENGTH_SHORT).show();

                    }
                });
            }


            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Gson gson = new Gson();
                String loginResult = response.body().string();
                RegisterResult registerResult = gson.fromJson(loginResult, RegisterResult.class);
                if (registerResult == null) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(LoginActivity.this, "网络连接失败", Toast.LENGTH_SHORT).show();

                        }
                    });
                    return;
                }
                if (registerResult.getResult().equals("true")) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(LoginActivity.this, "succ", Toast.LENGTH_SHORT).show();

                        }
                    });
                    return;

                } else {
                    if (registerResult.getMsg().equals("邮箱不存在")) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                emailEditText.setError(registerResult.getMsg());
                            }
                        });
                        return;
                    }
                    if (registerResult.getMsg().equals("密码不匹配")) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                passwordText.setError(registerResult.getMsg());
                            }
                        });
                        return;
                    }


                }
            }

        });

    }


    private String checkDataValid(String email, String password) {
        if (TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)) {
            emailEditText.setError("邮箱不能为空");
            return "fail";
        }
        if (TextUtils.isEmpty(password) && !TextUtils.isEmpty(email)) {
            passwordText.setError("密码不能为空");
            return "fail";
        }
        if (TextUtils.isEmpty(password) && TextUtils.isEmpty(email)) {
            emailEditText.setError("邮箱不能为空");
            passwordText.setError("密码不能为空");
            return "fail";
        }
        return "succ";
    }

    
}
