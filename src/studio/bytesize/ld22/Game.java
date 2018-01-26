package studio.bytesize.ld22;

import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Collection;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

import studio.bytesize.helper.MyOS;
import studio.bytesize.helper.MyXML;
import studio.bytesize.ld22.entity.Player;
import studio.bytesize.ld22.gfx.Color;
import studio.bytesize.ld22.gfx.Font;
import studio.bytesize.ld22.gfx.Screen;
import studio.bytesize.ld22.gfx.SpriteSheet;
import studio.bytesize.ld22.level.Level;
import studio.bytesize.ld22.level.tile.Tile;
import studio.bytesize.ld22.plugin.VanilllaPlugin;
import studio.bytesize.ld22.screen.AboutMenu;
import studio.bytesize.ld22.screen.DeadMenu;
import studio.bytesize.ld22.screen.DownloadMenu;
import studio.bytesize.ld22.screen.FirstMenu;
import studio.bytesize.ld22.screen.InstructionsMenu;
import studio.bytesize.ld22.screen.LevelTransitionMenu;
import studio.bytesize.ld22.screen.Menu;
import studio.bytesize.ld22.screen.OpeningMenu;
import studio.bytesize.ld22.screen.RestartMenu;
import studio.bytesize.ld22.screen.TitleMenu;
import studio.bytesize.ld22.screen.UpdateMenu;
import studio.bytesize.ld22.screen.WonMenu;

import net.xeoh.plugins.base.PluginManager;
import net.xeoh.plugins.base.impl.PluginManagerFactory;
import net.xeoh.plugins.base.util.PluginManagerUtil;

public class Game extends Canvas implements Runnable
{
	private static final long serialVersionUID = 1L;
	
	public static final String CURRENT_VERSION = "0.1.0";

	// Usually it's only dark underground, but plugins can override this using a darkness checker
	public static DarknessChecker bonusDarknessChecker = null;

	// Game constants
	public final static int WIDTH = 160;
	public final static int HEIGHT = 120;
	public final static int SCALE = 3;
	public final static Dimension DIMENSIONS = new Dimension(WIDTH * SCALE, HEIGHT * SCALE);
	public final static String NAME = "Minicraft: Ultimate Edition";
	public static ArrayList<MinicraftPlugin> plugins;
	public JFrame frame;

	// Important game variables
	private BufferedImage image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
	private int[] pixels = ((DataBufferInt)image.getRaster().getDataBuffer()).getData();
	private int[] colors = new int[256];
	private boolean running = false;
	public static boolean debug = false; // Show debug window info and spawn with bonus items

	// Important game objects
	private InputHandler input = new InputHandler(this);
	public Screen screen;
	public Screen lightScreen;
	public Level level;
	private Level[] levels = new Level[5];
	private int currentLevel = 3;
	public Player player;
	public Menu menu;
	private int playerDeadTime;
	private int pendingLevelChange;
	private int wonTimer = 0;
	public boolean hasWon = false;
	public int tickCount = 0;
	public int gameTime = 0;

	public void setMenu(Menu menu)
	{
		this.menu = menu;
		if (menu != null) menu.init(this, input);
	}

	public void start()
	{
		running = true;
		new Thread(this).start();
	}

	public void stop()
	{
		running = false;
	}

	public void run()
	{
		init();

		long lastTime = System.nanoTime();
		double unprocessed = 0;
		double nsPerTick = 1000000000.0 / 60.0; // 60 FPS
		int frames = 0;
		int ticks = 0;
		long lastTimer1 = System.currentTimeMillis();

		// Game Loop
		while (running)
		{
			long now = System.nanoTime();
			unprocessed += (now - lastTime) / nsPerTick;
			lastTime = now;
			boolean shouldRender = true;

			while (unprocessed >= 1)
			{
				ticks++;
				tick();
				unprocessed -= 1;
				shouldRender = true;
			}

			try
			{
				Thread.sleep(1);
			}
			catch (InterruptedException e)
			{
				e.printStackTrace();
			}

			if (shouldRender)
			{
				frames++;
				render();
			}

			if (System.currentTimeMillis() - lastTimer1 > 1000)
			{
				lastTimer1 += 1000;
				if (debug) frame.setTitle(Game.NAME + " - UPS: " + ticks + ", FPS: " + frames);
				frames = 0;
				ticks = 0;
			}
		}
	}

	public void resetGame()
	{
		playerDeadTime = 0;
		wonTimer = 0;
		gameTime = 0;
		hasWon = false;

		levels = new Level[5];
		currentLevel = 3;

		levels[4] = new Level(128, 128, 1, null);
		levels[3] = new Level(128, 128, 0, levels[4]);
		levels[2] = new Level(128, 128, -1, levels[3]);
		levels[1] = new Level(128, 128, -2, levels[2]);
		levels[0] = new Level(128, 128, -3, levels[1]);

		level = levels[currentLevel];

		// Creating the player and validating start position
		player = new Player(this, input);
		player.findStartPos(level);
		level.add(player);
		player.initializeInventory();

		for (int i = 0; i < 5; i++)
		{
			levels[i].trySpawn(5000); // Initialize level with mob spawns
		}
	}

	public void startGameForTheFirstTime()
	{
		plugins.add(0, new VanilllaPlugin()); // Regular Minicraft

		// Load the active plugins that weren't disabled in Plugin Menu
		for (MinicraftPlugin plugin : plugins)
		{
			plugin.onLoad(this);
			System.out.println("Loaded:\"" + plugin.getName() + "\"");
		}

		// Displays the Main Menu Screen
		setMenu(new TitleMenu());
	}

	public void init()
	{
		// Setting up colors
		int pp = 0;
		for (int r = 0; r < 6; r++)
		{
			for (int g = 0; g < 6; g++)
			{
				for (int b = 0; b < 6; b++)
				{
					int rr = (r * 255 / 5);
					int gg = (g * 255 / 5);
					int bb = (b * 255 / 5);
					int mid = (rr * 30 + gg * 59 + bb * 11) / 100;

					int r1 = ((rr + mid * 1) / 2) * 230 / 255 + 10;
					int g1 = ((gg + mid * 1) / 2) * 230 / 255 + 10;
					int b1 = ((bb + mid * 1) / 2) * 230 / 255 + 10;
					colors[pp++] = r1 << 16 | g1 << 8 | b1;
				}
			}
		}

		lightScreen = new Screen(WIDTH, HEIGHT);
		try
		{
			screen = new Screen(Game.WIDTH, Game.HEIGHT, new SpriteSheet(ImageIO.read(Game.class.getResource("/icons.png"))));
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		plugins = new ArrayList<MinicraftPlugin>();

		String appDataRoaming = MyOS.getAppdataLocation();

		File file = new File(appDataRoaming + "/.minicraft");
		if (!file.exists())
		{
			file.mkdir();
			System.out.println("Created .minicraft Folder");
		}
		else
		{
			System.out.println("Located .minicraft Folder");
		}
		
		File ff = new File(MyOS.getAppdataLocation() + "/.minicraft/tmp/");
		if(ff.isDirectory())
		{
		    File[] content = ff.listFiles();
		    for(int i = 0; i < content.length; i++)
		    {
		    	try
				{
		    		Path src = Paths.get(ff.getAbsolutePath() + "/" + content[i].getName());
		    		Path dest = Paths.get(file.getAbsolutePath() + "/" +content[i].getName());
		    		
					Files.copy(src, dest, StandardCopyOption.REPLACE_EXISTING);
				}
				catch (IOException e)
				{
					e.printStackTrace();
				}
		    }
		}
		deleteDir(ff);

		URI f = new File(file.getAbsolutePath() + "/").toURI();
		PluginManager pm = PluginManagerFactory.createPluginManager();
		pm.addPluginsFrom(f);
		PluginManagerUtil pmM = new PluginManagerUtil(pm);
		Collection<MinicraftPlugin> temp = pmM.getPlugins(MinicraftPlugin.class);
		plugins.addAll(temp);

		MyXML.initXML();
		
		player = new Player(this, input);
		setMenu(new OpeningMenu(this));
	}
	
	void deleteDir(File file)
	{
	    File[] contents = file.listFiles();
	    if (contents != null)
	    {
	        for (File f : contents)
	        {
	            deleteDir(f);
	        }
	    }
	    file.delete();
	}
	
	public void deletePlugins()
	{
		plugins.clear();
	}

	public void tick()
	{

		tickCount++;
		if (!hasFocus()) // If we don't have focus release the keys, otherwise input can get stuck
		{
			input.releaseAll();
		}
		else
		{
			if (!player.removed && !hasWon) gameTime++;

			input.tick();

			if (menu != null)
			{
				menu.tick();
			}
			else
			{
				if (player.removed)
				{
					playerDeadTime++;
					if (playerDeadTime > 60)
					{
						setMenu(new DeadMenu());
					}
				}
				else
				{
					if (pendingLevelChange != 0)
					{
						setMenu(new LevelTransitionMenu(pendingLevelChange));
						pendingLevelChange = 0;
					}
				}
				if (wonTimer > 0)
				{
					if (--wonTimer == 0)
					{
						setMenu(new WonMenu());
					}
				}
				level.tick();
				Tile.tickCount++;
			}

		}

	}

	public void changeLevel(int dir)
	{
		level.remove(player);
		currentLevel += dir;
		level = levels[currentLevel];
		player.x = (player.x >> 4) * 16 + 8;
		player.y = (player.y >> 4) * 16 + 8;
		level.add(player);
	}

	public void render()
	{
		// Setting up the BufferStrategy
		BufferStrategy bs = getBufferStrategy();
		if (bs == null)
		{
			createBufferStrategy(3);
			requestFocus();
			return;
		}

		// If we're on certain menu screens, don't bother rendering anything besides the menu
		if (menu != null && (menu instanceof FirstMenu || menu instanceof TitleMenu || menu instanceof AboutMenu || menu instanceof InstructionsMenu
				|| menu instanceof DownloadMenu || menu instanceof RestartMenu || menu instanceof OpeningMenu
				|| menu instanceof UpdateMenu))
		{
			renderMenu();

			// Drawing the screen pixels to the game
			for (int y = 0; y < screen.h; y++)
			{
				for (int x = 0; x < screen.w; x++)
				{
					pixels[x + y * WIDTH] = colors[screen.pixels[x + y * screen.w]];
				}
			}

			// Rendering the screen
			Graphics g = bs.getDrawGraphics();
			g.fillRect(0, 0, getWidth(), getHeight());

			int ww = WIDTH * SCALE;
			int hh = HEIGHT * SCALE;
			int xo = (getWidth() - ww) / 2;
			int yo = (getHeight() - hh) / 2;

			g.drawImage(image, xo, yo, ww, hh, null);
			g.dispose();
			bs.show();
			return;
		}

		// Rendering the background tiles with proper offsets
		int xScroll = player.x - screen.w / 2;
		int yScroll = player.y - (screen.h - 8) / 2;
		if (xScroll < 16) xScroll = 16;
		if (yScroll < 16) yScroll = 16;
		if (xScroll > level.w * 16 - screen.w) xScroll = level.w * 16 - screen.w - 16;
		if (yScroll > level.h * 16 - screen.h) yScroll = level.h * 16 - screen.h - 16;

		// Rendering the sky world properly
		if (currentLevel > 3)
		{
			int col = Color.get(20, 20, 121, 121);
			for (int y = 0; y < 14; y++)
			{
				for (int x = 0; x < 24; x++)
				{
					screen.render(x * 8 - ((xScroll / 4) & 7), y * 8 - ((yScroll / 4) & 7), 0, col, 0);
				}
			}
		}

		level.renderBackground(screen, xScroll, yScroll);
		level.renderSprites(screen, xScroll, yScroll);

		// Determining if we should render darkness - can be overriden by plugins via Darkness Checker
		if (currentLevel < 3 || (bonusDarknessChecker != null && bonusDarknessChecker.isDark()))
		{
			lightScreen.clear(0);
			level.renderLight(lightScreen, xScroll, yScroll);
			screen.overlay(lightScreen, xScroll, yScroll);
		}
		renderGui();

		// If the game window isn't focused display a message!
		if (!this.hasFocus()) renderFocusNagger();

		// Drawing the screen pixels to the game
		for (int y = 0; y < screen.h; y++)
		{
			for (int x = 0; x < screen.w; x++)
			{
				pixels[x + y * WIDTH] = colors[screen.pixels[x + y * screen.w]];
			}
		}

		// Rendering the screen
		Graphics g = bs.getDrawGraphics();
		g.fillRect(0, 0, getWidth(), getHeight());

		int ww = WIDTH * SCALE;
		int hh = HEIGHT * SCALE;
		int xo = (getWidth() - ww) / 2;
		int yo = (getHeight() - hh) / 2;

		g.drawImage(image, xo, yo, ww, hh, null);
		g.dispose();
		bs.show();
	}

	private void renderGui()
	{
		// Black bar at the bottom of the screen
		for (int y = 0; y < 2; y++)
		{
			for (int x = 0; x < 20; x++)
			{
				screen.render(x * 8, screen.h - 16 + y * 8, 0 + 12 * 32, Color.get(000, 000, 000, 000), 0);
			}
		}

		// Player hearts & energy
		for (int i = 0; i < 10; i++)
		{
			if (i < player.health) screen.render(i * 8, screen.h - 16, 0 + 12 * 32, Color.get(000, 200, 500, 533), 0);
			else screen.render(i * 8, screen.h - 16, 0 + 12 * 32, Color.get(000, 100, 000, 000), 0);

			if (player.staminaRechargeDelay > 0)
			{
				if (player.staminaRechargeDelay / 4 % 2 == 0) screen.render(i * 8, screen.h - 8, 1 + 12 * 32, Color.get(000, 555, 000, 000), 0);
				else screen.render(i * 8, screen.h - 8, 1 + 12 * 32, Color.get(000, 110, 000, 000), 0);
			}
			else
			{
				if (i < player.stamina) screen.render(i * 8, screen.h - 8, 1 + 12 * 32, Color.get(000, 220, 550, 553), 0);
				else screen.render(i * 8, screen.h - 8, 1 + 12 * 32, Color.get(000, 110, 000, 000), 0);
			}
		}

		// Player's currently equipped item
		if (player.activeItem != null)
		{
			player.activeItem.renderActive(screen, 10 * 8, screen.h - 16);
		}

		renderMenu();
	}

	public void renderMenu()
	{
		// Any other menu (inventory, crafting, etc)
		if (menu != null)
		{
			menu.render(screen);
		}
	}

	private void renderFocusNagger()
	{
		String msg = "Click to focus!";
		int xx = (WIDTH - msg.length() * 8) / 2;
		int yy = (HEIGHT - 8) / 2;
		int w = msg.length();
		int h = 1;

		// Rendering the four corners of message box
		screen.render(xx - 8, yy - 8, 0 + 13 * 32, Color.get(-1, 1, 5, 445), 0);
		screen.render(xx + w * 8, yy - 8, 0 + 13 * 32, Color.get(-1, 1, 5, 445), 1);
		screen.render(xx - 8, yy + 8, 0 + 13 * 32, Color.get(-1, 1, 5, 445), 2);
		screen.render(xx + w * 8, yy + 8, 0 + 13 * 32, Color.get(-1, 1, 5, 445), 3);

		// Rendering top and bottom of message box
		for (int x = 0; x < w; x++)
		{
			screen.render(xx + x * 8, yy - 8, 1 + 13 * 32, Color.get(-1, 1, 5, 445), 0);
			screen.render(xx + x * 8, yy + 8, 1 + 13 * 32, Color.get(-1, 1, 5, 445), 2);
		}

		// Rendering left and right of message box
		for (int y = 0; y < h; y++)
		{
			screen.render(xx - 8, yy + y * 8, 2 + 13 * 32, Color.get(-1, 1, 5, 445), 0);
			screen.render(xx + w * 8, yy + y * 8, 2 + 13 * 32, Color.get(-1, 1, 5, 445), 1);
		}

		// Makes text flash white / grey
		if ((tickCount / 20) % 2 == 0)
		{
			Font.draw(msg, screen, xx, yy, Color.get(5, 333, 333, 333));
		}
		else
		{
			Font.draw(msg, screen, xx, yy, Color.get(5, 555, 555, 555));
		}
	}

	public void scheduleLevelChange(int dir)
	{
		pendingLevelChange = dir;
	}

	public void won()
	{
		wonTimer = 60 * 3;
		hasWon = true;
	}
}
