package com.pavka;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Array;

public class Control extends Table {
    public Hex hex;
    public Array<Force> forces;

    public Control(Hex hex, Array<Force> forces) {
        this.hex = hex;
        this.forces = forces;
        BitmapFont font = new BitmapFont();
        font.getData().setScale(0.7f);
        Label costLabel = new Label("Cost: " + hex.cell.getTile().getProperties().get("cost"), new Label.LabelStyle(font, new Color(1, 0, 0, 1)));
        add(costLabel);
        row();
        Label forceLabel = new Label("Forces: ", new Label.LabelStyle(font, new Color(1, 0, 0, 1)));
        add(forceLabel);
        setPosition(hex.getX(), hex.getY());

    }
}
