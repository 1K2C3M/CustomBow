package me.Corey.CustomBow;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin implements Listener{

	public List<String> list = new ArrayList<String>();
	
	@Override
	public void onEnable() {
		this.getServer().getPluginManager().registerEvents(this, this);
	}
	
	@Override
	public void onDisable() {
		
	}
	
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		// /sword
		if (label.equalsIgnoreCase("bow")) {
			if (!(sender instanceof Player)) {
				sender.sendMessage("Seriously... not like you can do anything with this bow.");
				return true;
			}
			Player player = (Player) sender;
			if (player.getInventory().firstEmpty() == -1) {
				// inventory is full
				Location loc = player.getLocation();
				World world = player.getWorld();
				
				world.dropItemNaturally(loc, getitem());
				player.sendMessage(ChatColor.DARK_RED + "" + ChatColor.BOLD + "Go slay your enemies.. but pick up the bow first!");
				return true;
			}
			player.getInventory().addItem(getitem());
			player.sendMessage(ChatColor.DARK_RED + "" + ChatColor.BOLD + "Go slay your enemies");
			return true;
 		}
		return false;
	}
	
	
	public ItemStack getitem() {
		
		ItemStack item = new ItemStack(Material.BOW);
		ItemMeta meta = item.getItemMeta();
		
		meta.setDisplayName(ChatColor.DARK_RED + "" + ChatColor.BOLD + "Soul Splitter");
		List<String> lore = new ArrayList<String>();
		lore.add("");
		lore.add(ChatColor.RED + "" + ChatColor.ITALIC + "Destroy the souls of your enemies");
		meta.setLore(lore);
		
		meta.addEnchant(Enchantment.ARROW_INFINITE, 1, true);
		meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
		meta.setUnbreakable(true);
		
		item.setItemMeta(meta);
		
		return item;
		
	}

	@EventHandler()
	public void onClick(PlayerInteractEvent event) {
		if (event.getPlayer().getInventory().getItemInMainHand().getType().equals(Material.BOW))
			if (event.getPlayer().getInventory().getItemInMainHand().getItemMeta().hasLore()) {
				Player player = (Player) event.getPlayer();
				//Right click
				if (event.getAction() == Action.RIGHT_CLICK_AIR) {
					if (!list.contains(player.getName()))
						list.add(player.getName());
					return;
				}
				
				//left Click
				if (event.getAction() == Action.LEFT_CLICK_AIR) {
					player.launchProjectile(Fireball.class);
				}
			}
		if (list.contains(event.getPlayer().getName())) {
			list.remove(event.getPlayer().getName());
		}
	}
	
	@EventHandler()
	public void onLand(ProjectileHitEvent event) {
		if (event.getEntityType() == EntityType.ARROW) {
			if (event.getEntity().getShooter() instanceof Player) {
				Player player = (Player) event.getEntity().getShooter();
				if (list.contains(player.getName())) {
					// spawn zombies
					Location loc = event.getEntity().getLocation();
					loc.setY(loc.getY() + 1);
					
					for (int i = 1; i < 4 ; i++) {
						loc.getWorld().spawnEntity(loc, EntityType.ZOMBIE);
						loc.setX(loc.getX() + i);
					}
				}
			}
		}
	}
}
