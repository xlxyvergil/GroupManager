package org.anjocaido.groupmanager.events;

import org.anjocaido.groupmanager.GroupManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.world.WorldInitEvent;

/**
 * @author ElgarL
 * 
 *         Handle new world creation from other plugins
 * 
 */
public class GMWorldListener implements Listener {

	private final GroupManager plugin;

	public GMWorldListener(GroupManager instance) {

		plugin = instance;
		registerEvents();
	}

	private void registerEvents() {

		plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void onWorldInit(WorldInitEvent event) {

		String worldName = event.getWorld().getName();

		if (GroupManager.isLoaded() && !GroupManager.getWorldsHolder().isInList(worldName)) {
			GroupManager.logger.info("New world detected...");
			GroupManager.logger.info("Creating data for: " + worldName);
			
			if (GroupManager.getWorldsHolder().isWorldKnown("all_unnamed_worlds")) {
				
				String usersMirror = GroupManager.getWorldsHolder().getMirrorsUser().get("all_unnamed_worlds");
				String groupsMirror = GroupManager.getWorldsHolder().getMirrorsGroup().get("all_unnamed_worlds");
				
				if (usersMirror != null)
					GroupManager.getWorldsHolder().getMirrorsUser().put(worldName.toLowerCase(), usersMirror);
				
				if (groupsMirror != null)
					GroupManager.getWorldsHolder().getMirrorsGroup().put(worldName.toLowerCase(), groupsMirror);
				
			}
			
			GroupManager.getWorldsHolder().setupWorldFolder(worldName);
			GroupManager.getWorldsHolder().loadWorld(worldName);
			
			
			if (GroupManager.getWorldsHolder().isInList(worldName)) {
				GroupManager.logger.info("Don't forget to configure/mirror this world in config.yml.");
			} else
				GroupManager.logger.severe("Failed to configure this world.");
		}
	}
}