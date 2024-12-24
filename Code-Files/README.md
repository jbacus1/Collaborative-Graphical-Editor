## Files

- `Editor.java`: Implements a client-side graphical editor for a client-server architecture using Java Swing for the GUI and custom data structures for managing shapes (Sketch.java). It uses an event-driven design pattern to handle user interactions.

- `EditorCommunicator.java`: Manages server communication for the editor, handling connection setup, message sending, and processing server updates to synchronize the local sketch.

- `Ellipse.java`: Represents an ellipse defined by two corners, with methods to move, change color, check point containment, and draw it on a canvas.

- `Polyline.java`: Implements a polyline as a list of Point objects, with methods to add points, move, change color, check point containment, and draw lines connecting the points.

- `Rectangle.java`: Represents a rectangle defined by two corners with methods to move, change color, check point containment, and draw it on a canvas.

- `Segment.java`: Implements a line segment as two endpoints with methods to move, change color, check point containment, and draw it on a canvas.

- `Shape.java`: Defines basic operations for geometric shapes including moving, checking containment, setting color, and drawing on a canvas, emphasizing modularity and reusability across different shape implementations. (Implemented by Ellipse, Polyline, Rectangle, and Segment)

- `Sketch.java`: Manages a collection of Shape objects using a TreeMap for efficient storage, retrieval, and synchronization of shapes by unique identifiers.

- `SketchServer.java`: Manages client connections and broadcasts updates using a ServerSocket, ArrayList, and Sketch object (the effetive master copy of the sketch), allowing efficient communication and state synchronization across connected clients.

- `SketchServerCommunicator.java`: Manages client-server communication for the SketchServer, handling connection setup, message sending, and processing updates to synchronize the local sketch with clients.
