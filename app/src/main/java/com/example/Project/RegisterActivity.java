package com.example.Project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RegisterActivity extends AppCompatActivity {
EditText email,userName,password,confirmPassword;
Button button;

private FirebaseAuth mAuth ;
private ProgressDialog mLoadingBar ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        email = findViewById(R.id.inputEmail);
        userName = findViewById(R.id.inputUsername);
        password = findViewById(R.id.inputPassword);
        confirmPassword = findViewById(R.id.inputConformPassword);
        mAuth = FirebaseAuth.getInstance();
        mLoadingBar = new ProgressDialog(RegisterActivity.this);


    }


    public void gotOne(View view) {
        startActivity(new Intent(RegisterActivity.this,MainActivity.class));
    }
    public void checkCredintials(){
               String name = userName.getText().toString();
                String Email = email.getText().toString();
                String pass = password.getText().toString();
                String passConfirm = confirmPassword.getText().toString();
                if(name.isEmpty() || name.length()<7){
                   // Toast.makeText(this, "Your Name is not valid", Toast.LENGTH_SHORT).show();
                    showError (userName,"Your Name is not valid");
                }
                else if (Email.isEmpty() || !Email.contains("@")){
                   // Toast.makeText(this, "Your Email is not valid", Toast.LENGTH_SHORT).show();
                    showError(email,"Your Email is not valid");
                }
                else if (pass.isEmpty() || pass.length()<7){
                   // Toast.makeText(this, "Your Password is not valid", Toast.LENGTH_SHORT).show();
                    showError(password,"Your Password is not valid");
                }
                else if (pass.isEmpty() || !pass.equals(passConfirm)){
                   // Toast.makeText(this, "Password not match", Toast.LENGTH_SHORT).show();
                    showError(confirmPassword,"Password not match");
                }
                else {

                    mLoadingBar.setTitle("Registeration");
                    mLoadingBar.setMessage("Please Wait,while checking your credentials");
                    mLoadingBar.setCanceledOnTouchOutside(false);
                    mLoadingBar.show();

                    mAuth.createUserWithEmailAndPassword(Email,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()){
                                Toast.makeText(RegisterActivity.this, "Successfully Registered", Toast.LENGTH_SHORT).show();
                                mLoadingBar.dismiss();
                                Intent intent = new Intent(RegisterActivity.this,MainActivity2.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK| Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                            }
                            else {
                                Toast.makeText(RegisterActivity.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                }

    }

    private void showError(EditText input,String s) {

        input.setError(s);
        input.requestFocus();

    }

    public void register(View view) {
        checkCredintials();
    }
}