package com.touchmenotapps.marketplace.framework;

import android.app.TimePickerDialog;
import android.content.Context;
import android.support.v7.widget.AppCompatTextView;
import android.widget.TimePicker;

import java.sql.Time;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by arindamnath on 31/01/18.
 */

public class DateTimeUtil {

    public DateTimeUtil() {

    }

    public void showTimePicker(Context context, final AppCompatTextView textView) {
        final Calendar c = Calendar.getInstance();
        int mHour = c.get(Calendar.HOUR_OF_DAY);
        int mMinute = c.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(context,
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        Time tme = new Time(hourOfDay, minute, 0);//seconds by default set to zero
                        Format formatter = new SimpleDateFormat("h:mm a");
                        textView.setText(formatter.format(tme));
                    }
                }, mHour, mMinute, false);
        timePickerDialog.show();
    }
}
