package com.boggle.game.utils;

import com.boggle.game.model.TrieNode;

import java.util.ArrayList;

public class TrieBuilder {

    public TrieNode buildTrie(ArrayList<String> dictionary) {
        TrieNode root = new TrieNode();

        for (String string : dictionary) {
            if (string.length() > 2) {
                add(root, string);
            }
        }

        return root;
    }

    public void add(TrieNode trie, String s) {
        for (int i = 0; i < s.length(); i++) {
            Character c = s.charAt(i);

            if (trie.getChild(c) == null) {
                trie.setChild(c, new TrieNode());
            }
            trie = trie.getChild(c);
        }
        //once each letter is added, the isLeaf boolean is set to true to indicate a full
        trie.setLeaf(true);
    }
}
