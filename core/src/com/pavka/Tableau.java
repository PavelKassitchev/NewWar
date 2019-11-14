package com.pavka;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Array;

public class Tableau extends Table {
    Play play;
    Hex hex;
    Array<Force> forces;
    Base base;
    float totalHeight;

    Label hexLabel;

    public Tableau(Play play, Hex hex, Array<Force> forces, Base base) {
        this.play = play;
        this.hex = hex;
        this.forces = forces;
        this.base = base;
    }

    public void init() {

        BitmapFont font = new BitmapFont();
        font.getData().setScale(0.7f);
        Label.LabelStyle style = new Label.LabelStyle(font, new Color(1, 0, 0, 1));
        Pixmap labelColor = new Pixmap(1, 1, Pixmap.Format.RGB888);
        labelColor.setColor(Color.GOLD);
        labelColor.fill();
        style.background = new Image(new Texture(labelColor)).getDrawable();
        hexLabel = new Label("Cost: " + hex.cell.getTile().getProperties().get("cost") + " Crops: " + hex.currentHarvest,
                style);
        //labelColor.setColor(Color.GOLD);
        //costLabel.setWidth(18);
        hexLabel.setWrap(true);
        play.addActor(hexLabel);
        add(hexLabel).width(140f);
        //hexLabel.setAlignment(1);
        hexLabel.setDebug(true);
        totalHeight += hexLabel.getPrefHeight();
        labelColor.dispose();
        row();

        if (forces != null && forces.size > 0) {
            for (Force f : forces) {
                //Label forceLabel = new Label("Forces: " + forces, new Label.LabelStyle(font, new Color(1, 0, 0, 1)));
                Label forceLabel = new Label(f.getGeneralInfo(), new Label.LabelStyle(font, new Color(1, 0, 0, 1)));
                forceLabel.setWrap(true);
                play.addActor(forceLabel);
                add(forceLabel).width(140f);
                forceLabel.pack();
                forceLabel.setWidth(140f);
                //forceLabel.setAlignment(0);
                forceLabel.setDebug(true);
                totalHeight += forceLabel.getPrefHeight();
                row();
            }
        }
        if (base != null) {
            Label baseLabel = new Label(base.getGeneralInfo(), new Label.LabelStyle(font, new Color(1, 0, 0, 1)));
            baseLabel.setWrap(true);

            play.addActor(baseLabel);
            add(baseLabel).width(140f);
            baseLabel.pack();
            baseLabel.setWidth(140f);
            baseLabel.setDebug(true);
            totalHeight += baseLabel.getPrefHeight();
            System.out.println(baseLabel.getHeight());
        }

        System.out.println("Height is " + totalHeight + " Rows: " + getRow(totalHeight));
        setPosition(hex.getX(), hex.getY());
        setBounds(getX() - 8, getY() - 8, 164, totalHeight);
        setTouchable(Touchable.enabled);
        setVisible(true);
        align(1);
        //setColor(0, 0, 0, 1);
        Skin skin = new Skin();
        Color color = new Color(0, 0, 0, 1);
        skin.add("color", color);
        TextureRegion region = new TextureRegion();
        region.setRegion(new Texture("square-32.png"));
        skin.add("region", region);
        setSkin(skin);
        setBackground(skin.getDrawable("region"));
    }
}
