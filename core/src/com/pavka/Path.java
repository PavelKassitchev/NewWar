package com.pavka;

import com.badlogic.gdx.ai.pfa.Connection;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;

public class Path implements Connection<Hex> {

    Hex fromHex;
    Hex toHex;
    float cost;

    public Path(Hex fromHex, Hex toHex){
        this.fromHex = fromHex;
        this.toHex = toHex;
        //cost = Math.max(Math.abs(fromHex.col - toHex.col), Math.abs(fromHex.row - toHex.row));
        //cost = Vector2.dst(fromHex.col, fromHex.row, toHex.col, toHex.row);
        cost = ((Float)(fromHex.cell.getTile().getProperties().get("cost")) + (Float)(fromHex.cell.getTile().getProperties().get("cost"))) / 2;

    }

    @Override
    public float getCost() {
        return cost;
    }

    @Override
    public Hex getFromNode() {
        return fromHex;
    }

    @Override
    public Hex getToNode() {
        return toHex;
    }

    public void render(ShapeRenderer shapeRenderer){
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(1, 0, 0, 0.5f);
        shapeRenderer.rectLine(fromHex.getX(), fromHex.getY(), toHex.getX(), toHex.getY(), 1);
        shapeRenderer.end();
    }
}
