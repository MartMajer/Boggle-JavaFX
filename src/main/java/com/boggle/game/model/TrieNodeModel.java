package com.boggle.game.model;


public class TrieNodeModel {
    private boolean isLeaf;

    public static final int alphabet_size = 350;
    private TrieNodeModel[] child;

    public TrieNodeModel() {
        isLeaf = false;
        child = new TrieNodeModel[alphabet_size];

        for (int i = 0; i < alphabet_size; i++) {
            child[i] = null;
        }
    }

    public boolean getLeaf() {
        return isLeaf;
    }

    public void setLeaf(boolean t) {
        isLeaf = t;
    }

    public TrieNodeModel getChild(int i) {

        return child[i];
    }

    public void setChild(int i, TrieNodeModel node) {

        child[i] = node;
    }
}
