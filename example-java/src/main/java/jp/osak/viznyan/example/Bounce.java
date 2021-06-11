package jp.osak.viznyan.example;

import jp.osak.viznyan.output.FileOutput;
import jp.osak.viznyan.shape.Circle;
import jp.osak.viznyan.shape.Rectangle;
import jp.osak.viznyan.state.State;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class Bounce {
    private final State state = new State();
    private final ArrayList<Segment> walls = new ArrayList<>();
    private final Circle circle;
    private Vec p, v;

    Bounce() {
        addBlock(0, 0, 400, 1);
        addBlock(399, 0, 400, 400);
        addBlock(0, 399, 400, 400);
        addBlock(0, 0, 1, 400);
        addBlock(50, 80, 300, 100);

        p = new Vec(200, 350);
        v = new Vec(5, -5);
        circle = new Circle(0, (int) p.x, (int) p.y, 5);
        state.add(circle);
    }

    public void run(int steps, String filename) throws IOException {
        final FileOutput fileOutput = new FileOutput(new File(filename));

        for (int i = 0; i < steps; ++i) {
            step(1.0);
            fileOutput.write(state);
        }

        fileOutput.close();
    }

    private void addBlock(int x1, int y1, int x2, int y2) {
        state.add(new Rectangle(0, x1, y1, x2, y2));
        walls.add(new Segment(new Vec(x1, y1), new Vec(x1, y2)));
        walls.add(new Segment(new Vec(x1, y2), new Vec(x2, y2)));
        walls.add(new Segment(new Vec(x2, y2), new Vec(x2, y1)));
        walls.add(new Segment(new Vec(x2, y1), new Vec(x1, y1)));
    }

    private boolean intersect(Vec p1, Vec p2, Vec q1, Vec q2) {
        return q1.sub(p1).cross(p2.sub(p1)) * q2.sub(p1).cross(p2.sub(p1)) <= 0 &&
                p1.sub(q1).cross(q2.sub(q1)) * p2.sub(q1).cross(q2.sub(q1)) <= 0;
    }

    private void step(double len) {
        if (len < 1e-8) {
            return;
        }
        final Vec np = p.add(v.mul(len));
        double minT = 1.0;
        Vec minWall = null;
        for (Segment wall : walls) {
            if (intersect(p, np, wall.p1, wall.p2)) {
                final Vec v1 = np.sub(p);
                final Vec v2 = wall.p2.sub(wall.p1);
                final Vec v3 = wall.p1.sub(p);
                final double det = v1.cross(v2.mul(-1));
                final double t = (-v2.y * v3.x + v2.x * v3.y) / det;

                if (t < minT && Math.abs(t) > 1e-8) {
                    minT = t;
                    minWall = wall.p2.sub(wall.p1);
                } else if (minT == 1.0 && Math.abs(wall.p1.sub(p).cross(wall.p2.sub(p))) < 1e-8) {
                    minT = t;
                    minWall = wall.p2.sub(wall.p1);
                }
            }

        }
        p = p.add(v.mul(len * minT));
        circle.setX((int) p.x);
        circle.setY((int) p.y);

        if (minWall != null) {
            Vec perp = new Vec(minWall.y, minWall.x).unit();
            if (perp.dot(v) > 0) {
                perp = perp.mul(-1);
            }
            v = v.add(perp.mul(Math.abs(v.dot(perp)*2)));
            p = p.add(v.mul(1e-8));
            step(len * (1 - minT));
        }
    }

    public static void main(String[] args) throws IOException {
        new Bounce().run(1000, "example/a.txt");
    }
}

class Segment {
    Vec p1, p2;

    Segment(Vec p1, Vec p2) {
        this.p1 = p1;
        this.p2 = p2;
    }
}