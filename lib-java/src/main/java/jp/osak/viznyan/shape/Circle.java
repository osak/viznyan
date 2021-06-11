package jp.osak.viznyan.shape;

public class Circle extends Shape {
    private int x, y;
    private int radius;

    public Circle(int id, int x, int y, int radius) {
        super(id);
        this.x = x;
        this.y = y;
        this.radius = radius;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
        markDirty();
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
        markDirty();
    }

    public int getRadius() {
        return radius;
    }

    public void setRadius(int radius) {
        this.radius = radius;
        markDirty();
    }
}
