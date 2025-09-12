package com.metz;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class GameBoard {
    private int [][] board; //data structure
    private int numBombs;//number of bombs
    private int numBombOriginal = 1;
    private int numFlags;//number of flags that still need to be placed
    public static final int BOMB = -1;//help with readability
    private GamePlayScreen gameScreen;
    private boolean gameStart = true;
    private boolean gameOver = false;
    private boolean gameWon = false;
    private LeaderBoard leaders;

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
    private Texture gameover2;
    private Texture win;



    public GameBoard (GamePlayScreen gameScreen) {
        this.gameScreen = gameScreen;
        board = new int[16][30];
        numBombs = 1;
        numFlags = 0;
        leaders = new LeaderBoard();
        loadGraphics();
    }

    public GameBoard (GamePlayScreen gameScreen, int numRows, int numCols, int numBombs) {
        this.gameScreen = gameScreen;
        board = new int[numRows][numCols];
        this.numBombs = numBombs;
        this.numFlags = 0;
        LeaderBoard leaders = new LeaderBoard();
        loadGraphics();
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
        gameover2 = new Texture("assets/gameover4.jpg");
        win = new Texture("assets/win.jpg");
    }

    public void addBombs(int xOrigin, int yOrigin) {
        int originNumBombs = numBombs;

        while (numBombs > 0) {
            int randomRow = (int) (Math.random() * board.length);
            int randomCol = (int) (Math.random() * board[0].length);

            if (board[randomRow][randomCol] != -1 && (Math.abs(xOrigin - randomRow) + Math.abs(yOrigin - randomCol) > 2)) {
                board[randomRow][randomCol] = -1;
                numBombs -= 1;
            }
        }

        numBombs = originNumBombs;
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

    //precondition: location not a bomb
    private int countNeighbhors(int i, int j) {
        int count = 0;

        for (int k = i - 1; k <= i + 1; k++) {
            for (int l = j - 1; l <= j + 1; l++) {
                if (checkInBounds(k, l) && board[k][l] == BOMB) {
                    count++;
                }
            }
        }

        return count;
    }

    private boolean checkInBounds(int i, int j) {
        return i >= 0 && j >= 0 && i < board.length && j < board[0].length;
    }

    //Return location corresponding to given mouseX, mouseY position
    //Null if position not on gameboard
    public Location getTileAt(int mouseX, int mouseY) {
        int coordX = (int) ((mouseX - 300) / 25);
        int coordY = (int) ((mouseY - 100) / 25);

        if (coordX < 0 || coordY < 0 || coordY > board.length - 1 || coordX > board[0].length - 1) {
            return null;
        }
        
        else {
            return new Location(coordY, coordX);
        }
    }

    public void tileLeftClick(Location loc) {
        if (gameStart && loc != null) {
            addBombs(loc.getRow(), loc.getCol());
            initBoardNumber();
            gameStart = false;
        }

        if (!gameOver && !gameWon && loc != null) {
            if(board[loc.getRow()][loc.getCol()] < 9)
                board[loc.getRow()][loc.getCol()] += 10;
            
            else if(board[loc.getRow()][loc.getCol()] > 19)
                board[loc.getRow()][loc.getCol()] -= 10;
            
            if (board[loc.getRow()][loc.getCol()] == 9) 
                displayAllBombs(); 
        }

        if (loc != null && board[loc.getRow()][loc.getCol()] == 10){
            openTiles(loc);
        }
    }

    public void tileRightClick(Location loc) {
        if (!gameOver && !gameWon && loc != null) {
            if(board[loc.getRow()][loc.getCol()] < 9) { 
                board[loc.getRow()][loc.getCol()] += 20;
                numFlags += 1;
            }

            else if(board[loc.getRow()][loc.getCol()] >= 19) {
                board[loc.getRow()][loc.getCol()] -= 20;
                numFlags -= 1;
            }
        }

        if (gameStart) {
            return;
        }
        
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[0].length; j++) {
                if (board[i][j] == -1) {
                    return;
                }
            }
        }

        gameWon = true;
        gameScreen.getwinMusic().play();
        scoreUpdate(gameScreen.getExactTime());
    }

    //opens all blank tiles
    private void openTiles(Location loc) {
        for (int k = loc.getRow() - 1; k <= loc.getRow() + 1; k++) {
            for (int l = loc.getCol() - 1; l <= loc.getCol() + 1; l++) {
                if (checkInBounds(k, l) && board[k][l] == 0 && (Math.abs(loc.getRow() - k) + Math.abs(loc.getCol() - l) == 1)) {
                    board[k][l] += 10;
                    openTiles(new Location(k, l));
                }
                else {
                    displayNearby(loc);
                }
            }
        }
    }

    private void displayNearby(Location loc) {
        for (int k = loc.getRow() - 1; k <= loc.getRow() + 1; k++) {
            for (int l = loc.getCol() - 1; l <= loc.getCol() + 1; l++) {
                if (checkInBounds(k, l) && board[k][l] < 9 && board[k][l] > 0) {
                    board[k][l] += 10;
                }
            }
        }
    }

    private void displayAllBombs() {
        gameScreen.getLoseMusic().play();

        gameOver = true;

        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[0].length; j++) {
                if (board[i][j] == BOMB) {
                    board[i][j] += 10;  
                }
            }
        }
    }

    public void restart() {
        clearBoard();
        gameScreen.restartGameTimer();
        gameStart = true;
        gameOver = false;
        gameWon  = false;
        numBombs = numBombOriginal;
        numFlags = 0;
    }

    private void clearBoard() {
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[0].length; j++) {
                board[i][j] = 0;  
            }
        }
    }

    private void scoreUpdate(double score) {
        leaders.add(score);
    }

    public int getNumFlagsPlaced() {
        return numFlags;
    }

    public int getTotalBombs() {
        return numBombs;
    }

    public boolean getGameOver() {
        return gameOver;
    }

    public boolean getGameWon() {
        return gameWon;
    }

    public Texture getgameoverGraphic() {
        return gameover2;
    }

    public Texture getWinGraphic() {
        return win;
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