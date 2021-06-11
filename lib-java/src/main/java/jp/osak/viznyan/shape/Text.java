package jp.osak.viznyan.shape;

public class Text extends Shape {
    private int x, y;
    private String text;

    public Text(int id, int x, int y, String text) {
        super(id);
        this.x = x;
        this.y = y;
        this.text = text;
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

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
        markDirty();
    }
}
