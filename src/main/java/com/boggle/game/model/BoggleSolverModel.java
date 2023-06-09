package com.boggle.game.model;


import com.boggle.game.utils.TrieBuilder;

import java.io.*;
import java.util.*;
public class BoggleSolverModel implements Serializable {

    private int _size;
    private ArrayList<String> _wordsFound;
    private boolean[][] _isVisited;
    private char[][] _boggle;

    public BoggleSolverModel(char[][] boggle, ArrayList<String> dictionary) {
        _boggle = boggle;
        _size = _boggle.length;
        _isVisited = new boolean[_size][_size];
        _wordsFound = new ArrayList<>();

        // Create TrieBuilder
        TrieBuilder trieBuilder = new TrieBuilder();

        // Build trie with dictionary
        TrieNodeModel root = trieBuilder.buildTrie(dictionary);

        // Call method that searches the boggle grid for all words using the words input into the trie as reference
        this.findWords(root);
    }

    public ArrayList<String> getWords() {
        return _wordsFound;
    }

    private void findWords(TrieNodeModel root) {
        StringBuilder str = new StringBuilder();

        // loops through all elements in the 2d character array
        for (int i = 0 ; i < _size; i++) {
            for (int j = 0 ; j < _size; j++) {
                str.append(_boggle[i][j]);
                this.search(i, j, root.getChild(_boggle[i][j]), str.toString());
                str = new StringBuilder();
            }
        }
    }

    private void search(int i, int j, TrieNodeModel root, String string) {
        // if word is found in trie, and not already found: adds to list of found words
        if (root != null && root.getLeaf() &&  !_wordsFound.contains(string)) {
            _wordsFound.add(string);
        }

        // If both I and j in range and we visited that element of matrix first time
        if (root != null && !_isVisited[i][j]) {
            // make it visited
            _isVisited[i][j] = true;

            // This loops through all childs of the current node
            for (char c = 'A'; c <= 'ž'; c++) {
                TrieNodeModel childNode = root.getChild(c);
                if (childNode != null) {
                    // This loop recursively searches reaming characters that are adjacent to the current character
                    for (int a = -1; a <= 1; a++) {
                        for (int b = -1; b <= 1; b++) {
                            //conditions insure we dont go out of the array bounds
                            if (i + a < _size && j + b < _size && i + a >= 0 && j + b >= 0 && !(a == 0 && b == 0) && !_isVisited[i + a][j + b] && _boggle[i + a][j + b] == c) {
                                this.search(i + a, j + b, childNode, string + c);
                            }
                        }
                    }
                }
            }
            // marks current element as not visited
            _isVisited[i][j] = false;
        }
    }
}
