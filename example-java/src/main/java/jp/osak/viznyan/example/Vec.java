package jp.osak.viznyan.example;

class Vec {
    public double x;
    public double y;

    public Vec() {
        this.x = 0;
        this.y = 0;
    }

    public Vec(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public Vec add(Vec v) {
        return new Vec(x + v.x, y + v.y);
    }

    public Vec sub(Vec v) {
        return new Vec(x - v.x, y - v.y);
    }

    public Vec mul(double r) {
        return new Vec(x * r, y * r);
    }

    public double dot(Vec v) {
        return x * v.x + y * v.y;
    }

    public double cross(Vec v) {
        return x * v.y - y * v.x;
    }

    public Vec unit() {
        return this.mul(1 / Math.sqrt(this.dot(this)));
    }
}
