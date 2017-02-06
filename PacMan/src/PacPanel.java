import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.JPanel;
import javax.swing.Timer;

public class PacPanel extends JPanel implements ActionListener, KeyListener {

	private boolean isChomping = false;
	private int timeSinceLastChomp = 0;

	private int stageWidth = 700;
	private int stageHeight = 700;
	private int gridSize = 35;
	/*
	 * moveDirection is an int that represents PacMan's direction 0 - not moving
	 * 1 - right 2 - left 3 - up 4 - down
	 */
	private int moveDirection = 0;
	private int directionQueued = 0;
	private int moveLookDirection = 0;

	private int pacX = 300;
	private int pacY = 300;
	private int pacDiameter = 20;
	private int pacWidth = 20;
	private int pacHeight = 20;
	private int pacDeltaX = 2;
	private int pacDeltaY = 2;

	private int wallLength = stageHeight / gridSize;

	ArrayList<Wall> walls = new ArrayList<Wall>();

	public PacPanel() throws IOException {
		setBackground(Color.BLACK);

		setFocusable(true);
		addKeyListener(this);

		Image image = loadPacImage();
		generateWalls();

		Timer timer = new Timer(1000 / 100, this);
		timer.start();
	}

	public void actionPerformed(ActionEvent e) {
		step();
	}

	private void step() {
		timeSinceLastChomp++;

		if (timeSinceLastChomp >= 25) {
			if (isChomping) {
				timeSinceLastChomp = 0;
				isChomping = false;
			} else if (!isChomping) {
				timeSinceLastChomp = 0;
				isChomping = true;
			}
		}

		
		if (pacX + pacWidth <= 0) {
			pacX = stageWidth;
		} else if (pacX >= stageWidth) {
			pacX = -pacWidth;
		}

		if (directionQueued == 3 || directionQueued == 4) {
			if (pacX % 20 == 0) {
				moveDirection = directionQueued;
				moveLookDirection = directionQueued;
			}
		} else if (directionQueued == 1 || directionQueued == 2) {
			if (pacY % 20 == 0 || pacY == 302) {
				moveDirection = directionQueued;
				moveLookDirection = directionQueued;
			}
		}

		if (hittingWall() && !throughDoor()) {
			if (moveDirection == 1) {
				pacX -= pacDeltaX;
				moveDirection = 0;
				directionQueued = 0;
			} else if (moveDirection == 2) {
				pacX += pacDeltaX;
				moveDirection = 0;
				directionQueued = 0;
			} else if (moveDirection == 3) {
				pacY += pacDeltaY;
				moveDirection = 0;
				directionQueued = 0;
			} else if (moveDirection == 4) {
				pacY -= pacDeltaY;
				moveDirection = 0;
				directionQueued = 0;
			}
		} else if (!hittingWall() || throughDoor()) {
			if (moveDirection == 1) {
				pacX += pacDeltaX;
			} else if (moveDirection == 2) {
				pacX -= pacDeltaX;
			} else if (moveDirection == 3) {
				pacY -= pacDeltaY;
			} else if (moveDirection == 4) {
				pacY += pacDeltaY;
			}
		}
		System.out.println(moveDirection);

		// if (!hittingWall()) {
		// if (moveDirection == 1) {
		// pacX += pacDeltaX;
		// } else if (moveDirection == 2) {
		// pacX -= pacDeltaX;
		// } else if (moveDirection == 3) {
		// pacY -= pacDeltaY;
		// } else if (moveDirection == 4) {
		// pacY += pacDeltaY;
		// }
		// } else if (hittingWall()) {
		// if (moveDirection == 1) {
		// pacX -= pacDeltaX;
		// moveDirection = 0;
		// } else if (moveDirection == 2) {
		// pacX += pacDeltaX;
		// moveDirection = 0;
		// } else if (moveDirection == 3) {
		// pacY += pacDeltaY;
		// moveDirection = 0;
		// } else if (moveDirection == 4) {
		// pacY -= pacDeltaY;
		// moveDirection = 0;
		// }
		// }
		// System.out.println(throughDoor());

		repaint();
	}

	public Image loadPacImage() throws IOException {
		AffineTransform at = new AffineTransform();
		File pacImg = new File("resources/PacImage.png");
		BufferedImage pacImage = ImageIO.read(pacImg);
		if (moveLookDirection == 1) {
			return pacImage;
		} else if (moveLookDirection == 2) {
			at.rotate(Math.PI, pacImage.getWidth() / 2, pacImage.getHeight() / 2);
			AffineTransformOp op = new AffineTransformOp(at, AffineTransformOp.TYPE_BILINEAR);
			pacImage = op.filter(pacImage, null);
			return pacImage;
		} else if (moveLookDirection == 3) {
			at.rotate(3 * Math.PI / 2, pacImage.getWidth() / 2, pacImage.getHeight() / 2);
			AffineTransformOp op = new AffineTransformOp(at, AffineTransformOp.TYPE_BILINEAR);
			pacImage = op.filter(pacImage, null);
			return pacImage;
		} else if (moveLookDirection == 4) {
			at.rotate(Math.PI / 2, pacImage.getWidth() / 2, pacImage.getHeight() / 2);
			AffineTransformOp op = new AffineTransformOp(at, AffineTransformOp.TYPE_BILINEAR);
			pacImage = op.filter(pacImage, null);
			return pacImage;
		}
		return pacImage;
	}

	public void generateWalls() {
		// generate right side wall
		for (int numOfWallsOnEdge = 0; numOfWallsOnEdge < gridSize; numOfWallsOnEdge++) {
			walls.add(new Wall(stageWidth - wallLength, wallLength * numOfWallsOnEdge, wallLength));
			System.out.println((stageWidth - wallLength));
		}
		// generate top wall
		for (int numOfWallsOnEdge = 0; numOfWallsOnEdge < gridSize; numOfWallsOnEdge++) {
			walls.add(new Wall(wallLength * numOfWallsOnEdge, 0, wallLength));
		}
		// generate left side wall
		for (int numOfWallsOnEdge = 0; numOfWallsOnEdge < gridSize; numOfWallsOnEdge++) {
			walls.add(new Wall(0, wallLength * numOfWallsOnEdge, wallLength));
		}
		// generate bottom wall
		for (int numOfWallsOnEdge = 0; numOfWallsOnEdge < gridSize; numOfWallsOnEdge++) {
			walls.add(new Wall(wallLength * numOfWallsOnEdge, stageHeight - wallLength, wallLength));
		}
		//generate middle horizontal walls
		for (int numOfWalls = 1; numOfWalls <= 9; numOfWalls++) {
			walls.add(new Wall(wallLength * (numOfWalls + 12), wallLength * 12, wallLength));
			walls.add(new Wall(wallLength * (numOfWalls + 12), wallLength * 20, wallLength));
		}
		//generate middle vertical walls
		for (int numOfWalls = 1; numOfWalls <= 2; numOfWalls++) {
			walls.add(new Wall(wallLength * 13, wallLength * (12 + numOfWalls), wallLength));
			walls.add(new Wall(wallLength * 21, wallLength * (12 + numOfWalls), wallLength));
			walls.add(new Wall(wallLength * 13, wallLength * (17 + numOfWalls), wallLength));
			walls.add(new Wall(wallLength * 21, wallLength * (17 + numOfWalls), wallLength));
		}
	}

	public boolean hittingWall() {
		Wall[] wallsArray = walls.toArray(new Wall[walls.size()]);
		for (int i = 0; i < wallsArray.length; i++) {
			if (pacX + pacWidth > wallsArray[i].getX() && pacX < wallsArray[i].getX() + wallsArray[i].getSideLength()) {
				if (pacY + pacHeight > wallsArray[i].getY()
						&& pacY < wallsArray[i].getY() + wallsArray[i].getSideLength()) {
					return true;
				}
			}
		}
		return false;
	}

	public boolean throughDoor() {
		if (pacY > 301 && pacY + pacHeight < 301 + ((wallLength * 3) - 1)) {
			if (pacX <= wallLength + 5 || pacX + pacWidth >= stageWidth - wallLength - 5) {
				return true;
			}
		}
		return false;
	}

	public void paintComponent(Graphics g) {

		super.paintComponent(g);

		g.setColor(Color.green);
		Wall[] wallsArray = walls.toArray(new Wall[walls.size()]);
		for (int i = 0; i < wallsArray.length; i++) {
			g.drawRect(wallsArray[i].getX(), wallsArray[i].getY(), wallsArray[i].getSideLength(),
					wallsArray[i].getSideLength());
		}

		g.setColor(Color.black);
		g.fillRect(0, 301, wallLength + 1, (wallLength * 3) - 1);
		g.fillRect(stageWidth - wallLength, 301, wallLength + 1, (wallLength * 3) - 1);

		if (!isChomping) {
			try {
				if (pacY == 300) {
					pacY += 2;
					g.drawImage(loadPacImage(), pacX, pacY, pacWidth, pacHeight - 2, this);
				} else {
					g.drawImage(loadPacImage(), pacX, pacY, pacWidth, pacHeight, this);
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else if (isChomping) {
			g.setColor(Color.YELLOW);
			g.fillOval(pacX, pacY, pacWidth, pacHeight);
		}
		System.out.println(pacX);
	}

	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
			System.exit(0);
		}

		if (!(pacX < 0) || !(pacX + pacWidth > stageWidth)) {
		if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
			directionQueued = 1;
			// moveDirection = 1;
			// moveLookDirection = 1;
		} else if (e.getKeyCode() == KeyEvent.VK_LEFT) {
			directionQueued = 2;
			// moveDirection = 2;
			// moveLookDirection = 2;
		} else if (e.getKeyCode() == KeyEvent.VK_UP) {
			directionQueued = 3;
			// moveDirection = 3;
			// moveLookDirection = 3;
		} else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
			directionQueued = 4;
			// moveDirection = 4;
			// moveLookDirection = 4;
		}
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub

	}

}
