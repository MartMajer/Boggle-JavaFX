# Boggle Game 

Boggle is a word game that is played on a 4x4 board with 16 letter tiles. The goal is to find as many words as possible given a time constraint.

## Technologies

The project uses the following technologies:

- Java for the game logic using [Trie](https://en.wikipedia.org/wiki/Trie) type of tree 
- JavaFX for the graphical user interface
- Sockets for real-time chatroom functionality
- Java Naming and Directory Interface (JNDI) for connecting and binding objects in the game
- Java Remote Method Invocation (RMI) for remote game logic
- XML Serialization for saving and loading game state
- Java Object Serialization for maintaining highscores across sessions

## Features

- Singleplayer and multiplayer *currently only 2
- Real-time chatroom for live player communication
- The game is packaged as a standalone MSI installer that can be run on any Windows machine *still in progress
- Players can find words by connecting adjacent letters on the board 
- The game includes a highscore feature that stores top scores between sessions
- Ability to save and load game state at any time

## Installation 

To install the game, download the latest MSI installer from the releases page and run it. The installer will guide you through the steps needed to install the game on your computer.
*This part I'm still trying to figure out, I am using template created by PerryCameron https://github.com/PerryCameron/maven-jpackage-template, but game jar can be played by installing Java Liberica JDK https://bell-sw.com/pages/downloads/#/java-18-current  

## Usage 

After installation, you can start the game from your Start menu or desktop. The game board will be immediately presented to you.

To play the game:
1. The timer starts as soon as the game board is presented after choosing game mode.
2. Find words on the board by connecting adjacent letters. Words must be at least 3 letters long.
3. Click the letters to form words you find into the text box and press Enter to submit them.
4. The game ends when the timer runs out. Your score will be the total number of valid words you found.
5. Game can be played idefinately because of New round function.

## Credits

This project was created and is maintained by Martin Majeric
