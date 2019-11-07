package com.ArmGuide.tourapplication.ui.home;

import android.content.res.AssetManager;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;

import su.levenetc.android.textsurface.Text;
import su.levenetc.android.textsurface.TextBuilder;
import su.levenetc.android.textsurface.TextSurface;
import su.levenetc.android.textsurface.animations.Alpha;
import su.levenetc.android.textsurface.animations.ChangeColor;
import su.levenetc.android.textsurface.animations.Circle;
import su.levenetc.android.textsurface.animations.Delay;
import su.levenetc.android.textsurface.animations.Parallel;
import su.levenetc.android.textsurface.animations.Rotate3D;
import su.levenetc.android.textsurface.animations.Sequential;
import su.levenetc.android.textsurface.animations.ShapeReveal;
import su.levenetc.android.textsurface.animations.SideCut;
import su.levenetc.android.textsurface.animations.Slide;
import su.levenetc.android.textsurface.animations.TransSurface;
import su.levenetc.android.textsurface.contants.Align;
import su.levenetc.android.textsurface.contants.Direction;
import su.levenetc.android.textsurface.contants.Pivot;
import su.levenetc.android.textsurface.contants.Side;

/**
 * Created by Eugene Levenetc.
 */
public class TextViewAnimation {

    public static void play(TextSurface textSurface, AssetManager assetManager) {

        final Typeface robotoBlack = Typeface.createFromAsset(assetManager, "fonts/second.ttf");
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setTypeface(robotoBlack);

        Text text = TextBuilder
                .create("ArmGuide")
                .setPaint(paint)
                .setSize(76)
                .setAlpha(0)
                .setColor(Color.RED)
                .setPosition(Align.SURFACE_CENTER).build();

        textSurface.play(
                new Sequential(

                        ShapeReveal.create(text, 1500, SideCut.show(Side.LEFT), false),
                        new Parallel(ShapeReveal.create(text, 500, SideCut.hide(Side.LEFT), false),
                                new Sequential(Delay.duration(200), ShapeReveal.create(text, 400, SideCut.show(Side.LEFT), false))),
                        new Parallel(ShapeReveal.create(text, 500, SideCut.hide(Side.LEFT), false),
                                new Sequential(Delay.duration(200), ShapeReveal.create(text, 1000, SideCut.show(Side.LEFT), true)))
                )
        );

    }

}