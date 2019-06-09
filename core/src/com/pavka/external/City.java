package com.pavka.external;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class City {
    float x;
    float y;
    String name;
    int index;

    public void setIndex(int index) {
        this.index = index;
    }

    public City(float x, float y, String name) {
        this.x = x;
        this.y = y;
        this.name = name;
    }

    public void render(ShapeRenderer shapeRenderer, SpriteBatch batch, BitmapFont font, boolean inPath){
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        if(inPath) {
            // green
            shapeRenderer.setColor(.57f, .76f, .48f, 1);
        }
        else{
            // blue
            shapeRenderer.setColor(.8f, .88f, .95f, 1);
        }
        shapeRenderer.circle(x, y, 20);
        shapeRenderer.end();

        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(0, 0, 0, 1);
        shapeRenderer.circle(x, y, 20);
        shapeRenderer.end();

        batch.begin();
        font.setColor(0, 0, 0, 255);
        font.draw(batch, name, x-5, y+5);
        batch.end();
    }



}
