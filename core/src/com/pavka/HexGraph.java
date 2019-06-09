package com.pavka;

import com.badlogic.gdx.ai.pfa.Connection;
import com.badlogic.gdx.ai.pfa.DefaultGraphPath;
import com.badlogic.gdx.ai.pfa.GraphPath;
import com.badlogic.gdx.ai.pfa.indexed.IndexedAStarPathFinder;
import com.badlogic.gdx.ai.pfa.indexed.IndexedGraph;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;



public class HexGraph implements IndexedGraph<Hex> {

    HexHeuristic hexHeuristic = new HexHeuristic();
    Array<Hex> hexes = new Array<Hex>();
    Array<Path> paths = new Array<Path>();

    ObjectMap<Hex, Array<Connection<Hex>>> pathsMap = new ObjectMap<Hex, Array<Connection<Hex>>>();
    private int lastNodeIndex = 0;

    public void addHex(Hex hex){
        hex.index = lastNodeIndex;
        lastNodeIndex++;

        hexes.add(hex);
    }

    public void connectHexes(Hex fromHex, Hex toHex){
        Path path = new Path(fromHex, toHex);
        //if (fromHex == null || toHex == null) return;
        if(!pathsMap.containsKey(fromHex)){
            pathsMap.put(fromHex, new Array<Connection<Hex>>());
        }
        pathsMap.get(fromHex).add(path);
        paths.add(path);
    }

    public GraphPath<Hex> findPath(Hex startHex, Hex goalHex){
        GraphPath<Hex> hexPath = new DefaultGraphPath<Hex>();
        System.out.println("Default path length = " + hexPath.getCount());
        new IndexedAStarPathFinder<Hex>(this).searchNodePath(startHex, goalHex, hexHeuristic, hexPath);
        System.out.println("Path length = " + hexPath.getCount());
        return hexPath;
    }

    @Override
    public int getIndex(Hex node) {
        return node.index;
    }

    @Override
    public int getNodeCount() {
        return lastNodeIndex;
    }

    @Override
    public Array<Connection<Hex>> getConnections(Hex fromNode) {
        if(pathsMap.containsKey(fromNode)){
            return pathsMap.get(fromNode);
        }

        return new Array<Connection<Hex>>(0);
    }
}
