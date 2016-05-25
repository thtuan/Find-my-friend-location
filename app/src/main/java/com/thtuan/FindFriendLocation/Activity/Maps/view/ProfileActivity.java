package com.thtuan.FindFriendLocation.Activity.Maps.view;

import android.os.Bundle;
import android.support.design.widget.BottomSheetDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.datetimepicker.date.DatePickerDialog;
import com.android.datetimepicker.time.RadialPickerLayout;
import com.android.datetimepicker.time.TimePickerDialog;
import com.thtuan.FindFriendLocation.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ProfileActivity extends AppCompatActivity implements View.OnClickListener, TimePickerDialog
        .OnTimeSetListener, DatePickerDialog.OnDateSetListener {

    @Bind(R.id.ivProfile)
    ImageView ivProfile;
    @Bind(R.id.etProfileName)
    TextView etProfileName;
    @Bind(R.id.tvSex)
    TextView tvSex;
    @Bind(R.id.tvBirthday)
    TextView tvBirthday;
    @Bind(R.id.etProfileAddr)
    TextView etProfileAddr;
    @Bind(R.id.etProfileEmail)
    TextView etProfileEmail;
    @Bind(R.id.etProfileDetail)
    TextView etProfileDetail;
    @Bind(R.id.tvSave)
    TextView tvSave;
    @Bind(R.id.tvRevert)
    TextView tvRevert;
    private Calendar calendar;
    private DateFormat dateFormat;
    private SimpleDateFormat timeFormat;
    private static final String TIME_PATTERN = "HH:mm"; // dinh dạng giờ


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        ButterKnife.bind(this);
        calendar = Calendar.getInstance();
        dateFormat = DateFormat.getDateInstance(DateFormat.LONG, Locale.getDefault());
        timeFormat = new SimpleDateFormat(TIME_PATTERN, Locale.getDefault());
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

    @OnClick({R.id.ivProfile, R.id.etProfileName, R.id.tvSex, R.id.tvBirthday, R.id.etProfileAddr, R.id.etProfileEmail, R.id.etProfileDetail, R.id.tvSave})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ivProfile:

                break;
            case R.id.etProfileName:
                BottomSheetDialog dialog = new BottomSheetDialog(this);
                dialog.setTitle("Sửa tên");
                dialog.setContentView(R.layout.activity_new_group);
                break;
            case R.id.tvSex:
                break;
            case R.id.tvBirthday:
                DatePickerDialog.newInstance(this, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show(getFragmentManager(), "datePicker");
                break;
            case R.id.etProfileAddr:
                break;
            case R.id.etProfileEmail:
                break;
            case R.id.etProfileDetail:
                break;
            case R.id.tvSave:
                break;
        }
    }
}
