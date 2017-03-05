package com.kharchenkovitaliypt.android.sample;

import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SeekBar;

import com.kharchenkovitaliypt.android.ui.Gravity.Horizontal;
import com.kharchenkovitaliypt.android.ui.Gravity.Vertical;
import com.kharchenkovitaliypt.android.ui.ScaleType;
import com.kharchenkovitaliypt.android.ui.ViewUtils;
import com.kharchenkovitaliypt.android.ui.listener.SimpleSeekBarChangeListener;

import kotlin.Unit;
import kotlin.jvm.functions.Function3;

public class ImageViewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final ViewGroup containerView = (ViewGroup) findViewById(R.id.container);

        final ImageView imageView = new ImageView(this);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imageView.setImageResource(R.drawable.rock_girl);
        final Drawable image = imageView.getDrawable();
        containerView.addView(imageView);

        imageView.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                Matrix matrix = ViewUtils.calculateScaleMatrix(
                        imageView, ScaleType.CROP, new Vertical.CENTER(Horizontal.CENTER),
                        image.getIntrinsicWidth(), image.getIntrinsicHeight(),
                        new Matrix()
                );
                imageView.setImageMatrix(matrix);
            }
        });

        ((SeekBar) findViewById(R.id.seekBarWidth)).setOnSeekBarChangeListener(
                new SimpleSeekBarChangeListener(new Function3<SeekBar, Integer, Boolean, Unit>() {
                    @Override
                    public Unit invoke(SeekBar seekBar, Integer progress, Boolean fromUser) {
                        ViewGroup.LayoutParams lp = imageView.getLayoutParams();
                        lp.width = (int) (containerView.getWidth() * (progress / 100f));
                        imageView.setLayoutParams(lp);
                        return Unit.INSTANCE;
                    }
                })
        );
        ((SeekBar) findViewById(R.id.seekBarHeight)).setOnSeekBarChangeListener(
                new SimpleSeekBarChangeListener(new Function3<SeekBar, Integer, Boolean, Unit>() {
                    @Override
                    public Unit invoke(SeekBar seekBar, Integer progress, Boolean fromUser) {
                        ViewGroup.LayoutParams lp = imageView.getLayoutParams();
                        lp.height = (int) (containerView.getHeight()  * (progress / 100f));
                        imageView.setLayoutParams(lp);
                        return Unit.INSTANCE;
                    }
                })
        );
    }
}
