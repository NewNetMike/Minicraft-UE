package studio.bytesize.ld22.screen;

import studio.bytesize.helper.MyXML;
import studio.bytesize.ld22.Game;
import studio.bytesize.ld22.gfx.Color;
import studio.bytesize.ld22.gfx.Font;
import studio.bytesize.ld22.gfx.Screen;
import studio.bytesize.ld22.sound.Sound;

public class OpeningMenu extends Menu
{
	Game game;
	public OpeningMenu(Game game)
	{
		this.game = game;
		Sound.load("pluginsSelected", this.getClass().getResource("/pluginsSelected.wav"));
	}

	public void tick()
	{
		if (input.attack.clicked || input.menu.clicked)
		{
			Sound.play("pluginsSelected");
			
			if(!MyXML.getReleaseVersion().equals(Game.CURRENT_VERSION))
			{
				game.setMenu(new UpdateMenu());
			}
			else
			{
				game.setMenu(new FirstMenu(game));
			}
		}
	}

	public void render(Screen screen)
	{
		screen.clear(0);

		Font.draw("CONTROLS:", screen, 0 * 8 + 4, 5 * 8, Color.get(0, 333, 333, 333));
		Font.draw("arrow keys,x,and c", screen, 0 * 8 + 4, 6 * 8, Color.get(0, 333, 333, 333));
		Font.draw("Press C to continue", screen, 0 * 8 + 4, 9 * 8, Color.get(0, 333, 333, 333));
	}
}
