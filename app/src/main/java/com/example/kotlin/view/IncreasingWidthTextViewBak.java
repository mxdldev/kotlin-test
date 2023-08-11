package com.example.kotlin.view;

/**
 * Description: <IncreasingWidthTextView><br>
 * Author:      mxdl<br>
 * Date:        2023/8/11<br>
 * Version:     V1.0.0<br>
 * Update:     <br>
 */
import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;

import androidx.appcompat.widget.AppCompatTextView;

public class IncreasingWidthTextViewBak extends AppCompatTextView {
    private static final int WIDTH_INCREMENT = 10; // 控制每次递增的宽度
    private static final int DELAY_MS = 50; // 控制递增的延迟时间

    private int targetWidth; // 目标宽度
    private int currentWidth; // 当前宽度
    private Handler handler;
    private Runnable widthIncreaseRunnable;

    public IncreasingWidthTextViewBak(Context context) {
        super(context);
        init();
    }

    public IncreasingWidthTextViewBak(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public IncreasingWidthTextViewBak(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        handler = new Handler();
        widthIncreaseRunnable = new Runnable() {
            @Override
            public void run() {
                if (currentWidth < targetWidth) {
                    currentWidth += WIDTH_INCREMENT;
                    setWidth(currentWidth);
                    Log.v("MYTAG",currentWidth+"");
                    handler.postDelayed(this, DELAY_MS);
                }
            }
        };
    }

    public void setTextWithIncreasingWidth(String text) {
        setText(text);
        currentWidth = 0;
        targetWidth = (int) getPaint().measureText(text); // 获取文本的宽度
        handler.removeCallbacks(widthIncreaseRunnable);
        handler.post(widthIncreaseRunnable);
    }
}

