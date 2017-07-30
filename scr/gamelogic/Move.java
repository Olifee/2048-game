package gamelogic;

/**
 * @author vanessa
 * Bewegung einer einzelnen Kachel; immutable
 */
 
public final class Move {
	final private Coordinate2D from, to;
	final private int oldValue, newValue;
	
	public Move(Coordinate2D from, Coordinate2D to, int oldValue, int newValue){
		if (oldValue<1||newValue<1) {
			throw new IllegalArgumentException("Die Werte für eine Bewegung müssen >= 1 sein");
		}
		this.from = from;
		this.to = to;
		this.oldValue = oldValue;
		this.newValue = newValue;
	}
	
	public Move(Coordinate2D from, Coordinate2D to, int value){
		if (value<1) {
			throw new IllegalArgumentException("Der Wert für eine Bewegung muss >= 1 sein");
		}
		this.from = from;
		this.to = to;
		this.oldValue = value;
		this.newValue = value;
	}

	/**
	 * @return the from
	 */
	public Coordinate2D getFrom() {
		return from;
	}

	/**
	 * @return the to
	 */
	public Coordinate2D getTo() {
		return to;
	}

	/**
	 * @return the oldValue
	 */
	public int getOldValue() {
		return oldValue;
	}

	/**
	 * @return the newValue
	 */
	public int getNewValue() {
		return newValue;
	}
	
	public boolean isMerge(){
		return (oldValue!=newValue);
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((from == null) ? 0 : from.hashCode());
		result = prime * result + newValue;
		result = prime * result + oldValue;
		result = prime * result + ((to == null) ? 0 : to.hashCode());
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Move other = (Move) obj;
		if (from == null) {
			if (other.from != null)
				return false;
		} else if (!from.equals(other.from))
			return false;
		if (newValue != other.newValue)
			return false;
		if (oldValue != other.oldValue)
			return false;
		if (to == null) {
			if (other.to != null)
				return false;
		} else if (!to.equals(other.to))
			return false;
		return true;
	}
}
