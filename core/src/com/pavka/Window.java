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

import static com.pavka.Nation.WHITE;

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
        minusStyle.background = plusMinusSkin.getDrawable("imageM");
        closeFont.getData().setScale(0.6f);
        font.getData().setScale(0.7f);
    }

    public Pixmap labelColor = new Pixmap(1, 1, Pixmap.Format.RGB888);

    public Play play;
    public Hex hex;
    public Base base;
    public Array<Force> forces;

    public Window parent;
    public SwitchLabel parentLabel;
    public Array<Window> children;

    public SwitchLabel closeLabel;
    public SwitchLabel hexLabel;
    public SwitchLabel baseLabel;
    public SwitchLabel[] forceLabels;
    public SwitchLabel[] extendLabels;
    public Choice choice;

    private float totalHeight;

    public Window (Play play, Hex hex, boolean create, float x, float y) {
        this.play = play;
        this.hex = hex;
        choice = new Choice(this, hex, create, x, y);
        init(x, y);
    }

    public Window(Play play, Hex hex, float x, float y) {
        this.play = play;
        this.hex = hex;

        if (hex.base != null) base = hex.base;
        if (!hex.whiteForces.isEmpty()) forces = hex.whiteForces;
        if (!hex.blackForces.isEmpty()) forces = hex.blackForces;

        init(x, y);
    }

    public Window(Play play, Force force, SwitchLabel parentLabel, float x, float y) {
        this.play = play;
        this.parentLabel = parentLabel;
        parentLabel.childWindow = this;
        setParent(parentLabel.window);
        int size = force.forces.size();
        forces = new Array<Force>(size);
        for(int i = 0; i < size; i++) {
            forces.add(force.forces.get(i));
        }
        init(x, y);
    }

    public Window(Play play, Window parent, Force force, float x, float y) {
        this.play = play;

        setParent(parent);
        choice = new Choice(this, force, x, y);
        init(x, y);
    }

    public Window(Play play, Window parent, Force force, boolean toAttach, float x, float y) {
        this.play = play;

        setParent(parent);
        if(toAttach) {
            Array<Force> fcs = force.nation.color == WHITE? force.hex.whiteForces : force.hex.blackForces;
            forces = new Array<Force>();
            for(Force f: fcs) {
                if(f != force) forces.add(f);
            }
        }
        else choice = new Choice(this, force, x, y);
        init(x, y);
    }

    public Window(Play play, Window parent, Hex hex, float x, float y) {
        this.play = play;
        this.hex = hex;

        setParent(parent);
        choice = new Choice(this, hex, x, y);
        init(x, y);
    }
    public Window(Play play, Window parent, Base base, float x, float y) {
        this.play = play;
        this.base = base;

        setParent(parent);
        choice = new Choice(this, base, x, y);
        init(x, y);
    }

    public void setChild(Window win) {
        children.add(win);
        win.parent = this;
    }
    public void setParent(Window win) {
        parent = win;
        win.children.add(this);
    }
    //TODO
    private void init(float x, float y) {

        children = new Array<Window>();

        labelColor.setColor(Color.GRAY);
        labelColor.fill();
        closeStyle.background = new Image(new Texture(labelColor)).getDrawable();
        closeLabel = new SwitchLabel(this, "CLOSE", closeStyle);
        closeLabel.setAlignment(Align.right);
        play.addActor(closeLabel);
        add(closeLabel).width(164);
        //closeLabel.setDebug(true);
        totalHeight += closeLabel.getPrefHeight();
        row();

        if (choice != null) {
            //totalHeight += choice.getPrefHeight();
            add(choice).width(164);
            choice.pack();
            choice.setWidth(164);
            //choice.setDebug(true);

            //choice.setDebug(true);
        } else {
            if (hex != null) {
                labelColor.setColor(Color.GOLD);
                labelColor.fill();
                hexStyle.background = new Image(new Texture(labelColor)).getDrawable();
                hexLabel = new SwitchLabel(this, hex.getGeneralInfo(), hexStyle);
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
                baseLabel = new SwitchLabel(this, base.getGeneralInfo(), baseStyle);
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
                forceLabels = new SwitchLabel[forces.size];

                extendLabels = new SwitchLabel[forces.size];

                Table[] table = new Table[forces.size];

                int i = 0;
                for (Force f : forces) {
                    forceLabels[i] = new SwitchLabel(this, f.getGeneralInfo(), forceStyle);
                    forceLabels[i].setWrap(true);
                    play.addActor(forceLabels[i]);
                    table[i] = new Table();
                    table[i].add().width(12);
                    table[i].add(forceLabels[i]).width(138f);
                    forceLabels[i].pack();
                    forceLabels[i].setWidth(138f);
                    //forceLabels[i].setDebug(true);
                    totalHeight += forceLabels[i].getPrefHeight();

                    extendLabels[i] = new SwitchLabel(this, "", plusStyle, minusStyle);
                    //extendButtons[i] = new SwitchLabel();
                    if (f.isUnit) table[i].add().width(12);
                    else {
                        play.addActor(extendLabels[i]);
                        table[i].add(extendLabels[i]).width(12);
                        extendLabels[i].pack();
                        extendLabels[i].setWidth(12);
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

        play.addActor(this);
        labelColor.dispose();
    }

}


