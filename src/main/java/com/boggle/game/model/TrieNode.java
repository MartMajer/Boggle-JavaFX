package com.boggle.game.model;


public class TrieNode{
    private boolean isLeaf;

    public static final int alphabet_size = 317;
    private TrieNode[] child;

    public TrieNode() {
        isLeaf = false;
        child = new TrieNode[alphabet_size];

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

    public TrieNode getChild(int i) {

        return child[i];
    }

    public void setChild(int i, TrieNode node) {

        child[i] = node;
    }
}
