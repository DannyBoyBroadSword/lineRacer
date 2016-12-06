package game;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import processing.core.PApplet;
import processing.data.Table;
import racers.*;

public class LineRacerDaemon extends PApplet {

	private Track t;
	private boolean editMode = true;
	private int brushSize = 3;
	private ArrayList<Integer> keysPressed = new ArrayList<Integer>();
	private Game g;

	public static void main(String[] args) {
		PApplet.main("game.LineRacerDaemon");
	}

	public void settings() {
		size(640, 480);
	}

	public void setup() {
		frameRate(500);
		System.out.println("Click and drag to draw the racetrack.\nHold x to add walls back in.");
		System.out.println("Press '[' and ']' to increase or decrease the brush size.");
		System.out.println("Press 'r' to reset the course to all walls.");
		System.out.println("Press 'i' to invert the course.");
		System.out.println("Press 's' and click to place the start and 'e' to place the end.");
		System.out.println("Press CMD+s to save a track and CMD+l to load one.");
		System.out.println("When you are ready to begin the race, press ENTER.");
		t = new Track(this, 64, 48);
		try {
			Table data = loadTable("racer_autosave.csv");
			t.fromTable(data);
		} catch (Exception e) {

		}
		g = new Game(this, t);
		// Add racers to the game here.
		g.addRacer(new DemoRacer(this, "Demo", 255, 0, 0));
	}

	public void draw() {
		background(170);
		g.update();
		HashMap<Racer, Integer> score = g.getScore();
		if (score != null) {
			noLoop();
			// exit();
		}
	}

	public void dispose() {
		Table data = t.toTable();
		saveTable(data, "racer_autosave.csv");
	}

	public void mousePressed() {
		int x = mouseX / (width / t.nCols());
		int y = mouseY / (height / t.nRows());
		if (editMode) {
			if (keysPressed.contains(83)) {
				t.setStart(x, y);
				System.out.println("START: (" + x + ", " + y + ")");
			} else if (keysPressed.contains(69)) {
				t.setEnd(x, y);
				System.out.println("END: (" + x + ", " + y + ")");
			} else {
				t.setCell(x, y, !keysPressed.contains(88), brushSize);
			}
		}
	}

	public void mouseDragged() {
		int x = mouseX / (width / t.nCols());
		int y = mouseY / (height / t.nRows());
		if (editMode) {
			t.setCell(x, y, !keysPressed.contains(88), brushSize);
		}
	}

	public void keyPressed() {
		if (!keysPressed.contains(keyCode))
			keysPressed.add(keyCode);
		if (key == '[')
			brushSize = brushSize > 1 ? brushSize - 1 : 1;
		else if (key == ']')
			brushSize++;
		else if (key == 'r')
			t.reset();
		else if (key == 'i')
			t.invert();
		else if (keyCode == ENTER) {
			if (t.raceReady()) {
				System.out.println("Ready to race!\n-----");
				editMode = false;
				g.activate();
				t.active = true;
			} else
				System.out.println("Track incomplete!");
		} else if (keysPressed.contains(157) && key == 's') {
			selectOutput("Select a file to write to:", "fileSaved");
			keysPressed.remove(Integer.valueOf(157));
		} else if (keysPressed.contains(157) && key == 'l') {
			selectInput("Select a file to load:", "fileLoaded");
			keysPressed.remove(Integer.valueOf(157));
		}
	}

	public void keyReleased() {
		if (keysPressed.contains(keyCode)) {
			keysPressed.remove(Integer.valueOf(keyCode));
		}
	}

	public void fileSaved(File selection) {
		String path = selection.getAbsolutePath();
		if (!path.substring(path.length() - 4).equals(".csv"))
			path += ".csv";
		println("Track saved to " + path);
		Table data = t.toTable();
		saveTable(data, path);
	}

	public void fileLoaded(File selection) {
		Table data = loadTable(selection.getAbsolutePath(), "csv");
		t.fromTable(data);
	}

}
