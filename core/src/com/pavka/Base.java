package com.pavka;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Array;

import static com.pavka.Nation.*;


public class Base extends Image implements Supplier {

    public static final float IMAGE_SIZE = 15f;

    Nation nation;
    Hex hex;
    double foodLimit;
    double ammoLimit;
    double foodStock;
    double ammoStock;
    Play play;
    boolean isSelected;
    boolean isOccupied;
    int num;

    //public Texture textureFrance = new Texture("symbols/CavBlueDivision.png");
    public Texture textureFrance = new Texture("blueBase.png");

    //public Texture textureAustria = new Texture("symbols/CavRedDivision.png");
    public Texture textureAustria = new Texture("redBase.png");

    public Base(Play play, Nation nation, Hex hex) {
        this.play = play;
        this.nation = nation;
        this.hex = hex;
        hex.locate(this);

        if(nation.color == WHITE) play.whiteBases.add(this);
        if(nation.color == BLACK) play.blackBases.add(this);

        play.addActor(this);

        setBounds(hex.getRelX() - 8, hex.getRelY() - 8, 15, 15);
        foodStock = 1000;
        ammoStock = 1000;
    }


    @Override
    public String toString() {
        return "BASE. Food: " + foodStock + " Ammo: " + ammoStock + " Hex: column: " + hex.col + " row: " + hex.row;
    }
    @Override
    public void draw(Batch batch, float alpha) {
        Texture texture = nation == FRANCE ? textureFrance : textureAustria;

        if (!isSelected) batch.draw(texture, hex.getRelX() - 8, hex.getRelY() - 8, IMAGE_SIZE, IMAGE_SIZE);
        else batch.draw(texture, hex.getRelX() - 8, hex.getRelY() - 8, IMAGE_SIZE * 1.1f, IMAGE_SIZE * 1.1f);}

    @Override
    public Force sendSupplies(Force force, double food, double ammo) {
        Force train = createTrain(food, ammo);
        train.name = "Train " + nation + " " + ++num;
        train.order.target = new Target(force, Target.JOIN);
        return train;
    }
    private Force createTrain(double food, double ammo) {
        Force train = new Force(nation, hex);
        train.setPlay(play);
        play.addActor(train);
        double foodLoad = Math.min(foodStock, food);
        double ammoLoad = Math.min(ammoStock, ammo);
        int num = (int)Math.ceil(Math.max(foodLoad / UnitType.SUPPLY.FOOD_LIMIT, ammoLoad / UnitType.SUPPLY.AMMO_LIMIT));
        for (int i=0; i<num; i++) {
            Wagon w = new Wagon(nation, hex);
            if(foodLoad < UnitType.SUPPLY.FOOD_LIMIT) {
                foodStock -= foodLoad;
                w.foodStock = foodLoad;
                foodLoad = 0;
            }
            else {
                foodStock -= UnitType.SUPPLY.FOOD_LIMIT;
                foodLoad -= UnitType.SUPPLY.FOOD_LIMIT;
            }
            if(ammoLoad < UnitType.SUPPLY.AMMO_LIMIT) {
                ammoStock -= ammoLoad;
                w.ammoStock = ammoLoad;
                ammoLoad = 0;
            }
            else {
                ammoStock -= UnitType.SUPPLY.AMMO_LIMIT;
                ammoLoad -= UnitType.SUPPLY.AMMO_LIMIT;
            }
            train.attach(w);
        }
        return train;
    }
}
