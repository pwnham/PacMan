import java.awt.BorderLayout;
import java.io.IOException;

import javax.swing.JFrame;

public class Driver {

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		JFrame frame = new JFrame("Flappy Bird");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLayout(new BorderLayout());

		PacPanel pacpanel = new PacPanel();
		frame.add(pacpanel, BorderLayout.CENTER);

		frame.setVisible(true);
		frame.setSize(716, 739);
	}

}
