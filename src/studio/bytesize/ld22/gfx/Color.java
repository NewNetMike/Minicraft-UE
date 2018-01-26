package studio.bytesize.ld22.gfx;

public class Color
{
	// Returns an int of all four ints cleverly combined
	public static int get(int a, int b, int c, int d)
	{
		return (get(d) << 24) + (get(c) << 16) + (get(b) << 8) + (get(a));
	}

	public static int get(int colour)
	{
		if (colour < 0) return 255;
		int r = colour / 100 % 10;
		int g = colour / 10 % 10;
		int b = colour % 10;
		return r * 36 + g * 6 + b;
	}
}
