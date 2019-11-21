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

    public Window window;
    Label.LabelStyle styleOne;
    Label.LabelStyle styleTwo;
    String text;


    public SwitchLabel(Window window, String text, Label.LabelStyle styleOne, Label.LabelStyle styleTwo) {
        super(text, styleOne);
        this.styleOne = styleOne;
        this.styleTwo = styleTwo;
        this.window = window;
        this.text = text;
    }

    public SwitchLabel(Window window, String text, Label.LabelStyle styleOne) {
        super(text, styleOne);
        this.styleOne = styleOne;

        this.window = window;
        this.text = text;
    }

    public void changeStyle() {
        if(getStyle() == styleOne) setStyle(styleTwo);
        else setStyle(styleOne);
    }

    public void setText(String newText) {
        text = newText;
    }

    /*public SwitchLabel() {
        super("", extendStyle);
        Texture textureP = new Texture("plus-sign-in-circle.png");
        Sprite spriteP = new Sprite(textureP);
        Skin plusSkin = new Skin();
        plusSkin.add("image", spriteP);
        extendStyle.background = plusSkin.getDrawable("image");
    }*/
}
