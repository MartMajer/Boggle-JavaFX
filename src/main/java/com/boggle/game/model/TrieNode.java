package com.boggle.game.model;


public class TrieNode{
    private boolean isLeaf;
    private TrieNode[] child;

    public TrieNode() {
        isLeaf = false;
        child = new TrieNode[30];

        for (int i = 0; i < 30; i++) {
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
