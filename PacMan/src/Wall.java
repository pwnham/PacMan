
public class Wall {

	private int x;
	private int y;
	private int sideLength;

	public Wall(int x, int y, int sideLength) {
		this.x = x;
		this.y = y;
		this.sideLength = sideLength;
	}

	public void setX(int newX) {
		x = newX;
	}

	public int getX() {
		return x;
	}

	public void setY(int newY) {
		y = newY;
	}

	public int getY() {
		return y;
	}

	public void setSideLength(int newLength) {
		sideLength = newLength;
	}

	public int getSideLength() {
		return sideLength;
	}
}
