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

    Window tableau;
    float totalHeight;
    Label.LabelStyle style;

    SwitchLabel pathLabel;
    SwitchLabel buildLabel;
    SwitchLabel createLabel;
    SwitchLabel upgradeLabel;
    SwitchLabel destroyLabel;
    SwitchLabel moveLabel;
    SwitchLabel detachLabel;
    SwitchLabel attachLabel;

    public Choice(Window tableau, Hex hex, float x, float y) {
        setStyle(tableau);
        this.hex = hex;

        pathLabel = new SwitchLabel(tableau, "Path to...", style);
        initSwitchLabel(pathLabel, false);
        row();

        buildLabel = new SwitchLabel(tableau, "Build a Base", style);
        initSwitchLabel(buildLabel, false);
        row();

        createLabel = new SwitchLabel(tableau, "Create New Force", style);
        initSwitchLabel(createLabel, false);
        init(x, y);
    }

    public Choice(Window tableau, Base base, float x, float y) {
        setStyle(tableau);
        this.base = base;


        upgradeLabel = new SwitchLabel(tableau, "Upgrade the Base", style);
        initSwitchLabel(upgradeLabel, false);
        row();

        destroyLabel = new SwitchLabel(tableau, "Destroy the Base", style);
        initSwitchLabel(destroyLabel, false);

        init(x, y);
    }

    public Choice(Window tableau, Force force, float x, float y) {
        setStyle(tableau);
        this.force = force;

        moveLabel = new SwitchLabel(tableau, "Move to...", style);
        initSwitchLabel(moveLabel, false);
        row();

        detachLabel = new SwitchLabel(tableau, "Detach", style);
        initSwitchLabel(detachLabel, false);
        row();

        attachLabel = new SwitchLabel(tableau, "Attach to...", style);
        initSwitchLabel(attachLabel, false);

        init(x, y);
    }


    private void initSwitchLabel(SwitchLabel label, boolean inactive) {

        if(inactive) {
            label.changeStyle();
        }
        else tableau.play.addActor(label);
        add(label).width(164);
        label.setAlignment(0);
        totalHeight += label.getPrefHeight();
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
