package com.boggle.game.utils;

import com.boggle.game.model.TrieNodeModel;
import java.util.ArrayList;

public class TrieBuilder {

    public TrieNodeModel buildTrie(ArrayList<String> dictionary) {
        TrieNodeModel root = new TrieNodeModel();

        for (String string : dictionary) {
            if (string.length() > 2) {
                add(root, string);
            }
        }

        return root;
    }

    public void add(TrieNodeModel trie, String s) {
        for (int i = 0; i < s.length(); i++) {
            Character c = s.charAt(i);

            if (trie.getChild(c) == null ) {
                trie.setChild(c, new TrieNodeModel());
            }
            trie = trie.getChild(c);
        }
        //once each letter is added, the isLeaf boolean is set to true to indicate a full complete word.
        trie.setLeaf(true);
    }
}
