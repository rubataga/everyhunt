package me.rubataga.manhunt.guis;

import de.themoep.inventorygui.DynamicGuiElement;
import de.themoep.inventorygui.GuiStateElement;
import de.themoep.inventorygui.InventoryGui;
import de.themoep.inventorygui.StaticGuiElement;
import me.rubataga.manhunt.Manhunt;
import me.rubataga.manhunt.roles.Hunter;
import me.rubataga.manhunt.utils.TrackingCompassUtils;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.inventory.ItemStack;

public class HunterGui extends InventoryGui {

    private final Hunter hunter;

    public HunterGui(Hunter hunter) {
        super(Manhunt.getInstance(),hunter.getEntity(),"Rubataga's Manhunt v1.0", new String[]{
                " l ",
                "tcb",
                " p "
        });
        this.hunter = hunter;
        initialize();
    }

    public void show(){
        show(hunter.getEntity());
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
            if(hunter.isLodestoneTracking()){
                trackingCompass = hunter.getCompass();
            } else {
                trackingCompass = new ItemStack(Material.COMPASS);
            }
            StaticGuiElement compass = new StaticGuiElement('c',
                    trackingCompass,
                    click -> {
                        TrackingCompassUtils.assignTrackingCompass(hunter);
                        return true;
            });
            if(hunter.getTarget()!=null){
                String alive = ChatColor.GREEN + "Alive!";
                if(hunter.getTargetEntity().isDead()){
                    alive = ChatColor.RED + "Dead!";
                }
                String environment = "Overworld";
                if(hunter.getTargetEntity().getWorld().getEnvironment()==World.Environment.NETHER){
                    environment = "The Nether";
                } else if (hunter.getTargetEntity().getWorld().getEnvironment()==World.Environment.THE_END){
                    environment = "The End";
                }
                compass.setText(
                        hunter.getTargetEntity().getName(),
                        environment,
                        alive
                );

            } else {
                String spawn = "World Spawn";
                if(hunter.getEntity().getBedSpawnLocation()!=null){
                    spawn = "Bed Spawn";
                }
                compass.setText("No target!",spawn);
            }
            return compass;
        });
    }

    private GuiStateElement guiLockElement(){
        GuiStateElement lock = new GuiStateElement('l',
                new GuiStateElement.State(
                        change -> {
                            hunter.setLocked(true);
                            hunter.getEntity().sendMessage("Your compass is locked");
                        },
                        "compassLocked",
                        new ItemStack(Material.BEDROCK),
                        "Compass is " + ChatColor.RED + "LOCKED",
                        "Click here to unlock your compass"
                ),
                new GuiStateElement.State(
                        change -> {
                            hunter.setLocked(false);
                            hunter.getEntity().sendMessage("Your compass is unlocked");
                        },
                        "compassUnlocked",
                        new ItemStack(Material.DIRT),
                        "Compass is " + ChatColor.GREEN + "UNLOCKED",
                        "Click here to lock your compass"
                )
        );
        if(hunter.isLocked()){
            lock.setState("compassLocked");
        } else {
            lock.setState("compassUnlocked");
        }
        return lock;
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
                click -> true
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