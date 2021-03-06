package com.xiaoka.xksupportui.wheel.pickerview;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.support.design.widget.TabLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.xiaoka.xksupportui.R;
import com.xiaoka.xksupportui.utils.ScreenUtil;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


/**
 * @author tuzhong
 * @version 2.2.0
 * @desc 起止日期选择
 * @date 16/3/8
 */

public class DayDistancePicker extends PopupWindow {

    private Context mContext;
    private TabLayout mTabLayout;
    private LayoutInflater mLayoutInflater;
    private View mStartDateView;
    private View mEndDateView;
    private TextView mTvTitle;


    private WheelTime mStartWheelTime;
    private WheelTime mEndWheelTime;

    WheelView mStartYearWheelView;
    WheelView mStartMonthWheelView;
    WheelView mStartDayWheelView;

    WheelView mEndYearWheelView;
    WheelView mEndMonthWheelView;
    WheelView mEndDayWheelView;

    TabLayout.Tab mStartTab;
    TabLayout.Tab mEndTab;

    private OnDatePickConfirmListener mOnDatePickConfirmListener;
    Calendar calendar;
    private int mStartYear;
    private int mEndYear;
    private boolean mIsCheckDate = true;

    public DayDistancePicker(Context context, OnDatePickConfirmListener pOnDatePickConfirmListener) {
        super(context);
        initData(context, pOnDatePickConfirmListener);
    }

    private void initData(Context context, OnDatePickConfirmListener pOnDatePickConfirmListener){
        this.mContext = context;
        this.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        this.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        this.setBackgroundDrawable(new ColorDrawable());
        this.setOutsideTouchable(true);
        this.setFocusable(true);
        this.setAnimationStyle(R.style.daywheel_popwindow_anim_style);
        this.mOnDatePickConfirmListener = pOnDatePickConfirmListener;
        loadView();
    }

    public DayDistancePicker(Context context, OnDatePickConfirmListener pOnDatePickConfirmListener,
                             int startYear, int endYear) {
        super(context);
        this.mStartYear = startYear;
        this.mEndYear = endYear;
        initData(context, pOnDatePickConfirmListener);
    }

    private void loadView() {
        mLayoutInflater = LayoutInflater.from(mContext);
        View _view = mLayoutInflater.inflate(R.layout.widget_start_end_day_pick_layout, null);
        mTabLayout = (TabLayout) _view.findViewById(R.id.tl);
        mStartDateView = _view.findViewById(R.id.start_timepicker);
        mEndDateView = _view.findViewById(R.id.end_timepicker);
        mEndDateView.setVisibility(View.GONE);
        mTvTitle = (TextView) _view.findViewById(R.id.tv_title);

        mStartYearWheelView = (WheelView) mStartDateView.findViewById(R.id.year);
        mStartMonthWheelView = (WheelView) mStartDateView.findViewById(R.id.month);
        mStartDayWheelView = (WheelView) mStartDateView.findViewById(R.id.day);

        mEndYearWheelView = (WheelView) mEndDateView.findViewById(R.id.year);
        mEndMonthWheelView = (WheelView) mEndDateView.findViewById(R.id.month);
        mEndDayWheelView = (WheelView) mEndDateView.findViewById(R.id.day);

        initTab();
        setContentView(_view);

        mStartYearWheelView.addChangingListener(mOnWheelChanged);
        mStartMonthWheelView.addChangingListener(mOnWheelChanged);
        mStartDayWheelView.addChangingListener(mOnWheelChanged);

        mEndYearWheelView.addChangingListener(mOnWheelChanged);
        mEndMonthWheelView.addChangingListener(mOnWheelChanged);
        mEndDayWheelView.addChangingListener(mOnWheelChanged);

        _view.findViewById(R.id.btn_submit).setOnClickListener(v -> {
            if (!checkDate()) {
                return;
            }

            if (mOnDatePickConfirmListener != null) {
                if (!mOnDatePickConfirmListener.onConfirmClick(mStartWheelTime.getYearMonthDayDate(), mEndWheelTime.getYearMonthDayDate())) {
                    return;
                }
            }
            dismiss();
        });

        _view.findViewById(R.id.btn_reset).setOnClickListener(v -> {
            if (mOnDatePickConfirmListener!=null ) {
                mOnDatePickConfirmListener.onResetClick();
            }
        });
    }

    public void setTabLayoutGone() {
        mTabLayout.setVisibility(View.GONE);
    }

    public void setTitle(String title) {
        mTvTitle.setText(title);
    }

    public void setStartYear(int year) {
        WheelTime.setEND_YEAR(year);
    }

    public void setEndYear(int year) {
        WheelTime.setSTART_YEAR(year);
    }

    public void setIsCheckDate(boolean isCheckDate){
        this.mIsCheckDate = isCheckDate;
    }

    private boolean checkDate() {
        if(!mIsCheckDate){
            return true;
        }
        long startLong = getLong(mStartWheelTime.getYearMonthDayDate(), "yyyy.MM.dd");
        long endLong = getLong(mEndWheelTime.getYearMonthDayDate(), "yyyy.MM.dd");
        long currentLong = new Date().getTime();
        if (startLong > endLong) {
            Toast.makeText(mContext,"开始时间不能大于结束时间！",Toast.LENGTH_LONG);
            return false;
        }

        if (startLong > currentLong) {
            Toast.makeText(mContext,"开始时间不能大于当前时间！",Toast.LENGTH_LONG);
            return false;
        }

        if (endLong > currentLong) {
            Toast.makeText(mContext,"结束时间不能大于当前时间！",Toast.LENGTH_LONG);
            return false;
        }
        return true;
    }

    public static long getLong(String dateStr, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        Date date = null;
        try {
            date = sdf.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
            return 0;
        }
        return date.getTime();
    }

    private void initTab() {
        loadDatePickerView();
        mStartTab = mTabLayout.getTabAt(0);
        mEndTab = mTabLayout.getTabAt(1);

        updateTabText(mStartTab, "开始时间\n" + mStartWheelTime.getYearMonthDayDate());
        updateTabText(mEndTab, "结束时间\n" + mEndWheelTime.getYearMonthDayDate());

        mTabLayout.setOnTabSelectedListener(mTabSelectedListener);
    }

    TabLayout.OnTabSelectedListener mTabSelectedListener = new TabLayout.OnTabSelectedListener() {
        @Override
        public void onTabSelected(TabLayout.Tab tab) {
            if (tab.getPosition() == 0) {
                mStartDateView.setVisibility(View.VISIBLE);
                mEndDateView.setVisibility(View.GONE);
            } else {
                mEndDateView.setVisibility(View.VISIBLE);
                mStartDateView.setVisibility(View.GONE);
            }
        }

        @Override
        public void onTabUnselected(TabLayout.Tab tab) {

        }

        @Override
        public void onTabReselected(TabLayout.Tab tab) {

        }
    };

    private void loadDatePickerView() {
        mStartWheelTime = new WheelTime(mStartDateView, WheelTime.Type.YEAR_MONTH_DAY);
        mEndWheelTime = new WheelTime(mEndDateView, WheelTime.Type.YEAR_MONTH_DAY);
        mStartWheelTime.screenheight = ScreenUtil.getHeigth(mContext);
        mEndWheelTime.screenheight = ScreenUtil.getHeigth(mContext);
        //默认选中当前时间
        resetTime();
    }

    private void resetTime() {
        calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int hours = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        if(0 != mStartYear){
            WheelTime.setSTART_YEAR(mStartYear);
        }else{
            WheelTime.setSTART_YEAR(2014);
        }
        if(0 != mEndYear){
            WheelTime.setEND_YEAR(mEndYear);
        }else{
            WheelTime.setEND_YEAR(year);
        }
        mStartWheelTime.setPicker(year, month, day, hours, minute);
        mEndWheelTime.setPicker(year, month, day, hours, minute);
    }

    public void setStartTime(int year, int month, int day) {
        mStartWheelTime.setPicker(year, month, day, 0, 0);
    }

    public void setEndTime(int year, int month, int day) {
        mEndWheelTime.setPicker(year, month, day, 23, 59);
    }

    OnWheelChangedListener mOnWheelChanged = new OnWheelChangedListener() {
        @Override
        public void onChanged(WheelView wheel, int oldValue, int newValue) {

            if (wheel == mStartYearWheelView || wheel == mStartMonthWheelView || wheel == mStartDayWheelView) {
                updateTabText(mStartTab, "开始时间\n" + mStartWheelTime.getYearMonthDayDate());
            } else if (wheel == mEndYearWheelView || wheel == mEndMonthWheelView || wheel == mEndDayWheelView) {
                updateTabText(mEndTab, "结束时间\n" + mEndWheelTime.getYearMonthDayDate());
            }
        }
    };

    private void updateTabText(TabLayout.Tab tab, String str) {
        if (tab == null) return;
        tab.setText(str);
    }

    @Override
    public void dismiss() {
        WindowManager.LayoutParams lp = ((Activity) mContext).getWindow().getAttributes();
        lp.alpha = 1f; //0.0-1.0
        ((Activity) mContext).getWindow().setAttributes(lp);
        super.dismiss();
    }

    public interface OnDatePickConfirmListener {
        boolean onConfirmClick(String pStartDate, String pEndDate);
        void onResetClick();
    }

    public void setOnDatePickConfirmListener(OnDatePickConfirmListener pOnDatePickConfirmListener) {
        mOnDatePickConfirmListener = pOnDatePickConfirmListener;
    }
}
