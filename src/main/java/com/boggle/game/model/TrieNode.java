package com.boggle.game.model;

import java.util.HashMap;

public class TrieNode {
    private boolean isLeaf;
    private final HashMap<Character, TrieNode> child;

    public TrieNode() {
        isLeaf = false;
        child = new HashMap<>();
    }

    public boolean getLeaf() {
        return isLeaf;
    }

    public void setLeaf(boolean t) {
        isLeaf = t;
    }

    public TrieNode getChild(Character c) {
        return child.get(c);
    }

    public void setChild(Character c, TrieNode node) {
        child.put(c, node);
    }
}
