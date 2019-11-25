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
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Array;

import java.util.Iterator;
import java.util.Random;

import static com.pavka.Nation.*;
import static com.pavka.UnitType.INFANTRY;

public class Play extends Stage implements Screen {

    public static final String MAP = "maps/WarMap.tmx";
    public static int turn;
    public static int time;
    public static TiledMap map = new TmxMapLoader().load(MAP);


    public static HexGraph hexGraph;

    public static GraphPath<Hex> graphPath;

    public static MapLayer objectLayer = map.getLayers().get("ObjectLayer");
    ;
    public static TiledMapTileLayer tileLayer = (TiledMapTileLayer) map.getLayers().get("TileLayer");
    public static Commander blackCommander;
    public static Commander whiteCommander;
    static Array<Force> blackTroops = new Array<Force>();
    static Array<Force> whiteTroops = new Array<Force>();
    static Array<Base> blackBases = new Array<Base>();
    static Array<Base> whiteBases = new Array<Base>();
    public boolean newMode;
    Hex startHex;
    Hex endHex;
    MileStone mileStone;
    ShapeRenderer shapeRenderer;
    Array<Path> paths;
    private HexagonalTiledMapRenderer renderer;
    private OrthographicCamera camera;
    private Force selectedForce;
    private Force forceToAttach;
    private Force forceToMove;
    private Hex selectedHex;
    private Hex hexToMove;
    private Base selectedBase;
    private Array<Path> selectedPaths;

    private Hex currentHex;
    private MileStone currentStone;
    private boolean secondClick;

    private Control control;


    private Window selectedWindow;
    //private Tableau tableau;
    private Array<Tableau> tableaus = new Array<Tableau>();
    private int tableauNum;
    private int attachNum;

    //TODO exclude this variables

    private Force austria;
    private Force france;
    private Base a;
    private Base b;
    private Force frenchArtillery;
    private Force austrianArtillery;
    private Force frenchCavalry;
    private Force austrianCavalry;

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

    public static Base selectRandomBase(int color) {
        Array<Base> bases = null;
        switch (color) {
            case WHITE:
                bases = whiteBases;
                break;
            case BLACK:
                bases = blackBases;
                break;
        }
        Random random = new Random();
        int index = random.nextInt(bases.size);
        return bases.get(index);
    }

    public static Array<Path> navigate(Hex start, Hex finish) {
        Array<Path> paths = new Array<Path>();
        if (start != finish) {
            graphPath = hexGraph.findPath(start, finish);
            Iterator<Hex> iterator = graphPath.iterator();
            Hex sHex = null;
            if (iterator.hasNext()) sHex = iterator.next();
            Hex eHex;
            while (iterator.hasNext()) {
                eHex = iterator.next();
                paths.add(Play.hexGraph.getPath(sHex, eHex));
                sHex = eHex;
            }


        }
        return paths;
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
        if(keycode == Input.Keys.G) {
            System.out.println("Selected force = " + selectedForce + " selected window = " + selectedWindow + " force to attach = " + forceToAttach + " selected hex = " +
                    selectedHex);
            if(selectedWindow != null) {
                System.out.println("Choice = " + selectedWindow.choice);
            }
        }
        if (keycode == Input.Keys.C) {
            /*Force force = new Force(new Squadron(this, Nation.FRANCE, hexGraph.getHex(8, 4)), new Squadron(this, Nation.FRANCE, hexGraph.getHex(8, 4)));
            force.order.isForaging = 0.8;
            force.order.seekBattle = true;
            force.name = "Cavalry Sq.";

            System.out.println(force.getX() + " " + force.getY());
            System.out.println(force.order.pathsOrder);
            whiteTroops.add(force);
            addActor(force);*/

            france = Test.force1;
            france.setPlay(this);
            //france.hex = hexGraph.getHex(8, 4);
            france.order.seekBattle = true;
            france.order.isForaging = 0.8;
            france.name = "France";
            //whiteTroops.add(france);
            addActor(france);
        }

        if (keycode == Input.Keys.O) {
            /*Force force = new Force(new Squadron(this, Nation.AUSTRIA, hexGraph.getHex(18, 18)), new Squadron(this, Nation.AUSTRIA, hexGraph.getHex(18, 18)));
            force.order.isForaging = 0.9;
            force.order.seekBattle = true;
            force.name = "2.Squadron";
            blackTroops.add(force);
            addActor(force);*/
            austria = Test.force2;
            austria.setPlay(this);
            //austria.hex = hexGraph.getHex(18, 18);
            austria.order.seekBattle = true;
            austria.order.isForaging = 0.8;
            austria.name = "Austria";
            //blackTroops.add(austria);
            addActor(austria);
        }

        if (keycode == Input.Keys.B) {

            a = new Base(this, Nation.AUSTRIA, hexGraph.getHex(28, 28));
            //addActor(a);

        }
        if (keycode == Input.Keys.R) {
            b = new Base(this, Nation.FRANCE, hexGraph.getHex(2, 2));
            //addActor(b);
        }
        if (keycode == Input.Keys.P) {

            a.sendSupplies(austria, 250, 50);
            //System.out.println(train.order.pathsOrder);
        }
        if (keycode == Input.Keys.L) {

            b.sendSupplies(france, 250, 50);
            //System.out.println(train.order.pathsOrder);
        }


        if (keycode == Input.Keys.T) {
            /*Commander commander = new Commander(Nation.FRANCE, hexGraph.getHex(32, 32));
            Force force = new Force(commander);
            force.name = "Headquarters";
            force.general = commander;
            whiteCommander = commander;
            whiteTroops.add(force);
            addActor(force);*/
            //addActor(commander);
            austrianArtillery = Test.austrianArt;
            austrianArtillery.setPlay(this);
            austrianArtillery.order.seekBattle = true;
            austrianArtillery.order.isForaging = 0.8;
            austrianArtillery.name = "Austrian Artillery";
            addActor(austrianArtillery);

            frenchArtillery = Test.frenchArt;
            frenchArtillery.setPlay(this);
            frenchArtillery.order.seekBattle = true;
            frenchArtillery.order.isForaging = 0.8;
            frenchArtillery.name = "French Artillery";
            addActor(frenchArtillery);

            austrianCavalry = Test.austrianCav;
            austrianCavalry.setPlay(this);
            austrianCavalry.order.seekBattle = true;
            austrianCavalry.order.isForaging = 0.8;
            austrianCavalry.name = "Austrian Cavalry";
            addActor(austrianCavalry);

            frenchCavalry = Test.frenchCav;
            frenchCavalry.setPlay(this);
            frenchCavalry.order.seekBattle = true;
            frenchCavalry.order.isForaging = 0.8;
            frenchCavalry.name = "French Cavalry";
            addActor(frenchCavalry);

        }
        if (keycode == Input.Keys.Q) {
            /*turn++;
            act();*/

            if (selectedForce != null) selectedForce.isSelected = false;
            selectedForce = null;
            startHex = null;
            endHex = null;
            paths = null;
            if (mileStone != null) {
                mileStone.remove();
                mileStone = null;
            }

            /*Array<Hex> battlefields = new Array<Hex>();
            for(Hex h: hexGraph.hexes){
                if(!h.whiteForces.isEmpty() && !h.blackForces.isEmpty()) battlefields.add(h);
            }
            for(Hex hx: battlefields) {
                Fighting fighting = hx.startFighting();
                fighting.resolve();
            }*/
            for (time = 0; time < 4; time++) act();

            System.out.println();
            System.out.println("Number of battles = " + Fighting.battles);
            System.out.println();

        }

        if (keycode == Input.Keys.S) {
            Test.main(null);
        }
        /*if (keycode == Input.Keys.L) {
            LogisticTest.main(null);
        }*/

        if (keycode == Input.Keys.M) {
            newMode = !newMode;
        }

        if (keycode == Input.Keys.F) {
            for (Force force : whiteTroops) {
                System.out.println(force.foodStock);
                force.eat();
                System.out.println(force.forage());
            }
        }
        if (keycode == Input.Keys.U) {
            System.out.println("White bases: " + whiteBases);
            for (Base base : blackBases) System.out.println(base);
        }
        if (keycode == Input.Keys.Z) {
            if (selectedForce != null) Test.list(selectedForce);
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
        } else {
            paths = new Array<Path>();
        }

    }

    private void closeTableau(int i) {
        for (int num = tableauNum; num > i - 1; num--) {
            tableaus.get(num - 1).remove();
            tableaus.removeValue(tableaus.get(num - 1), true);
        }
        tableauNum = i - 1;
    }
    private void clearWindow(Window w) {
        w.parent = null;
        w.remove();
        if(!w.children.isEmpty()) {
            for(Window child: w.children) {
                clearWindow(child);
            }
        }
    }

    private void closeWindow(Window w) {

        if(w.parentLabel != null) {
            w.parentLabel.childWindow = null;
            w.parentLabel.changeStyle();
        }
        if(w.parent != null) {
            Window p = w.parent;
            if(p.choice != null) {
                closeWindow(p);
                forceToAttach = null;
            }
            else {
                selectedWindow = p;
                p.children.removeValue(w, true);
            }
        }
        else selectedWindow = null;

        clearWindow(w);

        /*System.out.println("CHILDREN: " + w.children);
        if(w.children.isEmpty()) {
            w.remove();
            if(w.parent != null) {
                Window parent = w.parent;
                parent.children.removeValue(w, true);
            }
        }
        else {
            for(Window children: w.children) {
                System.out.println("CLOSING A CHILD..");
                closeWindow(children);
            }


        }

         */
    }

    private void closeWindows() {
        closeWindows(false);
    }

    private void closeWindows(boolean forceSelected) {
        if (selectedWindow != null) {
            Window root = selectedWindow;
            while (root.parent != null) {
                root = root.parent;
            }
            closeWindow(root);
            clearSelections(forceSelected);
        }
        clearSelections(forceSelected);
    }

    private void clearSelections() {
        clearSelections(false);
    }

    private void clearSelections(boolean forceSelected) {
        if(!forceSelected) {
            selectedForce = null;
        }
            forceToAttach = null;
            selectedWindow = null;
            selectedBase = null;
            selectedHex = null;

    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        //NEW VERSION
        if (button == Input.Buttons.LEFT) {

            float X = getMousePosOnMap().x;
            float Y = getMousePosOnMap().y;
            Actor a = hit(X, Y, true);
            System.out.println("ACTOR IS " + a);


                if (a instanceof Hex) {
                    Hex hx = (Hex) a;
                    if (selectedForce == null && selectedHex == null && selectedBase == null && forceToAttach == null) {
                        closeWindows();
                        selectedWindow = new Window(this, hx, X, Y);

                    } else closeWindows();
                }


                if (a instanceof Base) {
                    Base b = (Base) a;
                    Hex hx = b.hex;
                    if (selectedForce == null && selectedHex == null && selectedBase == null && forceToAttach == null) {
                        closeWindows();
                        selectedWindow = new Window(this, hx, X, Y);

                    }
                    else closeWindows();

                }
                if (a instanceof Force) {
                    Force f = (Force) a;
                    Hex hx = f.hex;
                    if (selectedForce == null && selectedHex == null && selectedBase == null && forceToAttach == null) {
                        closeWindows();
                        selectedWindow = new Window(this, hx, X, Y);

                    }
                    else {
                        System.out.println("Closing from force...");
                        closeWindows();
                    }

                }
                if (a instanceof SwitchLabel) {
                    SwitchLabel label = (SwitchLabel) a;
                    Window w = label.window;

                    if(label == w.closeLabel) {
                        closeWindow(w);
                    }
                    else {
                        for(int i = 0; i < w.forces.size; i++) {
                            if(label == w.extendLabels[i]) {
                                label.changeStyle();
                                if (label.getStyle() == label.styleTwo) {
                                    Force fc = w.forces.get(i);
                                    selectedWindow = new Window(this, fc, label, X, Y);

                                }
                                else {
                                    closeWindow(label.childWindow);
                                    label.changeStyle();
                                }
                            }
                            else if(label == w.forceLabels[i]) {
                                if (forceToAttach != null) {
                                    if (!(w.forces.get(i)).isUnit) {
                                        w.forces.get(i).attach(forceToAttach);
                                        closeWindows();
                                    }
                                }
                                else {
                                    selectedForce = w.forces.get(i);
                                    selectedWindow = new Window(this, w, selectedForce, X, Y);

                                }
                            }
                        }
                    }

                }
                else if(a instanceof Label) {
                    Label label = (Label) a;
                    Window p = selectedWindow.parent;

                    if (selectedWindow.choice != null) {
                        Choice choice = selectedWindow.choice;

                        if(label == choice.pathLabel) {
                            hexToMove = selectedHex;
                            closeWindows();
                        }

                        if(label == choice.builtLabel) {
                            selectedHex.builtBase();
                            closeWindows();
                        }
                        if(label == choice.createLabel) {
                            new Force(this, FRANCE, selectedHex);
                            closeWindows();
                        }
                        if(label == choice.upgradeLabel) {
                            selectedBase.upgrade();
                            closeWindows();
                        }
                        if(label == choice.destroyLabel) {
                            selectedBase.destroy();
                            closeWindows();
                        }
                        if(label == choice.detachLabel) {
                            if(selectedForce.superForce != null) {
                                selectedForce.superForce.detach(selectedForce);
                                closeWindows();
                            }
                        }
                        if(label == choice.attachLabel) {
                            forceToAttach = selectedForce;
                            selectedForce = null;
                            selectedWindow = new Window(this, selectedWindow, forceToAttach, true, X, Y);
                        }
                        if(label == choice.moveLabel) {
                            forceToMove = selectedForce;
                            closeWindows();
                        }
                        if(p != null && (label == p.baseLabel || label == p.hexLabel)) {
                            selectedHex = null;
                            selectedBase =null;
                            selectedForce = null;
                            closeWindow(selectedWindow);
                        }


                    }

                    else if (label != selectedWindow.hexLabel && label != selectedWindow.baseLabel) {
                        closeWindows();
                    }

                    else {
                        if(label == selectedWindow.hexLabel) {
                            selectedHex =  selectedWindow.hex;
                            selectedWindow = new Window(this, selectedWindow, selectedWindow.hex, X, Y);
                        }
                        if(label == selectedWindow.baseLabel) {
                            selectedBase = selectedWindow.base;
                            selectedWindow = new Window(this, selectedWindow, selectedWindow.base, X, Y);
                        }
                    }

                }

            }


        return true;
    }

    //OLD VERSION

    /*{
        if (button == Input.Buttons.LEFT) {

            Actor hex = hit(getMousePosOnMap().x, getMousePosOnMap().y, true);
            if (hex instanceof Hex && Path.isHexInside(paths, (Hex) hex)) {
                selectedHex = (Hex) hex;
                selectedPaths = paths;
                System.out.println("CLICKED!");
                endHex = paths.peek().toHex;
            }
        } else if (button == Input.Buttons.RIGHT) {
            Actor actor = hit(getMousePosOnMap().x, getMousePosOnMap().y, true);
            if (!secondClick) {
                if (actor instanceof Control) System.out.println("Control!");
                if (actor instanceof Hex) System.out.println("Hex! " + ((Hex)actor).getRelX() + " " + ((Hex)actor).getRelY()
                        + " Column: " + ((Hex)actor).col + " Row: " + ((Hex)actor).row + " BASE: " + ((Hex)(actor)).base);
                if (actor instanceof Force) System.out.println("Force!");
                if (actor instanceof Label) System.out.println("Label!");
                if (actor instanceof Base) System.out.println("Base!");
                startHex = null;
                endHex = null;
                selectedForce = null;
                selectedHex = null;
                paths = null;
                if (mileStone != null) mileStone.remove();
                mileStone = null;
                if (currentStone != null) currentStone.remove();
                currentStone = null;
                currentHex = null;
                secondClick = true;
                selectedPaths = null;
                if (control != null) control.remove();
            } else {
                if (actor instanceof Hex) {
                    control = new Control((Hex) actor);
                    addActor(control);
                }
                else if(actor instanceof Force) {
                    control = new Control(((Force)actor).hex);
                    addActor(control);
                }
                else if(actor instanceof Base) {
                    control = new Control(((Base)actor).hex);
                    addActor(control);
                }
                secondClick = false;
            }
        }

        return true;
    }*/

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        //NEW VERSION

        return true;
    }
    //OLD VERSION
    /*{
        if (button == Input.Buttons.LEFT) {

            System.out.println("Touch UP Pos: " + getMousePosOnMap().x + " " + getMousePosOnMap().y);


            Actor actor = hit(getMousePosOnMap().x, getMousePosOnMap().y, true);

            if (selectedHex != null && actor instanceof Hex && selectedHex != (Hex) actor) {
                System.out.println("YES!");
                Hex start = paths.first().fromHex;
                Hex finish = paths.peek().toHex;

                if (selectedHex != endHex) {

                } else {
                    endHex = (Hex) actor;
                }


                mileStone.remove();
                double speed = INFANTRY.SPEED;
                //mileStone = new MileStone(end);
                mileStone = new MileStone(endHex);
                if (selectedForce != null) {
                    System.out.println("FORCE SELECTED");
                    speed = selectedForce.getForceSpeed();
                    selectedForce.order.setPathsOrder(paths);
                    selectedForce.order.mileStone = mileStone;
                }
                mileStone.days = Path.getDaysToGo(paths, speed);
                System.out.println(mileStone.days);
                addActor(mileStone);
                selectedHex = null;
                //TODO DRAG PATH METHOD!
            } else {
                selectedHex = null;
                //first touch
                if (startHex == null && endHex == null) {
                    //hex touched
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
                            navigate(INFANTRY.SPEED);
                        }
                        //first force was touched
                        if (selectedForce != null) {
                            selectedForce.order.target = null;
                            navigate(selectedForce.getForceSpeed());
                            selectedForce.order.setPathsOrder(paths);
                            selectedForce.order.mileStone = mileStone;

                        }
                    }

                    //force touched
                    if (actor instanceof Force) {
                        Force force = (Force) actor;
                        endHex = force.hex;

                        //first hex was touched
                        if (selectedForce == null) {
                            navigate(INFANTRY.SPEED);
                        }

                        //first force was touched
                        if (selectedForce != null) {
                            navigate(selectedForce.getForceSpeed());
                            selectedForce.order.setPathsOrder(paths);
                            selectedForce.order.mileStone = mileStone;
                            //TODO attach?

                        }
                    }

                }
                //further touches
                else if (endHex != null) {
                    endHex = null;
                    graphPath = null;
                    paths = null;
                    if (selectedForce != null) selectedForce.isSelected = false;
                    selectedForce = null;
                    if (mileStone != null) mileStone.remove();
                    mileStone = null;

                    //hex touched
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
            }
        }


        return false;
    }*/

    /*@Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        if (selectedHex != null) {

            Hex start = paths.first().fromHex;
            Hex finish = paths.peek().toHex;
            //Hex end = finish;

            Hex current = getHex(getMousePosOnMap().x, getMousePosOnMap().y);
            if (selectedHex != endHex) {

                paths = navigate(start, current);
                paths.addAll(navigate(current, finish));
            } else {
                System.out.println("FINISH SELECTED!");
                //paths = navigate(start, endHex);
                //paths.addAll(navigate(endHex, current));
                //end = current;
                //selectedPaths = paths;
                paths = new Array<Path>(selectedPaths);
                paths.addAll(navigate(endHex, current));
                System.out.println("SELECTED PATH: " + selectedPaths.size + "TOTAL PATH: " + paths.size);
            }
        }


        return true;
    }*/

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        if (startHex != null && endHex == null) {
            Hex hex = getHex(getMousePosOnMap().x, getMousePosOnMap().y);
            if (hex != currentHex) {
                if (currentStone != null) {
                    currentStone.remove();
                }
                currentStone = new MileStone((hex));
                addActor(currentStone);
                currentHex = hex;
                Array<Path> trace = navigate(startHex, currentHex);
                double speed = INFANTRY.SPEED;
                if (selectedForce != null) speed = selectedForce.getForceSpeed();
                currentStone.days = Path.getDaysToGo(trace, speed);
            }
        } else {
            currentHex = null;
            if (currentStone != null) currentStone.remove();
            currentStone = null;
        }
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