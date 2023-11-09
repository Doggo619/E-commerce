package com.base.e_com;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class LoginActivity extends AppCompatActivity {
    private TextInputLayout tvEmail, tvPassword;
    private TextInputEditText etEmail, etPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        tvEmail = findViewById(R.id.tv_email);
        tvPassword = findViewById(R.id.tv_password);
        etEmail = findViewById(R.id.et_email);
        etPassword = findViewById(R.id.et_password);

        MaterialButton btnSignIn = findViewById(R.id.btn_signin);
        MaterialButton btnRegister = findViewById(R.id.btn_register);

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = etEmail.getText().toString();
                String password = etPassword.getText().toString();

                if (validateLoginData(email, password)) {
                    UserDatabase userDatabase = UserDatabase.getInstance(LoginActivity.this);
                    UserDao userDao = userDatabase.userDao();
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            UserEntity user = userDao.login(email, password);

                            if (user != null) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(LoginActivity.this, "Login successful.", Toast.LENGTH_SHORT).show();

                                        String userId = user.getUserId();
                                        saveUserIdInSharedPreferences(userId); 

                                        if (user.getSeller()) {
                                            Intent dashboardIntent = new Intent(LoginActivity.this, SellerActivity.class)
                                                    .putExtra("userName", user.getName());
                                            startActivity(dashboardIntent);
                                        } else {
                                            Intent homeIntent = new Intent(LoginActivity.this, BuyerActivity.class);
                                            startActivity(homeIntent);
                                        }
                                    }
                                });
                            } else {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(LoginActivity.this, "Invalid email or password.", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        }
                    }).start();
                }
            }
        });


        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }

    private boolean validateLoginData(String email, String password) {
        boolean isValid = true;

        if (TextUtils.isEmpty(email) || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            tvEmail.setError("Invalid email address");
            isValid = false;
        } else {
            tvEmail.setError(null);
        }

        if (TextUtils.isEmpty(password) || password.length() < 6) {
            tvPassword.setError("Password must be at least 6 characters");
            isValid = false;
        } else {
            tvPassword.setError(null);
        }

        return isValid;
    }

    private void saveUserIdInSharedPreferences(String userId) {
        SharedPreferences sharedPreferences = getSharedPreferences("user_prefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("userId", userId); // Save the user ID
        editor.apply();
    }

}
