package com.boggle.game.model;


import java.io.*;
import java.net.URL;
import java.util.*;

/**
 * This is the BoggleSolver class. It contains the algorithm that is able to take in the array of characters and solve
 * for all possible words that can be formed by the boggle rules. It uses a trie data structure to efficiently check
 * if certain combinations of letters can form a word according to a supplied dictionary
 */
public class BoggleSolver {

    private int _size;
    private ArrayList<String> _wordsFound;
    private boolean[][] _isVisited;
    private char[][] _boggle;

    /*
     * The constructor for BoggleSolver. It sets up the solver by setting up the trie structure, taking in the array
     * of characters. It also reads the dictionary file using the URL and adds each string of dictionary words to
     * the trie data structure.
     */
    public BoggleSolver(char[][] boggle) throws IOException {

        _boggle = boggle;
        //size of board is determined by checking length of one side of the 2d array
        _size = _boggle.length;
        _isVisited = new boolean[_size][_size];
        _wordsFound = new ArrayList<>();
        ArrayList<String> dictionary = new ArrayList<>();

        /*
         * This is where the dictionary file is imported. Originally, the dictionary was a text file in the same
         * directory as the other java files, but there wer problems with getting the relative path of the file
         * to be read correctly. To fix this and insure that the file is able to be read by any windows/mac/linux user
         * with no issues, the file was uploaded to github and is accessed from there.
         */
        URL url = new URL("https://raw.githubusercontent.com/ilyabodo/boggle/master/dictionary.txt");
        BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
        String line;

        //an arraylist of all words in the dictionary is created by reading each line of the txt file
        while ((line = reader.readLine()) != null) {
            dictionary.add(line);
        }
        reader.close();

        //the initial trienode is created
        TrieNode root = new TrieNode();

        //each string is added to the trienode network
        for (String string : dictionary) {
            add(root, string);
        }

        //calls method that searches the boggle grid for all words using the words input into the trie as reference
        this.findWords(root);
    }

    /*
     * This method is called by the Boggle class and checks if the word that a user input is actually a real english
     * word located within this boggle game grid
     */
    public boolean validWord(String word) {
        if (_wordsFound.contains(word)) {
            return true;
        }

        else {
            return false;
        }
    }

    /*
     * This method returns the list of all words that the bogglesolver found in the grid
     */
    public ArrayList<String> getWords() {
        return _wordsFound;
    }

    /*
     * This method is used to add each word from the dictionary to the trie
     */
    public void add(TrieNode trie, String s) {

        for (int i = 0; i < s.length(); i++) {
            //the character is converted into an int. 'A' is subtracted due to the default int values with characters
            int j = s.charAt(i) - 'A';

            if (trie.getChild(j) == null) {
                trie.setChild(j, new TrieNode());
            }
            trie = trie.getChild(j);
        }
        //once each letter is added, the isLeaf boolean is set to true to indicate a full complete word.
        trie.setLeaf(true);
    }

    /*
     * This method is the top method of the actual solving algorithm and is able to solve the boggle board
     */
    private void findWords(TrieNode root) {

        StringBuilder str = new StringBuilder();

        // loops through all elements in the 2d character array
        for (int i = 0 ; i < _size; i++) {
            for (int j = 0 ; j < _size; j++) {
                str.append(_boggle[i][j]);
                this.search( i, j, root.getChild(_boggle[i][j] - 'A'), str.toString());
                str = new StringBuilder();
            }
        }
    }

    /*
     * This method finds all of the words within the boggle board.
     */
    private void search(int i, int j, TrieNode root, String string) {
        // if word is found in trie, and not already found: adds to list of found words
        if (root.getLeaf() && !_wordsFound.contains(string)) {
            _wordsFound.add(string);
        }

        // If both I and j in  range and we visited
        // that element of matrix first time
        if (!_isVisited[i][j]) {
            // make it visited
            _isVisited[i][j] = true;

            // This loops through all childs of the current node
            for (int n = 0; n < 30; n++) {
                if (root.getChild(n) != null) {
                    // 'A' is  added because of numbering system for characters
                    char letter = (char) (n + 'A');

                    // This loop recursively searches reaming characters that are adjacent to the current character
                    for (int a = -1; a <= 1; a++) {
                        for (int b = -1; b <= 1; b++) {
                            //conditions insure we dont go out of the array bounds
                            if (i + a < _size && j + b < _size && i + a >= 0 && j + b >= 0 && !(a == 0 && b == 0) && !_isVisited[i + a][j + b] && _boggle[i + a][j + b] == letter) {
                                this.search(i + a, j + b,root.getChild(n),string + letter);
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
