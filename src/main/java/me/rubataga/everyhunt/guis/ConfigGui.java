package me.rubataga.everyhunt.guis;

import com.google.common.collect.ImmutableList;
import de.themoep.inventorygui.DynamicGuiElement;
import de.themoep.inventorygui.InventoryGui;
import de.themoep.inventorygui.StaticGuiElement;
import me.rubataga.everyhunt.Everyhunt;
import me.rubataga.everyhunt.game.GameCfg;
import me.rubataga.everyhunt.utils.Debugger;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class ConfigGui extends InventoryGui {

    private final ImmutableList<String> PARAMETERS = GameCfg.PARAMETERS;
    private static final String[] LAYOUT = new String[]{"012345678"};

    public ConfigGui() {
        super(Everyhunt.getInstance(),"Rubataga's Everyhunt v1.0", LAYOUT);
        initialize();
    }

    private void initialize(){
        setFiller(new ItemStack(Material.BLACK_STAINED_GLASS_PANE,1));
        for(int i = 0; i < PARAMETERS.size(); i++){
            char c = Character.forDigit(i,10);
            Debugger.send("config char: " + c);
            addElement(configElement(c,PARAMETERS.get(i)));
        }
    }

    private DynamicGuiElement configElement(char c, String parameter){
        return new DynamicGuiElement(c, (viewer) -> {
            StaticGuiElement element = new StaticGuiElement(c,
                    new ItemStack(Material.PUFFERFISH),
                    1,
                    click -> true
                );
            String text = String.format("%s",GameCfg.getValue(parameter));
            Debugger.send("dynamic gui element w/ text: " + parameter + ", " + text);
            element.setText(parameter,text);
            return element;
        });
    }

}
