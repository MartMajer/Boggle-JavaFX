package com.boggle.game.model;


import com.boggle.game.utils.TrieBuilder;

import java.io.Serializable;
import java.util.ArrayList;

public class BoggleSolverModel implements Serializable {

    private final int size;
    private final ArrayList<String> wordsFound;
    private final boolean[][] isVisited;
    private final char[][] boggle;

    public BoggleSolverModel(char[][] boggle, ArrayList<String> dictionary) {
        this.boggle = boggle;
        size = this.boggle.length;
        isVisited = new boolean[size][size];
        wordsFound = new ArrayList<>();

        // Create TrieBuilder
        TrieBuilder trieBuilder = new TrieBuilder();

        // Build trie with dictionary
        TrieNodeModel root = trieBuilder.buildTrie(dictionary);

        // Call method that searches the boggle grid for all words using the words input into the trie as reference
        this.findWords(root);
    }

    public ArrayList<String> getWords() {
        return wordsFound;
    }

    private void findWords(TrieNodeModel root) {
        StringBuilder str = new StringBuilder();

        // loops through all elements in the 2d character array
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                str.append(boggle[i][j]);
                this.search(i, j, root.getChild(boggle[i][j]), str.toString());
                str = new StringBuilder();
            }
        }
    }

    private void search(int i, int j, TrieNodeModel root, String string) {
        // if word is found in trie, and not already found: adds to list of found words
        if (root != null && root.getLeaf() && !wordsFound.contains(string)) {
            wordsFound.add(string);
        }

        // If both I and j in range and we visited that element of matrix first time
        if (root != null && !isVisited[i][j]) {
            // make it visited
            isVisited[i][j] = true;

            // This loops through all childs of the current node
            for (char c = 'A'; c <= 'ž'; c++) {
                TrieNodeModel childNode = root.getChild(c);
                if (childNode != null) {
                    // This loop recursively searches reaming characters that are adjacent to the current character
                    for (int a = -1; a <= 1; a++) {
                        for (int b = -1; b <= 1; b++) {
                            //conditions insure we dont go out of the array bounds
                            if (i + a < size && j + b < size && i + a >= 0 && j + b >= 0 && !(a == 0 && b == 0) && !isVisited[i + a][j + b] && boggle[i + a][j + b] == c) {
                                this.search(i + a, j + b, childNode, string + c);
                            }
                        }
                    }
                }
            }
            // marks current element as not visited
            isVisited[i][j] = false;
        }
    }
}
