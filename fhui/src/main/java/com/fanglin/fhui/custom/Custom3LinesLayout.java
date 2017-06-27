package com.fanglin.fhui.custom;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fanglin.fhui.R;

import java.text.DecimalFormat;

/**
 * Author: Created by lizhixin on 2016/6/6 11:56.
 * 自定义的三排文字控件
 * 功能：设置文字、文字大小、文字颜色、原点点击事件、是否有触碰边界后的回弹效果
 */
public class Custom3LinesLayout extends FrameLayout {

    private int paddingLeft, paddingTop, dotOffset;
    private int lastX = 0;
    private int lastY = 0;

    private TextView tvTop, tvMiddle, tvBottom;
    private View vDirection, lineLeft, lineRight;
    private boolean allowSpringback;//是否允许回弹效果
    private boolean allowSlide;//是否允许滑动
    private DisplayMetrics dm;
    private OnImageDotClickListener imageDotClickListener;

    public Custom3LinesLayout(Context context) {
        this(context, null);
    }

    public Custom3LinesLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        View view = View.inflate(context, R.layout.layout_custom_3_lines, null);
        tvTop = (TextView) view.findViewById(R.id.tvTop);
        tvMiddle = (TextView) view.findViewById(R.id.tvMiddle);
        tvBottom = (TextView) view.findViewById(R.id.tvBottom);

        ImageView ivDotBg = (ImageView) view.findViewById(R.id.ivDotBg);

        vDirection = view.findViewById(R.id.vDirection);
        lineLeft = view.findViewById(R.id.lineLeft);
        lineRight = view.findViewById(R.id.lineRight);

        ivDotBg.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (imageDotClickListener != null) {
                    imageDotClickListener.onImageDotClick();
                }
            }
        });

        ivDotBg.setOnLongClickListener(new OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (longClickListener != null) {
                    longClickListener.onImageDotLongClick();
                }
                return false;
            }
        });
        createCustomAttrs(context, attrs, view);
        dm = context.getResources().getDisplayMetrics();
        paddingLeft = getResources().getDimensionPixelOffset(R.dimen.custom_line_width);
        dotOffset = getResources().getDimensionPixelOffset(R.dimen.custom_dp_of_10);
        paddingTop = getResources().getDimensionPixelOffset(R.dimen.custom_dp_of_60);
    }

    private void createCustomAttrs(Context context, AttributeSet attrs, View view) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.custom3LinesStyle);
        String textTop = typedArray.getString(R.styleable.custom3LinesStyle_textTop);
        String textMiddle = typedArray.getString(R.styleable.custom3LinesStyle_textMiddle);
        String textBottom = typedArray.getString(R.styleable.custom3LinesStyle_textBottom);
        allowSpringback = typedArray.getBoolean(R.styleable.custom3LinesStyle_allowSpringback, true);
        allowSlide = typedArray.getBoolean(R.styleable.custom3LinesStyle_allowSlide, true);
        int textColor = typedArray.getColor(R.styleable.custom3LinesStyle_textColorAll, context.getResources().getColor(R.color.white));
        int bgResId = typedArray.getResourceId(R.styleable.custom3LinesStyle_bgRes, 0);

        fill3Text(textTop, textMiddle, textBottom);
        setTextColorAll(textColor);
        if (bgResId > 0)
            view.setBackgroundResource(bgResId);
        typedArray.recycle();
        addView(view);
    }

    /**
     * 初始化时需要填充的文字
     *
     * @param textTop    顶部文字
     * @param textMiddle 中间文字
     * @param textBottom 底部文字
     */
    private void fill3Text(String textTop, String textMiddle, String textBottom) {
        setTextTop(textTop);
        setTextMiddle(textMiddle);
        setTextBottom(textBottom);
    }

    /**
     * 设置三排文字的大小
     *
     * @param textSize size
     */
    public void setTextSizeAll(float textSize) {
        tvTop.setTextSize(textSize);
        tvMiddle.setTextSize(textSize);
        tvBottom.setTextSize(textSize);
    }

    /**
     * 设置三排文字的颜色
     *
     * @param textColor color
     */
    public void setTextColorAll(int textColor) {
        tvTop.setTextColor(textColor);
        tvMiddle.setTextColor(textColor);
        tvBottom.setTextColor(textColor);
    }

    /**
     * 设置顶部文字
     *
     * @param text text
     */
    public void setTextTop(String text) {
        tvTop.setText(text);
    }

    /**
     * 设置中部文字
     *
     * @param text text
     */
    public void setTextMiddle(String text) {
        tvMiddle.setText(text);
    }

    /**
     * 设置底部文字
     *
     * @param text text
     */
    public void setTextBottom(String text) {
        tvBottom.setText(text);
    }

    /**
     * 设置在父控件中的位置，目前父控件支持FrameLayout,LinearLayout,RelativeLayout三种
     *
     * @param left 左边距 px
     * @param top  上边距 px
     */
    public void setPosition(int left, int top) {
        if (getLayoutParams() instanceof FrameLayout.LayoutParams) {
            FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) getLayoutParams();
            lp.setMargins(left, top, 0, 0);
            setLayoutParams(lp);
        } else if (getLayoutParams() instanceof RelativeLayout.LayoutParams) {
            RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) getLayoutParams();
            lp.setMargins(left, top, 0, 0);
            setLayoutParams(lp);
        } else if (getLayoutParams() instanceof LinearLayout.LayoutParams) {
            LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) getLayoutParams();
            lp.setMargins(left, top, 0, 0);
            setLayoutParams(lp);
        }
    }


    /**
     * 通过location设定位置
     *
     * @param location 格式：0.5,0.2
     */
    public void setPositionByLocation(String location) {
        int[] pos = getLeftAndTop(location);
        int aleft = pos[0];
        int atop = pos[1];
        aleft = aleft < 0 ? 0 : aleft;
        atop = atop < 0 ? 0 : atop;
        setPosition(aleft, atop);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int boundWidth, boundHeight;
        if (allowSlide) {

            /**
             * 边界默认取父控件的宽高，如果没有父控件则取屏幕的宽高
             */
            if (getParent() != null && getParent() instanceof ViewGroup) {
                ViewGroup viewGroup = (ViewGroup) getParent();
                boundWidth = viewGroup.getMeasuredWidth();
                boundHeight = viewGroup.getMeasuredHeight();
            } else {
                boundWidth = dm.widthPixels;
                boundHeight = dm.heightPixels;
            }

            if (allowSpringback) {
                /**
                 * 触碰边界有回弹效果
                 */
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        lastX = (int) event.getRawX();
                        lastY = (int) event.getRawY();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        int dx = (int) (event.getRawX() - lastX);
                        int dy = (int) (event.getRawY() - lastY);

                        int left = getLeft() + dx;
                        int right = getRight() + dx;
                        int top = getTop() + dy;
                        int bottom = getBottom() + dy;

                        layout(left, top, right, bottom);
                        lastX = (int) event.getRawX();
                        lastY = (int) event.getRawY();
                        break;
                    case MotionEvent.ACTION_UP:
                        int leftUp = getLeft();
                        int rightUp = getRight();
                        int topUp = getTop();
                        int bottomUp = getBottom();
                        boolean changed = false;

                        if (leftUp < 0) {
                            leftUp = 0;
                            rightUp = getWidth();
                            changed = true;
                        }
                        if (rightUp > boundWidth) {
                            rightUp = boundWidth;
                            leftUp = rightUp - getWidth();
                            changed = true;
                        }
                        if (topUp < 0) {
                            topUp = 0;
                            bottomUp = getHeight();
                            changed = true;
                        }
                        if (bottomUp > boundHeight) {
                            bottomUp = boundHeight;
                            topUp = bottomUp - getHeight();
                            changed = true;
                        }
                        if (changed)
                            layout(leftUp, topUp, rightUp, bottomUp);
                        break;
                    default:
                        break;
                }
            } else {
                /**
                 * 触碰边界无回弹效果
                 */
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        lastX = (int) event.getRawX();
                        lastY = (int) event.getRawY();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        int dx = (int) (event.getRawX() - lastX);
                        int dy = (int) (event.getRawY() - lastY);

                        int left = getLeft() + dx;
                        int right = getRight() + dx;
                        int top = getTop() + dy;
                        int bottom = getBottom() + dy;

                        if (left < 0) {
                            left = 0;
                            right = getWidth();
                        }
                        if (right > boundWidth) {
                            right = boundWidth;
                            left = right - getWidth();
                        }
                        if (top < 0) {
                            top = 0;
                            bottom = getHeight();
                        }
                        if (bottom > boundHeight) {
                            bottom = boundHeight;
                            top = bottom - getHeight();
                        }

                        layout(left, top, right, bottom);
                        lastX = (int) event.getRawX();
                        lastY = (int) event.getRawY();
                        break;
                    case MotionEvent.ACTION_UP:

                        break;
                    default:
                        break;
                }
            }
            return true;
        } else {
            return super.onTouchEvent(event);
        }
    }

    public void setImageDotClickListener(OnImageDotClickListener listener) {
        this.imageDotClickListener = listener;
    }

    OnImageDotLongClickListener longClickListener;

    public void setImageDotLongClickListener(OnImageDotLongClickListener longClickListener) {
        this.longClickListener = longClickListener;
    }

    public interface OnImageDotLongClickListener {
        void onImageDotLongClick();
    }

    public interface OnImageDotClickListener {
        void onImageDotClick();
    }

    /**
     * 缺口的朝向
     */
    private boolean isLeft = false;

    public boolean getIsLeft() {
        return isLeft;
    }

    public String getLocStrOfLeftTop() {
        int aleft = getLeft();
        int atop = getTop();

        int posT, posL;
        if (isLeft) {
            posL = aleft + paddingLeft;
            posT = atop + paddingTop;
        } else {
            posL = aleft;
            posT = atop + paddingTop;
        }

        DecimalFormat decimalFormat = new DecimalFormat("#0.00");
        double ratioL = (posL * 100) / (dm.widthPixels * 100.0);
        double ratioT = (posT * 100) / (dm.widthPixels * 100.0);

        return decimalFormat.format(ratioL) + "," + decimalFormat.format(ratioT);
    }

    public void setDirection(boolean left) {
        isLeft = left;
        int lastLeft = getLeft();
        int lastRight = getRight();
        if (left) {
            vDirection.setPadding(paddingLeft, 0, 0, 0);
            lineLeft.setVisibility(INVISIBLE);
            lineRight.setVisibility(VISIBLE);
            tvTop.setGravity(Gravity.RIGHT);
            tvMiddle.setGravity(Gravity.RIGHT);
            tvBottom.setGravity(Gravity.RIGHT);
        } else {
            vDirection.setPadding(0, 0, 0, 0);
            lineLeft.setVisibility(VISIBLE);
            lineRight.setVisibility(INVISIBLE);
            tvTop.setGravity(Gravity.LEFT);
            tvMiddle.setGravity(Gravity.LEFT);
            tvBottom.setGravity(Gravity.LEFT);
        }
        if (allowSlide)
            handlePosition(lastLeft, lastRight);
    }


    private void handlePosition(int lastLeft, int lastRight) {
        int boundWidth;
        if (getParent() != null && getParent() instanceof ViewGroup) {
            ViewGroup viewGroup = (ViewGroup) getParent();
            boundWidth = viewGroup.getMeasuredWidth();
        } else {
            boundWidth = dm.widthPixels;
        }
        int lastTop = getTop();


        if (lastRight >= boundWidth) {
            lastLeft = boundWidth - getWidth() - dotOffset;
        }

        setPosition(lastLeft, lastTop);
    }

    public int[] getDotPositionOfLeftTop(String location) {
        int[] pos = new int[]{0, 0};
        if (!TextUtils.isEmpty(location)) {
            String[] res = location.split(",");
            if (res.length == 2) {
                pos[0] = percentStr2Int(res[0], dm.widthPixels);
                pos[1] = percentStr2Int(res[1], dm.widthPixels);
            }
        }
        return pos;
    }

    private int[] getLeftAndTop(String location) {
        int[] dotpos = getDotPositionOfLeftTop(location);
        int aleft = dotpos[0];
        int atop = dotpos[1];
        int[] pos = new int[]{0, 0};

        if (isLeft) {
            pos[0] = aleft - paddingLeft;
            pos[1] = atop - paddingTop;
        } else {
            pos[0] = aleft;
            pos[1] = atop - paddingTop;
        }
        return pos;
    }

    private int percentStr2Int(String strDouble, int distance) {
        try {
            double d = Double.parseDouble(strDouble);
            return (int) (d * distance + 0.5);
        } catch (Exception e) {
            return 0;
        }
    }

}
