package ua.javarush.games.ticktacktoe;

import com.javarush.engine.cell.Color;
import com.javarush.engine.cell.Game;
import com.javarush.engine.cell.Key;

public class TicTacToeGame extends Game {
private boolean isGameStopped;
private int[][] model = new int[3][3];
private int currentPlayer;

@Override
public void initialize() {
    setScreenSize(3, 3);
    startGame();
    updateView();
}

public void startGame() {
    isGameStopped = false;
    currentPlayer = 1;

    for (int x = 0; x < 3; x++)
        for (int y = 0; y < 3; y++)
            model[x][y] = 0;
}

public void updateView() {
    for (int x = 0; x < 3; x++)
        for (int y = 0; y < 3; y++) {
            updateCellView(x, y, model[x][y]);
        }
}

public void updateCellView(int x, int y, int value) {
    if (value == 1)
        setCellValueEx(x, y, Color.WHITE, "X", Color.RED);
    else if (value == 2)
        setCellValueEx(x, y, Color.WHITE, "O", Color.BLUE);
    else
        setCellValueEx(x, y, Color.WHITE, " ", Color.WHITE);
}

public void computerTurn() {
    //move to the center
    if (model[1][1] == 0) {
        setSignAndCheck(1, 1);
        return;
    }

    //try to win
    for (int x = 0; x < 3; x++)
        for (int y = 0; y < 3; y++)
            if (checkFutureWin(x, y, currentPlayer)) {
                setSignAndCheck(x, y);
                return;
            }

    //dont give the opponent a chance to win
    for (int x = 0; x < 3; x++)
        for (int y = 0; y < 3; y++)
            if (checkFutureWin(x, y, 3 - currentPlayer)) {
                setSignAndCheck(x, y);
                return;
            }

    for (int x = 0; x < 3; x++)
        for (int y = 0; y < 3; y++)
            if (model[x][y] == 0) {
                setSignAndCheck(x, y);
                return;
            }
}

public void setSignAndCheck(int x, int y) {
    model[x][y] = currentPlayer;
    updateView();

    if (checkWin(x, y, currentPlayer)) {
        isGameStopped = true;
        if (currentPlayer == 1)
            showMessageDialog(Color.NONE, "You Win!", Color.GREEN, 75);
        if (currentPlayer == 2)
            showMessageDialog(Color.NONE, "Game Over", Color.RED, 75);
        return;
    }

    if (!hasEmptyCell()) {
        isGameStopped = true;
        showMessageDialog(Color.NONE, " Draw!", Color.BLUE, 75);
        return;
    }
}

@Override
public void onMouseLeftClick(int x, int y) {
    if (isGameStopped)
        return;

    if (model[x][y] != 0)
        return;

    setSignAndCheck(x, y);
    if (!isGameStopped) {
        currentPlayer = 3 - currentPlayer; // 2 <--> 1
        computerTurn();
        if (!isGameStopped) {
            currentPlayer = 3 - currentPlayer; // 2 <--> 1
        }
    }
}

public boolean hasEmptyCell() {
    for (int x = 0; x < 3; x++)
        for (int y = 0; y < 3; y++)
            if (model[x][y] == 0)
                return true;
    return false;
}

public boolean checkWin(int x, int y, int n) {
    if (model[x][0] == n && model[x][1] == n && model[x][2] == n)
        return true;
    if (model[0][y] == n && model[1][y] == n && model[2][y] == n)
        return true;
    if (model[0][0] == n && model[1][1] == n && model[2][2] == n)
        return true;
    if (model[0][2] == n && model[1][1] == n && model[2][0] == n)
        return true;
    return false;
}

public void onKeyPress(Key key) {
    if ((key == Key.SPACE && isGameStopped) || key == Key.ESCAPE) {
        startGame();
        updateView();
    }
}

public boolean checkFutureWin(int x, int y, int n) {
    if (model[x][y] != 0)
        return false;

    model[x][y] = n;
    boolean isWin = checkWin(x, y, n);
    model[x][y] = 0;

    return isWin;
}
}