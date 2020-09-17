package com.example.p_edittext_windowsoftinputmode;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;

import android.os.Bundle;
import android.util.Log;

import com.example.keyboardutil.SoftKeyBoardListener;

public class SecondActivity extends AppCompatActivity {
    private SoftKeyBoardListener softKeyBoardListener;
    private ConstraintLayout container, conTop;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        container = findViewById(R.id.container);
        conTop = findViewById(R.id.conTop);
        setSoftKeyBoardListener();
    }


    private void setSoftKeyBoardListener() {
        softKeyBoardListener = new SoftKeyBoardListener(this);
        //软键盘状态监听
        softKeyBoardListener.setListener(new SoftKeyBoardListener.OnSoftKeyBoardChangeListener() {
            @Override
            public void keyBoardShow(int height) {
                //软键盘已经显示，做逻辑
                setPercent(0.9f);
                Log.v("hank","鍵盤彈出高度為:");
            }

            @Override
            public void keyBoardHide(int height) {
                //软键盘已经隐藏,做逻辑
                setPercent(0.1f);
                Log.v("hank","鍵盤關閉高度為:");
            }
        });

    }


    private void setPercent(float percent) {
        ConstraintSet constraintSet = new ConstraintSet();
        constraintSet.clone(container);
        constraintSet.constrainPercentHeight(R.id.conTop, percent);
        constraintSet.applyTo(container);
    }
}