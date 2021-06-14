package jp.osak.viznyan.command;

public class AddText implements Command {
    private final int id;
    private final int x, y;
    private final String text;

    public AddText(int id, int x, int y, String text) {
        this.id = id;
        this.x = x;
        this.y = y;
        this.text = text;
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

    public String getText() {
        return text;
    }
}
