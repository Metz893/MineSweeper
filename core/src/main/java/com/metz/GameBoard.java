package com.metz;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class GameBoard {
    private int [][] board; //data structure
    private int numBombs;//number of bombs
    private int numFlags;//number of flags that still need to be placed
    public static final int BOMB = -1;//help with readability
    private GamePlayScreen gameScreen;

    //texture = 2D graphic
    private Texture emptyTile;
    private Texture emptyFloorTile;
    private Texture oneTile;
    private Texture twoTile;
    private Texture threeTile;
    private Texture fourTile;
    private Texture fiveTile;
    private Texture sixTile;
    private Texture sevenTile;
    private Texture eightTile;
    private Texture bombTile; 
    private Texture flagTile;



    public GameBoard (GamePlayScreen gameScreen) {
        this.gameScreen = gameScreen;
        board = new int[16][30];
        numBombs = 50;
        numFlags = numBombs;
        loadGraphics();
        addBombs();
        initBoardNumber();;
    }

    public GameBoard (GamePlayScreen gameScreen, int numRows, int numCols, int numBombs) {
        this.gameScreen = gameScreen;
        board = new int[numRows][numCols];
        this.numBombs = numBombs;
        this.numFlags = this.numBombs;
        loadGraphics();
        addBombs();
        initBoardNumber();
    }

    public void loadGraphics() {
        emptyTile = new Texture("assets/emptyTile.jpg");
        emptyFloorTile = new Texture("assets/empty floor.jpg");
        oneTile = new Texture("assets/oneTile.jpg");
        twoTile = new Texture("assets/twoTile.jpg");
        threeTile = new Texture("assets/threeTile.jpg");
        fourTile = new Texture("assets/fourTile.jpg");
        fiveTile = new Texture("assets/fiveTile.jpg");
        sixTile = new Texture("assets/sixTile.jpg");
        sevenTile = new Texture("assets/sevenTile.jpg");
        eightTile = new Texture("assets/eightTile.jpg");
        bombTile = new Texture("assets/bomb.jpg");
        flagTile = new Texture("assets/flagTile.jpg");
    }

    public void addBombs() {
        while (numBombs > 0) {
            int randomRow = (int) (Math.random() * board.length);
            int randomCol = (int) (Math.random() * board[0].length);

            if (board[randomRow][randomCol] != -1) {
                board[randomRow][randomCol] = -1;
                numBombs -= 1;
            }
        }
    }

    public void initBoardNumber() {
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[0].length; j++) {

                if (board[i][j] != -1) {
                    board[i][j] = countNeighbhors(i, j);
                }
            }
        }
    }

    private int countNeighbhors(int i, int j) {
        int count = 0;

        for (int k = i - 1; k <= i + 1; k++) {
            for (int l = j - 1; l <= j + 1; l++) {
                if (checkInBounds(k, l) && !(k == i && l == j) && board[k][l] == BOMB) {
                    count++;
                }
            }
        }
                        
        return count;
    }

    private boolean checkInBounds(int i, int j) {
        return i >= 0 && j >= 0 && i < board.length && j < board[0].length;
    }

    public void draw(SpriteBatch spriteBatch) {
        int xOffset = 300;
        int yOffset = 600;

        //each tile is 25x25 pixels
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[0].length; j++) {
                //+10 to a tile that is uncovered
                if (board[i][j] == 9)
                    spriteBatch.draw(bombTile, j*25 + xOffset, yOffset - i*25);

                else if (board[i][j] == -1)
                    spriteBatch.draw(bombTile, j*25 + xOffset, yOffset - i*25);
                else if (board[i][j] == 1)
                    spriteBatch.draw(oneTile, j*25 + xOffset, yOffset - i*25);
                else if (board[i][j] == 2)
                    spriteBatch.draw(twoTile, j*25 + xOffset, yOffset - i*25);
                else if (board[i][j] == 3)
                    spriteBatch.draw(threeTile, j*25 + xOffset, yOffset - i*25);
                else if (board[i][j] == 4)
                    spriteBatch.draw(fourTile, j*25 + xOffset, yOffset - i*25);
                else if (board[i][j] == 5)
                    spriteBatch.draw(fiveTile, j*25 + xOffset, yOffset - i*25);
                else if (board[i][j] == 6)
                    spriteBatch.draw(sixTile, j*25 + xOffset, yOffset - i*25);
                else if (board[i][j] == 7)
                    spriteBatch.draw(sevenTile, j*25 + xOffset, yOffset - i*25);
                else if (board[i][j] == 8)
                    spriteBatch.draw(eightTile, j*25 + xOffset, yOffset - i*25);
                

                else if (board[i][j] == 10)
                    spriteBatch.draw(emptyFloorTile, j*25 + xOffset, yOffset - i*25);
                else if (board[i][j] == 11)
                    spriteBatch.draw(oneTile, j*25 + xOffset, yOffset - i*25);
                else if (board[i][j] == 12)
                    spriteBatch.draw(twoTile, j*25 + xOffset, yOffset - i*25);
                else if (board[i][j] == 13)
                    spriteBatch.draw(threeTile, j*25 + xOffset, yOffset - i*25);
                else if (board[i][j] == 14)
                    spriteBatch.draw(fourTile, j*25 + xOffset, yOffset - i*25);
                else if (board[i][j] == 15)
                    spriteBatch.draw(fiveTile, j*25 + xOffset, yOffset - i*25);
                else if (board[i][j] == 16)
                    spriteBatch.draw(sixTile, j*25 + xOffset, yOffset - i*25);
                else if (board[i][j] == 17)
                    spriteBatch.draw(sevenTile, j*25 + xOffset, yOffset - i*25);
                else if (board[i][j] == 18)
                    spriteBatch.draw(eightTile, j*25 + xOffset, yOffset - i*25);

                //plus 20 to tile to indicate flags
                else if (board[i][j] >= 19 && board[i][j] <= 28)
                    spriteBatch.draw(flagTile, j*25 + xOffset, yOffset - i*25);

                //If less then 9, it has not been clicked
                else
                    spriteBatch.draw(emptyTile, j*25 + xOffset, yOffset - i*25);
                
            }

            
        }
    }
}
