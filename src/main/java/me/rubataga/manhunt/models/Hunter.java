package me.rubataga.manhunt.models;
import org.bukkit.entity.Player;

public class Hunter {
    private Player player;
    private Player hunting;

    public Hunter(Player player){
        this.player = player;
        this.hunting = null;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public Player getHunting() {
        return hunting;
    }

    public void setHunting(Player hunting) {
        this.hunting = hunting;
    }

    public void compassTrackRunner(Player runner){
        player.setCompassTarget(runner.getLocation());
    }
}
