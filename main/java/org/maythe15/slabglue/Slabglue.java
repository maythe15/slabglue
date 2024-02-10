package org.maythe15.slabglue;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.logger.slf4j.ComponentLogger;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapelessRecipe;
import org.bukkit.plugin.java.JavaPlugin;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;

public final class Slabglue extends JavaPlugin {
    @Override
    public void onEnable() {
        ComponentLogger logger = this.getComponentLogger();
        logger.info(Component.text("Loading Slabglue"));
        int loaded=0;
        saveResource("config.yml", false);
        FileConfiguration config = this.getConfig();
        ArrayList typesarray =(ArrayList) config.get("types");
        for (Object typeobj: typesarray) {
            LinkedHashMap type = (LinkedHashMap) typeobj;
            String name = (String) type.keySet().toArray()[0];
            LinkedHashMap data = (LinkedHashMap) type.get(name);
            Set keys = data.keySet();
            if (!keys.contains("block")){
                logger.warn(Component.text("Type "+name+" is missing field 'block'"));
                continue;
            }
            if (!keys.contains("slab")) {
                logger.warn(Component.text("Type " + name + " is missing field 'slab'"));
                continue;
            }
            String slab_mat = (String) data.get("slab");
            String block_mat = (String) data.get("block");
            Material slab = Material.getMaterial(slab_mat);
            Material block = Material.getMaterial(block_mat);
            if (slab==null){
                logger.warn(Component.text("Invalid slab material " + slab_mat + " from type "+name));
                continue;
            }
            if (block==null){
                logger.warn(Component.text("Invalid block material " + block_mat + " from type "+name));
                continue;
            }
            ItemStack slab_stack = new ItemStack(slab);
            ItemStack block_stack = new ItemStack(block);
            ShapelessRecipe recipe=new ShapelessRecipe(new NamespacedKey("slabglue", name), block_stack);
            recipe.addIngredient(2, slab_stack);
            this.getServer().addRecipe(recipe);
            loaded++;
        }
        logger.info(Component.text("Loaded "+loaded+" recipes"));
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
