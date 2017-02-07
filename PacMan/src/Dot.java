
public class Dot {

	private int x;
	private int y;
	private int diameter;

	public Dot(int x, int y, int diameter) {
		this.x = x;
		this.y = y;
		this.diameter = diameter;
	}

	public void setX(int nextX) {
		x = nextX;
	}

	public int getX() {
		return x;
	}

	public void setY(int nextY) {
		y = nextY;
	}

	public int getY() {
		return y;
	}

	public void setDiameter(int newDiameter) {
		diameter = newDiameter;
	}

	public int getDiameter() {
		return diameter;
	}
}
