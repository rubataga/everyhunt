package me.rubataga.manhunt.events;

import me.rubataga.manhunt.Manhunt;
import me.rubataga.manhunt.models.Hunter;
import me.rubataga.manhunt.models.TrackingCompass;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerRespawnEvent;


public class CompassListener implements Listener {

    @EventHandler
    public void onRunnerMove(PlayerMoveEvent e){
        if(Manhunt.isRunner(e.getPlayer())){ // if runner moves
            Player runner = e.getPlayer(); // Player runner = the event's player
            for(Hunter hunter : Manhunt.huntersTargeting(runner)){ // for each hunter targeting the runner
                hunter.compassTrackRunner(runner); //update their compasses to point at targetHunters
            }
        }
    }

    @EventHandler
    public void hunterUseTrackingCompass(PlayerInteractEvent e){
        if(e.getItem()==null){
            return;
        }
        if(!Manhunt.isHunter(e.getPlayer()) || !Manhunt.isTrackerCompass(e.getItem()) || Manhunt.runners.size()==0){ // if there are no runners or player is not a hunter or item is not a Tracker Compass
            return;
        }
        Hunter hunter = Manhunt.hunters.get(e.getPlayer()); // Hunter hunter = the hunter using the compass
        int runnerIndex;  // initialize runnerIndex
        if(Manhunt.runners.size()==1 || hunter.getHunting()==null){ // if there's only one runner or the hunter has no target
            runnerIndex = 0; // set the runnerIndex to 0;
        } else {
            runnerIndex = Manhunt.runners.indexOf(hunter.getHunting()) + 1; // cycle to the next runner in the ArrayList
            if (runnerIndex == Manhunt.runners.size()) { // if the runnerIndex gets greater than the # of runners
                runnerIndex = 0; // set the runnerIndex back to 0
            }
        }
        Player runner = Manhunt.runners.get(runnerIndex); // Player runner = the runner with index runnerIndex
        hunter.setHunting(runner); // the hunter is set to be hunting runner
        hunter.compassTrackRunner(runner); // the hunter's compass is set to the runner's location
    }

    @EventHandler
    public void onHunterRevive(PlayerRespawnEvent e){
        if(Manhunt.isHunter(e.getPlayer())){
            e.getPlayer().getInventory().setItemInMainHand(TrackingCompass.getTrackingCompass());
        }
    }

}
