package com.pavka;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.ai.pfa.GraphPath;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.TextureMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.HexagonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;

import java.util.Iterator;

public class Play extends Stage implements Screen {


    public static final String MAP = "maps/WarMap.tmx";

    public static TiledMap map = new TmxMapLoader().load(MAP);


    public static HexGraph hexGraph;

    public static GraphPath<Hex> graphPath;

    public static MapLayer objectLayer = map.getLayers().get("ObjectLayer");
    ;
    public static TiledMapTileLayer tileLayer = (TiledMapTileLayer) map.getLayers().get("TileLayer");
    public boolean newMode;
    Hex startHex;
    Hex endHex;
    MileStone mileStone;
    ShapeRenderer shapeRenderer;
    Array<Path> paths;
    Array<Force> blackTroops = new Array<Force>();
    Array<Force> whiteTroops = new Array<Force>();
    private HexagonalTiledMapRenderer renderer;
    private OrthographicCamera camera;
    private Force selectedForce;
    private Hex selectedHex;

    {
        Hex hex;
        hexGraph = new HexGraph();
        for (int i = 0; i < 64; i++) {
            for (int j = 0; j < 64; j++) {
                hex = new Hex(i, j);
                hexGraph.addHex(hex);
                addActor(hex);
            }
        }
        for (int i = 0; i < 64; i++) {
            for (int j = 0; j < 64; j++) {
                hex = hexGraph.getHex(i, j);
                Array<Hex> hexes = hex.getNeighbours();
                for (Hex h : hexes) {
                    hexGraph.connectHexes(hex, hexGraph.getHex(h.col, h.row));
                }
            }
        }
        for (Path p : hexGraph.paths) addActor(p);

    }

    @Override
    public void show() {
        float w = Gdx.graphics.getWidth();
        float h = Gdx.graphics.getHeight();

        shapeRenderer = new ShapeRenderer();
        renderer = new MyInnerRenderer(map);
        camera = (OrthographicCamera) getCamera();
        camera.setToOrtho(false, w, h);
        Gdx.input.setInputProcessor(this);
    }


    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        renderer.setView(camera);
        renderer.render();
        draw();

        shapeRenderer.setProjectionMatrix(camera.combined);

        if (paths != null) {
            for (Path path : paths) {
                path.render(shapeRenderer);
            }
        }
        camera.update();
        //act(Gdx.graphics.getDeltaTime());
        //draw();
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
        shapeRenderer.dispose();
    }

    @Override
    public boolean keyDown(int keycode) {
        if (keycode == Input.Keys.LEFT)
            camera.translate(-32, 0);
        if (keycode == Input.Keys.RIGHT)
            camera.translate(32, 0);
        if (keycode == Input.Keys.UP)
            camera.translate(0, 32);
        if (keycode == Input.Keys.DOWN) {
            camera.translate(0, -32);
        }

        return false;
    }

    //TODO just for testing
    @Override
    public boolean keyUp(int keycode) {
        if (keycode == Input.Keys.C) {
            Force force = new Squadron(Nation.FRANCE, hexGraph.getHex(8, 4));

            System.out.println(force.getX() + " " + force.getY());
            System.out.println(force.order.pathsOrder);
            //force.hex = hexGraph.getHex(8, 4);

            //Texture texture = new Texture("symbols/InfRedCorps.png");
            //TextureRegion tr = new TextureRegion(texture);
            //TextureMapObject tmo = new TextureMapObject(tr);
            //tmo.setX(force.hex.getRelX() - 8);
            //tmo.setY(force.hex.getRelY() - 8);


            //objectLayer.getObjects().add(tmo);
            whiteTroops.add(force);
            //force.hex.forces.add(force);
            /*force.addListener(new InputListener() {
                public boolean touchDown (InputEvent event, float x, float y, int pointer, int button){
                    System.out.println(event.getTarget() +"hhh"+  x);
                    return true;
                }
            });*/
            //force.symbol = tmo;*/

            addActor(force);

        }
        if (keycode == Input.Keys.Q) {
            act();

            if (selectedForce != null) selectedForce.isSelected = false;
            selectedForce = null;
            startHex = null;
            endHex = null;
            paths = null;
            if (mileStone != null) {
                mileStone.remove();
                mileStone = null;
            }
        }
        if (keycode == Input.Keys.S) {
            Test.main(null);
        }
        if (keycode == Input.Keys.L) {
            LogisticTest.main(null);
        }

        if (keycode == Input.Keys.M) {
            newMode = !newMode;
        }
        return true;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    private void navigate(double speed) {
        if (startHex != endHex) {
            graphPath = hexGraph.findPath(startHex, endHex);
            paths = new Array<Path>();
            Iterator<Hex> iterator = graphPath.iterator();
            Hex sHex = iterator.next();
            Hex eHex;
            while (iterator.hasNext()) {
                eHex = iterator.next();
                paths.add(Play.hexGraph.getPath(sHex, eHex));
                sHex = eHex;
            }

            mileStone = new MileStone(paths.peek().getToNode());
            mileStone.days = Path.getDaysToGo(paths, speed);
            addActor(mileStone);
        }
        else {
            paths = new Array<Path>();
        }

    }


    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        Actor hex = hit(getMousePosOnMap().x, getMousePosOnMap().y, true);
        if(hex instanceof Hex) selectedHex = (Hex)hex;

        System.out.println("Touch Down Pos: " + getMousePosOnMap().x + " " + getMousePosOnMap().y);
        //TODO THIS METHOD MOVED TO TOUCHUP!
        // TODO FOR LEFT MOUSE BUTTON AND OLD MODE!!
        //parameters: graphPath, paths, startHex, endHex, mileStone, seletedForce
        /*Actor actor = hit(getMousePosOnMap().x, getMousePosOnMap().y, true);
        //first touch
        if (startHex == null && endHex == null) {
            //hec touched
            if (actor instanceof Hex) {
                startHex = (Hex) actor;
            }
            //force touched
            if (actor instanceof Force) {
                selectedForce = (Force) actor;
                selectedForce.isSelected = true;
                startHex = selectedForce.hex;

                //orders has already been set
                if (selectedForce.order.pathsOrder.size > 0) {
                    paths = selectedForce.order.pathsOrder;
                    mileStone = selectedForce.order.mileStone;
                    addActor(mileStone);
                }
            }
        }
        //second touch
        else if (startHex != null && endHex == null) {
            if (mileStone != null) mileStone.remove();
            //hex touched
            if (actor instanceof Hex) {
                endHex = (Hex) actor;

                //first hex was touched
                if (selectedForce == null) {
                    navigate(Battalion.SPEED);
                }
                //first force was touched
                if (selectedForce != null) {
                    navigate(selectedForce.speed);
                    selectedForce.order.setPathsOrder(paths);
                    selectedForce.order.mileStone = mileStone;
                    selectedForce.isSelected = false;
                    selectedForce = null;
                }
            }

            //force touched
            if (actor instanceof Force) {
                Force force = (Force) actor;
                endHex = force.hex;

                //first hex was touched
                if (selectedForce == null) {
                    navigate(Battalion.SPEED);
                }

                //first force was touched
                if (selectedForce != null) {
                    navigate(selectedForce.speed);
                    selectedForce.order.setPathsOrder(paths);
                    selectedForce.order.mileStone = mileStone;
                    //TODO attach?
                    selectedForce.isSelected = false;
                    selectedForce = null;
                }
            }

        }
        //further touches
        else if (endHex != null) {
            endHex = null;
            graphPath = null;
            paths = null;
            if (mileStone != null) mileStone.remove();
            mileStone = null;

            //hec touched
            if (actor instanceof Hex) {
                startHex = (Hex) actor;
            }
            //force touched
            if (actor instanceof Force) {
                selectedForce = (Force) actor;
                selectedForce.isSelected = true;
                startHex = selectedForce.hex;

                //orders has already been set
                System.out.println(selectedForce.order.pathsOrder);
                if (selectedForce.order.pathsOrder.size > 0) {
                    paths = selectedForce.order.pathsOrder;
                    mileStone = selectedForce.order.mileStone;
                    addActor(mileStone);
                }
            }
        }*/


        //TODO Общий обзор работы с экраном
        //LAST VERSION
        /*if (!newMode) {
            if (button == Input.Buttons.LEFT) {
                Actor actor = hit(getMousePosOnMap().x, getMousePosOnMap().y, true);
                //TiledMapTileLayer.Cell cell = null;
                if (actor instanceof Hex) {
                    //cell = tileLayer.getCell(((Hex) actor).col, ((Hex) actor).row);
                    if (startHex == null) startHex = (Hex) actor;
                    else {
                        if (endHex == null) {
                            endHex = (Hex) actor;
                            graphPath = hexGraph.findPath(startHex, endHex);
                            paths = new Array<Path>();
                            Iterator<Hex> iterator;
                            if (Play.graphPath != null) {
                                iterator = Play.graphPath.iterator();
                                Hex sHex = iterator.next();
                                Hex eHex;
                                while (iterator.hasNext()) {
                                    eHex = iterator.next();
                                    paths.add(Play.hexGraph.getPath(sHex, eHex));
                                    sHex = eHex;
                                }
                                mileStone = new MileStone(paths.peek().getToNode());
                                System.out.println("Milestone: " + mileStone.hex.getRelX() + " " + mileStone.hex.getRelY());
                                mileStone.days = Path.getDaysToGo(paths, Battalion.SPEED);


                            }
                            if (selectedForce != null) {
                                selectedForce.order.setPathsOrder(paths);
                                selectedForce.order.mileStone = mileStone;
                                mileStone.days = Path.getDaysToGo(paths, selectedForce.speed);
                                selectedForce.isSelected = false;
                                selectedForce = null;
                            }
                            addActor(mileStone);
                        }
                        else {
                            endHex = null;
                            startHex = (Hex) actor;
                            paths = null;
                            graphPath = null;
                            mileStone.remove();
                        }


                    }
                }

                if (actor instanceof Force) {

                    selectedForce = (Force)actor;
                    selectedForce.isSelected = true;
                    paths = selectedForce.order.pathsOrder;
                    if (mileStone != null) mileStone.remove();
                    mileStone = selectedForce.order.mileStone;

                    mileStone.days = Path.getDaysToGo(paths, ((Force)actor).speed);
                    System.out.println("Hex X: " + mileStone.hex.getRelX());
                    addActor(mileStone);
                    startHex = selectedForce.hex;
                    endHex = null;
                    graphPath = null;

                    //if (mileStone != null) mileStone.remove();
                }*/
        //END OF LAST VERSION
        // VERY OLD VERSION
                /*Hex hex = getHex(getMousePosOnMap().x, getMousePosOnMap().y);

                if (hex != null && hex.forces.size() == 0) {
                    if (selectedForce == null) {

                        TiledMapTileLayer layer = (TiledMapTileLayer) map.getLayers().get("TileLayer");
                        TiledMapTileLayer.Cell cell = null;
                        if (hex != null) {
                            cell = layer.getCell(hex.col, hex.row);
                            System.out.println("Cost: " + cell.getTile().getProperties().get("cost") + " Path: " +
                                    graphPath);
                        }

                        if (startHex == null) startHex = hex;
                        else {
                            if (endHex == null) {
                                endHex = hex;
                                graphPath = hexGraph.findPath(startHex, endHex);
                                System.out.println("Start = " + startHex.index + " end = " + endHex.index +
                                        " Counts = " + graphPath.getCount());
                                paths = new Array<Path>();
                                Iterator<Hex> iterator;
                                if (Play.graphPath != null) {
                                    iterator = Play.graphPath.iterator();
                                    Hex sHex = iterator.next();
                                    Hex eHex;
                                    while (iterator.hasNext()) {
                                        eHex = iterator.next();
                                        paths.add(Play.hexGraph.getPath(sHex, eHex));
                                        sHex = eHex;
                                    }

                                }
                            } else {
                                endHex = null;
                                startHex = hex;
                                paths = null;
                                graphPath = null;
                            }
                        }
                    } else {
                        System.out.println("Chosen Force: " + selectedForce);
                        //startHex.forces.remove(selectedForce);
                        //selectedForce.symbol.setX(hex.getRelX() - 8);
                        //selectedForce.symbol.setY(hex.getRelY() - 8);
                        //selectedForce.hex.hex = hex;
                        //hex.forces.add(selectedForce);
                        endHex = hex;
                        graphPath = hexGraph.findPath(startHex, endHex);
                        System.out.println("Start = " + startHex.index + " end = " + endHex.index +
                                " Counts = " + graphPath.getCount());
                        paths = new Array<Path>();
                        Iterator<Hex> iterator;
                        if (Play.graphPath != null) {
                            iterator = Play.graphPath.iterator();
                            Hex sHex = iterator.next();
                            Hex eHex;
                            while (iterator.hasNext()) {
                                eHex = iterator.next();
                                paths.add(Play.hexGraph.getPath(sHex, eHex));
                                sHex = eHex;
                            }

                        }
                        Texture t = new Texture("symbols/RedSuspected.png");
                        TextureRegion tr = new TextureRegion(t);
                        TextureMapObject tmo = new TextureMapObject(tr);
                        tmo.setX(hex.getRelX() - 8);
                        tmo.setY(hex.getRelY() - 8);
                        objectLayer.getObjects().add(tmo);
                        selectedForce.order.pathsOrder = paths;
                        selectedForce = null;
                    }


                } else if (selectedForce == null && hex != null) {
                    Force force = hex.forces.get(0);
                    if (force != null) System.out.println("FORCE CHOSEN! Forces size = " + hex.forces.size());
                    selectedForce = force;
                    //TODO check carefully! The fragment does not work properly
                    paths = selectedForce.order.pathsOrder;
                    startHex = hex;
                    endHex = null;
                    //paths = null;
                    graphPath = null;
                    System.out.println("Chosen Force: " + selectedForce + "Map Object: " + selectedForce.symbol);
                }

                System.out.println(hit(getMousePosOnMap().x, getMousePosOnMap().y, true) + " " + screenX + " " + screenY);
            }
        }*/
        // END OF VERY OLD VERSION
        return true;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        System.out.println("Touch UP Pos: " + getMousePosOnMap().x + " " + getMousePosOnMap().y);

        Actor actor = hit(getMousePosOnMap().x, getMousePosOnMap().y, true);
        //first touch
        if (startHex == null && endHex == null) {
            //hec touched
            if (actor instanceof Hex) {
                startHex = (Hex) actor;
            }
            //force touched
            if (actor instanceof Force) {
                selectedForce = (Force) actor;
                selectedForce.isSelected = true;
                startHex = selectedForce.hex;

                //orders has already been set
                if (selectedForce.order.pathsOrder.size > 0) {
                    paths = selectedForce.order.pathsOrder;
                    mileStone = selectedForce.order.mileStone;
                    addActor(mileStone);
                }
            }
        }
        //second touch
        else if (startHex != null && endHex == null) {
            if (mileStone != null) mileStone.remove();
            //hex touched
            if (actor instanceof Hex) {
                endHex = (Hex) actor;

                //first hex was touched
                if (selectedForce == null) {
                    navigate(Battalion.SPEED);
                }
                //first force was touched
                if (selectedForce != null) {
                    navigate(selectedForce.speed);
                    selectedForce.order.setPathsOrder(paths);
                    selectedForce.order.mileStone = mileStone;
                    selectedForce.isSelected = false;
                    selectedForce = null;
                }
            }

            //force touched
            if (actor instanceof Force) {
                Force force = (Force) actor;
                endHex = force.hex;

                //first hex was touched
                if (selectedForce == null) {
                    navigate(Battalion.SPEED);
                }

                //first force was touched
                if (selectedForce != null) {
                    navigate(selectedForce.speed);
                    selectedForce.order.setPathsOrder(paths);
                    selectedForce.order.mileStone = mileStone;
                    //TODO attach?
                    selectedForce.isSelected = false;
                    selectedForce = null;
                }
            }

        }
        //further touches
        else if (endHex != null) {
            endHex = null;
            graphPath = null;
            paths = null;
            if (mileStone != null) mileStone.remove();
            mileStone = null;

            //hec touched
            if (actor instanceof Hex) {
                startHex = (Hex) actor;
            }
            //force touched
            if (actor instanceof Force) {
                selectedForce = (Force) actor;
                selectedForce.isSelected = true;
                startHex = selectedForce.hex;

                //orders has already been set
                System.out.println(selectedForce.order.pathsOrder);
                if (selectedForce.order.pathsOrder.size > 0) {
                    paths = selectedForce.order.pathsOrder;
                    mileStone = selectedForce.order.mileStone;
                    addActor(mileStone);
                }
            }
        }


        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {

        System.out.println("DRAGGED!" + getMousePosOnMap().x + " " + getMousePosOnMap().y);
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        if (amount == 1) camera.zoom += 0.2;
        else camera.zoom -= 0.2;
        return true;
    }

    public int getDaysToArrival(double speed) {
        if (paths != null) {
            double trip = 0;
            for (Path path : paths) trip += path.getDays(speed);
            return (int) Math.round(trip);
        }
        return 0;
    }

    Vector3 getMousePosOnMap() {
        return camera.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));
    }

    //TODO make it correct, this is the simplest version
    Hex getHex(float x, float y) {
        if (y < 2 || y > 774) return null;
        int row = (int) ((y - 2) / 12);

        int col;
        if (row % 2 == 0) {
            if (x < 8 || x > 1032) return null;
            col = (int) ((x - 8) / 16);
        } else {
            if (x < 0 || x > 1024) return null;
            col = (int) (x / 16);
        }


        return hexGraph.getHex(col, row);
    }

    class MyInnerRenderer extends HexagonalTiledMapRenderer {
        public MyInnerRenderer(TiledMap map) {
            super(map);
        }

        @Override
        public void render() {
            super.render();

        }

        @Override
        public void renderObject(MapObject object) {
            float width = 14;
            float height = 14;
            if (object instanceof TextureMapObject) {
                TextureMapObject textureObj = (TextureMapObject) object;
                if (selectedForce != null && textureObj == selectedForce.symbol) {
                    width = 16;
                    height = 16;
                }
                this.getBatch().draw(textureObj.getTextureRegion(), textureObj.getX(), textureObj.getY(),
                        width, height);
            }
        }

    }
}