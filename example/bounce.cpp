#include <iostream>
#include <string>
#include <vector>
#include <algorithm>
#include <cmath>

struct Shape {
    int id;

    Shape(int id_) : id(id_) {}
    virtual ~Shape() {}
};

struct Circle : public virtual Shape {
    int x, y, radius;

    Circle(int id_, int x_, int y_, int r_) : Shape(id_), x(x_), y(y_), radius(r_) {}
};

struct Rectangle : public virtual Shape {
    int x1, y1, x2, y2;

    Rectangle(int id_, int x1_, int y1_, int x2_, int y2_) : Shape(id_), x1(x1_), y1(y1_), x2(x2_), y2(y2_) {}
};

struct Line : public virtual Shape {
    int x1, y1, x2, y2;

    Line(int id_, int x1_, int y1_, int x2_, int y2_) : Shape(id_), x1(x1_), y1(y1_), x2(x2_), y2(y2_) {}
};

struct Text : public virtual Shape {
    int x, y;
    std::string text;

    Text(int id_, int x_, int y_, std::string &text_) : Shape(id_), x(x_), y(y_), text(text_) {}
};

struct VizState {
    std::vector<Shape*> shapes;

    int addShape(Shape *s) {
        if (s->id == 0) {
            s->id = shapes.size();
            shapes.push_back(s);
        } else {
            if (s->id >= shapes.size()) {
                shapes.resize(s->id + 1);
            }
            shapes[s->id] = s;
        }
        return s->id;
    }
};

std::ostream& operator <<(std::ostream &os, const Circle &c) {
    return os << c.id << ' ' << c.x << ' ' << c.y << ' ' << c.radius << std::endl;
}

std::ostream& operator <<(std::ostream &os, const Rectangle &r) {
    return os << r.id << ' ' << r.x1 << ' ' << r.y1 << ' ' << r.x2 << ' ' << r.y2 << ' ' << std::endl;
}

std::ostream& operator <<(std::ostream &os, const Line &l) {
    return os << l.id << ' ' << l.x1 << ' ' << l.y1 << ' ' << l.x2 << ' ' << l.y2 << ' ' << std::endl;
}

std::ostream& operator <<(std::ostream &os, const Text &t) {
    return os << t.id << ' ' << t.x << ' ' << t.y << ' ' << t.text << std::endl;
}

template <typename T> void countAndPrint(std::ostream &os, const VizState &state) {
    int count = 0;
    for (Shape *s : state.shapes) {
        if (dynamic_cast<T*>(s) != nullptr) {
            ++count;
        }
    }
    os << count << std::endl;
    for (Shape *s : state.shapes) {
        const T *t = dynamic_cast<T*>(s);
        if (t != nullptr) {
            os << *t;
        }
    }
}

std::ostream& operator <<(std::ostream &os, const VizState &state) {
    countAndPrint<Circle>(os, state);
    countAndPrint<Rectangle>(os, state);
    countAndPrint<Line>(os, state);
    countAndPrint<Text>(os, state);
    return os;
}

struct Vec {
    double x, y;
};

Vec operator +(const Vec &v1, const Vec &v2) {
    return Vec{v1.x + v2.x, v1.y + v2.y};
}
Vec operator -(const Vec &v1, const Vec &v2) {
    return Vec{v1.x - v2.x, v1.y - v2.y};
}
Vec operator *(const Vec &v1, double r) {
    return Vec{r * v1.x, r * v1.y};
}
Vec operator *(double r, const Vec &v1) {
    return Vec{r * v1.x, r * v1.y};
}
Vec operator /(const Vec &v1, double r) {
    return Vec{v1.x / r, v1.y / r};
}
double dot(const Vec &v1, const Vec &v2) {
    return v1.x * v2.x + v1.y * v2.y;
}
double cross(const Vec &v1, const Vec &v2) {
    return v1.x * v2.y - v1.y * v2.x;
}
Vec unit(const Vec &v1) {
    return v1 / sqrt(dot(v1, v1));
}

struct Wall {
    Vec p1, p2;
};

bool intersect(const Vec &p1, const Vec &p2, const Vec &q1, const Vec &q2) {
    return cross(q1 - p1, p2 - p1) * cross(q2 - p1, p2 - p1) <= 0 &&
            cross(p1 - q1, q2 - q1) * cross(p2 - q1, q2 - q1) <= 0;
}

struct SimState {
    VizState vizState;
    std::vector<Wall> walls;
    Vec p, v;
    double radius;
    Circle *vizCircle;

    SimState() : p(Vec{200, 350}), v(Vec{5, -5}), radius(5) {
        vizCircle = new Circle(1, (int) p.x, (int) p.y, (int) radius);
        vizState.addShape(vizCircle);
    }

    void addBlock(double x1, double y1, double x2, double y2) {
        vizState.addShape(new Rectangle(0, x1, y1, x2, y2));
        walls.push_back(Wall{Vec{x1, y1}, Vec{x1, y2}});
        walls.push_back(Wall{Vec{x1, y2}, Vec{x2, y2}});
        walls.push_back(Wall{Vec{x2, y2}, Vec{x2, y1}});
        walls.push_back(Wall{Vec{x2, y1}, Vec{x1, y1}});
    }

    void step(double len = 1.0) {
        if (len < 1e-8) {
            return;
        }
        const Vec np = p + v * len;
        double minT = 1.0;
        Vec minWall;
        for (const Wall &wall : walls) {
            if (intersect(p, np, wall.p1, wall.p2)) {
                const Vec v1 = np - p;
                const Vec v2 = wall.p2 - wall.p1;
                const Vec v3 = wall.p1 - p;
                const double det = cross(v1, v2);
                const double t = (v2.y * v3.x - v1.y * v3.y) / det;
                if (t < minT) {
                    minT = t;
                    minWall = wall.p2 - wall.p1;
                }
            }
        }
        p = p + v * len * minT;
        vizCircle->x = (int) p.x;
        vizCircle->y = (int) p.y;

        if (minT != 1.0) {
            const Vec hitP = p + v * minT;
            Vec perp = unit(Vec{minWall.y, minWall.x});
            if (dot(perp, v) > 0) {
                perp = perp * -1;
            }
            const Vec refP = hitP + v + perp*fabs(dot(v, perp)*2);
            v = refP - hitP;
            step(len * (1 - minT));
        }
    }
};

int main(int argc, char **argv) {
    SimState state;

    state.addBlock(0, 0, 400, 1);
    state.addBlock(399, 0, 400, 400);
    state.addBlock(0, 399, 400, 400);
    state.addBlock(0, 0, 1, 400);
    state.addBlock(50, 50, 350, 100);

    int steps = 100;
    if (argc > 1) {
        steps = std::stoi(argv[1]);
    }
    for (int i = 0; i < steps; ++i) {
        state.step();
        std::cout << state.vizState;
    }
    return 0;
}