package studio.bytesize.ld22.entity;

import java.util.List;

import studio.bytesize.ld22.Game;
import studio.bytesize.ld22.InputHandler;
import studio.bytesize.ld22.entity.particles.TextParticle;
import studio.bytesize.ld22.gfx.Color;
import studio.bytesize.ld22.gfx.Screen;
import studio.bytesize.ld22.item.FurnitureItem;
import studio.bytesize.ld22.item.Item;
import studio.bytesize.ld22.item.PowerGloveItem;
import studio.bytesize.ld22.item.ResourceItem;
import studio.bytesize.ld22.item.ToolItem;
import studio.bytesize.ld22.item.ToolType;
import studio.bytesize.ld22.item.resource.Resource;
import studio.bytesize.ld22.level.Level;
import studio.bytesize.ld22.level.tile.Tile;
import studio.bytesize.ld22.screen.InventoryMenu;
import studio.bytesize.ld22.sound.Sound;

public class Player extends Mob
{
	private InputHandler input;
	private int attackTime, attackDir;

	public Inventory inventory = new Inventory();
	public Game game;
	public Item activeItem;
	public Item attackItem;
	public int stamina;
	public int staminaRecharge;
	public int staminaRechargeDelay;
	public int score;
	public int maxStamina = 10;
	private int onStairDelay;
	public int invulnerableTime = 0;
	public int shirtColor = 220;

	public Player(Game game, InputHandler input)
	{
		this.input = input;
		x = y = 24;
		this.game = game;
		stamina = maxStamina;
	}

	public void initializeInventory()
	{
		// Default inventory
		inventory.add(new FurnitureItem(new Workbench()));
		inventory.add(new PowerGloveItem());

		// Debug inventory
		if (Game.debug)
		{
			inventory.add(new ToolItem(ToolType.shovel, 4));
			inventory.add(new ToolItem(ToolType.pickaxe, 4));
			inventory.add(new ResourceItem(Resource.get("arrow"), 105));
			inventory.add(new ResourceItem(Resource.get("cloth"), 99));
			inventory.add(new ResourceItem(Resource.get("wood"), 99));
			inventory.add(new ResourceItem(Resource.get("stone"), 99));
			inventory.add(new ResourceItem(Resource.get("daisy"), 99));
			inventory.add(new ResourceItem(Resource.get("rose"), 99));
			inventory.add(new ResourceItem(Resource.get("salvia"), 99));
			inventory.add(new ResourceItem(Resource.get("b.rose"), 99));
			inventory.add(new ResourceItem(Resource.get("r.mshrm"), 99));
			inventory.add(new ResourceItem(Resource.get("b.mshrm"), 99));
			inventory.add(new FurnitureItem(new Chest()));
			inventory.add(new FurnitureItem(new Anvil()));
			inventory.add(new FurnitureItem(new Furnace()));
			inventory.add(new FurnitureItem(new Lantern()));
			inventory.add(new FurnitureItem(new Oven()));
			inventory.add(new FurnitureItem(new Anvil()));
		}
	}

	public void tick()
	{
		super.tick();

		if (invulnerableTime > 0) invulnerableTime--;

		Tile onTile = level.getTile(x >> 4, y >> 4);
		if (onTile == Tile.get("stairsDown") || onTile == Tile.get("stairsUp"))
		{
			if (onStairDelay == 0)
			{
				changeLevel(onTile == Tile.get("stairsUp") ? 1 : -1);
				onStairDelay = 10;
				return;
			}
			onStairDelay = 10;
		}
		else
		{
			if (onStairDelay > 0)
			{
				onStairDelay--;
			}
		}

		if (stamina <= 0 && staminaRechargeDelay == 0 && staminaRecharge == 0)
		{
			staminaRechargeDelay = 40;
		}

		if (staminaRechargeDelay > 0)
		{
			staminaRechargeDelay--;
		}

		if (staminaRechargeDelay == 0)
		{
			staminaRecharge++;
			if (isSwimming())
			{
				staminaRecharge = 0;
			}
			while (staminaRecharge > maxStamina)
			{
				staminaRecharge -= 10;
				if (stamina < maxStamina) stamina++;
			}
		}

		// Moving the player
		int xa = 0;
		int ya = 0;

		if (input.up.down)
		{
			ya--;
		}
		if (input.down.down)
		{
			ya++;
		}
		if (input.left.down)
		{
			xa--;
		}
		if (input.right.down)
		{
			xa++;
		}

		if (isSwimming() && tickTime % 60 == 0)
		{
			if (stamina > 0) stamina--;
			else
			{
				hurt(this, 1, dir ^ 1);
			}
		}

		if (staminaRechargeDelay % 2 == 0)
		{
			move(xa, ya);
		}

		// Pressing the attack button
		if (input.attack.clicked)
		{
			if (stamina == 0)
			{

			}
			else
			{
				stamina--;
				staminaRecharge = 0;
				attack();
			}
		}

		// Pressing the menu button
		if (input.menu.clicked)
		{
			if (!use())
			{
				game.setMenu(new InventoryMenu(this));
			}
		}

		if (attackTime > 0) attackTime--;
	}

	private void attack()
	{
		walkDist += 8;
		attackDir = dir;

		attackItem = activeItem;
		boolean done = false;

		if (activeItem != null)
		{
			attackTime = 10;
			int yo = -2;
			int range = 12;

			// Interacting w/ entities inside of tiles within the player's attack zone
			if (dir == 0 && interact(x - 8, y + 4 + yo, x + 8, y + range + yo)) done = true;
			if (dir == 1 && interact(x - 8, y - range + yo, x + 8, y - 4 + yo)) done = true;
			if (dir == 3 && interact(x + 4, y - 8 + yo, x + range, y + 8 + yo)) done = true;
			if (dir == 2 && interact(x - range, y - 8 + yo, x - 4, y + 8 + yo)) done = true;
			if (done) return;

			// Interacts with the tile the player is facing
			int xt = x >> 4;
			int yt = (y + yo) >> 4;
			int r = 12;

			if (attackDir == 0) yt = (y + r + yo) >> 4;
			if (attackDir == 1) yt = (y - r + yo) >> 4;
			if (attackDir == 2) xt = (x - r) >> 4;
			if (attackDir == 3) xt = (x + r) >> 4;

			if (xt >= 0 && yt >= 0 && xt < level.w && yt < level.h)
			{
				if (activeItem.interactOn(level.getTile(xt, yt), level, xt, yt, this, attackDir))
				{
					done = true;
				}
				else
				{
					if (level.getTile(xt, yt).interact(level, xt, yt, this, activeItem, attackDir))
					{
						done = true;
					}
				}

				if (activeItem.isDepleted())
				{
					activeItem = null;
				}
			}
		}

		if (done) return;

		if (activeItem == null || activeItem.canAttack()) // If we have a bare hand
		{
			attackTime = 5;
			int yo = -2;
			int range = 20;

			// Hurts entities inside of tiles within the player's attack zone
			if (dir == 0)
			{
				hurt(x - 8, y + 4 + yo, x + 8, y + range + yo);
			}
			if (dir == 1)
			{
				hurt(x - 8, y - range + yo, x + 8, y - 4 + yo);
			}
			if (dir == 3)
			{
				hurt(x + 4, y - 8 + yo, x + range, y + 8 + yo);
			}
			if (dir == 2)
			{
				hurt(x - range, y - 8 + yo, x - 4, y + 8 + yo);
			}

			// Hurts the tile the player is facing
			int xt = x >> 4;
			int yt = (y + yo) >> 4;
			int r = 12;

			if (attackDir == 0) yt = (y + r + yo) >> 4;
			if (attackDir == 1) yt = (y - r + yo) >> 4;
			if (attackDir == 2) xt = (x - r) >> 4;
			if (attackDir == 3) xt = (x + r) >> 4;

			if (xt >= 0 && yt >= 0 && xt < level.w && yt < level.h)
			{
				level.getTile(xt, yt).hurt(level, xt, yt, this, random.nextInt(3) + 1, attackDir);
			}
		}

	}

	// Using menu button
	private boolean use()
	{
		int yo = -2;

		if (dir == 0 && use(x - 8, y + 4 + yo, x + 8, y + 12 + yo)) return true;
		if (dir == 1 && use(x - 8, y - 12 + yo, x + 8, y - 4 + yo)) return true;
		if (dir == 3 && use(x + 4, y - 8 + yo, x + 12, y + 8 + yo)) return true;
		if (dir == 2 && use(x - 12, y - 8 + yo, x - 4, y + 8 + yo)) return true;

		int xt = x >> 4;
		int yt = (y + yo) >> 4;
		int r = 12;

		if (attackDir == 0) yt = (y + r + yo) >> 4;
		if (attackDir == 1) yt = (y - r + yo) >> 4;
		if (attackDir == 2) xt = (x - r) >> 4;
		if (attackDir == 3) xt = (x + r) >> 4;

		if (xt >= 0 && yt >= 0 && xt < level.w && yt < level.h)
		{
			if (level.getTile(xt, yt).use(level, xt, yt, this, attackDir))
			{
				return true;
			}
		}

		return false;
	}

	// Usong menu button on entities
	private boolean use(int x0, int y0, int x1, int y1)
	{
		List<Entity> entities = level.getEntities(x0, y0, x1, y1);
		for (int i = 0; i < entities.size(); i++)
		{
			Entity e = entities.get(i);
			if (e != this) if (e.use(this, attackDir)) return true;
		}

		return false;
	}

	// Using the active item to interact with entities
	private boolean interact(int x0, int y0, int x1, int y1)
	{
		List<Entity> entities = level.getEntities(x0, y0, x1, y1);
		for (int i = 0; i < entities.size(); i++)
		{
			Entity e = entities.get(i);
			if (e != this) if (e.interact(this, activeItem, attackDir)) return true;
		}
		return false;
	}

	// Hurting enemies with bare hand (random number damage)
	private void hurt(int x0, int y0, int x1, int y1)
	{
		List<Entity> entities = level.getEntities(x0, y0, x1, y1);
		for (int i = 0; i < entities.size(); i++)
		{
			Entity e = entities.get(i);
			if (e != this) entities.get(i).hurt(this, getAttackDamage(e), attackDir);
		}
	}

	private int getAttackDamage(Entity e)
	{
		int dmg = random.nextInt(3) + 1;
		if (attackItem != null)
		{
			dmg += attackItem.getAttackDamageBonus(e);
		}

		return dmg;

	}

	public void render(Screen screen)
	{
		// Drawing the player
		int xt = 0;
		int yt = 14;
		int flip1 = (walkDist >> 3) & 1;
		int flip2 = (walkDist >> 3) & 1;

		if (dir == 1)
		{
			xt += 2;
		}

		if (dir > 1)
		{
			flip1 = 0;
			flip2 = ((walkDist >> 4) & 1);

			if (dir == 2)
			{
				flip1 = 1;
			}

			xt = 4 + ((walkDist >> 3) & 1) * 2;
		}

		int xo = x - 8;
		int yo = y - 11;
		if (isSwimming())
		{
			yo += 4;
			int waterColor = Color.get(-1, -1, 115, 115);
			if (tickTime / 8 % 2 == 0)
			{
				waterColor = Color.get(-1, 335, 5, 115);
			}
			screen.render(xo + 0, yo + 3, 5 + 13 * 32, waterColor, 0);
			screen.render(xo + 8, yo + 3, 5 + 13 * 32, waterColor, 1);

		}

		// Rendering attack thing-a-ma-bob
		if (attackTime > 0 && attackDir == 1)
		{
			screen.render(xo + 0, yo - 4, 6 + 13 * 32, Color.get(-1, 555, 555, 555), 0);
			screen.render(xo + 8, yo - 4, 6 + 13 * 32, Color.get(-1, 555, 555, 555), 1);

			if (attackItem != null)
			{
				attackItem.renderIcon(screen, xo + 4, yo - 4); // Rendering item icon
			}
		}

		// Rendering the player
		int col = Color.get(-1, 100, shirtColor, 532);
		if (hurtTime > 0)
		{
			col = Color.get(-1, 555, 555, 555);
		}

		if (activeItem instanceof FurnitureItem)
		{
			yt += 2;
		}

		screen.render(xo + 8 * flip1, yo + 0, xt + yt * 32, col, flip1);
		screen.render(xo + 8 - 8 * flip1, yo + 0, xt + 1 + yt * 32, col, flip1);

		if (!isSwimming()) // If we're in water, don't render lower half of the player
		{
			screen.render(xo + 8 * flip2, yo + 8, xt + (yt + 1) * 32, col, flip2);
			screen.render(xo + 8 - 8 * flip2, yo + 8, xt + 1 + (yt + 1) * 32, col, flip2);
		}

		// Rendering attack thing-a-ma-bob
		if (attackTime > 0 && attackDir == 2)
		{
			screen.render(xo - 4, yo, 7 + 13 * 32, Color.get(-1, 555, 555, 555), 1);
			screen.render(xo - 4, yo + 8, 7 + 13 * 32, Color.get(-1, 555, 555, 555), 3);

			if (attackItem != null)
			{
				attackItem.renderIcon(screen, xo - 4, yo + 4); // Rendering item icon
			}
		}

		// Rendering attack thing-a-ma-bob
		if (attackTime > 0 && attackDir == 3)
		{
			screen.render(xo + 8 + 4, yo, 7 + 13 * 32, Color.get(-1, 555, 555, 555), 0);
			screen.render(xo + 8 + 4, yo + 8, 7 + 13 * 32, Color.get(-1, 555, 555, 555), 2);

			if (attackItem != null)
			{
				attackItem.renderIcon(screen, xo + 8 + 4, yo + 4); // Rendering item icon
			}
		}

		// Rendering attack thing-a-ma-bob
		if (attackTime > 0 && attackDir == 0)
		{
			screen.render(xo + 0, yo + 8 + 4, 6 + 13 * 32, Color.get(-1, 555, 555, 555), 2);
			screen.render(xo + 8, yo + 8 + 4, 6 + 13 * 32, Color.get(-1, 555, 555, 555), 3);

			if (attackItem != null)
			{
				attackItem.renderIcon(screen, xo + 4, yo + 8 + 4); // Rendering item icon
			}
		}

		if (activeItem instanceof FurnitureItem)
		{
			Furniture furniture = ((FurnitureItem)activeItem).furniture;
			furniture.x = x;
			furniture.y = yo;
			furniture.render(screen);
		}
	}

	// Colliding with an ItemEntity AKA picking up loot
	public void touchItem(ItemEntity itemEntity)
	{
		inventory.add(itemEntity.item);
		itemEntity.take(this);
	}

	public boolean canSwim()
	{
		return true;
	}

	// validates a starting position so player doesn't spawn inside of walls
	public boolean findStartPos(Level level)
	{
		while (true)
		{
			int x = random.nextInt(level.w);
			int y = random.nextInt(level.h);
			if (level.getTile(x, y) == Tile.get("grass") || level.getTile(x, y) == Tile.get("dirt"))
			{
				this.x = x * 16 + 8;
				this.y = y * 16 + 8;

				return true;
			}
		}

	}

	public boolean payStamina(int cost)
	{
		if (cost > stamina) return false;
		stamina -= cost;
		return true;
	}

	public void changeLevel(int dir)
	{
		game.scheduleLevelChange(dir);
	}

	public int getLightRadius()
	{
		int r = 3;
		if (activeItem != null)
		{
			if (activeItem instanceof FurnitureItem)
			{
				int rr = ((FurnitureItem)activeItem).furniture.getLightRadius();
				if (rr > r) r = rr;
			}
		}
		return r;
	}

	protected void die()
	{
		super.die();
		Sound.play("playerDeath");
	}

	protected void touchedBy(Entity entity)
	{
		if (!(entity instanceof Player))
		{
			entity.touchedBy(this);
		}
	}

	protected void doHurt(int dmg, int attackDir)
	{
		// Removes health, adds particles, and sets knockback
		if (hurtTime > 0 || invulnerableTime > 0) return;

		Sound.play("playerHurt");

		level.add(new TextParticle("" + dmg, x, y, Color.get(-1, 504, 504, 504)));
		health -= dmg;

		if (attackDir == 0) yKnockback = 6;
		if (attackDir == 1) yKnockback = -6;
		if (attackDir == 2) xKnockback = -6;
		if (attackDir == 3) xKnockback = 6;
		hurtTime = 10;
		invulnerableTime = 30;
	}

	public void gameWon()
	{
		level.player.invulnerableTime = 60 * 5;
		game.won();
	}
}
