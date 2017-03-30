package com.xiaoka.xksupportui.viewgroup;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

/**
 * @author yuefeng
 * @version 3.4.3
 * @desc 用于解决TitleBaseActivity中填充View中包含滑动控件时使用
 * @date 16/3/30
 */
public class NoneSlidingConflictsLinearLayout extends LinearLayout {
	private View mScrollView;

	public NoneSlidingConflictsLinearLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public void setScrollView(View pScrollView) {
		this.mScrollView = pScrollView;
	}

	@Override
	public boolean canScrollVertically(int direction) {
		if (null != this.mScrollView) {
			return this.mScrollView.canScrollVertically(direction);
		}
		return super.canScrollVertically(direction);
	}
}
