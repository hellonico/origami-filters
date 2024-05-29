package origami.filters.brandnew.child;

import org.opencv.core.*;
import org.opencv.imgproc.Imgproc;
import origami.Filter;
import origami.filters.FilterWithPalette;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.opencv.core.Core.*;
import static org.opencv.core.Core.addWeighted;
import static org.opencv.imgproc.Imgproc.*;

public class ChildLikeImage extends FilterWithPalette  implements Filter {

    public int zoomIn = 3;
    public int zoomOut = 10;

    Random random = new Random();

    int numberOfTouches = 20; // Number of watercolor touches

    int radiusBound = 15;
    public boolean combined = true;

    public int getNumberOfTouches() {
        return numberOfTouches;
    }

    public void setNumberOfTouches(int numberOfTouches) {
        this.numberOfTouches = numberOfTouches;
    }

    public int getRadiusBound() {
        return radiusBound;
    }

    public void setRadiusBound(int radiusBound) {
        this.radiusBound = radiusBound;
    }

    public boolean isCombined() {
        return combined;
    }

    public void setCombined(boolean combined) {
        this.combined = combined;
    }

    private void addWatercolorTouches(Mat image) {

        for (int i = 0; i < numberOfTouches; i++) {
            int x = random.nextInt(image.cols());
            int y = random.nextInt(image.rows());
            int radius = random.nextInt(radiusBound) + 10; // Radius between 5 and 20
            Scalar color = palette.ratioColor((double) i /numberOfTouches);
            circle(image, new Point(x, y), radius, color, -1);
        }
    }

    private void addComplexWatercolorTouches3(Mat image) {
//        Random random = new Random();
//        int numberOfTouches = 20; // Number of watercolor touches

        // Create a mask to track filled regions
        Mat mask = Mat.zeros(image.size(), CvType.CV_8UC1);

        for (int i = 0; i < numberOfTouches; i++) {
            MatOfPoint points;
//            Scalar color;
            boolean overlap;
            int attempts = 0;

            do {
                overlap = false;
                attempts++;

                // Generate random points for the polygon
                int numPoints = random.nextInt(numberOfTouches) + 3; // Polygons with 3 to 7 points
                points = new MatOfPoint();
                List<Point> pointList = new ArrayList<>();

                for (int j = 0; j < numPoints; j++) {
                    int x = random.nextInt(image.cols());
                    int y = random.nextInt(image.rows());
                    pointList.add(new Point(x, y));
                }
                points.fromList(pointList);

                // Check if the new shape overlaps with the mask significantly
                Mat shapeMask = Mat.zeros(image.size(), CvType.CV_8UC1);
                fillPoly(shapeMask, List.of(points), new Scalar(255));
                Mat overlapMask = new Mat();
                bitwise_and(mask, shapeMask, overlapMask);
                if (countNonZero(overlapMask) > 0.1 * countNonZero(shapeMask)) {
                    overlap = true; // Significant overlap detected
                }

                if (attempts > 50) break; // Avoid infinite loop if too many attempts

            } while (overlap);

            // Generate a random color
//            color = new Scalar(random.nextInt(256), random.nextInt(256), random.nextInt(256));
            Scalar color = palette.ratioColor((double) i /numberOfTouches);


            // Fill the polygon with the color
            fillPoly(image, List.of(points), color);

            // Update the mask with the new shape
            fillPoly(mask, List.of(points), new Scalar(255));
        }
    }

    private void addComplexWatercolorTouches(Mat image) {
//        Random random = new Random();
//        int numberOfTouches = 20; // Number of watercolor touches

        // Create a mask to track filled regions
        Mat mask = Mat.zeros(image.size(), CvType.CV_8UC1);

        for (int i = 0; i < numberOfTouches; i++) {
            MatOfPoint points;
//            Scalar color;
            boolean overlap;
            int attempts = 0;

            do {
                overlap = false;
                attempts++;

                // Generate random points for the polygon
                int numPoints = random.nextInt(radiusBound) + 3; // Polygons with 3 to 7 points
                points = new MatOfPoint();
                List<Point> pointList = new ArrayList<>();

                for (int j = 0; j < numPoints; j++) {
                    int x = random.nextInt(image.cols());
                    int y = random.nextInt(image.rows());
                    pointList.add(new Point(x, y));
                }
                points.fromList(pointList);

                // Check if the new shape overlaps with the mask significantly
                Mat shapeMask = Mat.zeros(image.size(), CvType.CV_8UC1);
                fillPoly(shapeMask, List.of(points), new Scalar(255));
                Mat overlapMask = new Mat();
                bitwise_and(mask, shapeMask, overlapMask);
                if (countNonZero(overlapMask) > 0) {
                    overlap = true; // Overlap detected
                }

                if (attempts > 100) break; // Avoid infinite loop if too many attempts

            } while (overlap);

            if (attempts <= 100) {
                // Generate a random color
//                color = new Scalar(random.nextInt(256), random.nextInt(256), random.nextInt(256));
                Scalar color = palette.ratioColor((double) i /numberOfTouches);

                // Fill the polygon with the color
                fillPoly(image, List.of(points), color);

                // Update the mask with the new shape
                fillPoly(mask, List.of(points), new Scalar(255));
            }
        }
    }

    private Mat addComplexWatercolorTouches2(Mat image) {

//        int numberOfTouches = 20; // Number of watercolor touches

        for (int i = 0; i < numberOfTouches; i++) {
            // Generate random points for the polygon
            int numPoints = random.nextInt(radiusBound) + 3; // Polygons with 3 to 7 points
            MatOfPoint points = new MatOfPoint();
            List<Point> pointList = new ArrayList<>();

            for (int j = 0; j < numPoints; j++) {
                int x = random.nextInt(image.cols());
                int y = random.nextInt(image.rows());
                pointList.add(new Point(x, y));
            }
            points.fromList(pointList);

            // Generate a random color
//            Scalar color = new Scalar(random.nextInt(256), random.nextInt(256), random.nextInt(256));
            Scalar color = palette.ratioColor((double) i /numberOfTouches);

            // Fill the polygon with the color
            fillPoly(image, List.of(points), color);
        }
        return image;
    }


    private void addKandinskyShapesWithGradient(Mat image) {
        Random random = new Random();
//        int numberOfShapes = 20; // Number of shapes

        // Create a mask to track filled regions
        Mat mask = Mat.zeros(image.size(), CvType.CV_8UC1);

        for (int i = 0; i < numberOfTouches; i++) {
            MatOfPoint points;
            Scalar color;
            boolean overlap;
            int attempts = 0;

            do {
                overlap = false;
                attempts++;

                // Generate Kandinsky-like shape
                int shapeType = random.nextInt(3);
                points = new MatOfPoint();
                List<Point> pointList = new ArrayList<>();

                switch (shapeType) {
                    case 0: // Polygon
                        int numPoints = random.nextInt(radiusBound) + 3; // Polygons with 3 to 7 points
                        for (int j = 0; j < numPoints; j++) {
                            int x = random.nextInt(image.cols());
                            int y = random.nextInt(image.rows());
                            pointList.add(new Point(x, y));
                        }
                        break;
                    case 1: // Circle (approximated as a polygon)
                        Point center = new Point(random.nextInt(image.cols()), random.nextInt(image.rows()));
                        int radius = random.nextInt(radiusBound) + 10;
                        for (int j = 0; j < 12; j++) { // 12-sided polygon to approximate a circle
                            double angle = j * 2 * Math.PI / 12;
                            int x = (int) (center.x + radius * Math.cos(angle));
                            int y = (int) (center.y + radius * Math.sin(angle));
                            pointList.add(new Point(x, y));
                        }
                        break;
                    case 2: // Line (approximated as a thin polygon)
                        Point start = new Point(random.nextInt(image.cols()), random.nextInt(image.rows()));
                        Point end = new Point(random.nextInt(image.cols()), random.nextInt(image.rows()));
                        int thickness = random.nextInt(5) + 1;
                        Point dir = new Point(end.x - start.x, end.y - start.y);
                        double length = Math.sqrt(dir.x * dir.x + dir.y * dir.y);
                        Point unitDir = new Point(dir.x / length, dir.y / length);
                        Point perpendicular = new Point(-unitDir.y * thickness, unitDir.x * thickness);
                        pointList.add(new Point(start.x + perpendicular.x, start.y + perpendicular.y));
                        pointList.add(new Point(end.x + perpendicular.x, end.y + perpendicular.y));
                        pointList.add(new Point(end.x - perpendicular.x, end.y - perpendicular.y));
                        pointList.add(new Point(start.x - perpendicular.x, start.y - perpendicular.y));
                        break;
                }
                points.fromList(pointList);

                // Check if the new shape overlaps with the mask significantly
                Mat shapeMask = Mat.zeros(image.size(), CvType.CV_8UC1);
                fillPoly(shapeMask, List.of(points), new Scalar(255));
                Mat overlapMask = new Mat();
                bitwise_and(mask, shapeMask, overlapMask);
                if (countNonZero(overlapMask) > 0) {
                    overlap = true; // Overlap detected
                }

                if (attempts > 100) break; // Avoid infinite loop if too many attempts

            } while (overlap);

            if (attempts <= 100) {
                // Generate a random color
                // color = new Scalar(random.nextInt(256), random.nextInt(256), random.nextInt(256));
                color = palette.ratioColor((double) i /numberOfTouches);


                // Fill the polygon with a gradient from white to the color
                fillPolyWithGradient(image, points, color);

                // Update the mask with the new shape
                fillPoly(mask, List.of(points), new Scalar(255));
            }
        }
    }

    private void fillPolyWithGradient(Mat image, MatOfPoint points, Scalar color) {
        Rect boundingBox = boundingRect(points);
        Mat gradient = new Mat(boundingBox.size(), CvType.CV_8UC3);

        for (int y = 0; y < boundingBox.height; y++) {
            double alpha = (double) y / boundingBox.height;
            Scalar blendedColor = new Scalar(
                    (1 - alpha) * 255 + alpha * color.val[0],
                    (1 - alpha) * 255 + alpha * color.val[1],
                    (1 - alpha) * 255 + alpha * color.val[2]
            );
            line(gradient, new Point(0, y), new Point(boundingBox.width, y), blendedColor);
        }

        Mat mask = Mat.zeros(image.size(), CvType.CV_8UC1);
        fillPoly(mask, List.of(points), new Scalar(255));
        try {
            Mat image_submat = image.submat(boundingBox);
            Mat mask_submat = mask.submat(boundingBox);
            gradient.copyTo(image_submat, mask_submat);
        } catch(Exception e) {
            // fails
        }

    }

    public int getZoomIn() {
        return zoomIn;
    }

    public void setZoomIn(int zoomIn) {
        this.zoomIn = zoomIn;
    }

    public int getZoomOut() {
        return zoomOut;
    }

    public void setZoomOut(int zoomOut) {
        this.zoomOut = zoomOut;
    }

    @Override
    public Mat apply(Mat src) {

        // Convert to gray scale
        Mat gray = new Mat();
        cvtColor(src, gray, COLOR_BGR2GRAY);

        // Apply Canny edge detection
        Mat edges = new Mat();
        Canny(gray, edges, 100, 200);

        Mat dilatedEdges = new Mat();
        dilate(edges, dilatedEdges, getStructuringElement(MORPH_RECT, new Size(zoomOut, zoomOut)));
        erode(dilatedEdges, edges, getStructuringElement(MORPH_RECT, new Size(zoomIn, zoomIn)));

        // Convert edges to color image
        Mat edgesColor = new Mat();
        cvtColor(edges, edgesColor, COLOR_GRAY2BGR);

        // Apply bilateral filter to smooth the image
        Mat smoothed = new Mat();
        bilateralFilter(edgesColor, smoothed, 9, 75, 75);

        // Combine edges and smoothed image
        if(combined) {
            addWeighted(smoothed, 0.7, edgesColor, 0.3, 0, smoothed);
        }

        // Add random watercolor touches
//        addWatercolorTouches(smoothed);
//        addComplexWatercolorTouches(smoothed);
        addKandinskyShapesWithGradient(smoothed);

        return smoothed;
    }
}
