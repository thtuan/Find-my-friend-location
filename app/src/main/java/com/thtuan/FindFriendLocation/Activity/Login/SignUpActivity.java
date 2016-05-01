package com.thtuan.FindFriendLocation.Activity.Login;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;
import com.thtuan.FindFriendLocation.R;

public class SignUpActivity extends Activity implements SignUpCallback{

    Button btnsignUp;
    EditText etUser, etPassword, etEmail, etRepass;
    String User, Password,RePass,Email;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        btnsignUp = (Button) findViewById(R.id.btnSignUpSignup);
        etUser = (EditText) findViewById(R.id.etUserSignup);
        etPassword = (EditText) findViewById(R.id.etPassSignup);
        etRepass = (EditText) findViewById(R.id.etRePassSignup);
        etEmail = (EditText) findViewById(R.id.etEmailSignup);
        btnsignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Password = etPassword.getText().toString();
                RePass = etRepass.getText().toString();

                if(Password.equals(RePass)){
                    ParseUser user = new ParseUser();
                    user.setUsername(etUser.getText().toString());
                    user.setPassword(etPassword.getText().toString());
                    user.setEmail(etEmail.getText().toString());
                    user.signUpInBackground(SignUpActivity.this);
                }
                else {
                    Toast.makeText(getApplicationContext(),"Mat khau nhap lai khong khop", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    @Override
    public void done(ParseException e) {
        if(e == null){
            Toast.makeText(getApplicationContext(),"Dang ki thanh cong",Toast.LENGTH_LONG).show();
//            Bitmap bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.user);
//            ByteArrayOutputStream stream = new ByteArrayOutputStream();
//            bitmap.compress(Bitmap.CompressFormat.JPEG,100,stream);
//            byte[] bitmapdata = stream.toByteArray();
//            ParseFile parseFile = new ParseFile(bitmapdata);
//            parseFile.saveInBackground();
            sendResult(Activity.RESULT_OK);
            finish();
        }
        else {
            Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_LONG).show();
        }
    }
    public void sendResult(int resultCode){
        Intent intent = getIntent();
        intent.putExtra("USERNAME",etUser.getText().toString());
        intent.putExtra("PASSWORD",etPassword.getText().toString());
        setResult(resultCode,intent);
    }
}
