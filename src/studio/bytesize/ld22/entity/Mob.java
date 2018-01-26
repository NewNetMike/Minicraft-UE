package studio.bytesize.ld22.entity;

import studio.bytesize.ld22.entity.particles.TextParticle;
import studio.bytesize.ld22.gfx.Color;
import studio.bytesize.ld22.level.Level;
import studio.bytesize.ld22.level.tile.Tile;
import studio.bytesize.ld22.sound.Sound;

public class Mob extends Entity
{
	protected int walkDist = 0;
	public int dir = 0;
	public int hurtTime = 0;
	protected int xKnockback, yKnockback;
	public int maxHealth = 10;
	public int health = maxHealth;
	public int swimTimer = 0;
	public int tickTime = 0;
	public int spawnChance = 1;
	public int lvl;

	public Mob()
	{
		x = y = 8;
		xr = 4;
		yr = 3;
	}

	public void setLvl(int lvl)
	{

	}

	public void tick()
	{
		tickTime++;
		if (level.getTile(x >> 4, y >> 4) == Tile.get("lava"))
		{
			hurt(this, 4, dir ^ 1);
		}

		if (health <= 0)
		{
			die();
		}

		if (hurtTime > 0) hurtTime--;
	}

	protected void die()
	{
		remove();
	}

	public boolean move(int xa, int ya)
	{
		if (isSwimming())
		{
			if (swimTimer++ % 2 == 0) return true;
		}

		if (xKnockback < 0)
		{
			move2(-1, 0);
			xKnockback++;
		}
		if (xKnockback > 0)
		{
			move2(1, 0);
			xKnockback--;
		}
		if (yKnockback < 0)
		{
			move2(0, -1);
			yKnockback++;
		}
		if (yKnockback > 0)
		{
			move2(0, 1);
			yKnockback--;
		}

		if (hurtTime > 0) return true;

		if (xa != 0 || ya != 0)
		{
			walkDist++;
			if (xa < 0) dir = 2;
			if (xa > 0) dir = 3;
			if (ya < 0) dir = 1;
			if (ya > 0) dir = 0;
		}

		return super.move(xa, ya);
	}

	protected boolean isSwimming()
	{
		Tile tile = level.getTile(x >> 4, y >> 4);
		return tile == Tile.get("water") || tile == Tile.get("lava");
	}

	public boolean blocks(Entity e)
	{
		return e.isBlockableBy(this);
	}

	public void hurt(Mob mob, int dmg, int attackDir)
	{
		doHurt(dmg, attackDir);
	}

	protected void doHurt(int dmg, int attackDir)
	{
		// Removes health, adds particles, and sets knockback
		if (hurtTime > 0) return;

		if (level.player != null)
		{
			int xd = level.player.x - x;
			int yd = level.player.y - y;

			if (xd * xd + yd * yd < 85 * 85)
			{
				Sound.play("monsterHurt");
			}
		}

		level.add(new TextParticle("" + dmg, x, y, Color.get(-1, 500, 500, 500)));
		health -= dmg;

		if (attackDir == 0) yKnockback = 6;
		if (attackDir == 1) yKnockback = -6;
		if (attackDir == 2) xKnockback = -6;
		if (attackDir == 3) xKnockback = 6;
		hurtTime = 10;
	}

	public void heal(int heal)
	{
		// Removes health, adds particles, and sets knockback
		if (hurtTime > 0) return;

		level.add(new TextParticle("" + heal, x, y, Color.get(-1, 50, 50, 50)));
		health += heal;

		if (health > maxHealth) health = maxHealth;
	}

	public void hurt(Tile tile, int x, int y, int dmg)
	{
		int attackDir = dir ^ 1;
		doHurt(dmg, attackDir);
	}

	// validates a starting position so mobs don't spawn inside of walls
	public boolean findStartPos(Level level)
	{
		int x = random.nextInt(level.w);
		int y = random.nextInt(level.h);
		int xx = x * 16 + 8;
		int yy = y * 16 + 8;

		if (level.player != null)
		{
			int xd = level.player.x - xx;
			int yd = level.player.y - yy;
			if (xd * xd + yd * yd < 80 * 80) return false;
		}

		int r = level.monsterDensity * 16;
		if (level.getEntities(xx - r, yy - r, xx + r, yy + r).size() > 0) return false;

		if (level.getTile(x, y).mayPass(level, x, y, this))
		{
			this.x = xx;
			this.y = yy;
			return true;
		}

		return false;
	}

	public boolean canSpawn(int level)
	{
		return false;
	}
}
