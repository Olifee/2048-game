package gamelogic;

/**
 * Model for 2D-Coordinates of the game board
 * Coordinates are immutable!
 */
 
public final class Coordinate2D {
	final private int x, y;
	
	public Coordinate2D(int x, int y){
		if (x<0||y<0){
			throw new IllegalArgumentException("Coordinates have to be positive integers.");
		}
		this.x = x;
		this.y = y;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + x;
		result = prime * result + y;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Coordinate2D other = (Coordinate2D) obj;
		if (x != other.x)
			return false;
		if (y != other.y)
			return false;
		return true;
	}
}
