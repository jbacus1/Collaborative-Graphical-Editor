import java.awt.*;
import java.io.*;
import java.net.Socket;
import java.util.Arrays;
import java.util.Objects;
import java.util.TreeMap;

/**
 * Handles communication between the server and one client, for SketchServer
 *
 * @author Chris Bailey-Kellogg, Dartmouth CS 10, Fall 2012; revised Winter 2014 to separate SketchServerCommunicator
 * @author Tim Pierson Dartmouth CS 10, provided for Winter 2024
 * @author Jacob Bacus, completed functionality for CS10 Winter 2024
 */
public class SketchServerCommunicator extends Thread {
	private Socket sock;					// to talk with client
	private BufferedReader in;				// from client
	private PrintWriter out;				// to client
	private SketchServer server;			// handling communication for

	public SketchServerCommunicator(Socket sock, SketchServer server) {
		this.sock = sock;
		this.server = server;
	}

	/**
	 * Sends a message to the client
	 * @param msg
	 */
	public void send(String msg) {
		out.println(msg);
	}
	
	/**
	 * Keeps listening for and handling (your code) messages from the client
	 */
	public void run() {
		try {
			System.out.println("someone connected");
			
			// Communication channel
			in = new BufferedReader(new InputStreamReader(sock.getInputStream()));
			out = new PrintWriter(sock.getOutputStream(), true);

			// Tell the client the current state of the world
			for (Shape shape : server.getSketch().getSketch().values()) {
				send("draw " + shape);
			}



			// Keep getting and handling messages from the client

			String line;

			//line will be changes from an editor
			//these changes are applied to master sketch
			//after this they are broadcast to all editors
			while ((line = in.readLine()) != null) {
				String[] inputs = line.split(" ");
				TreeMap<Integer, Shape> shapes = server.getSketch().getSketch();

				//identifies input based on first string
				if (Objects.equals(inputs[0], "draw")) {

					//uses second string to determine which shape to draw
					//if no shapes present, puts with key of 0
					//otherwise puts with current highest key + 1
					if (Objects.equals(inputs[1], "ellipse")) {
						Shape newShape = new Ellipse(Integer.parseInt(inputs[2]), Integer.parseInt(inputs[3]),
								Integer.parseInt(inputs[4]), Integer.parseInt(inputs[5]),
								new Color(Integer.parseInt(inputs[6])));
						if (shapes.isEmpty()){
							shapes.put(0, newShape);
						} else {
							shapes.put(shapes.lastKey() + 1, newShape);
						}
						server.broadcast(line);
					} else if (Objects.equals(inputs[1], "rectangle")) {
						Shape newShape = new Rectangle(Integer.parseInt(inputs[2]), Integer.parseInt(inputs[3]),
								Integer.parseInt(inputs[4]), Integer.parseInt(inputs[5]),
								new Color(Integer.parseInt(inputs[6])));
						if (shapes.isEmpty()) {
							shapes.put(0, newShape);
						} else {
							shapes.put(shapes.lastKey() + 1, newShape);
						}
						server.broadcast(line);
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
						}
						else {
							shapes.put(shapes.lastKey() + 1, newLine);
						}
						server.broadcast(line);
					} else if (Objects.equals(inputs[1], "segment")) {
						Shape newShape =  new Segment(Integer.parseInt(inputs[2]), Integer.parseInt(inputs[3]),
								Integer.parseInt(inputs[4]), Integer.parseInt(inputs[5]),
								new Color(Integer.parseInt(inputs[6])));
						if (shapes.isEmpty()) {
							shapes.put(0, newShape);
						} else {
							shapes.put(shapes.lastKey() + 1, newShape);
						}
						server.broadcast(line);
					}
				}
				// moves given shape by given amounts
				else if (Objects.equals((inputs[0]), "move")) {
					shapes.get(Integer.parseInt(inputs[1])).moveBy(Integer.parseInt(inputs[2]), Integer.parseInt(inputs[3]));
					server.broadcast(line);
				}
				// changes given shape to given color
				else if (Objects.equals((inputs[0]), "recolor")) {
					Shape currShape = shapes.get(Integer.parseInt(inputs[1]));
					currShape.setColor(new Color(Integer.parseInt(inputs[2])));
					server.broadcast(line);
				}
				// removes current shape from shapes
				else if (Objects.equals((inputs[0]), "delete")) {
					shapes.remove(Integer.parseInt(inputs[1]));
					server.broadcast(line);
				}
			}


			// Clean up -- note that also remove self from server's list so it doesn't broadcast here
			server.removeCommunicator(this);
			out.close();
			in.close();
			sock.close();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}
}
