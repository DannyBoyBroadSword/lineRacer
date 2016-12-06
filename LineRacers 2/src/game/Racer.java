package game;

import processing.core.PApplet;
import processing.core.PVector;

public abstract class Racer {

	private PVector pos;
	private PVector vel;
	private PVector target;
	private PVector targetVel;
	private Track track;
	private PApplet parent;
	private int color;
	private String name;
	private int timeTilNext = 0;

	/**
	 * Creates a new Racer.
	 * 
	 * @param parent
	 *            The PApplet in which the Racer will be rendered.
	 * @param name
	 *            This Racer's name (for display purposes)
	 * @param r
	 *            The red value of the Racer's color
	 * @param g
	 *            The green value of the Racer's color
	 * @param b
	 *            The blue value of the Racer's color
	 */
	public Racer(PApplet parent, String name, float r, float g, float b) {
		this.parent = parent;
		this.color = parent.color(r, g, b);
		targetVel = new PVector();
		vel = new PVector();
		this.name = name;
	}

	String getName() {
		return name;
	}

	int getColor() {
		return color;
	}

	/**
	 * Returns the coordinates of the end point of the race as an array of
	 * integers.
	 * 
	 * @return The end point of the race in (x,y) format.
	 */
	public int[] getEnd() {
		return track.getEnd();
	}

	/**
	 * Returns a 2D array of booleans where each element represents a cell in
	 * the grid. A value of true indicates that the cell can be safely traversed
	 * by a racer, a value of false indicates that the cell is a wall and cannot
	 * be safely traversed.
	 * 
	 * @return A 2D array of booleans representing the track.
	 */
	public boolean[][] getGrid() {
		return track.getGrid();
	}

	/**
	 * 
	 * @return Your position in (x,y) format.
	 */
	public int[] getPosition() {
		return new int[] { (int) (pos.x + 0.5), (int) (pos.y + 0.5) };
	}

	PVector getPositionVector() {
		return new PVector(pos.x, pos.y);
	}

	/**
	 * 
	 * @return Your velocity in (vx, vy) format.
	 */
	public int[] getVelocity() {
		return new int[] { (int) vel.x, (int) vel.y };
	}

	PVector getVelocityVector() {
		return new PVector(vel.x, vel.y);
	}

	/**
	 * Determines how your racer is going to affect its velocity in the next
	 * turn. Must return an array of integers in the format (change in vx,
	 * change in vy). You may not change vx and vy in a turn, and you may not
	 * change vx or vy by more than 1 per turn. The legal arrays to return are
	 * [1,0], [-1,0], [0,1], [0,-1], [0,0]
	 * 
	 * @return Your bot's change in velocity for the turn.
	 */
	public abstract int[] go();

	/**
	 * Get your bot ready for the race! This function is called automatically
	 * after the race has been completely setup but before any bots make their
	 * first moves.
	 */
	public abstract void start();

	void newTarget() {
		target = PVector.add(pos, vel);
		targetVel = PVector.div(vel, 30);
	}

	void setTrack(Track t) {
		track = t;
		int[] start = track.getStart();
		pos = new PVector(start[0], start[1]);
		target = new PVector(start[0], start[1]);
		vel = new PVector();
	}

	void update() {
		if (pos == null)
			return;
		float cellWidth = 1f * parent.width / track.nCols();
		float cellHeight = 1f * parent.height / track.nRows();
		if (timeTilNext == 0) {
			pos = target;
			int[] dVel = new int[] { 0, 0 };
			try {
				dVel = go();
				if (Math.abs(dVel[0]) + Math.abs(dVel[1]) <= 1) {
					vel.add(dVel[0], dVel[1]);
				}
			} catch (Exception e) {

			}
			newTarget();
			timeTilNext = 30;
		} else {
			pos.add(targetVel);
			timeTilNext--;
		}
		parent.fill(getColor());
		parent.noStroke();
		parent.ellipse((pos.x + 0.5f) * cellWidth, (pos.y + 0.5f) * cellHeight, cellWidth, cellHeight);
		parent.stroke(getColor());
		parent.line((pos.x + 0.5f) * cellWidth, (pos.y + 0.5f) * cellHeight, (target.x + 0.5f) * cellWidth,
				(target.y + 0.5f) * cellHeight);
	}

	boolean ready() {
		return target == null;
	}

}
