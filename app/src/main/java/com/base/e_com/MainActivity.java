package com.base.e_com;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.radiobutton.MaterialRadioButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Random;
import java.util.Properties;
import java.util.UUID;

import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;



public class MainActivity extends AppCompatActivity {
    private TextInputLayout tvMail, tvPassword, tvName;
    private TextInputEditText etMail, etPassword, etName;
    private RadioGroup radioGroup;
    private MaterialRadioButton rbSeller, rbBuyer;

    private MaterialButton btnSignup, btnSignin;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etName = findViewById(R.id.et_name);
        etMail = findViewById(R.id.et_email);
        etPassword = findViewById(R.id.et_password);
        radioGroup = findViewById(R.id.radiogroup);
        rbSeller = findViewById(R.id.rb_seller);
        rbBuyer = findViewById(R.id.rb_buyer);
        btnSignin = findViewById(R.id.btn_signin);
        btnSignup = findViewById(R.id.btn_signup);
        tvName = findViewById(R.id.tv_name);
        tvMail = findViewById(R.id.tv_email);
        tvPassword = findViewById(R.id.tv_password);

        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userId = UUID.randomUUID().toString();
                String name = etName.getText().toString();
                String email = etMail.getText().toString();
                String password = etPassword.getText().toString();
                boolean isSeller = radioGroup.getCheckedRadioButtonId() == R.id.rb_seller;
                UserEntity user = new UserEntity();
                user.setUserId(userId);
                user.setName(name);
                user.setEmail(email);
                user.setPassword(password);
                user.setSeller(isSeller);

                // Validate and save the user (simplified for demonstration)
                if (validateUserData(name, email, password)) {


                    UserDatabase userDatabase = UserDatabase.getInstance(getApplicationContext());
                    UserDao userDao = userDatabase.userDao();

                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            userDao.insertUser(user);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(MainActivity.this, "User registered successfully.", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }).start();

//                    Redirect to the OTP verification page
//                    Implement this navigation logic
//                    String generatedOTP = generateRandomOTP();
//                    sendOTPByEmail(email, generatedOTP);

                    Intent loginIntent = new Intent(MainActivity.this, LoginActivity.class);
//                    loginIntent.putExtra("expectedOTP", generatedOTP);
//                    loginIntent.putExtra("email", email);
                    startActivity(loginIntent);
                }
            }
        });

        btnSignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signInIntent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(signInIntent);
            }
        });
    }

    private String generateRandomOTP() {
        // Generate a random 6-digit OTP
        Random random = new Random();
        int otp = 100000 + random.nextInt(900000);
        return String.valueOf(otp);
    }

//    private void sendOTPByEmail(String email, String otp) {
//
//            // Email sending configuration
//            final String username = process.env.email; // Your Gmail email address
//            final String password = process.env.password; // Your Gmail password
//            String fromEmail = ""; // Same as username
//
//            Properties props = new Properties();
//            props.put("mail.smtp.auth", "true");
//            props.put("mail.smtp.starttls.enable", "true");
//            props.put("mail.smtp.host", "smtp.gmail.com"); // Use Gmail's SMTP server
//            props.put("mail.smtp.port", "587"); // Gmail SMTP port
//
//            // Create a session with your Gmail account
//            Session session = Session.getInstance(props,
//                    new javax.mail.Authenticator() {
//                        protected PasswordAuthentication getPasswordAuthentication() {
//                            return new PasswordAuthentication(username, password);
//                        }
//                    });
//
//            try {
//                // Create a MimeMessage object
//                Message message = new MimeMessage(session);
//                message.setFrom(new InternetAddress(fromEmail));
//                message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(email));
//                message.setSubject("OTP Verification");
//                message.setText("Your OTP is: " + otp);
//
//                // Send the email
//                Transport.send(message);
//            } catch (MessagingException e) {
//                e.printStackTrace();
//                // Handle email sending failure
//            }
//        }




    private boolean validateUserData(String name, String email, String password) {
        boolean isValid = true;

        if (TextUtils.isEmpty(name)) {
            tvName.setError("Please enter your name");
            isValid = false;
        } else {
            tvName.setError(null);
        }

        if (TextUtils.isEmpty(email) || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            tvMail.setError("Invalid email address");
            isValid = false;
        } else {
            tvMail.setError(null);
        }

        if (TextUtils.isEmpty(password) || password.length() < 6) {
            tvPassword.setError("Password must be at least 6 characters");
            isValid = false;
        } else {
            tvPassword.setError(null);
        }

        if (radioGroup.getCheckedRadioButtonId() == -1) {
            Toast.makeText(this, "Select a role (Seller or Buyer)", Toast.LENGTH_SHORT).show();
            isValid = false;
        }

        return isValid;
    }

}
