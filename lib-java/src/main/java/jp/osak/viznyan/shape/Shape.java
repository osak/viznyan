package jp.osak.viznyan.shape;

public class Shape {
    private int id;
    protected boolean dirty;

    protected Shape(int id) {
        this.id = id;
        dirty = false;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        if (id <= 0) {
            throw new IllegalArgumentException("ID must be a positive integer");
        }
        if (this.id != 0) {
            throw new IllegalStateException("ID must not be overridden once it's set");
        }
        this.id = id;
        markDirty();
    }

    protected void markDirty() {
        dirty = true;
    }
}
