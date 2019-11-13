package com.pavka;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Array;

public class Tableau extends Table {
    Play play;
    Hex hex;
    Array<Force> forces;
    Base base;

    Label costLabel;

    public Tableau(Play play, Hex hex, Array<Force> forces, Base base) {
        this.play = play;
        this.hex = hex;
        this.forces = forces;
        this.base = base;
    }

    public void init() {

        BitmapFont font = new BitmapFont();
        font.getData().setScale(0.7f);
        costLabel = new Label("Cost: " + hex.cell.getTile().getProperties().get("cost"),
                new Label.LabelStyle(font, new Color(1, 0, 0, 1)));
        //costLabel.setWidth(18);
        costLabel.setWrap(true);
        play.addActor(costLabel);
        add(costLabel).width(64f);

        System.out.println(costLabel.getPrefWidth() + " " + costLabel.getStyle());
        costLabel.setAlignment(0);
        row();
        Label cropsLabel = new Label("Crops: " + hex.currentHarvest,
                new Label.LabelStyle(font, new Color(1, 0, 0, 1)));
        cropsLabel.setWrap(true);
        play.addActor(cropsLabel);
        add(cropsLabel);
        cropsLabel.setWrap(true);
        cropsLabel.setAlignment(0);
        row();
        if (forces != null && forces.size > 0) {
            for (Force f : forces) {
                //Label forceLabel = new Label("Forces: " + forces, new Label.LabelStyle(font, new Color(1, 0, 0, 1)));
                Label forceLabel = new Label(f.getGeneralInfo(), new Label.LabelStyle(font, new Color(1, 0, 0, 1)));
                forceLabel.setWrap(true);
                play.addActor(forceLabel);
                add(forceLabel).width(128f);
                forceLabel.setAlignment(0);
                row();
            }
        }
        if (base != null) {
            Label baseLabel = new Label(hex.base.toString(), new Label.LabelStyle(font, new Color(1, 0, 0, 1)));
            baseLabel.setWrap(true);
            add(baseLabel);
            row();
        }
        setPosition(hex.getX(), hex.getY());
        setBounds(getX() - 8, getY() - 8, 164, 64);
        setTouchable(Touchable.enabled);
        setVisible(true);
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
