/* *****************************************************************************
 *  Name: Mikhail Perminov
 *  Date: 2018-10-07
 *  Description: An implementation of data type that represents
 *  a set of points in the unit square.
 **************************************************************************** */

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.RectHV;

import java.util.TreeSet;

public class PointSET {
    private TreeSet<Point2D> backedSet;

    // construct an empty set of points
    public PointSET() {
        backedSet = new TreeSet<>();

    }

    // is the set empty?
    public boolean isEmpty() {
        return backedSet.isEmpty();
    }

    // number of points in the set
    public int size() {
        return backedSet.size();
    }

    // add the point to the set (if it is not already in the set)
    public void insert(Point2D p) {

        if (p == null) throw new
                java.lang.IllegalArgumentException("Arg cannot be null");

        if (!contains(p)) backedSet.add(p);

    }

    // does the set contain point p?
    public boolean contains(Point2D p) {

        if (p == null) throw new
                java.lang.IllegalArgumentException("Arg cannot be null");
        return backedSet.contains(p);

    }

    // draw all points to standard draw
    public void draw() {

        for (Point2D point2D : backedSet) {
            point2D.draw();
        }

    }

    // all points that are inside the rectangle (or on the boundary)
    public Iterable<Point2D> range(RectHV rect) {

        if (rect == null) throw new
                java.lang.IllegalArgumentException("Arg cannot be null");

        Queue<Point2D> inRectQueue = new Queue<>();

        for (Point2D point2D : backedSet) {
            if (rect.contains(point2D)) inRectQueue.enqueue(point2D);
        }

        return inRectQueue;

    }

    // a nearest neighbor in the set to point p; null if the set is empty
    public Point2D nearest(Point2D p) {
        if (p == null) throw new
                java.lang.IllegalArgumentException("Arg cannot be null");

        if (isEmpty()) return null;

        double minDistance = Double.POSITIVE_INFINITY;
        Point2D nearestPoint = new Point2D(0, 0);

        for (Point2D point2D : backedSet) {
            // if (point2D.equals(p)) continue;
            if (p.distanceSquaredTo(point2D) < minDistance) {
                minDistance = p.distanceSquaredTo(point2D);
                nearestPoint = point2D;
            }
        }

        return nearestPoint;
    }

    // unit testing of the methods (optional)
    public static void main(String[] args) {

    }
}
