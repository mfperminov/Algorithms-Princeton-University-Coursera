/* *****************************************************************************
 *  Name: Mikhail Perminov
 *  Date: 2018-09-09
 *  Description: implementation of Percolation model using union-find algorithm
 **************************************************************************** */

import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    // size of the grid
    private final int n;
    // array containg state of each site
    // "true" for open state
    private boolean[] nOpen;
    // data structure that contains info about which sites
    // are connected to which other sites
    private final WeightedQuickUnionUF wquf;
    // number of open sites in grid
    private int openSites = 0;

    /**
     * Initializes an empty data structure with {@code n} * {@code n} sites
     * representing grid and two additional siites containg virtual site
     * at top and bottom.
     * Each site except virtual one is initially in blocked state.
     *
     * @param  n the number of sites in one row or column
     * @throws IllegalArgumentException if {@code n <= 0}
     */
    public Percolation(int n) {
        if (n <= 0) throw
                new java.lang.IllegalArgumentException("n must be more than zero");
        this.n = n;
        this.nOpen = new boolean[n * n + 2];
        this.wquf = new WeightedQuickUnionUF(n * n + 2);

        for (int i = 0; i < (n * n + 2); i++) {
            nOpen[i] = false;
            if (i > (n * n)-1) nOpen[i] = true;
        }

    }

    /**
     * Map from a 2-dimensional (row, column) pair of site coordinates
     * to a 1-dimensional union find object index
     * @param row row number of site
     * @param col column number of site
     * @return index of array
     */
    private int xyTo1D(int row, int col) {
        // validate(row, col);
        if (row == 1) return col;
        return this.n * (row - 1) + col - 1;
    }

    /**
     * Validates 2D-dimensional pair of site coordinates.
     * @param row row number of site
     * @param col column number of site
     * @return true if site with given (row, col) coordinates exist in grid
     */
    private boolean validate(int row, int col) {
        int indice = this.n;
        if (row < 1 || row > indice || col < 1 || col > indice) {
            return false;
        }
        return true;
    }

    /**
     * Makes site open. First if site is placed in top or bottom row connects
     * it to first or second virtual site repectively. Then check up to 4
     * adjacent sites and connect given site to valid ones.
     * @param row row number of site
     * @param col column number of site
     */
    public void open(int row, int col) {
        if (validate(row, col) && row == 1) wquf.union(xyTo1D(row, col), n * n);
        if (validate(row, col) && row == n) wquf.union(xyTo1D(row, col), n * n + 1);
        int indice = xyTo1D(row, col);
        nOpen[indice] = true;
        openSites++;
        int first = xyTo1D(row - 1, col);
        int second = xyTo1D(row + 1, col);
        int third = xyTo1D(row, col - 1);
        int fourth = xyTo1D(row, col + 1);
        if (validate(row - 1, col) && isOpen(row - 1, col))
            wquf.union(indice, first);
        if (validate(row + 1, col) && isOpen(row + 1, col))
            wquf.union(indice, second);
        if (validate(row, col - 1) && isOpen(row, col - 1))
            wquf.union(indice, third);
        if (validate(row, col + 1) && isOpen(row, col + 1))
            wquf.union(indice, fourth);
    }

    /**
     * Validates that site is open.
     * @param row row number of site
     * @param col column number of site
     * @return true if site with given (row, col) is open
     */
    public boolean isOpen(int row, int col) {
        return nOpen[xyTo1D(row, col)];
    }

    /**
     * Validates that given site is connected to the top row of the grid
     * (in full state).
     * @param row row number of site
     * @param col column number of site
     * @return true if site with given (row, col) coordinates
     * connected to the top row
     */
    public boolean isFull(int row, int col) {
        return wquf.connected(xyTo1D(row, col), n * n);
    }

    /**
     *  Return number of open sites in current grid.
     * @return number of open sites
     */
    public int numberOfOpenSites() {
        return openSites;
    }

    /**
     * Return true if system does percolate
     * @return true if virtual sites are connected
     */
    public boolean percolates() {
        return wquf.connected(n * n, n * n + 1);
    }

    public static void main(String[] args) {
        Percolation p = new Percolation(3);
        p.open(1, 1);
        p.open(1, 2);
        StdOut.print(p.wquf.connected(p.xyTo1D(1, 1),
                                      p.xyTo1D(1, 2)));
    }

}

