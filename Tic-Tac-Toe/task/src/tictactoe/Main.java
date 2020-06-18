package tictactoe;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Scanner;

class GameField {
    private char[][] fieldData;
    private int rows;
    private int cols;
    private String inStrFieldData;
    private boolean isXWin;
    private boolean isOWin;
    private int numOfX;
    private int numOfO;
    private String stance;

    public GameField(int rows, int cols) {
        fieldData = new char[rows][cols];
        this.rows = rows;
        this.cols = cols;

        for (char[] row : fieldData) {
            Arrays.fill(row, '_');
        }
    }

    public void fillField(String inStrData) {
        int curRow = 0;
        int curCol = 0;
        inStrFieldData = inStrData;

        for (int i = 0; i < inStrFieldData.length(); i++) {
            fieldData[curRow][curCol] = inStrFieldData.charAt(i);

            curCol++;
            if (curCol >= cols) {
                curCol = 0;
                curRow++;
            }

            if (curRow >= rows) {
                break;
            }
        }
    }

    public boolean isEmptyCellOfField(int[] arrayCoord) {
        return fieldData[arrayCoord[0]][arrayCoord[1]] == '_';
    }

    public boolean checkEmptyCellByUserInput(int[] userCoord) {
        return isEmptyCellOfField(convertToInnerCoord(userCoord[0], userCoord[1]));
    }

    public void setCellInFieldByUserInput(int[] userCoord, char val) {
        int[] arrayCoord = convertToInnerCoord(userCoord[0], userCoord[1]);
        fieldData[arrayCoord[0]][arrayCoord[1]] = val;
    }

    public void printStanceOfField() {
        System.out.println(stance);
    }

    public void printField() {
        printLine();
        for (int row = 0; row < rows; row++) {
            System.out.print("| ");
            for (int cnum = 0; cnum < cols; cnum++) {
                System.out.print(fieldData[row][cnum] + " ");
            }
            System.out.println("|");
        }
        printLine();
    }

    private static void printLine() {
        System.out.println("---------");
    }

    private int[] convertToInnerCoord(int x, int y) {
        // Convert from Cartesian coordinate system to 2 dim array coordinates.
        int[] arrayCoord = new int[2];
        // row
        arrayCoord[0] = rows - y;
        // col
        arrayCoord[1] = --x;
        return arrayCoord;
    }

    public boolean analyseStance() {
        numOfX = numOfO =0;
        isOWin = isXWin = false;
        HashSet<Character> uniqHorizontalElems = new HashSet<>(4);
        HashSet<Character> uniqVerticalElems = new HashSet<>(4);
        HashSet<Character> uniqDiagonalElems = new HashSet<>(4);
        HashSet<Character> uniqDiagonalElems2 = new HashSet<>(4);

        for (int i = 0; i < rows; i++) {
            uniqHorizontalElems.clear();
            uniqVerticalElems.clear();

            for (int j = 0; j < cols; j++) {
                if (fieldData[i][j] == 'X') {
                    numOfX++;
                } else if (fieldData[i][j] == 'O') {
                    numOfO++;
                }

                uniqHorizontalElems.add(fieldData[i][j]);
                uniqVerticalElems.add(fieldData[j][i]);
                if (i == j) {
                    uniqDiagonalElems.add(fieldData[i][j]);
                }
                if (j == cols - i - 1) {
                    uniqDiagonalElems2.add(fieldData[i][j]);
                }
            }
            checkElemsForWinCondition(uniqHorizontalElems);
            checkElemsForWinCondition(uniqVerticalElems);
        }

        checkElemsForWinCondition(uniqDiagonalElems);
        checkElemsForWinCondition(uniqDiagonalElems2);

        if (isXWin && isOWin || Math.abs(numOfO - numOfX) > 1) {
            stance = "Impossible";
            return true;
        } else if (isXWin) {
            stance = "X wins";
            return true;
        } else if (isOWin) {
            stance = "O wins";
            return true;
        } else if (cols * rows == numOfO + numOfX) {
            stance = "Draw";
            return true;
        } else {
            stance = "Game not finished";
            return false;
        }
    }

    private void checkElemsForWinCondition(HashSet<Character> elems) {
        if (elems.size() == 1 && (!isXWin || !isOWin)) {
            if (elems.contains('X')) {
                isXWin = true;
            } else if (elems.contains('O'))    {
                isOWin = true;
            }
        }
    }
}

public class Main {
    private static Scanner sc;
    private static GameField gameField;

    public static void main(String[] args) {
        boolean isGameOver = false;
        sc = new Scanner(System.in);
        char uiChar = 'X';

        gameField = new GameField(3, 3);
        gameField.printField();
        do {
            gameField.setCellInFieldByUserInput(getUserMove(), uiChar);
            gameField.printField();
            isGameOver = gameField.analyseStance();
            uiChar = uiChar == 'X' ? 'O' : 'X';
        } while (!isGameOver);
		gameField.printStanceOfField();
    }

    public static int[] getUserMove() {
        boolean checkError;
        String oneArg = null;
        int[] usrInput = new int[2];

        do {
            checkError = false;
            System.out.print("Enter the coordinates: ");
            for (int i = 0; i < 2; i++) {
                oneArg = sc.next();
                try {
                    usrInput[i] = Integer.parseInt(oneArg);
                } catch (Exception ex) {
                    System.out.println("You should enter numbers!");
                    checkError = true;
                    continue;
                }
                if (usrInput[i] < 1 || usrInput[i] > 3) {
                    System.out.println("Coordinates should be from 1 to 3!");
                    checkError = true;
                    continue;
                }
            }

            if (!checkError && !gameField.checkEmptyCellByUserInput(usrInput)) {
                System.out.println("This cell is occupied! Choose another one!");
                checkError = true;
            }
        } while (checkError);

        return usrInput;
    }
}