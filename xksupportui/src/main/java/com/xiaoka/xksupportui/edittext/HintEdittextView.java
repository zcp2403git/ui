package com.xiaoka.xksupportui.edittext;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.FontMetrics;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.EditText;

import com.xiaoka.xksupportui.R;
import com.xiaoka.xksupportui.utils.ScreenUtil;


public class HintEdittextView extends EditText {
 
    float icon_width = 0,edit_imageheight = 0 ;
    float textSize = 0;
    int textColor = 0xFF000000;
    Drawable mDrawable;
    int DrawableResourceId;
    Paint paint;
    String hint_text="";
    int leftMargin=30;
 
    public HintEdittextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        InitResource(context, attrs);
        InitPaint();
    }
 
    private void InitResource(Context context, AttributeSet attrs) {
        TypedArray mTypedArray = context.obtainStyledAttributes(attrs, R.styleable.HintIconView);
        float density = context.getResources().getDisplayMetrics().density;
        icon_width = mTypedArray.getDimension(R.styleable.HintIconView_edit_imageheight, 18 * density + 0.5F);
        edit_imageheight = mTypedArray.getDimension(R.styleable.HintIconView_edit_imageheight, 18 * density + 0.5F);
        textColor = mTypedArray.getColor(R.styleable.HintIconView_edit_textColor, 0xFF848484);
        textSize = mTypedArray.getDimension(R.styleable.HintIconView_edit_textSize, 14 * density + 0.5F);
        hint_text = (mTypedArray.getString(R.styleable.HintIconView_hint_text)==null)?hint_text:mTypedArray.getString(R.styleable.HintIconView_hint_text);
        DrawableResourceId = (mTypedArray.getResourceId(R.styleable.HintIconView_hint_drawable,0) ==0)?DrawableResourceId:mTypedArray.getResourceId(R.styleable.HintIconView_hint_drawable, 0);
        mTypedArray.recycle();
        leftMargin= ScreenUtil.dp2Px(context, 10);
    }
 
    private void InitPaint() {
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(textColor);
        paint.setTextSize(textSize);
    }
 
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        DrawSearchIcon(canvas);
    }
 
    private void DrawSearchIcon(Canvas canvas) {
        if (this.getText().toString().length() == 0) {
            float textWidth = paint.measureText(hint_text);
            float textHeight = getFontLeading(paint);
 
//            float dx = (getWidth() - icon_width - textWidth - 8) / 2;//中间
//            float dy = (getHeight() - icon_width) / 2;//中间
            float dx = leftMargin;
            float dy = (getHeight() - icon_width) / 2;
            canvas.save();
            canvas.translate(getScrollX() + dx, getScrollY() + dy);
            if (mDrawable != null) {
                mDrawable.draw(canvas);
            }
            canvas.drawText(hint_text, getScrollX() + icon_width + 8, getScrollY() + (getHeight() - (getHeight() - textHeight) / 2) - paint.getFontMetrics().bottom - dy, paint);
            canvas.restore();
        }
    }
 
    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (mDrawable == null) {
            try {
                mDrawable = getContext().getResources().getDrawable(DrawableResourceId);
                mDrawable.setBounds(0, 0, (int) icon_width, (int) icon_width);
            } catch (Exception e) {
 
            }
        }
    }
 
    @Override
    protected void onDetachedFromWindow() {
        if (mDrawable != null) {
            mDrawable.setCallback(null);
            mDrawable = null;
        }
        super.onDetachedFromWindow();
    }
 
    public float getFontLeading(Paint paint) {
        FontMetrics fm = paint.getFontMetrics();
        return fm.bottom - fm.top;
    }
 
}