package com.fanglin.fhui.datapicker;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.fanglin.fhui.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class TimePickDialog extends Activity implements OnClickListener {

    Button btn_cancel;
    Button btn_ok;
    DatePicker datePicker;
    TextView tv_title;
    Calendar mCalendar = Calendar.getInstance();
    public static final String PICKERTITLE = "Choose Date";
    public static final String PICKERYES = "OK";
    public static final String PICKERNO = "Cancel";

    long time = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_time_pick);
        btn_cancel = (Button) findViewById(R.id.btn_cancel);
        btn_ok = (Button) findViewById(R.id.btn_ok);
        datePicker = (DatePicker) findViewById(R.id.datePicker);
        tv_title = (TextView) findViewById(R.id.tv_title);

        btn_cancel.setOnClickListener(this);
        btn_ok.setOnClickListener(this);
        time = 0;
        try {
            time = getIntent().getLongExtra("time", 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
        boolean english = getIntent().getBooleanExtra("english", false);
        if (english) {
            tv_title.setText(PICKERTITLE);
            btn_ok.setText(PICKERYES);
            btn_cancel.setText(PICKERNO);
        }
        if (time != 0) {
            mCalendar.setTimeInMillis(time);
            datePicker.setCalendar(mCalendar);
//			datePicker.setYear(mCalendar.get(Calendar.YEAR));
//			datePicker.setMonth(mCalendar.get(Calendar.MONTH));
//			datePicker.setDay(mCalendar.get(Calendar.DAY_OF_MONTH));
        } else {
            datePicker.setCalendar(mCalendar);
//			datePicker.setYear(mCalendar.get(Calendar.YEAR));
//			datePicker.setMonth(mCalendar.get(Calendar.MONTH));
//			datePicker.setDay(mCalendar.get(Calendar.DAY_OF_MONTH));
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_ok) {
            mCalendar.set(Calendar.YEAR, datePicker.getYear());
            mCalendar.set(Calendar.MONTH, datePicker.getMonth());
            mCalendar.set(Calendar.DAY_OF_MONTH, datePicker.getDay());
            Intent intent = new Intent();
            intent.putExtra("timestamp", mCalendar.getTimeInMillis());
            intent.putExtra("time", getFormatedTime(mCalendar.getTimeInMillis()));
            setResult(RESULT_OK, intent);
            TimePickDialog.this.finish();
        } else {
            TimePickDialog.this.finish();
        }

    }

    public static String getFormatedTime(long timemiles) {
        SimpleDateFormat df3 = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
        return df3.format(new Date(timemiles));
    }

    public static void pickeDate(Activity activity, long time, int reqCode) {
        Intent intent = new Intent(activity, TimePickDialog.class);
        intent.putExtra("time", time);
        activity.startActivityForResult(intent, reqCode);
    }
}
