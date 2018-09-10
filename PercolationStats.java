/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {
    private final int trials;
    private final int n;
    private double[] percolationThresholds;
    public PercolationStats(int n, int trials) {
        if(n < 1 || trials < 1) throw
                new IllegalArgumentException("n and number of trials must be"
                                                     + "more than 1");
        this.n = n;
        this.trials = trials;
    }    // perform trials independent experiments on an n-by-n grid
    public double mean()  {
        int openSites;
        percolationThresholds = new double[this.trials];
        for (int i = 0; i < trials; i++) {
            Percolation p = new Percolation(n);
            openSites = 0;
            while (!p.percolates()) {
                int row = StdRandom.uniform(n)+1;
                int col = StdRandom.uniform(n)+1;
                if (!p.isOpen(row, col)) {
                    p.open(row, col);
                    openSites++;
                }
            }
            percolationThresholds[i] = (double) openSites / (n * n);
        }

        return StdStats.mean(percolationThresholds);
    }
    public double stddev()     {
        return StdStats.stddev(percolationThresholds);
    }                     // sample standard deviation of percolation threshold
    public double confidenceLo()     {
        return mean() - 1.96 * (Math.sqrt(stddev()/trials));
    }               // low  endpoint of 95% confidence interval
    public double confidenceHi()     {
        return mean() + 1.96 * (Math.sqrt(stddev()/trials));
    }               // high endpoint of 95% confidence interval

    public static void main(String[] args)  {
        PercolationStats p = new PercolationStats(25, 10000);
        StdOut.println("Mean = " + p.mean());
        StdOut.println("Stddev = " + p.stddev());
        StdOut.println("95% confidence interval = [" + p.confidenceLo() + ", "
        + p.confidenceHi() + "]");

    }        // test client (described below)
}
