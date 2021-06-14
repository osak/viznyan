package jp.osak.viznyan.command;

public class AddCircle implements Command {
    private final int id;
    private final int x;
    private final int y;
    private final int radius;

    public AddCircle(int id, int x, int y, int radius) {
        this.id = id;
        this.x = x;
        this.y = y;
        this.radius = radius;
    }

    public int getId() {
        return id;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getRadius() {
        return radius;
    }
}
