import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class BruteCollinearPoints {
    private int segmentsNumber = 0;
    private LineSegment[] lineSegments;

    public BruteCollinearPoints(Point[] points) {
        if (points == null) throw new IllegalArgumentException("Argument must be non-null");
        List<Point> pointsList = new ArrayList<>(points.length);
        lineSegments = new LineSegment[points.length];
        for (Point p : points) {
            if (p == null) throw new IllegalArgumentException("All points must be non-null");
            if (pointsList.contains(p)) {
                throw new IllegalArgumentException("Cannot have repeated points");
            } else pointsList.add(p);
        }
        pointsList = null;
        for (int i = 0; i < points.length; i++)
            for (int j = i+1; j < points.length; j++)
                for (int k = j+1; k < points.length; k++)
                    for (int m = k+1; m < points.length; m++) {
                        Point[] pts = new Point[4];
                        pts[0] = points[i];
                        pts[1] = points[j];
                        pts[2] = points[k];
                        pts[3] = points[m];
                        parseLineSegment(pts);
                    }
        lineSegments = Arrays.copyOfRange(lineSegments,0, segmentsNumber);


    }   // finds all line segmentsNumber containing 4 points

    private void parseLineSegment(Point[] points) {
        Arrays.sort(points);
        if (points[0].slopeTo(points[1]) == points[1].slopeTo(points[2])
                && points[0].slopeTo(points[1]) == points[0].slopeTo(points[2])
                && points[0].slopeTo(points[1]) == points[0].slopeTo(points[3]))
         lineSegments[segmentsNumber++] = new LineSegment(points[0], points[3]);
    }

    public int numberOfSegments() {
        return segmentsNumber;
    }       // the number of line segmentsNumber

    public LineSegment[] segments() {
        return lineSegments;
    }                // the line segmentsNumber
}