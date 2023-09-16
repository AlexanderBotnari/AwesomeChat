package com.alexander.awesomechat;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignInActivity extends AppCompatActivity {

    private EditText emailEditText;
    private EditText passwordEditText;
    private EditText confirmPasswordEditText;
    private EditText nameEditText;
    private Button signupButton;
    private TextView toggleLoginSignupButton;

    private FirebaseAuth auth;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference usersReference;

    private boolean loginModeActive;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        confirmPasswordEditText = findViewById(R.id.confirmPasswordEditText);
        nameEditText = findViewById(R.id.nameEditText);
        signupButton = findViewById(R.id.signupButton);
        toggleLoginSignupButton = findViewById(R.id.toggleLoginSignupButton);

        auth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        usersReference = firebaseDatabase.getReference().child("users");

        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginSignupUser(emailEditText.getText().toString().trim(),
                                passwordEditText.getText().toString().trim());
            }
        });

        toggleLoginSignupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (loginModeActive){
                    loginModeActive = false;
                    signupButton.setText("Sign Up");
                    toggleLoginSignupButton.setText("Or, Log In");
                    confirmPasswordEditText.setVisibility(View.VISIBLE);
                    nameEditText.setVisibility(View.VISIBLE);
                }else {
                    loginModeActive = true;
                    signupButton.setText("Log In");
                    toggleLoginSignupButton.setText("Or, Sign Up");
                    confirmPasswordEditText.setVisibility(View.GONE);
                    nameEditText.setVisibility(View.GONE);
                }
            }
        });

        if (auth.getCurrentUser() != null){
            startActivity(new Intent(SignInActivity.this, UserListActivity.class));
        }
    }

    private void loginSignupUser(String email, String password) {
        if (loginModeActive){
            if(passwordEditText.getText().toString().trim().length() < 7){
                Toast.makeText(this, "Password must be least 7 characters", Toast.LENGTH_SHORT).show();
            }else if(emailEditText.getText().toString().trim().equals("")){
                Toast.makeText(this, "Please input your email", Toast.LENGTH_SHORT).show();
            }else{
                auth.signInWithEmailAndPassword(email, password).
                        addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(Task<AuthResult> task) {
                                if(task.isSuccessful()){
                                    Log.d("LoginActivity", "successful login user");
                                    FirebaseUser user = auth.getCurrentUser();
                                    startActivity(new Intent(SignInActivity.this, UserListActivity.class));
                                }else {
                                    Log.w("LoginActivity", "login user failed.", task.getException());
                                    Toast.makeText(SignInActivity.this, "login failed", Toast.LENGTH_LONG).show();
                                }
                            }
                        });
            }

        }else{
            if(!passwordEditText.getText().toString().trim().equals(confirmPasswordEditText.getText().toString().trim())){
                Toast.makeText(this, "Passwords don't match", Toast.LENGTH_SHORT).show();
            }else if(passwordEditText.getText().toString().trim().length() < 7){
                Toast.makeText(this, "Password must be least 7 characters", Toast.LENGTH_SHORT).show();
            }else if(emailEditText.getText().toString().trim().equals("")){
                Toast.makeText(this, "Please input your email", Toast.LENGTH_SHORT).show();
            }else{
                auth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(Task<AuthResult> task) {
                                if (task.isSuccessful()){
                                    Log.d("SigninActivity", "successful create user");
                                    FirebaseUser user = auth.getCurrentUser();
                                    createUser(user);
                                    Intent intent = new Intent(SignInActivity.this, UserListActivity.class);
                                    intent.putExtra("userName", nameEditText.getText().toString().trim());
                                    startActivity(intent);
                                }else {
                                    Log.w("SigninActivity", "create user failed.", task.getException());
                                    Toast.makeText(SignInActivity.this, "authentication failed", Toast.LENGTH_LONG).show();
                                }
                            }
                        });
            }

        }

    }

    private void createUser(FirebaseUser firebaseUser) {
        User user = new User();
        user.setEmail(firebaseUser.getEmail());
        user.setId(firebaseUser.getUid());
        user.setProfilePhoto("");
        user.setName(nameEditText.getText().toString().trim());

        usersReference.push().setValue(user);
    }


}