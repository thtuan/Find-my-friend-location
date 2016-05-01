package com.thtuan.FindFriendLocation.Activity.Login;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseFacebookUtils;
import com.parse.ParseUser;
import com.thtuan.FindFriendLocation.Activity.Maps.MapsActivity;
import com.thtuan.FindFriendLocation.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class LoginActivity extends AppCompatActivity {

    EditText user;
    EditText password;
    Button login;
    Button signUp;
    Button loginFacebook;
    static final int LOGIN_REQUEST = 189;
//    ParseUser parseUser;
    public static final List<String> permission = new ArrayList<String>(){{
    add("public_profile");
    add("email");
}};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        user = (EditText) findViewById(R.id.etUser);
        password = (EditText) findViewById(R.id.etPass);
        login = (Button) findViewById(R.id.btnLogin);
        signUp = (Button) findViewById(R.id.btnSignUp);
        loginFacebook = (Button) findViewById(R.id.btnLoginFacebook);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ParseUser.logInInBackground(user.getText().toString(), password.getText().toString(), new LogInCallback() {
                    @Override
                    public void done(ParseUser parseUser, ParseException e) {
                        if (parseUser != null) {
                            Login();
                        } else {
                            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signUpIntent = new Intent(LoginActivity.this,SignUpActivity.class);
                startActivityForResult(signUpIntent, LOGIN_REQUEST);
            }
        });
        if(ParseUser.getCurrentUser()!= null){
            Login();
        }
        loginFacebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ParseFacebookUtils.logInWithReadPermissionsInBackground(LoginActivity.this, permission, new LogInCallback() {
                    @Override
                    public void done(ParseUser parseUser, ParseException e) {
                        if (user == null) {
                            //Log.d("MyApp", "Uh oh. The user cancelled the Facebook login.");
                        } else if (parseUser.isNew()) {
                            saveUser();
                            Login();
                            //Log.d("MyApp", "User signed up and logged in through Facebook!");
                        } else {
                            //Log.d("MyApp", "User logged in through Facebook!");
                            Login();
                        }
                    }
                });
            }
        });
    }


    public void saveUser(){
        GraphRequest request = GraphRequest.newMeRequest(AccessToken.getCurrentAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
            @Override
            public void onCompleted(JSONObject object, GraphResponse response) {
                try {
                    ParseUser user = ParseUser.getCurrentUser();
                    user.setUsername(object.getString("name"));
                    user.setEmail(object.getString("email"));
                    user.saveInBackground();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        Bundle parameter = new Bundle();
        parameter.putString("fields", "name,email");
        request.setParameters(parameter);
        request.executeAsync();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == LOGIN_REQUEST && resultCode == Activity.RESULT_OK){
            user.setText(data.getStringExtra("USERNAME"));
            password.setText(data.getStringExtra("PASSWORD"));
        }
        ParseFacebookUtils.onActivityResult(requestCode, resultCode, data);
    }
    void Login(){
        Intent intent = new Intent(LoginActivity.this,MapsActivity.class);
        startActivity(intent);
    }

}
