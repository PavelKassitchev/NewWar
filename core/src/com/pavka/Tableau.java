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
    Label baseLabel;
    Label[] forceLabels;

    public Tableau(Play play, Hex hex, Array<Force> forces, Base base) {
        this.play = play;
        this.hex = hex;
        this.forces = forces;
        this.base = base;
    }

    public void init() {

        BitmapFont font = new BitmapFont();
        font.getData().setScale(0.7f);
        Label.LabelStyle hexStyle = new Label.LabelStyle(font, new Color(1, 0, 0, 1));
        Pixmap labelColor = new Pixmap(1, 1, Pixmap.Format.RGB888);
        labelColor.setColor(Color.GOLD);
        labelColor.fill();
        hexStyle.background = new Image(new Texture(labelColor)).getDrawable();
        hexLabel = new Label("Cost: " + hex.cell.getTile().getProperties().get("cost") + " Crops: " + hex.currentHarvest,
                hexStyle);
        hexLabel.setWrap(true);
        play.addActor(hexLabel);
        add(hexLabel).width(140f);
        hexLabel.setDebug(true);
        totalHeight += hexLabel.getPrefHeight();
        row();

        if (base != null) {
            Label.LabelStyle baseStyle = new Label.LabelStyle(font, new Color(1, 0, 0, 1));
            labelColor.setColor(Color.CORAL);;
            labelColor.fill();
            baseStyle.background = new Image(new Texture(labelColor)).getDrawable();
            baseLabel = new Label(base.getGeneralInfo(), baseStyle);
            baseLabel.setWrap(true);

            play.addActor(baseLabel);
            add(baseLabel).width(140f);
            baseLabel.pack();
            baseLabel.setWidth(140f);
            baseLabel.setDebug(true);
            totalHeight += baseLabel.getPrefHeight();
            row();
        }

        if (forces != null && forces.size > 0) {
            Label.LabelStyle forceStyle = new Label.LabelStyle(font, new Color(1, 0, 0, 1));
            labelColor.setColor(Color.CYAN);;
            labelColor.fill();
            forceStyle.background = new Image(new Texture(labelColor)).getDrawable();

            forceLabels = new Label[forces.size];
            int i = 0;
            for (Force f : forces) {
                forceLabels[i] = new Label(f.getGeneralInfo(), forceStyle);
                forceLabels[i].setWrap(true);
                play.addActor(forceLabels[i]);
                add(forceLabels[i]).width(140f);
                forceLabels[i].pack();
                forceLabels[i].setWidth(140f);
                forceLabels[i].setDebug(true);
                totalHeight += forceLabels[i].getPrefHeight();
                i++;
                if(i < forces.size) row();
            }
        }
        if(base == null && (forces == null || forces.isEmpty())) hex.isSelected = true;

        setPosition(hex.getX(), hex.getY());
        setBounds(getX() - 8, getY() - 8, 164, totalHeight);
        setTouchable(Touchable.enabled);
        setVisible(true);
        align(1);
        Skin skin = new Skin();
        Color color = new Color(0, 0, 0, 1);
        skin.add("color", color);
        TextureRegion region = new TextureRegion();
        region.setRegion(new Texture("square-32.png"));
        skin.add("region", region);
        setSkin(skin);
        setBackground(skin.getDrawable("region"));
        labelColor.dispose();
    }
}
