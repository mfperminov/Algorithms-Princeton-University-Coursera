import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FastCollinearPoints {
    private int segmentsNumber = 0;
    private LineSegment[] lineSegments;


    public FastCollinearPoints(Point[] points) {
        if (points == null) throw new IllegalArgumentException("Argument must be non-null");
        List<Point> pointsList = new ArrayList<>(points.length);
        lineSegments = new LineSegment[points.length];
        for (Point p : points) {
            if (p == null) throw new IllegalArgumentException("All points must be non-null");
            if (pointsList.contains(p)) {
                throw new IllegalArgumentException("Cannot have repeated points");
            }
            else pointsList.add(p);
        }
        pointsList = null;
        parseLines(points);

    }

    // finds all line segments containing 4 or more points

    private void parseLines(Point[] points) {

        for (int i = 0; i < points.length - 1; i++) {
            Arrays.sort(points, i, points.length, points[i].slopeOrder());
            Arrays.sort(points, i, points.length);

            double slope = points[i].slopeTo(points[i + 1]);
            int lastPoint = 0;

            for (int k = i + 2; k < points.length; k++) {

                if (points[i].slopeTo(points[k])!=slope) {

                    lastPoint = k - 1;
                    break;

                }

                if (k == points.length - 1) lastPoint = k;

            }
            if (lastPoint - i >= 3) {

                lineSegments[segmentsNumber++] = new LineSegment(points[i], points[lastPoint]);

                i = lastPoint;
            }
        }
        lineSegments = Arrays.copyOfRange(lineSegments, 0, segmentsNumber);
    }


    public int numberOfSegments() {
        return segmentsNumber;
    } // the number of line segments

    public LineSegment[] segments() {
        return lineSegments;
    } // the line segments
}


