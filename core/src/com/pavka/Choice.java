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
    SwitchLabel createWLabel;
    SwitchLabel createBLabel;
    SwitchLabel upgradeLabel;
    SwitchLabel destroyLabel;
    SwitchLabel showLabel;
    SwitchLabel moveLabel;
    SwitchLabel detachLabel;
    SwitchLabel attachLabel;
    SwitchLabel meetLabel;

    SwitchLabel whiteBattalion;
    SwitchLabel whiteSquadron;
    SwitchLabel whiteBattery;
    SwitchLabel whiteWagon;
    SwitchLabel blackBattalion;
    SwitchLabel blackSquadron;
    SwitchLabel blackBattery;
    SwitchLabel blackWagon;

    public Choice (Window tableau, Hex hex, boolean create, float x, float y) {
        if(create) {
            setStyle(tableau);
            this.hex = hex;

            whiteBattalion = new SwitchLabel(tableau, "White Battalion", style);
            initSwitchLabel(whiteBattalion, false);
            row();

            whiteSquadron = new SwitchLabel(tableau, "White Squadron", style);
            initSwitchLabel(whiteSquadron, false);
            row();

            whiteBattery = new SwitchLabel(tableau, "White Battery", style);
            initSwitchLabel(whiteBattery, false);
            row();

            whiteWagon = new SwitchLabel(tableau, "White Wagon", style);
            initSwitchLabel(whiteWagon, false);
            row();

            blackBattalion = new SwitchLabel(tableau, "Black Battalion", style);
            initSwitchLabel(blackBattalion, false);
            row();

            blackSquadron = new SwitchLabel(tableau, "Black Squadron", style);
            initSwitchLabel(blackSquadron, false);
            row();

            blackBattery = new SwitchLabel(tableau, "Black Battery", style);
            initSwitchLabel(blackBattery, false);
            row();

            blackWagon = new SwitchLabel(tableau, "Black Wagon", style);
            initSwitchLabel(blackWagon, false);

            init(x, y);
        }

    }

    public Choice(Window tableau, Hex hex, float x, float y) {
        setStyle(tableau);
        this.hex = hex;

        pathLabel = new SwitchLabel(tableau, "Path to...", style);
        initSwitchLabel(pathLabel, false);
        row();

        buildLabel = new SwitchLabel(tableau, "Build a Base", style);
        initSwitchLabel(buildLabel, false);
        row();

        createWLabel = new SwitchLabel(tableau, "Create New White Force", style);
        initSwitchLabel(createWLabel, false);
        row();

        createBLabel = new SwitchLabel(tableau, "Create New Black Force", style);
        initSwitchLabel(createBLabel, false);

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

        showLabel = new SwitchLabel(tableau, "Show Path", style);
        initSwitchLabel(showLabel, false);
        row();

        moveLabel = new SwitchLabel(tableau, "Move to...", style);
        initSwitchLabel(moveLabel, false);
        row();

        detachLabel = new SwitchLabel(tableau, "Detach", style);
        initSwitchLabel(detachLabel, false);
        row();

        attachLabel = new SwitchLabel(tableau, "Reinforce/Attach...", style);
        initSwitchLabel(attachLabel, false);
        row();

        meetLabel = new SwitchLabel(tableau, "Meet...", style);
        initSwitchLabel(meetLabel, false);

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
