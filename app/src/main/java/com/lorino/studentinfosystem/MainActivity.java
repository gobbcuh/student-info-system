package com.lorino.studentinfosystem;

import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.ViewFlipper;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    private static final int SCREEN_LOGIN = 0;
    private static final int SCREEN_STUDENT_INFO = 1;
    private static final int SCREEN_SIGNUP = 2;
    private static final int SCREEN_FORGOT = 3;

    private ViewFlipper viewFlipper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WindowCompat.enableEdgeToEdge(getWindow());
        setContentView(R.layout.activity_main);

        viewFlipper = findViewById(R.id.viewFlipper);

        ViewCompat.setOnApplyWindowInsetsListener(viewFlipper, (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (viewFlipper != null && viewFlipper.getDisplayedChild() != SCREEN_LOGIN) {
                    viewFlipper.setDisplayedChild(SCREEN_LOGIN);
                } else {
                    setEnabled(false);
                    getOnBackPressedDispatcher().onBackPressed();
                }
            }
        });

        setupLoginScreen();
        setupStudentInfoScreen();
        setupSignupScreen();
        setupForgotScreen();
    }

    private void setupLoginScreen() {
        View screenLogin = findViewById(R.id.screenLogin);
        EditText etUsername = screenLogin.findViewById(R.id.etLoginUsername);
        EditText etPassword = screenLogin.findViewById(R.id.etLoginPassword);
        View btnLoginSubmit = screenLogin.findViewById(R.id.btnLoginSubmit);
        View tvForgotPassword = screenLogin.findViewById(R.id.tvForgotPassword);
        View tvRegisterLink = screenLogin.findViewById(R.id.tvRegisterLink);
        View btnGoogleLogin = screenLogin.findViewById(R.id.btnGoogleLogin);
        View btnMicrosoftLogin = screenLogin.findViewById(R.id.btnMicrosoftLogin);

        setupPasswordToggle(etPassword);

        btnLoginSubmit.setOnClickListener(v -> {
            String username = etUsername.getText().toString().trim();
            if (TextUtils.isEmpty(username)) {
                etUsername.setError("Please enter your Student ID or Email");
                etUsername.requestFocus();
                return;
            }
            Toast.makeText(this, "Welcome back, " + username + "!", Toast.LENGTH_SHORT).show();
            viewFlipper.setDisplayedChild(SCREEN_STUDENT_INFO);
        });

        tvForgotPassword.setOnClickListener(v -> viewFlipper.setDisplayedChild(SCREEN_FORGOT));
        tvRegisterLink.setOnClickListener(v -> viewFlipper.setDisplayedChild(SCREEN_SIGNUP));

        View.OnClickListener ssoListener = v -> {
            Toast.makeText(this, "SSO Sign-In successful!", Toast.LENGTH_SHORT).show();
            viewFlipper.setDisplayedChild(SCREEN_STUDENT_INFO);
        };
        btnGoogleLogin.setOnClickListener(ssoListener);
        btnMicrosoftLogin.setOnClickListener(ssoListener);
    }

    private void setupStudentInfoScreen() {
        View screenStudentInfo = findViewById(R.id.screenStudentInfo);
        View btnRecordBack = screenStudentInfo.findViewById(R.id.btnRecordBack);
        View btnSaveRecord = screenStudentInfo.findViewById(R.id.btnSaveRecord);

        btnRecordBack.setOnClickListener(v -> viewFlipper.setDisplayedChild(SCREEN_LOGIN));

        btnSaveRecord.setOnClickListener(v ->
            Toast.makeText(this, "Student Academic Record updated successfully!", Toast.LENGTH_LONG).show()
        );
    }

    private void setupSignupScreen() {
        View screenSignup = findViewById(R.id.screenSignup);
        View btnSignupBack = screenSignup.findViewById(R.id.btnSignupBack);
        View tvLoginLink = screenSignup.findViewById(R.id.tvLoginLink);
        View btnSignupSubmit = screenSignup.findViewById(R.id.btnSignupSubmit);
        EditText etPassword = screenSignup.findViewById(R.id.etSignupPassword);
        EditText etConfirmPassword = screenSignup.findViewById(R.id.etSignupConfirmPassword);

        setupPasswordToggle(etPassword);
        setupPasswordToggle(etConfirmPassword);

        View.OnClickListener backToLogin = v -> viewFlipper.setDisplayedChild(SCREEN_LOGIN);
        btnSignupBack.setOnClickListener(backToLogin);
        tvLoginLink.setOnClickListener(backToLogin);

        btnSignupSubmit.setOnClickListener(v -> {
            Toast.makeText(this, "Student account created! Please sign in.", Toast.LENGTH_LONG).show();
            viewFlipper.setDisplayedChild(SCREEN_LOGIN);
        });
    }

    private void setupForgotScreen() {
        View screenForgot = findViewById(R.id.screenForgot);
        View btnForgotBack = screenForgot.findViewById(R.id.btnForgotBack);
        View tvForgotBackToLogin = screenForgot.findViewById(R.id.tvForgotBackToLogin);
        View btnForgotSubmit = screenForgot.findViewById(R.id.btnForgotSubmit);
        EditText etEmail = screenForgot.findViewById(R.id.etForgotEmail);

        View.OnClickListener backToLogin = v -> viewFlipper.setDisplayedChild(SCREEN_LOGIN);
        btnForgotBack.setOnClickListener(backToLogin);
        tvForgotBackToLogin.setOnClickListener(backToLogin);

        btnForgotSubmit.setOnClickListener(v -> {
            String email = etEmail.getText().toString().trim();
            if (TextUtils.isEmpty(email)) {
                etEmail.setError("Please enter your university email");
                etEmail.requestFocus();
                return;
            }
            Toast.makeText(this, "Password reset link sent to " + email, Toast.LENGTH_LONG).show();
            viewFlipper.setDisplayedChild(SCREEN_LOGIN);
        });
    }

    private void setupPasswordToggle(EditText editText) {
        editText.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_UP && editText.getCompoundDrawables()[2] != null) {
                int drawableWidth = editText.getCompoundDrawables()[2].getBounds().width();
                if (event.getRawX() >= (editText.getRight() - drawableWidth - editText.getPaddingEnd())) {
                    int selectionStart = editText.getSelectionStart();
                    int selectionEnd = editText.getSelectionEnd();
                    int passType = InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD;
                    int visiblePassType = InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD;
                    editText.setInputType(editText.getInputType() == passType ? visiblePassType : passType);
                    editText.setSelection(selectionStart, selectionEnd);
                    v.performClick();
                    return true;
                }
            }
            return false;
        });
    }
}