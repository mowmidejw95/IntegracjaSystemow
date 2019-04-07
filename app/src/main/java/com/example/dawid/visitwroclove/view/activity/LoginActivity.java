package com.example.dawid.visitwroclove.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.dawid.visitwroclove.R;
import com.example.dawid.visitwroclove.model.UserDTO;
import com.example.dawid.visitwroclove.presenter.LoginPresenter;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LoginActivity extends BaseActivity implements LoginView {

    @BindView(R.id.login_button)
    Button loginBtn;
    @BindView(R.id.email_edittext)
    EditText emailEt;
    @BindView(R.id.password_edittext)
    EditText passwordEt;

    public static final Pattern VALID_EMAIL_ADDRESS_REGEX =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
    private LoginPresenter presenter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getComponent().inject(this);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        presenter = new LoginPresenter();

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validate(emailEt.getText().toString())) {
                    presenter.login(new UserDTO(emailEt.getText().toString(), passwordEt.getText().toString()));
                } else {
                    showError("Błędny adres email, przykładowy email: przyklad@gmail.com");
                }
            }
        });
    }

    @Override
    public void onResume() {
        presenter.attachView(this);
        super.onResume();
    }

    @Override
    public void onPause() {
        presenter.detachView();
        super.onPause();
    }

    @Override
    public void showLoadingScreen() {
        Intent intent = new Intent(this, SplashScreenActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void showError(String errorMessage) {
        Toast.makeText(this, "Błąd podczas logowania: " + errorMessage, Toast.LENGTH_LONG).show();
    }

    public static boolean validate(String emailStr) {
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(emailStr);
        return matcher.find();
    }
}