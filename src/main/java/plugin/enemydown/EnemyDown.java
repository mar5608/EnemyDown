package plugin.enemydown;

import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.plugin.java.JavaPlugin;
import plugin.enemydown.command.EnemyDownCommand;

public final class EnemyDown extends JavaPlugin {

    @Override
    public void onEnable() {
        // Plugin startup logic
        EnemyDownCommand enemyDownCommand = new EnemyDownCommand();
        //Bukkit.getPluginManager().registerEvents(this, this);
        Bukkit.getPluginManager().registerEvents(enemyDownCommand, this);
        getCommand("enemyDown").setExecutor(new EnemyDownCommand());
    }

}
