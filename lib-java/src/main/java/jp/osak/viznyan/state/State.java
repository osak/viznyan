package jp.osak.viznyan.state;

import jp.osak.viznyan.shape.Shape;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class State {
    private final HashMap<Integer, Shape> shapes = new HashMap<>();
    int maxId = 0;

    public State() {
    }

    public Shape add(Shape shape) {
        if (shape.getId() == 0) {
            shape.setId(maxId + 1);
        }
        if (shapes.containsKey(shape.getId())) {
            throw new IllegalArgumentException("ID " + shape.getId() + " is already registered");
        }
        shapes.put(shape.getId(), shape);
        maxId = Math.max(maxId, shape.getId());
        return shape;
    }

    public Shape get(int id) {
        final Shape shape = shapes.get(id);
        if (shape == null) {
            throw new IllegalArgumentException("Shape with ID " + id + " doesn't exist");
        }
        return shape;
    }

    public <T extends Shape> List<T> getList(Class<T> targetClass) {
        return (List<T>) shapes.values().stream()
                .filter(targetClass::isInstance)
                .collect(Collectors.toList());
    }
}
