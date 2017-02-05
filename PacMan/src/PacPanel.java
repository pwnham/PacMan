import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;
import javax.swing.Timer;

public class PacPanel extends JPanel implements ActionListener {

	private boolean isChomping = false;
	private int timeSinceLastChomp = 0;

	private int pacX = 300;
	private int pacY = 300;
	private int pacDiameter = 20;
	private int pacDeltaX = 0;
	private int pacDeltaY = 0;

	public PacPanel() throws IOException {
		setBackground(Color.BLACK);

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

		repaint();
	}

	public Image loadPacImage() throws IOException {
		File pacImg = new File("resources/PacImage.png");
		final BufferedImage pacImage = ImageIO.read(pacImg);
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

}
