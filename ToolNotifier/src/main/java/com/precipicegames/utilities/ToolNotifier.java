package com.precipicegames.utilities;

import java.io.File;
import java.util.HashMap;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

/*
 * TODO: Add permissions support
 * TODO: convert to YamlConfiguration
 * TODO: Fork version for SpoutAPI
 * TODO: Fix Enchanted items
 */

public class ToolNotifier extends JavaPlugin
{
	Listener toolListener = new ToolListener(this);
	HashMap<Material, Integer> tools = new HashMap<Material, Integer>();
	File configDirectory;
	PluginYaml config;
	
	public void onLoad() {
		configDirectory = this.getDataFolder();
		configDirectory.mkdirs();
		config = new PluginYaml(new File(configDirectory, "config.yml"));
	}
	
	public void onEnable() {
		PluginManager pm = getServer().getPluginManager();
		pm.registerEvents(this.toolListener, this);

		this.tools.put(Material.GOLD_AXE, Integer.valueOf(33)); this.tools.put(Material.GOLD_HOE, Integer.valueOf(33)); this.tools.put(Material.GOLD_PICKAXE, Integer.valueOf(33)); this.tools.put(Material.GOLD_SPADE, Integer.valueOf(33)); this.tools.put(Material.GOLD_SWORD, Integer.valueOf(33));
		this.tools.put(Material.WOOD_AXE, Integer.valueOf(60)); this.tools.put(Material.WOOD_HOE, Integer.valueOf(60)); this.tools.put(Material.WOOD_PICKAXE, Integer.valueOf(60)); this.tools.put(Material.WOOD_SPADE, Integer.valueOf(60)); this.tools.put(Material.WOOD_SWORD, Integer.valueOf(60));
		this.tools.put(Material.STONE_AXE, Integer.valueOf(132)); this.tools.put(Material.STONE_HOE, Integer.valueOf(132)); this.tools.put(Material.STONE_PICKAXE, Integer.valueOf(132)); this.tools.put(Material.STONE_SPADE, Integer.valueOf(132)); this.tools.put(Material.STONE_SWORD, Integer.valueOf(132));
		this.tools.put(Material.IRON_AXE, Integer.valueOf(251)); this.tools.put(Material.IRON_HOE, Integer.valueOf(251)); this.tools.put(Material.IRON_PICKAXE, Integer.valueOf(251)); this.tools.put(Material.IRON_SPADE, Integer.valueOf(251)); this.tools.put(Material.IRON_SWORD, Integer.valueOf(251));
		this.tools.put(Material.DIAMOND_AXE, Integer.valueOf(1562)); this.tools.put(Material.DIAMOND_HOE, Integer.valueOf(1562)); this.tools.put(Material.DIAMOND_PICKAXE, Integer.valueOf(1562)); this.tools.put(Material.DIAMOND_SPADE, Integer.valueOf(1562)); this.tools.put(Material.DIAMOND_SWORD, Integer.valueOf(1562));

		PluginDescriptionFile pdfFile = getDescription();
		System.out.println(pdfFile.getName() + " version " + pdfFile.getVersion() + " is enabled!");
	}

	public void onDisable() {
		this.config.save();
	}

	//TODO: Modify this to take advantage of the new save format
	public void onToolDamaged(Player player) {
		Material toolInHand = player.getItemInHand().getType();

		double toolUses = player.getItemInHand().getDurability();
		double maxUses = ((Integer)this.tools.get(toolInHand)).intValue();
		double usesLeft = maxUses - 1.0D - toolUses;

		if (this.config.notify.containsKey(Integer.valueOf((int)usesLeft)))
		{
			if (!this.config.players.containsKey(player))
			{
				this.config.players.put(player, Integer.valueOf((int)usesLeft));

				String message = (String)this.config.notify.get(Integer.valueOf((int)usesLeft));
				player.sendMessage(ChatColor.valueOf(LoadSettings.Color) + ChatColor.stripColor(message));
			}

			if (((Integer)this.config.players.get(player)).intValue() != (int)usesLeft)
			{
				String message = (String)this.config.notify.get(Integer.valueOf((int)usesLeft));
				player.sendMessage(ChatColor.valueOf(LoadSettings.Color) + ChatColor.stripColor(message));

				this.config.players.put(player, Integer.valueOf((int)usesLeft));
			}
		}
	}
}