package jp.osak.viznyan.output;

import jp.osak.viznyan.shape.Circle;
import jp.osak.viznyan.shape.Line;
import jp.osak.viznyan.shape.Rectangle;
import jp.osak.viznyan.shape.Text;
import jp.osak.viznyan.state.State;

import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.List;

public class CompProgSerializer {
    public void serialize(OutputStream outputStream, State state) {
        final PrintWriter writer = new PrintWriter(new OutputStreamWriter(outputStream));
        serializeCircles(writer, state);
        serializeRectangles(writer, state);
        serializeLines(writer, state);
        serializeTexts(writer, state);
        writer.flush();
    }

    private void serializeCircles(PrintWriter writer, State state) {
        final List<Circle> circles = state.getList(Circle.class);
        writer.println(circles.size());
        for (Circle c : circles) {
            writer.printf("%d %d %d %d\n", c.getId(), c.getX(), c.getY(), c.getRadius());
        }
    }

    private void serializeRectangles(PrintWriter writer, State state) {
        final List<Rectangle> rects = state.getList(Rectangle.class);
        writer.println(rects.size());
        for (Rectangle r : rects) {
            writer.printf("%d %d %d %d %d\n", r.getId(), r.getX1(), r.getY1(), r.getX2(), r.getY2());
        }
    }

    private void serializeLines(PrintWriter writer, State state) {
        final List<Line> lines = state.getList(Line.class);
        writer.println(lines.size());
        for (Line l : lines) {
            writer.printf("%d %d %d %d %d\n", l.getId(), l.getX1(), l.getY1(), l.getX2(), l.getY2());
        }
    }

    private void serializeTexts(PrintWriter writer, State state) {
        final List<Text> texts = state.getList(Text.class);
        writer.println(texts.size());
        for (Text t : texts) {
            writer.printf("%d %d %d %s\n", t.getId(), t.getX(), t.getY(), t.getText());
        }
    }
}
