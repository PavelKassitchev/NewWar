package com.pavka;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.ui.Button.ButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;

public class Tableau extends Table {

    public static BitmapFont closeFont = new BitmapFont();
    public static Label.LabelStyle closeStyle = new Label.LabelStyle(closeFont, debugTableColor);
    public static BitmapFont font = new BitmapFont();
    public static Label.LabelStyle hexStyle = new Label.LabelStyle(font, new Color(1, 0, 0, 1));
    public static Label.LabelStyle baseStyle = new Label.LabelStyle(font, new Color(1, 0, 0, 1));
    public static Label.LabelStyle forceStyle = new Label.LabelStyle(font, new Color(1, 0, 0, 1));
    public static Label.LabelStyle extendStyle = new Label.LabelStyle(font, new Color(1, 0, 0, 1));

    public Pixmap labelColor = new Pixmap(1, 1, Pixmap.Format.RGB888);

    Play play;
    Hex hex;
    Array<Force> forces;
    Force extendingForce;
    Base base;
    float totalHeight;

    int num;

    Label hexLabel;
    Label baseLabel;
    Label[] forceLabels;
    Label[] extendButtons;
    Label closeLabel;

    public Tableau(int num, Play play, Force force, Label label) {
        this.num = num;
        this.play = play;
        forces = new Array<Force>(force.forces.size());
        for (Force f: force.forces) {
            forces.add(f);
        }
        closeFont.getData().setScale(0.6f);
        labelColor.setColor(Color.GRAY);
        labelColor.fill();
        closeStyle.background = new Image(new Texture(labelColor)).getDrawable();
        closeLabel = new Label("CLOSE", closeStyle);
        closeLabel.setAlignment(Align.right);
        play.addActor(closeLabel);
        add(closeLabel).width(164);
        closeLabel.setDebug(true);
        totalHeight += closeLabel.getPrefHeight();
        row();

        font.getData().setScale(0.7f);

        labelColor.setColor(Color.CYAN);
        labelColor.fill();
        forceStyle.background = new Image(new Texture(labelColor)).getDrawable();
        forceLabels = new Label[force.forces.size()];

        extendButtons = new Label[force.forces.size()];
        Texture texture = new Texture("plus-sign-in-circle.png");
        Sprite sprite = new Sprite(texture);
        Skin plusSkin = new Skin();
        plusSkin.add("image", sprite);
        extendStyle.background = plusSkin.getDrawable("image");

        Table table = new Table();
        int i = 0;

        for(Force f: forces) {
            forceLabels[i] = new Label(f.getGeneralInfo(), forceStyle);
            forceLabels[i].setWrap(true);
            play.addActor(forceLabels[i]);
            table.add().width(12);
            table.add(forceLabels[i]).width(138f);
            forceLabels[i].pack();
            forceLabels[i].setWidth(138f);
            forceLabels[i].setDebug(true);
            totalHeight += forceLabels[i].getPrefHeight();

            extendButtons[i] = new Label("", extendStyle);
            if (f.isUnit) table.add().width(12);
            else {
                play.addActor(extendButtons[i]);
                table.add(extendButtons[i]).width(12);
                extendButtons[i].pack();
                extendButtons[i].setWidth(12);
            }
            i++;
            add(table).width(150);

        }
        setPosition(label.getX(), label.getY());
        setBounds(getX() + 8, getY() + 8, 164, totalHeight + 1);
        setTouchable(Touchable.enabled);
        setVisible(true);
        //align(1);
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

    public Tableau(int num, Play play, Force force){
        this.num = num;
        this.play = play;
        extendingForce = force;
        forces = new Array<Force>(extendingForce.forces.size());
        for (Force f: extendingForce.forces) {
            forces.add(f);
        }

        closeFont.getData().setScale(0.6f);
        labelColor.setColor(Color.GRAY);
        labelColor.fill();
        closeStyle.background = new Image(new Texture(labelColor)).getDrawable();
        closeLabel = new Label("CLOSE", closeStyle);
        closeLabel.setAlignment(Align.right);
        play.addActor(closeLabel);
        add(closeLabel).width(164);
        closeLabel.setDebug(true);
        totalHeight += closeLabel.getPrefHeight();
        row();

        font.getData().setScale(0.7f);

        labelColor.setColor(Color.CYAN);
        labelColor.fill();
        forceStyle.background = new Image(new Texture(labelColor)).getDrawable();
        forceLabels = new Label[force.forces.size()];

        extendButtons = new Label[force.forces.size()];
        Texture texture = new Texture("plus-sign-in-circle.png");
        Sprite sprite = new Sprite(texture);
        Skin plusSkin = new Skin();
        plusSkin.add("image", sprite);
        extendStyle.background = plusSkin.getDrawable("image");

        Table table = new Table();
        int i = 0;

        for(Force f: forces) {
            forceLabels[i] = new Label(f.getGeneralInfo(), forceStyle);
            forceLabels[i].setWrap(true);
            play.addActor(forceLabels[i]);
            table.add().width(12);
            table.add(forceLabels[i]).width(138f);
            forceLabels[i].pack();
            forceLabels[i].setWidth(138f);
            forceLabels[i].setDebug(true);
            totalHeight += forceLabels[i].getPrefHeight();

            extendButtons[i] = new Label("", extendStyle);
            if (f.isUnit) table.add().width(12);
            else {
                play.addActor(extendButtons[i]);
                table.add(extendButtons[i]).width(12);
                extendButtons[i].pack();
                extendButtons[i].setWidth(12);
            }
            i++;
            add(table).width(150);

        }

    }



    public Tableau(int num, Play play, Hex hex, Array<Force> forces, Base base) {
        this.num = num;
        this.play = play;
        this.hex = hex;
        this.forces = forces;
        this.base = base;
        initHex();
    }

    private void initHex() {

        //BitmapFont closeFont = new BitmapFont();
        closeFont.getData().setScale(0.6f);
        //Pixmap labelColor = new Pixmap(1, 1, Pixmap.Format.RGB888);

        //Label.LabelStyle closeStyle = new Label.LabelStyle(closeFont, debugTableColor);
        labelColor.setColor(Color.GRAY);
        labelColor.fill();
        closeStyle.background = new Image(new Texture(labelColor)).getDrawable();
        closeLabel = new Label("CLOSE", closeStyle);
        closeLabel.setAlignment(Align.right);
        play.addActor(closeLabel);
        add(closeLabel).width(164);
        closeLabel.setDebug(true);
        totalHeight += closeLabel.getPrefHeight();
        row();

        //BitmapFont font = new BitmapFont();
        font.getData().setScale(0.7f);

        //Label.LabelStyle hexStyle = new Label.LabelStyle(font, new Color(1, 0, 0, 1));
        labelColor.setColor(Color.GOLD);
        labelColor.fill();
        hexStyle.background = new Image(new Texture(labelColor)).getDrawable();
        hexLabel = new Label("Cost: " + hex.cell.getTile().getProperties().get("cost") + " Crops: " + hex.currentHarvest,
                hexStyle);
        hexLabel.setWrap(true);
        play.addActor(hexLabel);
        add(hexLabel).width(138f);
        hexLabel.setDebug(true);
        totalHeight += hexLabel.getPrefHeight();
        row();

        if (base != null) {
            //Label.LabelStyle baseStyle = new Label.LabelStyle(font, new Color(1, 0, 0, 1));
            labelColor.setColor(Color.CORAL);
            labelColor.fill();
            baseStyle.background = new Image(new Texture(labelColor)).getDrawable();
            baseLabel = new Label(base.getGeneralInfo(), baseStyle);
            baseLabel.setWrap(true);

            play.addActor(baseLabel);
            add(baseLabel).width(138f);
            baseLabel.pack();
            baseLabel.setWidth(138f);
            baseLabel.setDebug(true);
            totalHeight += baseLabel.getPrefHeight();
            row();
        }

        if (forces != null && forces.size > 0) {
            //Label.LabelStyle forceStyle = new Label.LabelStyle(font, new Color(1, 0, 0, 1));
            labelColor.setColor(Color.CYAN);
            labelColor.fill();
            forceStyle.background = new Image(new Texture(labelColor)).getDrawable();
            forceLabels = new Label[forces.size];

            extendButtons = new Label[forces.size];
            Texture texture = new Texture("plus-sign-in-circle.png");
            Sprite sprite = new Sprite(texture);
            Skin plusSkin = new Skin();
            plusSkin.add("image", sprite);

            //Label.LabelStyle extendStyle = new Label.LabelStyle(font, new Color(1, 0, 0, 1));
            extendStyle.background = plusSkin.getDrawable("image");

            Table table = new Table();

            int i = 0;
            for (Force f : forces) {
                forceLabels[i] = new Label(f.getGeneralInfo(), forceStyle);
                forceLabels[i].setWrap(true);
                play.addActor(forceLabels[i]);
                table.add().width(12);
                table.add(forceLabels[i]).width(138f);
                forceLabels[i].pack();
                forceLabels[i].setWidth(138f);
                forceLabels[i].setDebug(true);
                totalHeight += forceLabels[i].getPrefHeight();

                extendButtons[i] = new Label("", extendStyle);
                if (f.isUnit) table.add().width(12);
                else {
                    play.addActor(extendButtons[i]);
                    table.add(extendButtons[i]).width(12);
                    extendButtons[i].pack();
                    extendButtons[i].setWidth(12);
                }
                i++;
                add(table).width(150);


                //if(i < forces.size) row();
            }

        }
        if (base == null && (forces == null || forces.isEmpty())) hex.isSelected = true;

        setPosition(hex.getX(), hex.getY());
        setBounds(getX() + 8, getY() + 8, 164, totalHeight + 1);
        setTouchable(Touchable.enabled);
        setVisible(true);
        //align(1);
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
