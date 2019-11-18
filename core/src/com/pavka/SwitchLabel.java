package com.pavka;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class SwitchLabel extends Label {
    public static BitmapFont font = new BitmapFont();
    public static Label.LabelStyle extendStyle = new Label.LabelStyle(font, new Color(1, 0, 0, 1));

    public SwitchLabel() {
        super("", extendStyle);
        Texture textureP = new Texture("plus-sign-in-circle.png");
        Sprite spriteP = new Sprite(textureP);
        Skin plusSkin = new Skin();
        plusSkin.add("image", spriteP);
        extendStyle.background = plusSkin.getDrawable("image");
    }
}
