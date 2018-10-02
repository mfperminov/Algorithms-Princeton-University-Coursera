/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;

import java.util.Comparator;

public class Solver {
    private Stack<Board> solutionBoard;
    private int moves = 0;
    private boolean hasSolution = false;

    private class SearchNode implements Comparator<SearchNode> {
        private int moves;
        private Board current;


        private SearchNode predecessor;

        public SearchNode(int moves, SearchNode predecessor, Board current) {
            this.moves = moves;
            this.predecessor = predecessor;
            this.current = current;
        }

        @Override
        public int compare(SearchNode sn1, SearchNode sn2) {
            if (sn1.current.manhattan() + sn1.moves == sn2.current.manhattan() + sn2.moves) {
                return Integer.compare(sn1.current.manhattan(),
                                       sn2.current.manhattan());
            }
            else if (sn1.current.manhattan() + sn1.moves < sn2.current.manhattan() + sn2.moves)
                return -1;
            else return +1;

        }
    }

    // find a solution to the initial board (using the A* algorithm)
    public Solver(Board initial) {
        if (initial == null) throw
                new IllegalArgumentException("Arg cannot be null");
        SearchNode original = new SearchNode(moves, null,
                                             initial
        );
        MinPQ<SearchNode> gameTreeOriginal = new MinPQ<>(original);
        gameTreeOriginal.insert(original);
        SearchNode twin = new SearchNode(moves, null,
                                         initial.twin()
        );
        MinPQ<SearchNode> gameTreeTwin = new MinPQ<>(twin);
        gameTreeTwin.insert(twin);

        while (!original.current.isGoal() && !twin.current.isGoal()) {

            Stack<Board> neighbors = new Stack<>();

            for (Board b : original.current.neighbors()) {
                neighbors.push(b);
            }

            int n = neighbors.size();
            for (int i = 0; i < n; i++) {
                Board b = neighbors.pop();
                if (original.predecessor == null || !b.equals(original.predecessor.current))
                    gameTreeOriginal.insert(new SearchNode(original.moves + 1, original, b));
            }

            Stack<Board> twinNeigbouts = new Stack<>();

            for (Board b : twin.current.neighbors()) {
                twinNeigbouts.push(b);
            }

            int m = twinNeigbouts.size();
            for (int j = 0; j < m; j++) {
                Board b = twinNeigbouts.pop();
                if (twin.predecessor == null || !b.equals(twin.predecessor.current))
                    gameTreeTwin.insert(new SearchNode(twin.moves + 1, twin, b));
            }
            original = gameTreeOriginal.delMin();
            twin = gameTreeTwin.delMin();
        }

        solutionBoard = new Stack<Board>();

        if (original.current.isGoal()) {
            hasSolution = true;
            solutionBoard.push(original.current);
            while (original.predecessor != null) {
                solutionBoard.push(original.predecessor.current);
                original = original.predecessor;
            }
        }
        moves = solutionBoard.size() - 1;


    }

    // is the initial board solvable?
    public boolean isSolvable() {
        return hasSolution;
    }

    // min number of moves to solve initial board; -1 if unsolvable
    public int moves() {
        if (isSolvable()) return moves;
        else return -1;
    }

    // sequence of boards in a shortest solution; null if unsolvable
    public Iterable<Board> solution() {
        // MinPQ<Board> minPQ = new Board();
        if (isSolvable()) {
            return solutionBoard;
        }
        else {
            return null;
        }
    }

    public static void main(String[] args) {
        // create initial board from file
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] blocks = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                blocks[i][j] = in.readInt();
        Board initial = new Board(blocks);


        Solver solver = new Solver(initial);

        // print solution to standard output
        if (!solver.isSolvable())
            StdOut.println("No solution possible");
        else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution())
                StdOut.println(board);
        }
    }
}
