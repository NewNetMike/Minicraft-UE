package studio.bytesize.ld22.entity;

import studio.bytesize.ld22.gfx.Color;

public class Lantern extends Furniture
{
	public Lantern()
	{
		super("Lantern");
		col = Color.get(-1, 000, 111, 555);
		sprite = 5;
		xr = 4;
		yr = 2;
	}

	public int getLightRadius()
	{
		return 8;
	}

}
