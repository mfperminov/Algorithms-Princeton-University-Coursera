/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdRandom;

import java.util.Arrays;

public class Board {
    private final int[][] curBlocks;
    private final int n;
    private int zeroi = 0;
    private int zeroj = 0;
    private Board twinBoard;
    private int manhattanCache;

    // construct a board from an n-by-n array of curBlocks
    // (where curBlocks[i][j] = block in row i, column j)
    public Board(int[][] blocks) {
        if (blocks == null) throw
                new IllegalArgumentException("Arg cannot be null");
        n = blocks.length;
        curBlocks = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++) {
                curBlocks[i][j] = blocks[i][j];
                if (blocks[i][j] == 0) {
                    zeroi = i;
                    zeroj = j;
                }
            }
        manhattanCache = this.manhattanInit();

    }

    private Board(Board parent, int[][] blocks, int manhattanDiff) {
        n = blocks.length;
        curBlocks = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++) {
                curBlocks[i][j] = blocks[i][j];
                if (blocks[i][j] == 0) {
                    zeroi = i;
                    zeroj = j;
                }
            }
        manhattanCache = parent.manhattanCache + manhattanDiff;
    }

    private int xyTo1D(int row, int col) {
        int mRow = row + 1;
        int mCol = col + 1;
        if (mRow == 1) return mCol;
        // if (col == 1) return this.n * (row - 1) + col;
        return this.n * (mRow - 1) + mCol;

    }

    private int[] oneDtoIj(int number) {
        int row = (number - 1) / n;
        int col = number - row * n - 1;
        int[] ij = { row, col };
        return ij;
    }

    // board dimension n
    public int dimension() {
        return n;
    }

    // number of blocks out of place
    public int hamming() {
        int hammingNumber = 0;
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++) {
                if (curBlocks[i][j] == 0) continue;
                if (curBlocks[i][j] != xyTo1D(i, j)) {
                    hammingNumber++;
                }
            }
        return hammingNumber;

    }

    private int manhattanInit() {
        return manhattanCache;
    }

    // sum of Manhattan distances between blocks and goal
    public int manhattan() {
        int manhattanNumber = 0;
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++) {
                if (curBlocks[i][j] == 0) continue;
                if (curBlocks[i][j] != xyTo1D(i, j)) {
                    int[] correctCoordinates = oneDtoIj(curBlocks[i][j]);
                    manhattanNumber += Math.abs(correctCoordinates[0] - i)
                            + Math.abs(correctCoordinates[1] - j);

                }
            }
        return manhattanNumber;
    }

    // is this board the goal board?
    public boolean isGoal() {
        return manhattan() == 0;
    }

    // a board that is obtained by exchanging any pair of blocks
    public Board twin() {
        if (twinBoard != null) return twinBoard;
        int firsti, firstj, secondi, secondj;
        do {
            firsti = StdRandom.uniform(n);
            firstj = StdRandom.uniform(n);
            secondi = StdRandom.uniform(n);
            secondj = StdRandom.uniform(n);
        } while (curBlocks[firsti][firstj] == 0
                || curBlocks[secondi][secondj] == 0
                || (curBlocks[firsti][firstj] == curBlocks[secondi][secondj]));


        int[][] newBlocks = new int[n][n];
        for (int c = 0; c < n; c++) {
            System.arraycopy(curBlocks[c], 0, newBlocks[c], 0, n);
        }
        int temp = newBlocks[firsti][firstj];
        newBlocks[firsti][firstj] = newBlocks[secondi][secondj];
        newBlocks[secondi][secondj] = temp;
        twinBoard = new Board(newBlocks);
        return twinBoard;

    }

    // does this board equal other?
    public boolean equals(Object other) {
        if (other == this) return true;
        if (other == null) return false;
        if (other.getClass() != this.getClass()) return false;
        Board that = (Board) other;
        return (Arrays.deepEquals(this.curBlocks, that.curBlocks));
    }

    // all neighboring boards
    public Iterable<Board> neighbors() {
        Stack<Board> nBoards = new Stack<>();
        if (zeroi - 1 >= 0) {
            int[][] newBlocks1 = swapPair(zeroi - 1, zeroj);
            int diff = manhattanDiff(zeroi - 1, zeroj);
            Board b1 = new Board(this, newBlocks1, diff);
            nBoards.push(b1);
        }
        if (zeroi + 1 < n) {
            int[][] newBlocks2 = swapPair(zeroi + 1, zeroj);
            int diff = manhattanDiff(zeroi + 1, zeroj);
            Board b2 = new Board(this, newBlocks2, diff);
            nBoards.push(b2);
        }
        if (zeroj - 1 >= 0) {
            int[][] newBlocks3 = swapPair(zeroi, zeroj - 1);
            int diff = manhattanDiff(zeroi, zeroj - 1);
            Board b3 = new Board(this, newBlocks3, diff);
            nBoards.push(b3);
        }
        if (zeroj + 1 < n) {
            int[][] newBlocks4 = swapPair(zeroi, zeroj + 1);
            int diff = manhattanDiff(zeroi, zeroj + 1);
            Board b4 = new Board(this, newBlocks4, diff);
            nBoards.push(b4);
        }

        return nBoards;
    }

    private int manhattanDiff(int i, int j) {
        int[] correctCoordinates = oneDtoIj(curBlocks[i][j]);
        int manhattanOriginal = Math.abs(zeroi - correctCoordinates[0])
                + Math.abs(zeroj - correctCoordinates[0]);
        int manhattanNewPlace = Math.abs(i - correctCoordinates[0])
                + Math.abs(j - correctCoordinates[0]);
        if (manhattanNewPlace > manhattanOriginal) return +1;
        else return -1;

    }

    private int[][] swapPair(int i, int j) {
        int[][] newBlocks = new int[n][n];
        for (int c = 0; c < n; c++) {
            System.arraycopy(curBlocks[c], 0, newBlocks[c], 0, n);
        }
        int temp = newBlocks[i][j];
        newBlocks[i][j] = 0;
        newBlocks[zeroi][zeroj] = temp;
        return newBlocks;
    }

    // string representation of this board (in the output format
    // specified below)
    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append(n + "\n");
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                s.append(String.format("%2d ", curBlocks[i][j]));
            }
            s.append("\n");
        }
        return s.toString();
    }

    public static void main(String[] args) {

    }
}
