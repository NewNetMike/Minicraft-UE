package studio.bytesize.ld22.level.levelgen;

import studio.bytesize.ld22.level.Level;

public interface CustomLevelGen
{
	public void go(Level level); // the level gen code
	public int getLevel(); // the level number the code applies to
}
