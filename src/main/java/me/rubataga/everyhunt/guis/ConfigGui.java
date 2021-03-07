package me.rubataga.everyhunt.guis;

import com.google.common.collect.ImmutableList;
import de.themoep.inventorygui.DynamicGuiElement;
import de.themoep.inventorygui.InventoryGui;
import de.themoep.inventorygui.StaticGuiElement;
import me.rubataga.everyhunt.Everyhunt;
import me.rubataga.everyhunt.config.GameCfg;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.LinkedList;
import java.util.List;

public class ConfigGui extends InventoryGui {

    private static final String[] layout = new String[]{"012345678"};

    public ConfigGui() {
        super(Everyhunt.getInstance(),"Rubataga's Everyhunt v1.0", layout);
        initialize();
    }

    private void initialize(){
        List<String> parameters = ImmutableList.copyOf(GameCfg.getFields().keySet());
        setFiller(new ItemStack(Material.BLACK_STAINED_GLASS_PANE,1));
        for(int i = 0; i < parameters.size(); i++){
            char c = Character.forDigit(i,10);
            //Debugger.send("config char: " + c);
            addElement(configElement(c, parameters.get(i)));
        }
    }

    private DynamicGuiElement configElement(char c, String parameter){
        return new DynamicGuiElement(c, (viewer) -> {
            StaticGuiElement element = new StaticGuiElement(c,
                    new ItemStack(Material.PUFFERFISH),
                    1,
                    click -> true
                );
            String text = String.format("%s", GameCfg.getValue(parameter));
            //Debugger.send("dynamic gui element w/ text: " + parameter + ", " + text);
            element.setText(parameter,text);
            return element;
        });
    }

}
