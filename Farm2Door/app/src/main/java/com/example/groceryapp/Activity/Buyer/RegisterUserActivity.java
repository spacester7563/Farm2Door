package com.example.groceryapp.Activity.Buyer;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.groceryapp.Activity.LoginActivity;
import com.example.groceryapp.Activity.Seller.RegisterSellerActivity;
import com.example.groceryapp.InputValidation;
import com.example.groceryapp.R;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Objects;

public class RegisterUserActivity extends AppCompatActivity implements View.OnClickListener{

    private EditText edEmail, edPassword, edConfirmPassword;
    private Button btnSignUp;
    private TextView txtRegisterAsSeller, txtSignIn;

    private String email,password,confirmPass;

    private ProgressDialog progressDialog;
    private InputValidation inputValidation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_user);

        inputValidation = new InputValidation(this);

        //progress dialog setup
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Please wait...");
        progressDialog.setCanceledOnTouchOutside(false);

        init();

        btnSignUp.setOnClickListener(this);
        txtRegisterAsSeller.setOnClickListener(this);
        txtSignIn.setOnClickListener(this);
    }


    private void init() {

        edEmail =findViewById(R.id.email);
        edPassword =findViewById(R.id.password);
        edConfirmPassword =findViewById(R.id.confirm_password);

        btnSignUp =findViewById(R.id.nextBtn);
        txtRegisterAsSeller =findViewById(R.id.registerSeller);
        txtSignIn =findViewById(R.id.signIn);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){

            case R.id.nextBtn:
                inputValidation();
                break;

            case R.id.signIn:
                startActivity(new Intent(RegisterUserActivity.this, LoginActivity.class));
                break;

            case R.id.registerSeller:
                startActivity(new Intent(RegisterUserActivity.this, RegisterSellerActivity.class));
                finish();
                break;
        }
    }

    private void inputValidation() {

        email = edEmail.getText().toString();
        password = edPassword.getText().toString();
        confirmPass = edConfirmPassword.getText().toString();

        if (email.isEmpty())
        {
            return;
        }

        if (password.isEmpty())
        {
            return;
        }

        if (!Objects.equals(confirmPass, password)){
            return;
        }
        else {
            Intent intent  = new Intent(RegisterUserActivity.this, RegisterUserProfile.class);
            intent.putExtra("email",email);
            intent.putExtra("password",password);
            startActivity(intent);
        }
    }
}