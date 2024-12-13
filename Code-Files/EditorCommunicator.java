import java.awt.*;
import java.io.*;
import java.net.Socket;
import java.util.Arrays;
import java.util.Objects;
import java.util.TreeMap;

/**
 * Handles communication to/from the server for the editor
 * 
 * @author Chris Bailey-Kellogg, Dartmouth CS 10, Fall 2012
 * @author Chris Bailey-Kellogg; overall structure substantially revised Winter 2014
 * @author Travis Peters, Dartmouth CS 10, Winter 2015; remove EditorCommunicatorStandalone (use echo server for testing)
 * @author Tim Pierson Dartmouth CS 10, provided for Winter 2024
 * @author Jacob Bacus, completed functinality for CS10 Winter 2024
 */
public class EditorCommunicator extends Thread {
	private PrintWriter out;		// to server
	private BufferedReader in;		// from server
	protected Editor editor;		// handling communication for

	/**
	 * Establishes connection and in/out pair
	 */
	public EditorCommunicator(String serverIP, Editor editor) {
		this.editor = editor;
		System.out.println("connecting to " + serverIP + "...");
		try {
			Socket sock = new Socket(serverIP, 4242);
			out = new PrintWriter(sock.getOutputStream(), true);
			in = new BufferedReader(new InputStreamReader(sock.getInputStream()));
			System.out.println("...connected");
		}
		catch (IOException e) {
			System.err.println("couldn't connect");
			System.exit(-1);
		}
	}

	/**
	 * Sends message to the server
	 */
	public void send(String msg) {
		out.println(msg);
	}

	/**
	 * Keeps listening for and handling (your code) messages from the server
	 */
	public void run() {
		try {
			// Handle messages
			String line;

			//lines will be messages from server about updates
			//updates will come from one of the current editors

			//updates received from server applied to client's sketch
			while ((line = in.readLine()) != null) {
				String[] inputs = line.split(" ");
				TreeMap<Integer, Shape> shapes = editor.getSketch().getSketch();

				//identifies input based on first string
				if (Objects.equals(inputs[0], "draw")) {

					//uses second string to determine which shape to draw
					//if no shapes present, puts with key of 0
					//otherwise puts with current highest key + 1
					if (Objects.equals(inputs[1], "ellipse")) {
						Shape newShape = new Ellipse(Integer.parseInt(inputs[2]), Integer.parseInt(inputs[3]),
								Integer.parseInt(inputs[4]), Integer.parseInt(inputs[5]),
								new Color(Integer.parseInt(inputs[6])));
						if (shapes.isEmpty()) {
							shapes.put(0, newShape);
						} else {
							shapes.put(shapes.lastKey() + 1, newShape);
						}
					} else if (Objects.equals(inputs[1], "rectangle")) {
						Shape newShape = new Rectangle(Integer.parseInt(inputs[2]), Integer.parseInt(inputs[3]),
								Integer.parseInt(inputs[4]), Integer.parseInt(inputs[5]),
								new Color(Integer.parseInt(inputs[6])));
						if (shapes.isEmpty()) {
							shapes.put(0, newShape);
						} else {
							shapes.put(shapes.lastKey() + 1, newShape);
						}
					} else if (Objects.equals(inputs[1], "polyline")) {
						Polyline newLine = null;
						for (int i = 2; i < inputs.length - 1; i += 2) { // reads point pairs in sets of two (x, y)
							if (newLine == null) {
								newLine = new Polyline(Integer.parseInt(inputs[i]), Integer.parseInt(inputs[i+1]), new Color(Integer.parseInt(inputs[inputs.length-1])));
							} else {
								newLine.addNextPoint(Integer.parseInt(inputs[i]), Integer.parseInt(inputs[i+1]));
							}
						}
						if (shapes.isEmpty()) {
							shapes.put(0, newLine);
						} else {
							shapes.put(shapes.lastKey() + 1, newLine);
						}
					} else if (Objects.equals(inputs[1], "segment")) {
						Shape newShape = new Segment(Integer.parseInt(inputs[2]), Integer.parseInt(inputs[3]),
								Integer.parseInt(inputs[4]), Integer.parseInt(inputs[5]),
								new Color(Integer.parseInt(inputs[6])));
						if (shapes.isEmpty()) {
							shapes.put(0, newShape);
						} else {
							shapes.put(shapes.lastKey() + 1, newShape);
						}
					}
				}
				// moves given shape by given amounts
				else if (Objects.equals((inputs[0]), "move")) {
					shapes.get(Integer.parseInt(inputs[1])).moveBy(Integer.parseInt(inputs[2]), Integer.parseInt(inputs[3]));
				}
				// changes given shape to given color
				else if (Objects.equals((inputs[0]), "recolor")) {
					Shape currShape = shapes.get(Integer.parseInt(inputs[1]));
					currShape.setColor(new Color(Integer.parseInt(inputs[2])));
				}
				// removes current shape from shapes
				else if (Objects.equals((inputs[0]), "delete")) {
					shapes.remove(Integer.parseInt(inputs[1]));
				}
				editor.repaint();
			}
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		finally {
			System.out.println("server hung up");
		}
	}
}
