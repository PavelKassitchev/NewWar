package com.pavka;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;

public class Window extends Table {

    public static BitmapFont closeFont = new BitmapFont();
    public static Label.LabelStyle closeStyle = new Label.LabelStyle(closeFont, debugTableColor);
    public static BitmapFont font = new BitmapFont();
    public static Label.LabelStyle hexStyle = new Label.LabelStyle(font, new Color(1, 0, 0, 1));
    public static Label.LabelStyle baseStyle = new Label.LabelStyle(font, new Color(1, 0, 0, 1));
    public static Label.LabelStyle forceStyle = new Label.LabelStyle(font, new Color(1, 0, 0, 1));
    public static Label.LabelStyle plusStyle = new Label.LabelStyle(font, new Color(1, 0, 0, 1));
    public static Label.LabelStyle minusStyle = new Label.LabelStyle(font, new Color(1, 0, 0, 1));
    static {
        Texture textureP = new Texture("plus-sign-in-circle.png");
        Sprite spriteP = new Sprite(textureP);
        Skin plusMinusSkin = new Skin();
        Texture textureM = new Texture("round-delete-button.png");
        Sprite spriteM = new Sprite(textureM);
        plusMinusSkin.add("imageP", spriteP);
        plusMinusSkin.add("imageM", spriteM);
        plusStyle.background = plusMinusSkin.getDrawable("imageP");
        minusStyle.background =plusMinusSkin.getDrawable("imageM");
        closeFont.getData().setScale(0.6f);
        font.getData().setScale(0.7f);
    }

    public Pixmap labelColor = new Pixmap(1, 1, Pixmap.Format.RGB888);

    public Play play;
    public Hex hex;
    public Base base;
    public Array<Force> forces;

    public Window parent;
    public Array<Window> children;

    public SwitchLabel closeLabel;
    public Label hexLabel;
    public Label baseLabel;
    public SwitchLabel[] forceLabels;
    public SwitchLabel[] extendLabels;
    public Label[] choiceLabels;

    private float totalHeight;

    public Window(Play play, Hex hex, float x, float y) {
        this.play = play;
        this.hex = hex;

        if(hex.base != null) this.base = base;
        if (!hex.whiteForces.isEmpty()) forces = hex.whiteForces;
        if (!hex.blackForces.isEmpty()) forces = hex.blackForces;
    }
    //TODO
    /*private void init(float x, float y) {
        labelColor.setColor(Color.GRAY);
        labelColor.fill();
        closeStyle.background = new Image(new Texture(labelColor)).getDrawable();
        closeLabel = new SwitchLabel(this,"CLOSE", closeStyle);
        closeLabel.setAlignment(Align.right);
        play.addActor(closeLabel);
        add(closeLabel).width(164);
        //closeLabel.setDebug(true);
        totalHeight += closeLabel.getPrefHeight();
        row();

        if (choiceLabels != null) {
            //totalHeight += choice.getPrefHeight();
            for (Label label: choiceLabels) {
            add(label).width(164);
            label.pack();
            label.setWidth(164);}
            //choice.setDebug(true);
        }
        else {
            if (hex != null) {
                labelColor.setColor(Color.GOLD);
                labelColor.fill();
                hexStyle.background = new Image(new Texture(labelColor)).getDrawable();
                hexLabel = new Label("Cost: " + hex.cell.getTile().getProperties().get("cost") + " Crops: " + hex.currentHarvest,
                        hexStyle);
                hexLabel.setWrap(true);
                play.addActor(hexLabel);
                add(hexLabel).width(138f);
                //hexLabel.setDebug(true);
                totalHeight += hexLabel.getPrefHeight();
                row();
            }

            if (base != null) {
                labelColor.setColor(Color.CORAL);
                labelColor.fill();
                baseStyle.background = new Image(new Texture(labelColor)).getDrawable();
                baseLabel = new Label(base.getGeneralInfo(), baseStyle);
                baseLabel.setWrap(true);
                play.addActor(baseLabel);
                add(baseLabel).width(138f);
                baseLabel.pack();
                baseLabel.setWidth(138f);
                //baseLabel.setDebug(true);
                totalHeight += baseLabel.getPrefHeight();
                row();
            }

            if (forces != null && forces.size > 0) {
                labelColor.setColor(Color.CYAN);
                labelColor.fill();
                forceStyle.background = new Image(new Texture(labelColor)).getDrawable();
                forceLabels = new Label[forces.size];

                extendButtons = new Label[forces.size];
                Texture textureP = new Texture("plus-sign-in-circle.png");
                Sprite spriteP = new Sprite(textureP);
                Skin plusMinusSkin = new Skin();
                Texture textureM = new Texture("round-delete-button.png");
                Sprite spriteM = new Sprite(textureM);
                plusMinusSkin.add("imageP", spriteP);
                plusMinusSkin.add("imageM", spriteM);
                extendStyle.background = plusMinusSkin.getDrawable("imageP");
                extendStyleM.background =plusMinusSkin.getDrawable("imageM");

                Table[] table = new Table[forces.size];

                int i = 0;
                for (Force f : forces) {
                    forceLabels[i] = new Label(f.getGeneralInfo(), forceStyle);
                    forceLabels[i].setWrap(true);
                    play.addActor(forceLabels[i]);
                    table[i] = new Table();
                    table[i].add().width(12);
                    table[i].add(forceLabels[i]).width(138f);
                    forceLabels[i].pack();
                    forceLabels[i].setWidth(138f);
                    //forceLabels[i].setDebug(true);
                    totalHeight += forceLabels[i].getPrefHeight();

                    extendButtons[i] = new Label("", extendStyle);
                    //extendButtons[i] = new SwitchLabel();
                    if (f.isUnit) table[i].add().width(12);
                    else {
                        play.addActor(extendButtons[i]);
                        table[i].add(extendButtons[i]).width(12);
                        extendButtons[i].pack();
                        extendButtons[i].setWidth(12);
                    }
                    add(table[i]).width(150);
                    i++;
                    row();
                }
            }
        }
        setPosition(x, y);
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

}*/

}
