import java.util.TreeMap;

/**
 * Stores a sketch that holds shapes
 * @author Jacob Bacus for CS10 Winter 2024
 */
public class Sketch {

    private TreeMap<Integer, Shape> sketch;

    public Sketch() {
        sketch = new TreeMap<>();
    }

    /**
     * returns a map of all shapes in sketch
     * @return TreeMap of ShapeIDs (Integers) to Shapes
     */
    public synchronized TreeMap<Integer, Shape> getSketch() {
        return sketch;
    }

    /**
     * inserts shape into sketch
     * @param id new shape's identifier
     * @param newShape the shape being added
     */
    public void addToSketch(Integer id, Shape newShape) {
        sketch.put(id, newShape);
    }
}
