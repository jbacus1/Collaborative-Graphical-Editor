## Files

- Editor.java: Implements a client-side graphical editor for a client-server architecture using Java Swing for the GUI and custom data structures for managing shapes (Sketch.java). It uses an event-driven design pattern to handle user interactions.
- EditorCommunicator.java: Manages server communication for the editor, handling connection setup, message sending, and processing server updates to synchronize the local sketch.
- Ellipse.java: Represents an ellipse defined by two corners, with methods to move, change color, check point containment, and draw it on a canvas.
- Polyline.java: Implements a polyline as a list of Point objects, with methods to add points, move, change color, check point containment, and draw lines connecting the points.
- Rectangle.java: Represents a rectangle defined by two corners with methods to move, change color, check point containment, and draw it on a canvas.
