package me.rubataga.everyhunt.guis;

import de.themoep.inventorygui.DynamicGuiElement;
import de.themoep.inventorygui.GuiStateElement;
import de.themoep.inventorygui.InventoryGui;
import de.themoep.inventorygui.StaticGuiElement;
import me.rubataga.everyhunt.Everyhunt;
import me.rubataga.everyhunt.roles.Hunter;
import me.rubataga.everyhunt.utils.TrackingCompassUtils;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.inventory.ItemStack;

public class HunterGui extends InventoryGui {

    private final Hunter hunter;

    public HunterGui(Hunter hunter) {
        super(Everyhunt.getInstance(),hunter.getEntity(),"Rubataga's Everyhunt v1.0", new String[]{
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
                //System.out.println("Hunter compass is null: " + (hunter.getCompass()==null));
                if(hunter.inventoryHasCompass()){
                    //System.out.println("Hunter inventory has compass");
                    trackingCompass = hunter.getCompass();
                } else {
                    //System.out.println("Hunter inventory does not have compass");
                    trackingCompass = hunter.getLodestoneCompass(); // issue!!! works fine when compass is in inventory
                }
                //System.out.println("tracking compass is null:" + (trackingCompass==null));
            } else {
                //System.out.println("DEBUG 1");
                trackingCompass = TrackingCompassUtils.trackingCompass(); // this works fine
            }
            //System.out.println("DEBUG 2");
            StaticGuiElement compass = new StaticGuiElement('c',
                    trackingCompass,
                    1,
                    click -> {
                        TrackingCompassUtils.assignTrackingCompass(hunter);
                        return true;
            });
            //System.out.println("DEBUG 3");
            if(hunter.getTarget()!=null){
                //System.out.println("DEBUG 4");
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
                //System.out.println("DEBUG 5");
                String spawn = "World Spawn";
                if(hunter.getEntity().getBedSpawnLocation()!=null){
                    spawn = "Bed Spawn";
                }
                compass.setText("No target!",spawn);
            }
            //System.out.println("DEBUG 6");
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