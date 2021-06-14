package jp.osak.viznyan.example;

import jp.osak.viznyan.command.AddCircle;
import jp.osak.viznyan.command.AddRect;
import jp.osak.viznyan.command.MoveCircle;
import jp.osak.viznyan.output.FileOutput;
import jp.osak.viznyan.output.Output;
import jp.osak.viznyan.output.SocketOutput;
import jp.osak.viznyan.shape.Circle;
import jp.osak.viznyan.shape.Rectangle;
import jp.osak.viznyan.state.FrameBuffer;
import jp.osak.viznyan.state.State;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class Bounce {
    private final ArrayList<Segment> walls = new ArrayList<>();
    private final FrameBuffer frameBuffer;
    private Vec p, v;

    Bounce(Output output) throws IOException {
        frameBuffer = new FrameBuffer(output);

        addBlock(0, 0, 400, 1);
        addBlock(399, 0, 400, 400);
        addBlock(0, 399, 400, 400);
        addBlock(0, 0, 1, 400);
        addBlock(50, 80, 300, 100);

        p = new Vec(200, 350);
        v = new Vec(5, -5);
        frameBuffer.add(new AddCircle(1, (int) p.x, (int) p.y, 5));
        frameBuffer.endFrame();
    }

    public void run(int steps) throws IOException, InterruptedException {
        System.out.println("start");

        for (int i = 0; i < steps; ++i) {
            step(1.0);
            frameBuffer.endFrame();
        }
    }

    private void addBlock(int x1, int y1, int x2, int y2) {
        frameBuffer.add(new AddRect(1000 + walls.size(), x1, y1, x2, y2));
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
        frameBuffer.add(new MoveCircle(1, (int)p.x, (int)p.y));

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

    public static void main(String[] args) throws Exception {
        final Output output = new FileOutput(new File("example/a.txt"));
        //final Output output = new SocketOutput(4444);
        new Bounce(output).run(1000);
        output.close();
    }
}

class Segment {
    Vec p1, p2;

    Segment(Vec p1, Vec p2) {
        this.p1 = p1;
        this.p2 = p2;
    }
}