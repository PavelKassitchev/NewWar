package com.pavka;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Array;

import java.util.ArrayList;

public class Hex extends Image {
    public static TiledMapTileLayer layer = (TiledMapTileLayer) (Play.map).getLayers().get("TileLayer");
    public int col;
    public int row;
    public String name;
    public double maxHarvest = 6;
    public double currentHarvest;
    public int index;
    public TiledMapTileLayer.Cell cell;
    public Array<Force> forces;
    public final static int SIZE = 10;


    //static Texture texture = new Texture("symbols/Blue.png");


    public Hex() {
        forces = new Array<Force>();
    }
    public Hex(int q, int r) {

        col = q;
        row = r;
        cell = layer.getCell(col, row);
        forces = new Array<Force>();
        setBounds(getRelX() - 8, getRelY() - 8, 16, 16);
        currentHarvest = maxHarvest;

    }

    /*@Override
    public void draw(Batch batch, float alpha) {
        batch.draw(texture, getRelX(), getRelY());
    }*/

    public Array<Hex> getNeighbours() {
        Array<Hex> neighbours = new Array<Hex>();
        if (col > 0) neighbours.add(new Hex(col - 1, row));
        if (col < 63) neighbours.add(new Hex(col + 1, row));
        int offset = 0;
        if (row % 2 == 1) {
            offset = -1;
        }

        if (row > 0) {
            if (col + offset >= 0) neighbours.add(new Hex(col + offset, row - 1));

            if (col + 1 + offset < 64) {
                neighbours.add(new Hex(col + 1 + offset, row - 1));
            }
        }
        if (row < 63) {
            if (col + offset >= 0) neighbours.add(new Hex(col + offset, row + 1));


            if (col + 1 + offset < 64) {
                neighbours.add(new Hex(col + 1 + offset, row + 1));
            }
        }

        return neighbours;
    }

    public float getRelX() {
        if (row % 2 == 0) return (16 + col * 16);
        return (8 + col * 16);
    }

    public float getRelY() {
        return (8 + row * 12);
    }
}
