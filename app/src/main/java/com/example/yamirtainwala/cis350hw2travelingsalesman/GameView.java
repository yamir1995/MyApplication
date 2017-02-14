package com.example.yamirtainwala.cis350hw2travelingsalesman;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static android.graphics.Color.RED;
import static android.graphics.Color.YELLOW;

/**
 * Created by yamirtainwala on 2/6/17.
 */
public class GameView extends View {
    int spinner_num;
    Paint p;
    private List<Point> locations;
    private List<Point> path;
    private List<Point> shortest_path;
    private int count_attempts;

    public GameView(Context context) {
        super(context);
        init();
    }

    public GameView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public GameView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    protected void setSpinner_num(int n) {
        spinner_num = n;
    }

    private void set_Locations() {
        locations = new ArrayList<Point>(13);
        locations.add(0, new Point(1427, 68));
        locations.add(1, new Point(476, 134));
        locations.add(2, new Point(140, 271));
        locations.add(3, new Point(273, 518));
        locations.add(4, new Point(509, 638));
        locations.add(5, new Point(1324, 401));
        locations.add(6, new Point(1452, 242));
        locations.add(7, new Point(1667, 253));
        locations.add(8, new Point(750, 670));
        locations.add(9, new Point(1019, 380));
        locations.add(10, new Point(870, 251));
        locations.add(11, new Point(540, 477));
        locations.add(12, new Point(828, 424));
    }

    private void init() {
        count_attempts=0;
        path = new ArrayList<Point>();
        shortest_path = new ArrayList<Point>();
        setBackgroundResource(R.drawable.campus);
        set_Locations();
        Collections.shuffle(locations);
    }

    public void onDraw(Canvas c) {
        p = new Paint();
        p.setColor(RED);
        p.setStrokeWidth(15);
        //Draws Points
        for (int i = 0; i < spinner_num; i++) {
            c.drawPoint(locations.get(i).x, locations.get(i).y, p);
        }

        //Draws Mouse Movements
        p.setColor(YELLOW);
        p.setStrokeWidth(10);
        for (int i = 0; i < path.size() - 1; i++) {
            Point p1 = path.get(i);
            Point p2 = path.get(i + 1);
            c.drawLine(p1.x, p1.y, p2.x, p2.y, p);
        }

        drawTracedPath(c);
        //drawSolution(c);
    }

    private void drawTracedPath(Canvas c) {
        for (int i = 0; i < shortest_path.size() - 1; i = i + 2) {
            Point p1 = shortest_path.get(i);
            Point p2 = shortest_path.get(i + 1);
            p.setColor(RED);
            p.setStrokeWidth(10);
            c.drawLine(p1.x, p1.y, p2.x, p2.y, p);
        }
    }

    // Checks if an entered point(mouse click or release) is one of some locations on the map
    // highlighted in red
    private Point isLocation(Point p) {
        for (int i = 0; i < spinner_num; i++) {
            if (withinRadius(p, locations.get(i))) {
                return locations.get(i);
            }
        }
        return null;
    }

    public boolean onTouchEvent(MotionEvent e) {
        Point down, up;
        if (e.getAction() == MotionEvent.ACTION_DOWN) {//if mouse button was pushed
            down = new Point((int) e.getX(), (int) e.getY());
            if (isLocation(down) != null) {
                shortest_path.add(isLocation(down));
            }
        }
        if (e.getAction() == MotionEvent.ACTION_MOVE) {//mouse movement action
            path.add(new Point((int) e.getX(), (int) e.getY()));
        }
        if (e.getAction() == MotionEvent.ACTION_UP) {//if mouse button was released
            up = new Point((int) e.getX(), (int) e.getY());
            //     Check to see that the DownEvent preceded the UpEvent in the same motion
            if (shortest_path.size() != 0 && shortest_path.size() % 2 == 1) {
                //     Check if new Location (and not the same location as DownEvent)
                if (isLocation(up) != null && isLocation(up) != shortest_path.get(shortest_path.size() - 1)) {
                    shortest_path.add(isLocation(up));
                } else {
                    shortest_path.remove(shortest_path.size() - 1);
                }
            }
            path.clear();

            // Checks if maximum number of journeys have been made
            if (shortest_path.size() >= spinner_num * 2) {
                //     Check if Circuit created
                if (!isCircuit()) {
                    Toast.makeText(
                            getContext(),
                            "Not a circuit :( Choose clear from Options Menu and try again!",
                            Toast.LENGTH_LONG)
                            .show();
                } else {
                    if (comparePathLengths() == 0.0) {
                        Toast.makeText(
                                getContext(),
                                "Good job! You found the shortest path.",
                                Toast.LENGTH_LONG)
                                .show();
                    } else {
                        //if(count_attempts < 2) {
                            Toast.makeText(
                                    getContext(),
                                    "Not quite. Your path is about " + (int) comparePathLengths() + "% longer than the shortest path.",
                                    Toast.LENGTH_LONG)
                                    .show();
                            count_attempts++;
                       // }
//                        else {
//                            Toast.makeText(
//                                    getContext(),
//                                    "Sorry that's wrong :( Here's the correct solution.",
//                                    Toast.LENGTH_LONG)
//                                    .show();
//
//                            //drawSolution();
//                        }
                    }
                }
            }
        }
        invalidate();
        return true;
    }

    // Checks the length of the path returned by the ShortestPath class against the length of the
    // path stored in the shortest_path arraylist
    private double comparePathLengths() {
        double distance_computed = 0.0;
        for (int i = 0; i < shortest_path.size() - 1; i += 2) {
            distance_computed += ShortestPath.dist(shortest_path.get(i), shortest_path.get(i + 1));
        }
        Point[] locations_array = new Point[spinner_num];
        for (int i = 0; i < spinner_num; i++) {
            locations_array[i] = locations.get(i);
        }
        ArrayList<Point> shortestPath = ShortestPath.shortestPath(locations_array);
        double actual_distance = 0.0;
        for (int i = 0; i < locations_array.length - 1; i++) {
            actual_distance += ShortestPath.dist(shortestPath.get(i), shortestPath.get(i + 1));
        }
        actual_distance += ShortestPath.dist(shortestPath.get(0),
                shortestPath.get(shortestPath.size() - 1));
        if ((int) actual_distance == (int) distance_computed) {
            return 0.0;
        } else {
            return ((distance_computed - actual_distance) / actual_distance * 100);
        }
    }

    private void drawSolution(Canvas c){
        if(count_attempts >= 2) {
            //Log.v("correct solution","entered drawSolution");
            Point[] locations_array = new Point[spinner_num];
            for (int i = 0; i < spinner_num; i++) {
                locations_array[i] = locations.get(i);
            }
            p.setColor(YELLOW);
            p.setStrokeWidth(10);
            ArrayList<Point> shortestPathSolution = ShortestPath.shortestPath(locations_array);
            for (int i = 0; i < shortestPathSolution.size() - 1; i = i++) {
                Point p1 = shortestPathSolution.get(i);
                Point p2 = shortestPathSolution.get(i + 1);
                c.drawLine(p1.x, p1.y, p2.x, p2.y, p);
            }
            Point p1 = shortestPathSolution.get(0);
            Point p2 = shortestPathSolution.get(shortestPathSolution.size()-1);
            c.drawLine(p1.x, p1.y, p2.x, p2.y, p);
            //Log.v("correct solution","-------------");
        }

    }


    private boolean isCircuit() {
        int[] count = new int[spinner_num];
        for (int i = 0; i < spinner_num; i++) {
            for (int j = 0; j < shortest_path.size(); j++) {
                if (locations.get(i).equals(shortest_path.get(j))) {
                    count[i]++;
                }
            }
        }
        for (int i = 0; i < spinner_num; i++) {
            if (count[i] != 2) {
                return false;
            }
        }
        return true;
    }


    private boolean withinRadius(Point p1, Point p2) {
        if (Math.abs(p1.x - p2.x) <= 20 && Math.abs(p1.y - p2.y) <= 20) {
            return true;
        }
        return false;
    }


    public void clear() {
        shortest_path.clear();
        invalidate();
    }

    public boolean undo() {
        if (shortest_path.size() >= 2) {
            shortest_path.remove(shortest_path.size() - 1);
            shortest_path.remove(shortest_path.size() - 1);
            invalidate();
            return true;
        } else {
            return false;
        }
    }

}
