package com.thtuan.FindFriendLocation.Activity.Maps.view;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.thtuan.FindFriendLocation.Class.Constants;
import com.thtuan.FindFriendLocation.Class.RealPathUtil;
import com.thtuan.FindFriendLocation.Class.RoundedImageView;
import com.thtuan.FindFriendLocation.Class.UserObject;
import com.thtuan.FindFriendLocation.R;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class EditProfile extends Activity {


    @Bind(R.id.ivProfile)
    ImageView ivProfile;
    @Bind(R.id.etSex)
    EditText etSex;
    @Bind(R.id.etBirthday)
    EditText etBirthday;
    @Bind(R.id.etProfileAddr)
    EditText etProfileAddr;
    @Bind(R.id.etProfileEmail)
    EditText etProfileEmail;
    @Bind(R.id.etProfileDetail)
    EditText etProfileDetail;
    @Bind(R.id.tvSave)
    TextView tvSave;
    @Bind(R.id.tvRevert)
    TextView tvRevert;
    @Bind(R.id.etProfilePhone)
    EditText etProfilePhone;
    @Bind(R.id.etProfileName)
    TextView etProfileName;
    private String path;
    UserObject user;
    Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        ButterKnife.bind(this);
        user = new UserObject();
        ParseQuery<ParseObject> query = ParseQuery.getQuery("DataUser");
        query.whereEqualTo("alias", getIntent().getExtras().getString("data"));
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                new AsyncTask<ParseObject, UserObject, Void>() {
                    @Override
                    protected Void doInBackground(ParseObject... params) {
                        UserObject userObject = new UserObject();
                        try {
                            Bitmap bitmap = RoundedImageView.getCroppedBitmap
                                    (BitmapFactory.decodeStream(params[0].getParseFile("imageUser").getDataStream()),
                                            80);
                            userObject.setAvatar(bitmap);
                            userObject.setName(params[0].getString("alias"));
                            userObject.setCharacter(params[0].getString("sex"));
                            userObject.setBirthday(params[0].getString("birthday"));
                            userObject.setAddr(params[0].getString("address"));
                            userObject.setEmail(params[0].getString("email"));
                            userObject.setContact(params[0].getString("detail"));
                            userObject.setPhone(params[0].getString("phone"));
                            publishProgress(userObject);

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        return null;
                    }

                    @Override
                    protected void onProgressUpdate(UserObject... values) {
                        ivProfile.setImageBitmap(values[0].getAvatar());
                        etProfileName.setText(values[0].getName());
                        etProfilePhone.setText(values[0].getPhone());
                        etBirthday.setText(values[0].getPhone());
                        etSex.setText(values[0].getCharacter());
                        etProfileAddr.setText(values[0].getAddr());
                        etProfileEmail.setText(values[0].getEmail());
                        etProfileDetail.setText(values[0].getContact());
                    }
                }.execute(objects.get(0));
            }
        });
    }

    @OnClick({R.id.ivProfile, R.id.tvSave, R.id.tvRevert})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ivProfile:
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, Constants.REQUEST_GET_PHOTO);
                break;
            case R.id.tvSave:
                if(bitmap!=null)
                    user.setAvatar(bitmap);
                user.setCharacter(etSex.getText().toString());
                user.setBirthday(etBirthday.getText().toString());
                user.setAddr(etProfileAddr.getText().toString());
                user.setEmail(etProfileEmail.getText().toString());
                user.setContact(etProfileDetail.getText().toString());
                user.setPhone(etProfilePhone.getText().toString());
                Intent intent1 = new Intent();
                intent1.putExtra("data", user);
                intent1.putExtra("stringpath", path);
                setResult(RESULT_OK, intent1);
                finish();
                break;
            case R.id.tvRevert:
                finish();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Constants.REQUEST_GET_PHOTO && resultCode == RESULT_OK) {
            path = RealPathUtil.getRealPathFromURI(data.getData(), this);
            bitmap = RoundedImageView.getCroppedBitmap(BitmapFactory.decodeFile(path), 240);
            ivProfile.setImageBitmap(bitmap);
        }
    }

}
