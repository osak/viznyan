package jp.osak.viznyan.command;

public class MoveCircle implements Command {
    private final int id;
    private final int x, y;

    public MoveCircle(int id, int x, int y) {
        this.id = id;
        this.x = x;
        this.y = y;
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
}
