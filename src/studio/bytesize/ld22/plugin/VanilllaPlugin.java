package studio.bytesize.ld22.plugin;

import studio.bytesize.ld22.Game;
import studio.bytesize.ld22.MinicraftPlugin;
import studio.bytesize.ld22.crafting.Crafting;
import studio.bytesize.ld22.crafting.FurnitureRecipe;
import studio.bytesize.ld22.crafting.ResourceRecipe;
import studio.bytesize.ld22.crafting.ToolRecipe;
import studio.bytesize.ld22.entity.Anvil;
import studio.bytesize.ld22.entity.Chest;
import studio.bytesize.ld22.entity.Furnace;
import studio.bytesize.ld22.entity.Lantern;
import studio.bytesize.ld22.entity.Oven;
import studio.bytesize.ld22.entity.Slime;
import studio.bytesize.ld22.entity.Workbench;
import studio.bytesize.ld22.entity.Zombie;
import studio.bytesize.ld22.gfx.Color;
import studio.bytesize.ld22.item.ToolType;
import studio.bytesize.ld22.item.resource.FoodResource;
import studio.bytesize.ld22.item.resource.PlantableResource;
import studio.bytesize.ld22.item.resource.Resource;
import studio.bytesize.ld22.level.Level;
import studio.bytesize.ld22.level.tile.CactusTile;
import studio.bytesize.ld22.level.tile.CloudCactusTile;
import studio.bytesize.ld22.level.tile.CloudTile;
import studio.bytesize.ld22.level.tile.DirtTile;
import studio.bytesize.ld22.level.tile.FarmTile;
import studio.bytesize.ld22.level.tile.FlowerTile;
import studio.bytesize.ld22.level.tile.GrassTile;
import studio.bytesize.ld22.level.tile.HardRockTile;
import studio.bytesize.ld22.level.tile.HoleTile;
import studio.bytesize.ld22.level.tile.InfiniteFallTile;
import studio.bytesize.ld22.level.tile.LavaTile;
import studio.bytesize.ld22.level.tile.OreTile;
import studio.bytesize.ld22.level.tile.RockTile;
import studio.bytesize.ld22.level.tile.SandTile;
import studio.bytesize.ld22.level.tile.SaplingTile;
import studio.bytesize.ld22.level.tile.StairsTile;
import studio.bytesize.ld22.level.tile.Tile;
import studio.bytesize.ld22.level.tile.TreeTile;
import studio.bytesize.ld22.level.tile.WaterTile;
import studio.bytesize.ld22.level.tile.WheatTile;
import studio.bytesize.ld22.sound.Sound;

public class VanilllaPlugin implements MinicraftPlugin
{
	public void onLoad(Game game)
	{
		Sound.load("playerHurt", this.getClass().getResource("/playerhurt.wav"));
		Sound.load("playerDeath", this.getClass().getResource("/death.wav"));
		Sound.load("monsterHurt", this.getClass().getResource("/monsterhurt.wav"));
		Sound.load("test", this.getClass().getResource("/test.wav"));
		Sound.load("pickup", this.getClass().getResource("/pickup.wav"));
		Sound.load("bossdeath", this.getClass().getResource("/bossdeath.wav"));
		Sound.load("craft", this.getClass().getResource("/craft.wav"));

		Tile.load("grass", new GrassTile());
		Tile.load("rock", new RockTile());
		Tile.load("water", new WaterTile());
		Tile.load("flower", new FlowerTile());
		Tile.load("tree", new TreeTile());
		Tile.load("dirt", new DirtTile());
		Tile.load("sand", new SandTile());
		Tile.load("cactus", new CactusTile());
		Tile.load("hole", new HoleTile());
		Tile.load("treeSapling", new SaplingTile("grass", "tree"));
		Tile.load("cactusSapling", new SaplingTile("sand", "cactus"));
		Tile.load("farmland", new FarmTile());
		Tile.load("wheat", new WheatTile());
		Tile.load("lava", new LavaTile());
		Tile.load("stairsDown", new StairsTile(false));
		Tile.load("stairsUp", new StairsTile(true));
		Tile.load("infiniteFall", new InfiniteFallTile());
		Tile.load("cloud", new CloudTile());
		Tile.load("hardRock", new HardRockTile());
		Tile.load("ironOre", new OreTile("I.ORE"));
		Tile.load("goldOre", new OreTile("G.ORE"));
		Tile.load("gemOre", new OreTile("gem"));
		Tile.load("cloudCactus", new CloudCactusTile());

		Resource.load(new Resource("Wood", 1 + 4 * 32, Color.get(-1, 200, 531, 430)));
		Resource.load(new Resource("Stone", 2 + 4 * 32, Color.get(-1, 111, 333, 555)));
		Resource.load(new PlantableResource("Flower", 0 + 4 * 32, Color.get(-1, 10, 444, 330), "flower", "grass"));
		Resource.load(new PlantableResource("Acorn", 3 + 4 * 32, Color.get(-1, 100, 531, 320), "treeSapling", "grass"));
		Resource.load(new PlantableResource("Dirt", 2 + 4 * 32, Color.get(-1, 100, 322, 432), "dirt", "hole", "water", "lava"));
		Resource.load(new PlantableResource("Sand", 2 + 4 * 32, Color.get(-1, 110, 440, 550), "sand", "grass", "dirt"));
		Resource.load(new PlantableResource("Cactus", 4 + 4 * 32, Color.get(-1, 10, 40, 50), "cactusSapling", "sand"));
		Resource.load(new PlantableResource("Seeds", 5 + 4 * 32, Color.get(-1, 10, 40, 50), "wheat", "farmland"));
		Resource.load(new Resource("Wheat", 6 + 4 * 32, Color.get(-1, 110, 330, 550)));
		Resource.load(new FoodResource("Bread", 8 + 4 * 32, Color.get(-1, 110, 330, 550), 2, 5));
		Resource.load(new FoodResource("Apple", 9 + 4 * 32, Color.get(-1, 100, 300, 500), 1, 5));

		Resource.load(new Resource("COAL", 10 + 4 * 32, Color.get(-1, 000, 111, 111)));
		Resource.load(new Resource("I.ORE", 10 + 4 * 32, Color.get(-1, 100, 322, 544)));
		Resource.load(new Resource("G.ORE", 10 + 4 * 32, Color.get(-1, 110, 440, 553)));
		Resource.load(new Resource("IRON", 11 + 4 * 32, Color.get(-1, 100, 322, 544)));
		Resource.load(new Resource("GOLD", 11 + 4 * 32, Color.get(-1, 110, 330, 553)));

		Resource.load(new Resource("SLIME", 10 + 4 * 32, Color.get(-1, 10, 30, 50)));
		Resource.load(new Resource("GLASS", 12 + 4 * 32, Color.get(-1, 555, 555, 555)));
		Resource.load(new Resource("cloth", 1 + 4 * 32, Color.get(-1, 25, 252, 141)));
		Resource.load(new PlantableResource("cloud", 2 + 4 * 32, Color.get(-1, 222, 555, 444), "cloud", "infiniteFall"));
		Resource.load(new Resource("gem", 13 + 4 * 32, Color.get(-1, 101, 404, 545)));

		try
		{
			// WORKBENCH RECIPES
			////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
			Crafting.workbenchRecipes.add(new FurnitureRecipe(Lantern.class).addCost("Wood", 5).addCost("SLIME", 10).addCost("GLASS", 4));

			Crafting.workbenchRecipes.add(new FurnitureRecipe(Oven.class).addCost("Stone", 15));
			Crafting.workbenchRecipes.add(new FurnitureRecipe(Furnace.class).addCost("Stone", 20));
			Crafting.workbenchRecipes.add(new FurnitureRecipe(Workbench.class).addCost("Wood", 20));
			Crafting.workbenchRecipes.add(new FurnitureRecipe(Chest.class).addCost("Wood", 20));
			Crafting.workbenchRecipes.add(new FurnitureRecipe(Anvil.class).addCost("IRON", 5));

			Crafting.workbenchRecipes.add(new ToolRecipe(ToolType.sword, 0).addCost("Wood", 5));
			Crafting.workbenchRecipes.add(new ToolRecipe(ToolType.axe, 0).addCost("Wood", 5));
			Crafting.workbenchRecipes.add(new ToolRecipe(ToolType.hoe, 0).addCost("Wood", 5));
			Crafting.workbenchRecipes.add(new ToolRecipe(ToolType.pickaxe, 0).addCost("Wood", 5));
			Crafting.workbenchRecipes.add(new ToolRecipe(ToolType.shovel, 0).addCost("Wood", 5));
			Crafting.workbenchRecipes.add(new ToolRecipe(ToolType.sword, 1).addCost("Wood", 5).addCost("Stone", 5));
			Crafting.workbenchRecipes.add(new ToolRecipe(ToolType.axe, 1).addCost("Wood", 5).addCost("Stone", 5));
			Crafting.workbenchRecipes.add(new ToolRecipe(ToolType.hoe, 1).addCost("Wood", 5).addCost("Stone", 5));
			Crafting.workbenchRecipes.add(new ToolRecipe(ToolType.pickaxe, 1).addCost("Wood", 5).addCost("Stone", 5));
			Crafting.workbenchRecipes.add(new ToolRecipe(ToolType.shovel, 1).addCost("Wood", 5).addCost("Stone", 5));
			////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

			// ANVIL RECIPES
			////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
			Crafting.anvilRecipes.add(new ToolRecipe(ToolType.sword, 2).addCost("Wood", 5).addCost("IRON", 5));
			Crafting.anvilRecipes.add(new ToolRecipe(ToolType.axe, 2).addCost("Wood", 5).addCost("IRON", 5));
			Crafting.anvilRecipes.add(new ToolRecipe(ToolType.hoe, 2).addCost("Wood", 5).addCost("IRON", 5));
			Crafting.anvilRecipes.add(new ToolRecipe(ToolType.pickaxe, 2).addCost("Wood", 5).addCost("IRON", 5));
			Crafting.anvilRecipes.add(new ToolRecipe(ToolType.shovel, 2).addCost("Wood", 5).addCost("IRON", 5));

			Crafting.anvilRecipes.add(new ToolRecipe(ToolType.sword, 3).addCost("Wood", 5).addCost("GOLD", 5));
			Crafting.anvilRecipes.add(new ToolRecipe(ToolType.axe, 3).addCost("Wood", 5).addCost("GOLD", 5));
			Crafting.anvilRecipes.add(new ToolRecipe(ToolType.hoe, 3).addCost("Wood", 5).addCost("GOLD", 5));
			Crafting.anvilRecipes.add(new ToolRecipe(ToolType.pickaxe, 3).addCost("Wood", 5).addCost("GOLD", 5));
			Crafting.anvilRecipes.add(new ToolRecipe(ToolType.shovel, 3).addCost("Wood", 5).addCost("GOLD", 5));

			Crafting.anvilRecipes.add(new ToolRecipe(ToolType.sword, 4).addCost("Wood", 5).addCost("gem", 50));
			Crafting.anvilRecipes.add(new ToolRecipe(ToolType.axe, 4).addCost("Wood", 5).addCost("gem", 50));
			Crafting.anvilRecipes.add(new ToolRecipe(ToolType.hoe, 4).addCost("Wood", 5).addCost("gem", 50));
			Crafting.anvilRecipes.add(new ToolRecipe(ToolType.pickaxe, 4).addCost("Wood", 5).addCost("gem", 50));
			Crafting.anvilRecipes.add(new ToolRecipe(ToolType.shovel, 4).addCost("Wood", 5).addCost("gem", 50));
			////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

			// FURNACE RECIPES
			////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
			Crafting.furnaceRecipes.add(new ResourceRecipe("IRON").addCost("I.ORE", 4).addCost("COAL", 1));
			Crafting.furnaceRecipes.add(new ResourceRecipe("GOLD").addCost("G.ORE", 4).addCost("COAL", 1));
			Crafting.furnaceRecipes.add(new ResourceRecipe("Glass").addCost("Sand", 4).addCost("COAL", 1));
			////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

			// OVEN RECIPES
			////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
			Crafting.ovenRecipes.add(new ResourceRecipe("Bread").addCost("Wheat", 4));
			////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		Level.addMobToLevelSpawner(Slime.class);
		Level.addMobToLevelSpawner(Zombie.class);
	}

	public String getName()
	{
		return "Vanilla Minicraft";
	}

	@Override
	public boolean autoEnabled()
	{
		// TODO Auto-generated method stub
		return true;
	}
}
