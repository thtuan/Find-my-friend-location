package com.thtuan.FindFriendLocation.Activity.Maps.view;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.datetimepicker.date.DatePickerDialog;
import com.android.datetimepicker.time.RadialPickerLayout;
import com.android.datetimepicker.time.TimePickerDialog;
import com.thtuan.FindFriendLocation.Class.UserObject;
import com.thtuan.FindFriendLocation.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ProfileActivity extends AppCompatActivity implements TimePickerDialog
        .OnTimeSetListener, DatePickerDialog.OnDateSetListener {

    @Bind(R.id.ivProfile)
    ImageView ivProfile;
    @Bind(R.id.tvProfileName)
    TextView tvProfileName;
    @Bind(R.id.tvSex)
    TextView tvSex;
    @Bind(R.id.tvBirthday)
    TextView tvBirthday;
    @Bind(R.id.tvProfileAddr)
    TextView tvProfileAddr;
    @Bind(R.id.tvProfileEmail)
    TextView tvProfileEmail;
    @Bind(R.id.tvProfileDetail)
    TextView tvProfileDetail;
    @Bind(R.id.tvSave)
    TextView tvSave;
    @Bind(R.id.tvRevert)
    TextView tvRevert;
    @Bind(R.id.tvProfilePhone)
    TextView tvProfilePhone;
    private Calendar calendar;
    private DateFormat dateFormat;
    private SimpleDateFormat timeFormat;
    private static final String TIME_PATTERN = "HH:mm"; // dinh dạng giờ
    private UserObject user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        ButterKnife.bind(this);
        calendar = Calendar.getInstance();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        dateFormat = DateFormat.getDateInstance(DateFormat.LONG, Locale.getDefault());
        timeFormat = new SimpleDateFormat(TIME_PATTERN, Locale.getDefault());
        user = getIntent().getParcelableExtra("data");

        ivProfile.setImageBitmap(user.getAvatar());
        tvProfileName.setText(user.getName());
        tvSex.setText(user.getCharacter());
        tvBirthday.setText(user.getBirthday());
        tvProfileAddr.setText(user.getAddr());
        tvProfileEmail.setText(user.getEmail());
        tvProfileDetail.setText(user.getContact());
        tvProfilePhone.setText(user.getPhone());
    }

    /**
     * lấy thời gian
     */
    private void update() {
        tvBirthday.setText(dateFormat.format(calendar.getTime()));
//        tvTime.setText(timeFormat.format(calendar.getTime()));
    }

    // lay ngay
    @Override
    public void onDateSet(DatePickerDialog dialog, int year, int monthOfYear, int dayOfMonth) {
        calendar.set(year, monthOfYear, dayOfMonth);
        update();
    }

    //l lay gio
    @Override
    public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute) {
        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
        calendar.set(Calendar.MINUTE, minute);
        update();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
