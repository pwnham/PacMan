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

import javax.imageio.ImageIO;
import javax.swing.JPanel;
import javax.swing.Timer;

public class PacPanel extends JPanel implements ActionListener, KeyListener {

	private boolean isChomping = false;
	private int timeSinceLastChomp = 0;

	/*
	 * moveDirection is an int that represents PacMan's direction 0 - not moving
	 * 1 - right 2 - left 3 - up 4 - down
	 */
	private int moveDirection = 0;
	private int pacX = 300;
	private int pacY = 300;
	private int pacDiameter = 20;
	private int pacDeltaX = 2;
	private int pacDeltaY = 2;

	public PacPanel() throws IOException {
		setBackground(Color.BLACK);

		setFocusable(true);
		addKeyListener(this);

		Image image = loadPacImage();

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

		System.out.println(moveDirection);
		if (moveDirection == 1) {
			pacX += pacDeltaX;
		} else if (moveDirection == 2) {
			pacX -= pacDeltaX;
		} else if (moveDirection == 3) {
			pacY -= pacDeltaY;
		} else if (moveDirection == 4) {
			pacY += pacDeltaY;
		}

		repaint();
	}

	public Image loadPacImage() throws IOException {
		AffineTransform at = new AffineTransform();
		File pacImg = new File("resources/PacImage.png");
		BufferedImage pacImage = ImageIO.read(pacImg);
		if (moveDirection == 1) {
			return pacImage;
		} else if (moveDirection == 2) {
			at.rotate(Math.PI, pacImage.getWidth() / 2, pacImage.getHeight() / 2);
			AffineTransformOp op = new AffineTransformOp(at, AffineTransformOp.TYPE_BILINEAR);
			pacImage = op.filter(pacImage, null);
			return pacImage;
		} else if (moveDirection == 3) {
			at.rotate(3 * Math.PI / 2, pacImage.getWidth() / 2, pacImage.getHeight() / 2);
			AffineTransformOp op = new AffineTransformOp(at, AffineTransformOp.TYPE_BILINEAR);
			pacImage = op.filter(pacImage, null);
			return pacImage;
		} else if (moveDirection == 4) {
			at.rotate(Math.PI / 2, pacImage.getWidth() / 2, pacImage.getHeight() / 2);
			AffineTransformOp op = new AffineTransformOp(at, AffineTransformOp.TYPE_BILINEAR);
			pacImage = op.filter(pacImage, null);
			return pacImage;
		}
		return pacImage;
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);

		if (!isChomping) {
			try {
				g.drawImage(loadPacImage(), pacX, pacY, pacDiameter, pacDiameter, this);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else if (isChomping) {
			g.setColor(Color.YELLOW);
			g.fillOval(pacX, pacY, pacDiameter, pacDiameter);
		}
	}

	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
			System.exit(0);
		}

		if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
			moveDirection = 1;
		} else if (e.getKeyCode() == KeyEvent.VK_LEFT) {
			moveDirection = 2;
		} else if (e.getKeyCode() == KeyEvent.VK_UP) {
			moveDirection = 3;
		} else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
			moveDirection = 4;
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
