package com.pavka;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.ai.pfa.GraphPath;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapLayer;

import com.badlogic.gdx.maps.objects.TextureMapObject;
import com.badlogic.gdx.maps.tiled.*;
import com.badlogic.gdx.maps.tiled.renderers.HexagonalTiledMapRenderer;

import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;

public class Play implements Screen, InputProcessor {

    public static final String MAP = "WarMap.tmx";

    public static TiledMap map = new TmxMapLoader().load(MAP);

    public static HexGraph hexGraph;


    {
        Hex hex;
        hexGraph = new HexGraph();
        for (int i = 0; i < 64; i++) {
            for (int j = 0; j < 64; j++) {
                hex = new Hex(i, j);
                hexGraph.addHex(hex);
                //System.out.println(hex.index);
                //for (Hex neighbour: hex.getNeighbours()) {
                //   hexGraph.connectHexes(hex, neighbour);
                //}
            }
        }
        for (int i = 0; i < 64; i++) {
            for (int j = 0; j < 64; j++) {
                hex = hexGraph.getHex(i, j);
                Array<Hex> hexes = hex.getNeighbours();
                for (Hex h: hexes) {
                    hexGraph.connectHexes(hex, hexGraph.getHex(h.col, h.row));
                }
            }
        }
        /*Hex hex1 = new Hex(0, 0);
        Hex hex2 = new Hex(1,1);
        hexGraph = new HexGraph();
        hexGraph.addHex(hex1);
        hexGraph.addHex(hex2);
        hexGraph.connectHexes(hex1, hex2);*/
        //GraphPath<Hex> graphPath = hexGraph.findPath(hexGraph.getHex(0, 0), hexGraph.getHex(1, 1));
        //System.out.println("SIZE = " +  hexGraph.hexes.size + " PATHS = " + hexGraph.paths.size + " Nodes: " +
        //        graphPath.getCount());
    }

    private HexagonalTiledMapRenderer renderer;
    private OrthographicCamera camera;
    private Texture texture;
    private Sprite sprite;
    private SpriteBatch sb;
    MapLayer objectLayer;
    TiledMapTileLayer tileLayer;
    Hex startHex;
    Hex endHex;


    @Override
    public void show() {
        float w = Gdx.graphics.getWidth();
        float h = Gdx.graphics.getHeight();
        //map = new TmxMapLoader().load(MAP);
        renderer = new MyRenderer(map);
        camera = new OrthographicCamera();
        camera.setToOrtho(false, w, h);
        Gdx.input.setInputProcessor(this);
        texture = new Texture("badlogic.jpg");
        //sprite = new Sprite(texture);
        //sprite.setSize(32f, 32f);
        TextureRegion tr = new TextureRegion(texture);
        sb = new SpriteBatch();
        objectLayer = map.getLayers().get("ObjectLayer");
        TextureMapObject tmo = new TextureMapObject(tr);
        tmo.setX(8);
        tmo.setY(0);

        objectLayer.getObjects().add(tmo);

        tileLayer = (TiledMapTileLayer)map.getLayers().get("TileLayer");
        TiledMapTileLayer.Cell cell = tileLayer.getCell(0, 0);
        TiledMapTileSet tileSet = map.getTileSets().getTileSet("WarTiles");
        float type = (Float) cell.getTile().getProperties().get("cost");

        System.out.println(type);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);



        renderer.setView(camera);
        renderer.render();
        /*sb.setProjectionMatrix(camera.combined);
        sprite.setSize(32f, 32f);
        sb.begin();
        sprite.draw(sb);
        sb.end();*/
        camera.update();

    }

    @Override
    public void resize(int width, int height) {
        camera.viewportWidth = width;
        camera.viewportHeight = height;
        camera.position.set(width / 2, height / 2, 0);
        camera.update();

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {
        dispose();
    }

    @Override
    public void dispose() {
        map.dispose();
        renderer.dispose();
    }

    @Override
    public boolean keyDown(int keycode) {
        if(keycode == Input.Keys.LEFT)
            camera.translate(-32,0);
        if(keycode == Input.Keys.RIGHT)
            camera.translate(32,0);
        if(keycode == Input.Keys.UP)
            camera.translate(0,32);
        if(keycode == Input.Keys.DOWN) {
            camera.translate(0,-32);
        }

        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }


    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        if (button == Input.Buttons.LEFT) {
            Hex hex = getHex(getMousePosOnMap().x, getMousePosOnMap().y);

            System.out.println("X = " + getMousePosOnMap().x + " Y = " + getMousePosOnMap().y);
            if (hex != null) System.out.println(" NEIGHBOURS: " + hex.getNeighbours().size);

            TiledMapTileLayer layer = (TiledMapTileLayer) map.getLayers().get("TileLayer");
            TiledMapTileLayer.Cell cell = null;
            if (hex != null) {
                cell = layer.getCell(hex.col, hex.row);
                System.out.println(cell.getTile().getProperties().get("cost"));
            }

            if (startHex == null) startHex = hex;
            else {
                if (endHex == null) {
                    endHex = hex;
                    GraphPath<Hex> graphPath = hexGraph.findPath(startHex, endHex);
                    System.out.println("Start = " + startHex.index + " end = " + endHex.index +
                            " Counts = " + graphPath.getCount());
                }
                else {
                    endHex = null;
                    startHex = hex;
                }
            }
            if (startHex != null) System.out.println("Star Hex = (" + startHex.row + ", " + startHex.col + ")");
            if (endHex != null) System.out.println("End Hex = (" + endHex.row + ", " + endHex.col + ")");
        }
        return true;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        if (amount == 1)camera.zoom += 0.2;
        else camera.zoom -= 0.2;
        return true;
    }

    Vector3 getMousePosOnMap() {
        return camera.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));
    }

    //TODO make it correct, this is the simplest version
    Hex getHex(float x, float y) {
        if (y < 2 || y > 774) return null;
        int row = (int)((y - 2) / 12);
        System.out.println("Row = " + row);
        int col;
        if (row % 2 == 0) {
            if (x < 8 || x > 1032) return null;
            col = (int)((x - 8) / 16);
        }
        else {
            if (x < 0 || x > 1024) return null;
            col = (int)(x / 16);
        }
        System.out.println("Col = " + col);

        return hexGraph.getHex(col, row);
    }
}