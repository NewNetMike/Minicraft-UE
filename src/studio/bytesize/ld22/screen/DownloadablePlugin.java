package studio.bytesize.ld22.screen;

import studio.bytesize.ld22.gfx.Screen;

public class DownloadablePlugin
{

	public boolean enabled = true;
	public String name, filePath, simpleFileName;

	public DownloadablePlugin(String string, String filePath, String simpleFileName)
	{
		this.name = string;
		this.filePath = filePath;
		enabled = false;
		this.simpleFileName = simpleFileName;
	}

	public void renderInventory(Screen screen, int x, int y)
	{
		// Font.draw(plugin.getName(), screen, x, y, Color.get(0, 555, 555, 555));
	}

	public String getName()
	{
		return name;
	}
	
	public String getFilePath()
	{
		return filePath;
	}
	
	public String getSimpleFileName()
	{
		return simpleFileName;
	}

}
