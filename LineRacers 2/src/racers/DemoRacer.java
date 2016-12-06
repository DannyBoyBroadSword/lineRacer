package racers;

import game.Racer;
import processing.core.PApplet;

public class DemoRacer extends Racer {

	public DemoRacer(PApplet parent, String name, float r, float g, float b) {
		super(parent, name, r, g, b);
	}

	public void start() {

	}

	@Override
	public int[] go() {
		
		double r = Math.random();
		if (r < 0.2)
			return new int[] { 1, 0 };
		else if (r < 0.4)
			return new int[] { -1, 0 };
		else if (r < 0.6)
			return new int[] { 0, 1 };
		else if (r < 0.8)
			return new int[] { 0, -1 };
		else
			return new int[] { 0, 0 };
	}

}
