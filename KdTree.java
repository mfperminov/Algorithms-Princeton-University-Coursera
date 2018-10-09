/* *****************************************************************************
 *  Name: Mikhail Perminov
 *  Date: 2018-10-07
 *  Description: A mutable data type that uses a 2d-tree to implement
 *  a set of points in the unit square.
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdDraw;


public class KdTree {
    private Node root;
    private int size;

    private static class Node {
        private Point2D p;      // the point
        private RectHV rect;    // the axis-aligned rectangle corresponding to this node
        private Node lb;        // the left/bottom subtree
        private Node rt; // the right/top subtree
        private int level;


        public Node(Point2D p, int level, RectHV rect) {
            this.p = p;
            this.rect = rect;
            this.level = level;
        }

    }

    public KdTree() {
        root = null;
        size = 0;
    }

    // is the set empty?
    public boolean isEmpty() {
        return size == 0;
    }

    // number of points in the set
    public int size() {
        return size;
    }

    // add the point to the set (if it is not already in the set)
    public void insert(Point2D p) {
        if (p == null) throw new
                java.lang.IllegalArgumentException("Arg cannot be null");
        if (!contains(p)) {
            root = put(root, p, 1, 0.0, 0.0, 1.0, 1.0);
            size++;
        }
    }

    private Node put(Node x, Point2D p, int level, double x0, double y0,
                     double x1, double y1) {

        if (x == null) {
            return new Node(p, level, new RectHV(x0, y0, x1, y1));
        }
        level = x.level;
        if ((level & 1) != 0 && p.x() >= x.p.x())
            x.rt = put(x.rt, p, level + 1, x.p.x(), y0, x1, y1);
        else if ((level & 1) != 0 && p.x() < x.p.x())
            x.lb = put(x.lb, p, level + 1, x0, y0, x.p.x(), y1);
        else if ((level & 1) == 0 && p.y() >= x.p.y())
            x.rt = put(x.rt, p, level + 1, x0, x.p.y(), x1, y1);
        else if ((level & 1) == 0 && p.y() < x.p.y())
            x.lb = put(x.lb, p, level + 1, x0, y0, x1, x.p.y());

        return x;
    }

    // does the set contain point p?
    public boolean contains(Point2D p) {
        if (p == null) throw new
                java.lang.IllegalArgumentException("Arg cannot be null");
        Point2D target = get(root, p, 1);
        return target != null;
    }

    private Point2D get(Node node, Point2D p, int level) {
        if (node == null) return null;
        if (node.p.equals(p)) return node.p;

        if ((level & 1) != 0) {
            if (p.x() >= node.p.x())
                return get(node.rt, p, node.level + 1);
            else
                return get(node.lb, p, node.level + 1);
        }
        else {
            if (p.y() >= node.p.y())
                return get(node.rt, p, node.level + 1);
            else
                return get(node.lb, p, node.level + 1);
        }

    }


    // draw all points to standard draw
    public void draw() {
        drawNode(root);
    }

    private void drawNode(Node node) {
        if (node == null) return;
        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.setPenRadius(0.01);
        node.p.draw();
        StdDraw.setPenRadius();
        if ((node.level & 1) == 0) {
            StdDraw.setPenColor(StdDraw.BLUE);
            StdDraw.line(node.rect.xmin(), node.p.y(), node.rect.xmax(), node.p.y());
        }
        else {
            StdDraw.setPenColor(StdDraw.RED);
            StdDraw.line(node.p.x(), node.rect.ymin(), node.p.x(), node.rect.ymax());
        }
        drawNode(node.lb);
        drawNode(node.rt);


    }

    // all points that are inside the rectangle (or on the boundary)
    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) throw new
                java.lang.IllegalArgumentException("Arg cannot be null");
        Stack<Point2D> points = new Stack<>();
        searchRange(root, points, rect);
        return points;
    }

    private void searchRange(Node node, Stack<Point2D> points, RectHV rect) {
        if (node == null) return;
        if (rect.contains(node.p)) points.push(node.p);
        if (node.lb != null && rect.intersects(node.lb.rect))
            searchRange(node.lb, points, rect);
        if (node.rt != null && rect.intersects(node.rt.rect))
            searchRange(node.rt, points, rect);
    }

    // a nearest neighbor in the set to point p; null if the set is empty
    public Point2D nearest(Point2D p) {
        if (p == null) throw new
                java.lang.IllegalArgumentException("Arg cannot be null");
        Point2D nearest;
        if (root == null) return null;
        else nearest = searchNearest(root, p, root.p);
        return nearest;
    }

    private Point2D searchNearest(Node node, Point2D p, Point2D closestPoint) {
        Point2D c = closestPoint;
        if (node == null) return c;
        if (node.p.distanceSquaredTo(p) < c.distanceSquaredTo(p)) c = node.p;
        if (node.rect.distanceSquaredTo(p) < c.distanceSquaredTo(p)) {
            Node near, far;
            if (((node.level & 1) != 0 && (p.x() < node.p.x()))
                    || ((node.level & 1) == 0 && (p.y() < node.p.y()))) {
                near = node.lb;
                far = node.rt;
            }
            else {
                near = node.rt;
                far = node.lb;
            }
            c = searchNearest(near, p, c);
            c = searchNearest(far, p, c);
        }
        return c;
    }

    public static void main(String[] args) {
        String filename = args[0];
        In in = new In(filename);
        KdTree kd = new KdTree();
        while (!in.isEmpty()) {
            double x = in.readDouble();
            double y = in.readDouble();
            Point2D p = new Point2D(x, y);
            kd.insert(p);

        }


    }
}
