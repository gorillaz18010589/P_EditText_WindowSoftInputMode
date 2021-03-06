package com.example.p_edittext_windowsoftinputmode;
/*
1,stateUnspecified:軟體盤的狀態沒有制定，系統將選擇一個合適的狀態或依賴於主題的設定

2.stateUnchanged：當這個activity出現時，軟鍵盤將一直保持在上一個activity裡的狀態，無論是隱藏還是顯示

3.stateHidden：使用者選擇activity時，軟鍵盤總是被隱藏

4.stateAlwaysHidden：當該Activity主視窗獲取焦點時，軟鍵盤也總是被隱藏的

5.stateVisible：軟鍵盤通常是可見的

6.stateAlwaysVisible：使用者選擇activity時，軟鍵盤總是顯示的狀態

7.adjustUnspecified：預設設定，通常由系統自行決定是隱藏還是顯示

8.adjustResize：該Activity總是調整螢幕的大小以便留出軟鍵盤的空間

9adjustPan：當前視窗的內容將自動移動以便當前焦點從不被鍵盤覆蓋和使用者能總是看到輸入內容的部分*/

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;

import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceControl;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.EditText;
import android.widget.Toast;

import com.example.displayutils.DisplayUtils;
import com.example.keyboardutil.KeyboardUtil;

public class MainActivity extends AppCompatActivity implements KeyboardUtil.OnSoftKeyboardChangeListener {
    private int deviceHeight;
    private String TAG = "hank";
    private EditText edText;
    private ConstraintLayout container, conTop;
    private ViewTreeObserver.OnGlobalLayoutListener mOnGlobalLayoutListener;
    private int screenHeight;
    private int keyHeight;
    public boolean isKeyboardVisible;// true-->弹出,false-->末弹出,可直接继承这activity,然后通过这个标志位判断软键盘是否弹出

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mOnGlobalLayoutListener = KeyboardUtil.observeSoftKeyboard(this, this);

        init();
    }

    private void init() {
        edText = findViewById(R.id.edText);
        container = findViewById(R.id.container);
        conTop = findViewById(R.id.conTop);
        deviceHeight = DisplayUtils.getRealScreenHeight(this);
        Log.v(TAG, "裝置高度：" + deviceHeight);

        screenHeight = this.getWindowManager().getDefaultDisplay().getHeight();
        //阀值设置为屏幕高度的1/3
        keyHeight = screenHeight / 3;
        container.addOnLayoutChangeListener(onLayoutChangeListener);
    }

    View.OnLayoutChangeListener onLayoutChangeListener = new View.OnLayoutChangeListener() {
        @Override
        public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
            Log.v(TAG, "onLayoutChangeListener()" + "botto:" + bottom + "/oldBottom:" + oldBottom);
            Rect rect = new Rect();
            getWindow().getDecorView().getWindowVisibleDisplayFrame(rect);
            if (oldBottom != 0 && bottom != 0 && (oldBottom - bottom > keyHeight)) {
//                conTop.setVisibility(View.VISIBLE);
                setPercent(0.9f);
                Log.v(TAG, "onLayoutChangeListener() -> 縮回");
            } else if (oldBottom != 0 && bottom != 0 && (bottom - oldBottom > keyHeight)) {
//                conTop.setVisibility(View.GONE);
                setPercent(0.1f);
                Log.v(TAG, "onLayoutChangeListener() -> 彈出");
            }

        }
    };

    private void setPercent(float percent) {
        ConstraintSet constraintSet = new ConstraintSet();
        constraintSet.clone(container);
        constraintSet.constrainPercentHeight(R.id.edText, percent);
        constraintSet.applyTo(container);
    }


    //當軟體鍵盤開關時
    @Override
    public void onSoftKeyBoardChange(int softKeyboardHeight, boolean visible) {
        isKeyboardVisible = visible;
        Log.v(TAG, "onSoftKeyBoardChange() -> softKeyboardHeight:" + softKeyboardHeight + "/isVisible:" + visible);
    }


    //取消軟體鍵盤監聽
    @Override
    protected void onDestroy() {
        super.onDestroy();
        KeyboardUtil.removeSoftKeyboardObserver(this, mOnGlobalLayoutListener);
    }


    public void toPage2(View view) {
        startActivity(new Intent(MainActivity.this,SecondActivity.class));
    }
}