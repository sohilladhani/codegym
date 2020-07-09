package com.codegym.games.minesweeper;

import com.codegym.engine.cell.*;
import java.util.*;

public class MinesweeperGame extends Game {
    private final static int SIDE = 9;
    private GameObject[][] gameField = new GameObject[SIDE][SIDE];
    private int countMinesOnField = 0;
    private static final String MINE = "\uD83D\uDCA3";
    private static final String FLAG = "\uD83D\uDEA9";
    private int countFlags;
    private boolean isGameStopped;
    private int countClosedTiles = SIDE * SIDE;
    private int score;
    
    public void initialize() {
        setScreenSize(SIDE, SIDE);
        createGame();
    }
    
    private void restart() {
        isGameStopped = false;
        countMinesOnField = 0;
        countClosedTiles = SIDE * SIDE;
        score = 0;
        setScore(score);
        createGame();
    }
    
    private void win() {
        isGameStopped = true;
        showMessageDialog(Color.GREEN, "Congratulations! \uD83C\uDF89", Color.YELLOW, 25);
    }
    
    private void gameOver() {
        isGameStopped = true;
        for(int y = 0; y < SIDE; y++) {
            for(int x = 0; x < SIDE; x++) {
                if(gameField[y][x].isMine) {
                    setCellValue(x, y, MINE);
                    setCellColor(x, y, Color.RED);

                }
            }
        }
        showMessageDialog(Color.RED, "Game Over", Color.YELLOW, 25);
    }
    
    public void onMouseRightClick(int x, int y) {
        markTile(x, y);
    }
    
    private void markTile(int x, int y) {
        if(!gameField[y][x].isOpen && countFlags != 0 && !isGameStopped) {
            if(!gameField[y][x].isFlag) {
                gameField[y][x].isFlag = true;
                countFlags--;
                setCellColor(x, y, Color.YELLOW);
                setCellValue(x, y, FLAG);
            } else {
                gameField[y][x].isFlag = false;
                countFlags++;
                setCellColor(x, y, Color.ORANGE);
                setCellValue(x, y, "");  
            }
        }
    }
    
    public void onMouseLeftClick(int x, int y) {
        if(!isGameStopped) {
            openTile(x, y);

        } else {
            restart();
        }
    }
    
    private void openTile(int x, int y) {
        if(!gameField[y][x].isFlag && !gameField[y][x].isOpen && !isGameStopped) {
            setCellColor(x, y, Color.GREEN);
            gameField[y][x].isOpen = true;
            countClosedTiles--;
            if(gameField[y][x].isMine == true) {
                setCellValueEx(x, y, Color.RED, MINE);
                gameOver();
            } else {
                score+=5;
                setScore(score);
                if(countClosedTiles == countMinesOnField) {
                    win();
                }
                if(gameField[y][x].countMineNeighbors == 0) {
                    setCellValue(x, y, "");
                    for(GameObject neighbor: getNeighbors(gameField[y][x])) {
                        if(!neighbor.isOpen) {
                            openTile(neighbor.x, neighbor.y);
                        }
                    }
                } else {
                    setCellNumber(x, y, gameField[y][x].countMineNeighbors);
                }
            }
        }
    }
    
    private void createGame() {
        for(int y = 0; y < SIDE; y++) {
            for(int x = 0; x < SIDE; x++) {
                setCellValue(x, y, "");
                setCellColor(x, y, Color.ORANGE);
            }
        }
        boolean isMine = false;
        for(int y = 0; y < SIDE; y++) {
            for(int x = 0; x < SIDE; x++) {
                if(getRandomNumber(10) == 1) {
                    isMine = true;
                    countMinesOnField++;
                } else {
                    isMine = false;
                }
                this.gameField[y][x] = new GameObject(x, y, isMine);
                setCellColor(x, y, Color.ORANGE);
            }
        }
        countFlags = countMinesOnField;
        countMineNeighbors();
    }
    
    private void countMineNeighbors() {
        for(int y = 0; y < SIDE; y++) {
            for(int x = 0; x < SIDE; x++) {
                if(gameField[y][x].isMine == false) {
                    for(GameObject neighbor : getNeighbors(gameField[y][x])) {
                        if(neighbor.isMine == true) {
                            gameField[y][x].countMineNeighbors++;
                        }
                    }
                }
            }
        }
    }
    
    private List<GameObject> getNeighbors(GameObject cell) {
        List<GameObject> neighbors = new ArrayList<>();
        int x = cell.x;
        int y = cell.y;
        if (y - 1 >= 0) {
            if (x - 1 >= 0) neighbors.add(gameField[y - 1][x - 1]);
            if (x >= 0 && x < SIDE) neighbors.add(gameField[y - 1][x]);
            if (x + 1 < SIDE) neighbors.add(gameField[y - 1][x + 1]);
        }
        if (y >= 0 && y < SIDE) {
            if (x - 1 >= 0) neighbors.add(gameField[y][x - 1]);
            if (x + 1 < SIDE) neighbors.add(gameField[y][x + 1]);
        }
        if (y + 1 < SIDE) {
            if (x - 1 >= 0) neighbors.add(gameField[y + 1][x - 1]);
            if (x >= 0 && x < SIDE) neighbors.add(gameField[y + 1][x]);
            if (x + 1 < SIDE) neighbors.add(gameField[y + 1][x + 1]);
        }
        return neighbors;
    }
}
