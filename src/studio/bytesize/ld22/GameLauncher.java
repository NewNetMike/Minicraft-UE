package studio.bytesize.ld22;

import java.applet.Applet;
import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

public class GameLauncher extends Applet
{
	private static final long serialVersionUID = 1L;
	private static Game game = new Game();

	public void init()
	{
		setLayout(new BorderLayout());
		add(game, BorderLayout.CENTER);
		setMaximumSize(Game.DIMENSIONS);
		setMinimumSize(Game.DIMENSIONS);
		setPreferredSize(Game.DIMENSIONS);
	}

	public void start()
	{
		game.start();
	}

	public void stop()
	{
		game.stop();
	}

	public static void main(String[] args)
	{
		SwingUtilities.invokeLater(new Runnable()
		{
			public void run()
			{
				// Setting canvas properties
				game.setMinimumSize(Game.DIMENSIONS);
				game.setMaximumSize(Game.DIMENSIONS);
				game.setPreferredSize(Game.DIMENSIONS);

				// Setting up JFrame
				game.frame = new JFrame(Game.NAME);
				game.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				game.frame.setLayout(new BorderLayout());

				// Adding the canvas to the JFrame
				game.frame.add(game, BorderLayout.CENTER);
				game.frame.pack();
				game.frame.setResizable(false);
				game.frame.setLocationRelativeTo(null);
				game.frame.setVisible(true);

				game.start();
			}
		});
	}
}
