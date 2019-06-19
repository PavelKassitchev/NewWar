package com.pavka;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

import java.awt.*;

public class MileStone extends Image {
    BitmapFont font = new BitmapFont();
    int days = 16;

    public void draw(Batch batch, float alpha) {

        font.getData().setScale(0.8f);
        font.draw(batch, String.valueOf(days), 60, 60);
    }

}
