package com.pavka;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Array;

public class Window extends Table {

    public Play play;
    public Hex hex;
    public Base base;
    public Array<Force> forces;

    public Label closeLabel;
    public Label hexLabel;
    public Label baseLabel;
    public SwitchLabel[] forceLabels;
    public SwitchLabel[] extendLabels;
    public Label[] choiceLabels;

    private float totalHeight;

    public Window(Play play, Hex hex) {
        this.play = play;
        this.hex = hex;

        if(hex.base != null) this.base = base;
        if (!hex.whiteForces.isEmpty()) forces = hex.whiteForces;
        if (!hex.blackForces.isEmpty()) forces = hex.blackForces;
    }

}
