package me.rubataga.everyhunt.guis;

import de.themoep.inventorygui.DynamicGuiElement;
import de.themoep.inventorygui.GuiStateElement;
import de.themoep.inventorygui.InventoryGui;
import de.themoep.inventorygui.StaticGuiElement;
import me.rubataga.everyhunt.Everyhunt;
import me.rubataga.everyhunt.configs.GameCfg;
import me.rubataga.everyhunt.roles.Hunter;
import me.rubataga.everyhunt.utils.TrackingCompassUtils;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class HunterGui extends InventoryGui {

    private final Hunter HUNTER;
    private final Player PLAYER;
    private static final String[] LAYOUT = new String[]{
            " l ",
            "tcb",
            " p "
    };

    public HunterGui(Hunter hunter) {
        super(Everyhunt.getInstance(),hunter.getEntity(),"Rubataga's Everyhunt v1.0.2",LAYOUT);
        this.HUNTER = hunter;
        this.PLAYER = hunter.getEntity();
        initialize();
    }

    public void show(){
        show(PLAYER);
    }

    private void initialize(){
        setFiller(new ItemStack(Material.BLACK_STAINED_GLASS_PANE,1));
        addElement(guiCompassElement());
        addElement(guiLockElement());
        addElement(guiTargetElement());
        addElement(guiPufferfishElement());
        addElement(guiRunnerElement());
    }

    private DynamicGuiElement guiCompassElement(){
        return new DynamicGuiElement('c', (viewer) -> {
            ItemStack trackingCompass;
            if(HUNTER.isLodestoneTracking()){
                if(HUNTER.inventoryHasCompass()){
                    trackingCompass = HUNTER.getCompass();
                } else {
                    trackingCompass = HUNTER.getLodestoneCompass();
                }
            } else {
                trackingCompass = TrackingCompassUtils.trackingCompass(HUNTER);
            }
            StaticGuiElement compass = new StaticGuiElement('c',
                    trackingCompass,
                    1,
                    click -> {
                        if(TrackingCompassUtils.isTrackingCompass(PLAYER.getItemOnCursor())){
                            PLAYER.setItemOnCursor(null);
                        }
                        TrackingCompassUtils.assignTrackingCompass(HUNTER);
                        return true;
            });
            if(HUNTER.getTarget()!=null){
                Entity targetEntity = HUNTER.getTargetEntity();
                String alive = ChatColor.GREEN + "Alive!";
                if(targetEntity.isDead()){
                    alive = ChatColor.RED + "Dead!";
                }
                String environment = "Overworld";
                World.Environment targetEnvironment = targetEntity.getWorld().getEnvironment();
                if(targetEnvironment==World.Environment.NETHER){
                    environment = "The Nether";
                } else if (targetEnvironment==World.Environment.THE_END){
                    environment = "The End";
                }
                compass.setText(
                        targetEntity.getName(),
                        environment,
                        alive
                );

            } else {
                String spawn = "World Spawn";
                if(PLAYER.getBedSpawnLocation()!=null){
                    spawn = "Bed Spawn";
                }
                compass.setText("No target!",spawn);
            }
            return compass;
        });
    }

    private DynamicGuiElement guiLockElement(){
        return new DynamicGuiElement('l', (viewer) -> {
            GuiStateElement lock;
            if(GameCfg.huntersCanChangeTarget){
                lock = new GuiStateElement('l',compassLocked(),compassUnlocked());
            } else {
                lock = new GuiStateElement('l',compassLocked());
            }
            if(HUNTER.isLockedOnTarget()){
                lock.setState("compassLocked");
            } else {
                lock.setState("compassUnlocked");
            }
            return lock;
        });
    }

    private GuiStateElement.State compassLocked(){
        String finalLine;
        if(GameCfg.huntersCanChangeTarget){
            finalLine = "Click here to unlock your compass";
        } else {
            finalLine = "You cannot change your target!";
        }
        return new GuiStateElement.State(
                change -> {
                    HUNTER.setLockedOnTarget(true);
                    PLAYER.sendMessage("Your compass is locked");
                },
                "compassLocked",
                new ItemStack(Material.BEDROCK),
                "Compass is " + ChatColor.RED + "LOCKED",
                finalLine
        );
    }

    private GuiStateElement.State compassUnlocked(){
        return new GuiStateElement.State(
                change -> {
                    HUNTER.setLockedOnTarget(false);
                    PLAYER.sendMessage("Your compass is unlocked");
                },
                "compassUnlocked",
                new ItemStack(Material.GRASS_BLOCK),
                "Compass is " + ChatColor.GREEN + "UNLOCKED",
                "Click here to lock your compass"
        );
    }

    private StaticGuiElement guiTargetElement(){
        return new StaticGuiElement('t',
                new ItemStack(Material.TARGET),
                1,
                click -> true
        );
    }

    private StaticGuiElement guiPufferfishElement(){
        return new StaticGuiElement('p',
                new ItemStack(Material.PUFFERFISH_BUCKET),
                1,
                click -> {
                    if(PLAYER.isOp()){
                        GameCfg.getGui().show(PLAYER);
                    }
                    this.close();
                    return true;
                }
        );
    }

    private StaticGuiElement guiRunnerElement(){
        return new StaticGuiElement('b',
                new ItemStack(Material.LEATHER_BOOTS),
                1,
                click -> true
        );
    }
}