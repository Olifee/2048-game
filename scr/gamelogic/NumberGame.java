package gamelogic;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
 
public class NumberGame {
	final private int width, height; // x-axis (left -> right) | y-axis (up -> down)
	private int points;
	private Tile[][] playingField; // game board model (Tile[0][0] is on upper left corner)

	public NumberGame(int width, int height) {
		if (width < 1 || height < 1) {
			throw new IllegalArgumentException("Game board size needs to be 1x1 or bigger.");
		}
		this.width = width;
		this.height = height;
		this.points = 0;

		playingField = new Tile[width][height];
	}

	public NumberGame(int width, int height, int initialTiles) {
		if (initialTiles < 0 || initialTiles > width * height) {
			throw new IllegalArgumentException("Thats to few/many starting tiles.");
		}

		if (width < 1 || height < 1) {
			throw new IllegalArgumentException("Game board size needs to be 1x1 or bigger.");
		}
		this.width = width;
		this.height = height;
		this.points = 0;

		playingField = new Tile[width][height];

		// Kacheln hinzuügen
		for (int i = 1; i <= initialTiles; i++) {
			addRandomTile();
		}
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public int get(Coordinate2D coord) {
		if (coord.getX() >= width || coord.getY() >= height) {
			throw new IndexOutOfBoundsException("This position is out of bounds.");
		}
		Tile tileHere = playingField[coord.getX()][coord.getY()];
		if (tileHere == null)
			return 0;
		else
			return tileHere.getValue();
	}

	public int get(int x, int y) {
		if (x >= width || y >= height) {
			throw new IndexOutOfBoundsException("This position is out of bounds.");
		}
		Tile tileHere = playingField[x][y];
		if (tileHere == null)
			return 0;
		else
			return tileHere.getValue();
	}

	public int getPoints() {
		return points;
	}

	public boolean isFull() {
		Boolean result = true;
		breakpoint: for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				if (playingField[i][j] == null) {
					result = false;
					break breakpoint;
				}
			}
		}
		return result;
	}

	public Tile addRandomTile() {
		if (isFull()) {
			throw new TileExistsException("Game board full already.");
		}

		Tile newTile = null;
		Random random = new Random();

		Coordinate2D coord = getFreeTile();

		//erzeugt zu 10% 4-er Felder, sonst 2er
		switch (random.nextInt(10)) {
		case 0:
			newTile = new Tile(coord, 4);
			break;
		default:
			newTile = new Tile(coord, 2);
			break;
		}
		playingField[coord.getX()][coord.getY()] = newTile;
		return newTile;
	}
	
	public Coordinate2D getFreeTile(){
		Random random = new Random();
		
		Tile newTile = null;
		int x,y;
		do {
			x = random.nextInt(width);
			y = random.nextInt(height);
			newTile = playingField[x][y];
		} while (newTile != null);
		return new Coordinate2D(x, y);
	}

	public Tile addTile(int x, int y, int value) {
		if (playingField[x][y]!=null){
			throw new TileExistsException("Cannot be added, the field is already occupied.");
		}
		Coordinate2D coord = new Coordinate2D(x, y);
		Tile newTile = new Tile(coord, value);
		playingField[x][y] = newTile;
		return newTile;
	}

	public List<Move> move(Direction dir) {
		List<Move> listMove = new ArrayList<Move>();
		
		switch(dir){
			case UP:
				moveUp(listMove);
				break;
			case DOWN:
				moveDown(listMove);
				break;
			case LEFT:
				moveLeft(listMove);
				break;
			case RIGHT:
				moveRight(listMove);
				break;
		}
		return listMove;
	}

	//column-wise from the top
	private void moveUp(List<Move> listMove) {
		for(int x = 0; x<width; x++){
			for(int y = 0; y<height; y++){
				if (playingField[x][y]!=null){
					fieldHandler(playingField[x][y], Direction.UP, listMove);
				}
			}
		}
		
	}
	
	//column-wise from the bottom
	private void moveDown(List<Move> listMove) {
		for(int x = 0; x<width; x++){
			for(int y = height-1; y>=0; y--){
				if (playingField[x][y]!=null){
					fieldHandler(playingField[x][y], Direction.DOWN, listMove);
				}
			}
		}
	}

	//line-wise from left side
	private void moveLeft(List<Move> listMove) {
		for(int y = 0; y<height; y++){
			for(int x = 0; x<width; x++){
				if (playingField[x][y]!=null){
					fieldHandler(playingField[x][y], Direction.LEFT, listMove);
				}
			}
		}
	}
	
	//line-wise from right side
	private void moveRight(List<Move> listMove) {
		for(int y = 0; y<height; y++){
			for(int x = height-1; x>=0; x--){
				if (playingField[x][y]!=null){
					fieldHandler(playingField[x][y], Direction.RIGHT, listMove);
				}
			}
		}

	}
	
	private void fieldHandler(Tile tile, Direction dir, List<Move> listMove){
		boolean exit = false;
		Coordinate2D prevCoord = tile.getCoordinate();
		Coordinate2D prevPrevCoord;
		
		while (!exit){
			prevPrevCoord = new Coordinate2D(prevCoord.getX(), prevCoord.getY());
			prevCoord = getPrevCoord(prevCoord, dir);
			//Tile already at bound of game board
			if (prevCoord==null){
				if (tile.getCoordinate().equals(prevPrevCoord)){
					break;
				}
				listMove.add(new Move(tile.getCoordinate(), prevPrevCoord, tile.getValue()));
				playingField[prevPrevCoord.getX()][prevPrevCoord.getY()] = new Tile(prevPrevCoord, tile.getValue());
				playingField[tile.getCoordinate().getX()][tile.getCoordinate().getY()] = null;
				break;
			}

			//Tile has other tile with same value as neighbor
			if (tile.getValue()==get(prevCoord)){
				listMove.add(new Move(tile.getCoordinate(), prevCoord, tile.getValue(), tile.getValue()*2));
				playingField[prevCoord.getX()][prevCoord.getY()] = new Tile(prevCoord, tile.getValue()*2);
				playingField[tile.getCoordinate().getX()][tile.getCoordinate().getY()] = null;
				points += tile.getValue()*2;
				break;
			}
			//Tile has other tile with different value as neighbor
			if (get(prevCoord)!=0 && tile.getValue()!=get(prevCoord)){
				if (tile.getCoordinate().equals(prevPrevCoord)){
					break;
				}
				listMove.add(new Move(tile.getCoordinate(), prevPrevCoord, tile.getValue()));
				playingField[prevPrevCoord.getX()][prevPrevCoord.getY()] = new Tile(prevPrevCoord, tile.getValue());
				playingField[tile.getCoordinate().getX()][tile.getCoordinate().getY()] = null;
				break;
			}
		}
	}
	
	private Coordinate2D getPrevCoord(Coordinate2D coord, Direction dir){
		Coordinate2D prev = null;
		try{
			switch(dir){
			case UP:
				prev = new Coordinate2D(coord.getX(), coord.getY()-1);
				break;
			case DOWN:
				prev = new Coordinate2D(coord.getX(), coord.getY()+1);
				break;
			case LEFT:
				prev = new Coordinate2D(coord.getX()-1, coord.getY());
				break;
			case RIGHT:
				prev = new Coordinate2D(coord.getX()+1, coord.getY());
				break;
			}
			int value = get(prev);
		}
		catch(IllegalArgumentException | IndexOutOfBoundsException exc){
			return null;
		}
		return prev;
	}

	public boolean canMove(Direction dir) {
		NumberGame copy = new NumberGame(width, height);
		copy.playingField = new Tile[width][height];
		for (int i = 0; i<width; i++){
			copy.playingField[i] = playingField[i].clone();
		}
		return !copy.move(dir).isEmpty();
	}

	public boolean canMove() {
		if (!isFull()){
			return true;
		}
		if (canMove(Direction.UP)||canMove(Direction.DOWN)||canMove(Direction.LEFT)||canMove(Direction.RIGHT)){
			return true;
		}
		return false;
	}

}
