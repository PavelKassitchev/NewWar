package com.pavka;

import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.utils.Array;

public class Hex {
    static TiledMapTileLayer layer = (TiledMapTileLayer) (Play.map).getLayers().get("TileLayer");
    int col;
    int row;
    String name;
    int index;
    TiledMapTileLayer.Cell cell;


    Hex(int q, int r) {

        col = q;
        row = r;
        cell = layer.getCell(col, row);
    }

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
}
