package com.cea.utils.ui;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TimePicker;

import com.cea.utils.R;
import com.cea.utils.ui.inject.Inject;
import com.cea.utils.ui.inject.InjectView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Carlos on 14/08/2015.
 */
public class DatePicker extends LinearLayout implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener{

    private Calendar mDate;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("dd / MM / yyyy    HH : mm");

    @InjectView(tag = "date_picker_date")
    private FontFitTextView wgtTxtRouteDate;
    @InjectView(tag = "date_picker_select_date")
    private View wgtBtnSelectDate;
    @InjectView(tag = "date_picker_select_hour")
    private View wgtBtnSelectHour;

    public DatePicker(Context context, AttributeSet attrs) {
        super(context, attrs);
        mDate = Calendar.getInstance();
        addView(LayoutInflater.from(context).inflate(R.layout.datepicker, null));
        Inject.inject(this, this);
        configureAttrs(context, attrs);
        updateDate();
        initEvents();
    }

    private void configureAttrs(Context context, AttributeSet attrs) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.DatePicker);
        if(!a.getBoolean(R.styleable.DatePicker_show_date, true)){
            dateFormat = new SimpleDateFormat("HH : mm");
            wgtBtnSelectDate.setVisibility(View.GONE);
        }
        if(!a.getBoolean(R.styleable.DatePicker_show_hour, true)){
            dateFormat = new SimpleDateFormat("dd / MM / yyyy");
            wgtBtnSelectHour.setVisibility(View.GONE);
        }
    }

    public Calendar getTime() {
        return mDate;
    }

    public void setTime(Date time) {
        mDate.setTime(time);
        updateDate();
    }

    public void setTime(Calendar time) {
        this.mDate = time;
    }

    private void initEvents() {
        wgtBtnSelectDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog dialog = new DatePickerDialog(getContext(), DatePicker.this, mDate.get(Calendar.YEAR), mDate.get(Calendar.MONTH), mDate.get(Calendar.DATE));
                dialog.show();
            }
        });
        wgtBtnSelectHour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog dialog = new TimePickerDialog(getContext(), DatePicker.this, mDate.get(Calendar.HOUR_OF_DAY), mDate.get(Calendar.MINUTE), true);
                dialog.show();
            }
        });
    }

    @Override
    public void onDateSet(android.widget.DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        mDate.set(year, monthOfYear, dayOfMonth);
        updateDate();
    }

    private void updateDate() {
        wgtTxtRouteDate.setText(dateFormat.format(mDate.getTime()));
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        mDate.set(Calendar.HOUR_OF_DAY, hourOfDay);
        mDate.set(Calendar.MINUTE, minute);
        updateDate();
    }
}
