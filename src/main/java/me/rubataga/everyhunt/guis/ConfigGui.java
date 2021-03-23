package me.rubataga.everyhunt.guis;

import com.google.common.collect.ImmutableList;
import de.themoep.inventorygui.DynamicGuiElement;
import de.themoep.inventorygui.InventoryGui;
import de.themoep.inventorygui.StaticGuiElement;
import me.rubataga.everyhunt.Everyhunt;
import me.rubataga.everyhunt.config.GameCfg;
import me.rubataga.everyhunt.utils.Debugger;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class ConfigGui extends InventoryGui {

    private static final List<String> PARAMETERS = ImmutableList.copyOf(GameCfg.getKeyFields().keySet());
    private static final List<String> STRING_LIST = new ArrayList<>();
    private static final char[] charArray = new char[]{'a','b','c','d','e','f','g','h','i','j','k','l','m','n','o','p','q','r'};
    private static String[] layout;

    public static void initialize() {
        StringBuilder sb = new StringBuilder();
        int upper9 = (int)(9*(Math.ceil(Math.abs(charArray.length/9))));
        for(int i = 0; i < upper9; i++){
            if(i<charArray.length) {
                sb.append(charArray[i]);
            }
            if((i+1)%9==0){
                STRING_LIST.add(sb.toString());
                sb.delete(0,10);
            }
        }
        layout = STRING_LIST.toArray(new String[0]);
    }

    public ConfigGui() {
        super(Everyhunt.getInstance(),"Rubataga's Everyhunt v1.0.2", layout);
        assignElements();
    }

    private void assignElements(){
        setFiller(new ItemStack(Material.BLACK_STAINED_GLASS_PANE,1));
        for(int i = 0; i < PARAMETERS.size(); i++){
            char c;
            int index = i;
            while(index>charArray.length) {
                index -= charArray.length;
            }
            c = charArray[index];
            addElement(configElement(c, PARAMETERS.get(i)));
        }
    }

    private DynamicGuiElement configElement(char c, String parameter){
        return new DynamicGuiElement(c, (viewer) -> {
            StaticGuiElement element = new StaticGuiElement(c,
                    new ItemStack(Material.PUFFERFISH),
                    1,
                    click -> true
            );
            String text = GameCfg.getFormattedValue(parameter);
            element.setText(parameter,text);
            return element;
        });
    }

    @Override
    public void draw() {
        setTitle("Rubataga's Everyhunt v1.0.2 - " + GameCfg.gameName);
        super.draw();
    }

    @Override
    public void show(HumanEntity player) {
        setTitle("Rubataga's Everyhunt v1.0.2 - " + GameCfg.gameName);
        super.show(player);
    }

}
