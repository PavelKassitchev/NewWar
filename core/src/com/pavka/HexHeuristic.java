package com.pavka;

import com.badlogic.gdx.ai.pfa.Heuristic;
import com.badlogic.gdx.math.Vector2;

public class HexHeuristic implements Heuristic<Hex> {
    @Override
    public float estimate(Hex node, Hex endNode) {

        if (node == null || endNode == null) return 0f;

        //return Math.max(Math.abs(node.col - endNode.col), Math.abs(node.row - endNode.row));
        //return Vector2.dst(node.col, node.row, endNode.col, endNode.row);
        return ((Float)(node.cell.getTile().getProperties().get("cost")) + (Float)(endNode.cell.getTile().getProperties().get("cost"))) / 2;
    }
}
