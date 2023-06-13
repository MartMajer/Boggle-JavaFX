package com.boggle.game.model;

import java.util.HashMap;

public class TrieNodeModel {
    private boolean isLeaf;
    private final HashMap<Character, TrieNodeModel> child;

    public TrieNodeModel() {
        isLeaf = false;
        child = new HashMap<>();
    }

    public boolean getLeaf() {
        return isLeaf;
    }

    public void setLeaf(boolean t) {
        isLeaf = t;
    }

    public TrieNodeModel getChild(Character c) {
        return child.get(c);
    }

    public void setChild(Character c, TrieNodeModel node) {
        child.put(c, node);
    }
}
