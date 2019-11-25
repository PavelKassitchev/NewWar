package com.pavka;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

public class Choice extends Table {
    Hex hex;
    Base base;
    Force force;
    Label pathLabel;
    Label builtLabel;
    Label createLabel;
    Label upgradeLabel;
    Label destroyLabel;
    Label moveLabel;
    Label detachLabel;
    Label attachLabel;
    Window tableau;
    float totalHeight;
    Label.LabelStyle style;

    SwitchLabel pLabel;
    SwitchLabel bLabel;
    SwitchLabel cLabel;
    SwitchLabel uLabel;
    SwitchLabel dLabel;
    SwitchLabel mLabel;
    SwitchLabel detLabel;
    SwitchLabel aLabel;

    public Choice(Window tableau, Hex hex, float x, float y) {
        setStyle(tableau);
        this.hex = hex;

        /*pathLabel = new Label("Path to...", style);
        initLabel(pathLabel);*/
        pLabel = new SwitchLabel(tableau, "Path to...", style);
        initSwitchLabel(pLabel, false);
        row();


        /*builtLabel = new Label("Build a Base", style);
        initLabel(builtLabel);*/
        bLabel = new SwitchLabel(tableau, "Build a Base", style);
        initSwitchLabel(bLabel, false);
        row();

        /*createLabel = new Label("Create a New Force", style);
        initLabel(createLabel);*/
        cLabel = new SwitchLabel(tableau, "Create New Force", style);
        initSwitchLabel(cLabel, false);
        init(x, y);
    }

    public Choice(Window tableau, Base base, float x, float y) {
        setStyle(tableau);
        this.base = base;

        /*upgradeLabel = new Label("Upgrade the Base", style);
        tableau.play.addActor(upgradeLabel);
        add(upgradeLabel).width(164);
        upgradeLabel.setDebug(true);
        upgradeLabel.setAlignment(0);
        totalHeight += upgradeLabel.getPrefHeight();*/
        uLabel = new SwitchLabel(tableau, "Upgrade the Base", style);
        initSwitchLabel(uLabel, false);
        row();
        /*destroyLabel = new Label("Destroy the Base", style);
        tableau.play.addActor(destroyLabel);
        add(destroyLabel).width(164);
        destroyLabel.setDebug(true);
        destroyLabel.setAlignment(0);
        totalHeight += destroyLabel.getPrefHeight();*/
        dLabel = new SwitchLabel(tableau, "Destroy the Base", style);
        initSwitchLabel(dLabel, false);

        init(x, y);
    }

    public Choice(Window tableau, Force force, float x, float y) {
        setStyle(tableau);
        this.force = force;

        /*moveLabel = new Label("Move to...", style);
        tableau.play.addActor(moveLabel);
        add(moveLabel).width(164);
        //moveLabel.setDebug(true);
        moveLabel.setAlignment(0);
        totalHeight += moveLabel.getPrefHeight();*/
        mLabel = new SwitchLabel(tableau, "Move to...", style);
        initSwitchLabel(mLabel, false);
        row();

        detLabel = new SwitchLabel(tableau, "Detach", style);
        initSwitchLabel(detLabel, false);
        /*detachLabel = new Label("Detach", style);
        initLabel(detachLabel);*/
        row();

        aLabel = new SwitchLabel(tableau, "Attach to...", style);
        initSwitchLabel(aLabel, false);
        /*attachLabel = new Label("Attach to...", style);
        initLabel(attachLabel);*/

        init(x, y);
    }

    private void initLabel(Label label) {
        tableau.play.addActor(label);
        add(label).width(164);
        //label.setDebug(true);
        label.setAlignment(0);
        totalHeight += label.getPrefHeight();
    }

    private void initSwitchLabel(SwitchLabel label, boolean inactive) {

        if(inactive) {
            label.changeStyle();
        }
        else tableau.play.addActor(label);
        add(label).width(164);
        label.setAlignment(0);
        totalHeight += label.getPrefHeight();
        /*if(inactive) {
            label.changeStyle();
        }
        else tableau.play.addActor(label);*/
    }


    private void setStyle(Window tableau) {
        this.tableau = tableau;
        tableau.labelColor.setColor(Color.BROWN);
        tableau.labelColor.fill();
        style = new Label.LabelStyle(tableau.font, new Color(1, 1, 0, 1));
        style.background = new Image(new Texture(tableau.labelColor)).getDrawable();
    }

    private void init(float x, float y) {
        setPosition(x, y);
        setBounds(getX() + 8, getY() + 8, 164, totalHeight);
        setTouchable(Touchable.enabled);
        setVisible(true);
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
