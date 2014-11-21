package com.khrismuc.crushthrees;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;

/**
 * @author Chris
 * 
 */
public class Field {

	public int width, height;

	private Stage mainStage;
	public Three[][] cell;

	public Three selected;

	Texture threes;

	public Field(Stage mainStage, int w, int h) {
		width = w;
		height = h;
		this.mainStage = mainStage;
		cell = new Three[width][height];
		init();
	}

	public Point getGraphicCoords(int x, int y) {
		int cellSize = Sys.game.cellSize;
		float xO = (mainStage.getWidth() - width * cellSize) / 2f;
		float yO = (mainStage.getHeight() - height * cellSize) / 2f;
		Point p = new Point();
		p.fx = xO + x * cellSize;
		p.fy = yO + y * cellSize;
		return p;
	}

	public void addCell(int x, int y, int xc, int yc) {

		// x,y: coordinates of cell
		// xc, yc: coordinates of graphical start

		int cellSize = Sys.game.cellSize;

		int[] vals = { 1, 1, 1, 1, 3, 3, 3, 3, 9, 9 }; // , 9};
		int val = Sys.random.nextInt(vals.length);

		int sp = Sys.random.nextInt(30) > 28 ? 1 : 0;

		cell[x][y] = new Three(vals[val], sp, this);
		Point gc = getGraphicCoords(xc, yc);
		cell[x][y].setPosition(gc.fx, gc.fy);
		cell[x][y].destX = gc.fx;
		cell[x][y].destY = gc.fy;
		cell[x][y].setSize(cellSize, cellSize);
		cell[x][y].setScale(1f);
		mainStage.addActor(cell[x][y]);
	}

	public void init() {

		mainStage.clear();
		threes = Sys.am.get("threes.png", Texture.class);

		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				addCell(x, y, x, y);
			}
		}

	}

	/*
	 * find neighbor of same value!
	 */
	private Three getNeighbor(Three t, int dx, int dy, boolean sameValue) {
		Point c = getCoords(t);
		if (c == null)
			return null;
		c.x += dx;
		c.y += dy;
		if (c.x < 0 || c.x >= width)
			return null;
		if (c.y < 0 || c.y >= height)
			return null;
		if (!sameValue)
			return cell[c.x][c.y];
		if (cell[c.x][c.y] == null)
			return null;
		return cell[c.x][c.y].value == t.value ? cell[c.x][c.y] : null;
	}

	private ArrayList<Three> checkNeighbors(Three t, int dx, int dy) {
		// go in direction dx, dy
		// find one or two of same value
		ArrayList<Three> neighbors = new ArrayList<Three>();
		Three n;

		n = getNeighbor(t, dx, dy, true);
		if (n != null)
			neighbors.add(n);
		else
			return neighbors;
		n = getNeighbor(t, dx * 2, dy * 2, true);
		if (n != null)
			neighbors.add(n);
		return neighbors;
	}
	
	private boolean checkForConnectedNeighbors(Three t) {
		Three n;
		n = getNeighbor(t, -1, 0, true);
		if (n != null && n.connected) return true;
		n = getNeighbor(t, 1, 0, true);
		if (n != null && n.connected) return true;
		n = getNeighbor(t, 0, -1, true);
		if (n != null && n.connected) return true;
		n = getNeighbor(t, 0, 1, true);
		if (n != null && n.connected) return true;
		return false;
	}

	private void markConnected(Three t, ArrayList<Three> tiles) {
		int foundNew;
		Three th;
		do {
			foundNew = 0;
			// look for connected but not yet marked tiles
			for (int y = 0; y < height; y++) {
				for (int x = 0; x < width; x++) {
					th = cell[x][y];
					if (th != null) {
						if (!th.connected && checkForConnectedNeighbors(th)) {
							th.connected = true;
							tiles.add(th);
							foundNew++;
						}
					}
				}
			}
		} while (foundNew > 0);
	}
	
	private ArrayList<Three> getConnected(Three t) {
		t.connected = true;
		ArrayList<Three> tiles = new ArrayList<Three>();
		markConnected(t, tiles);
		return tiles;
	}
	
	public void activate(Three t) {
		// cell was clicked

		if (t.special > 0) {
			activateSpecial(t);
			return;
		}
		
		ArrayList<Three> connectedTiles = getConnected(t);
		clearConnected();
		
		// if less than three, proceed with swapping
		if (connectedTiles.size() < 2) {
			activateSwap(t);
			return;
		}
		
		int total = connectedTiles.size() + 1;
		int multiplier = total / 3;
		
		// merge other tiles
		for (int i = 0; i < connectedTiles.size(); i++) connectedTiles.get(i).mergeTo(t);
		for (int i = 0; i < multiplier; i++) t.threeUp();
		
		selected = null;
	}
	
	private void clearConnected() {
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				if (cell[x][y] != null) cell[x][y].connected = false;
			}
		}
	}

	public void activateOld(Three t) {

		ArrayList<Three> left = checkNeighbors(t, -1, 0);
		ArrayList<Three> right = checkNeighbors(t, 1, 0);
		ArrayList<Three> up = checkNeighbors(t, 0, 1);
		ArrayList<Three> down = checkNeighbors(t, 0, -1);

		int threeUp = 0;

		// two horizontal neighbors?
		// first, check if left & right
		if (left.size() > 0 && right.size() > 0) {
			left.get(0).mergeTo(1, 0);
			right.get(0).mergeTo(-1, 0);
			threeUp++;
		} else if (left.size() >= 2) {
			left.get(0).mergeTo(1, 0);
			left.get(1).mergeTo(2, 0);
			threeUp++;
		} else if (right.size() >= 2) {
			right.get(0).mergeTo(-1, 0);
			right.get(1).mergeTo(-2, 0);
			threeUp++;
		}

		// two vertical neighbors?
		// first, check if up & down
		if (down.size() > 0 && up.size() > 0) {
			down.get(0).mergeTo(0, 1);
			up.get(0).mergeTo(0, -1);
			threeUp++;
		} else if (down.size() >= 2) {
			down.get(0).mergeTo(0, 1);
			down.get(1).mergeTo(0, 2);
			threeUp++;
		} else if (up.size() >= 2) {
			up.get(0).mergeTo(0, -1);
			up.get(1).mergeTo(0, -2);
			threeUp++;
		}

		// if at this point, threeUp is still zero, let's check
		// a corner three, for example one neighbor up and left
		if (threeUp == 0) {
			int ns = left.size() + right.size() + down.size() + up.size();
			if (ns == 2) {
				if (left.size() == 1)
					left.get(0).mergeTo(1, 0);
				if (right.size() == 1)
					right.get(0).mergeTo(-1, 0);
				if (down.size() == 1)
					down.get(0).mergeTo(0, 1);
				if (up.size() == 1)
					up.get(0).mergeTo(0, -1);
				threeUp++;
			}
		}

		for (int i = 0; i < threeUp; i++) {
			t.threeUp();
			selected = null;
		}

		// swap
		if (threeUp == 0) activateSwap(t);
	}

	private void activateSwap(Three t) {
		// deselect
		if (selected == t) {
			selected = null;
		} else if (selected != null) {
			// try to swap
			Point a = getCoords(selected);
			Point b = getCoords(t);
			int dx = Math.abs(a.x - b.x);
			int dy = Math.abs(a.y - b.y);
			if (t.value == selected.value || dx * dy != 0 || dx + dy != 1) {
				// no neighbors or same value, select new choice
				selected = t;
			} else {
				// swap
				Three tmp = cell[a.x][a.y];
				cell[a.x][a.y] = cell[b.x][b.y];
				cell[b.x][b.y] = tmp;
				// set destinations
				cell[a.x][a.y].moveTo(a.x, a.y);
				cell[b.x][b.y].moveTo(b.x, b.y);
				selected = null;
			}
		} else
			selected = t;
	}

	private void activateSpecial(Three t) {

		if (t.special == 1) {
			int v = t.value;
			t.remove();
			for (int y = 0; y < height; y++) {
				for (int x = 0; x < width; x++) {
					if (cell[x][y].value == v)
						remove(cell[x][y]);
				}
			}
		}

		checkForHolesAndDead();
	}

	public Point getCoords(Three t) {
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				if (cell[x][y] == t)
					return new Point(x, y);
			}
		}
		return null;
	}

	public void remove(Three t) {

		Point c = getCoords(t);
		if (c == null)
			return;

		cell[c.x][c.y] = null;
		t.remove();
	}

	public void checkForHolesAndDead() {

		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				// if cell has Three that isn't merging
				if (cell[x][y] != null) {
					// slashed for removal / dead?
					if (cell[x][y].dead)
						remove(cell[x][y]);

					// else, if cell beneath empty?
					else if (y >= 1 && cell[x][y - 1] == null) {
						// only if cell isn't merging
						if (!cell[x][y].removeOnDestReached) {
							cell[x][y - 1] = cell[x][y];
							cell[x][y] = null;
							cell[x][y - 1].destY -= Sys.game.cellSize;
						}
					}
				}

				// check top row
				if (y == height - 1 && cell[x][y] == null) {
					// add cell to top row, but it starts out above
					addCell(x, y, x, y + 1);
					cell[x][y].moveTo(x, y);
					/*
					 * cell[x][y].destX = cell[x][y].getX(); cell[x][y].destY =
					 * cell[x][y].getY() - Sys.game.cellSize;
					 */
				}
			}
		}
	}

	public void dispose() {
		threes.dispose();
	}
}
