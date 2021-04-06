package me.rubataga.everyhunt.commands;

import me.rubataga.everyhunt.services.TrackingService;
import me.rubataga.everyhunt.managers.TrackingManager;
import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.EntitySelectorArgument;
import dev.jorel.commandapi.arguments.PlayerArgument;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.Collections;

/**
 * Class containing commands relating to {@link TrackingManager}
 */
public class TrackingCommands {

    private TrackingCommands() {
    }

    /**
     * Command to add player as runner
     *
     * @return CommandAPICommand
     */
    public static CommandAPICommand addRunner() {
        return new CommandAPICommand("addrunner")
                .withArguments(new PlayerArgument("player"))
                .executes((sender, args) -> {
                    Collection<Entity> player = Collections.singletonList((Player) args[0]);
                    TrackingService.addRunnerCommand(sender, player);
                });
    }

    /**
     * Command to add self as runner
     *
     * @return CommandAPICommand
     */
    public static CommandAPICommand addRunnerSelf() {
        return new CommandAPICommand("addrunner")
                .executesPlayer((sender, args) -> {
                    Collection<Entity> player = Collections.singletonList(sender);
                    TrackingService.addRunnerCommand(sender, player);
                });
    }

    /**
     * Command to add multiple players as runners
     *
     * @return CommandAPICommand
     */
    @SuppressWarnings("unchecked")
    public static CommandAPICommand addRunnerMultiple() {
        return new CommandAPICommand("addrunner")
                .withArguments(new EntitySelectorArgument("players", EntitySelectorArgument.EntitySelector.MANY_PLAYERS))
                .executes((sender, args) -> {
                    TrackingService.addRunnerCommand(sender, (Collection<Entity>) args[0]);
                });
    }

    /**
     * Command to add entity as hunter
     *
     * @return CommandAPICommand
     */
    public static CommandAPICommand addHunter() {
        return new CommandAPICommand("addhunter")
                .withArguments(new PlayerArgument("player"))
                .executes((sender, args) -> {
                    Collection<Player> player = Collections.singletonList((Player) args[0]);
                    TrackingService.addHunterCommand(sender, player);
                });
    }

    /**
     * Command to add multiple entities as hunters
     *
     * @return CommandAPICommand
     *
     */
    @SuppressWarnings("unchecked")
    public static CommandAPICommand addHunterMultiple() {
        return new CommandAPICommand("addhunter")
                .withArguments(new EntitySelectorArgument("players", EntitySelectorArgument.EntitySelector.MANY_PLAYERS))
                .executes((sender, args) -> {
                    TrackingService.addHunterCommand(sender, (Collection<Player>) args[0]);
                });
    }

    /**
     * Command to add self as hunter
     *
     * @return CommandAPICommand
     */
    public static CommandAPICommand addHunterSelf() {
        return new CommandAPICommand("addhunter")
                .executesPlayer((sender, args) -> {
                    Collection<Player> players = Collections.singletonList(sender);
                    TrackingService.addHunterCommand(sender, players);
                });
    }

    /**
     * Remove player as hunter, target, and/or runner
     *
     * @return CommandAPICommand
     */
    public static CommandAPICommand removePlayer() {
        return new CommandAPICommand("remove")
                .withArguments(new PlayerArgument("player"))
                .executes((sender, args) -> {
                    Collection<Entity> player = Collections.singletonList((Player) args[0]);
                    TrackingService.removeEntityCommand(sender, player);
                });
    }


    @SuppressWarnings("unchecked")
    public static CommandAPICommand removePlayerMultiple() {
        return new CommandAPICommand("remove")
                .withArguments(new EntitySelectorArgument("entity", EntitySelectorArgument.EntitySelector.MANY_PLAYERS))
                .executes((sender, args) -> {
                    Collection<Entity> players = (Collection<Entity>) args[0];
                    TrackingService.removeEntityCommand(sender, players);
                });
    }

    /**
     * Remove entity as hunter, target, and/or runner
     *
     * @return CommandAPICommand
     */
    public static CommandAPICommand removeEntity() {
        return new CommandAPICommand("remove")
                .withArguments(new EntitySelectorArgument("entity"))
                .executes((sender, args) -> {
                    TrackingService.removeEntityCommand(sender, Collections.singletonList((Entity) args[0]));
                });
    }

    @SuppressWarnings("unchecked")
    public static CommandAPICommand removeEntityMultiple() {
        return new CommandAPICommand("remove")
                .withArguments(new EntitySelectorArgument("entity", EntitySelectorArgument.EntitySelector.MANY_ENTITIES))
                .executes((sender, args) -> {
                    Collection<Entity> entities = (Collection<Entity>) args[0];
                    TrackingService.removeEntityCommand(sender, entities);
                });
    }

    /**
     * Remove self as hunter, target, and/or runner
     *
     * @return CommandAPICommand
     */
    public static CommandAPICommand removeSelf() {
        return new CommandAPICommand("remove")
                .executesPlayer((sender, args) -> {
                    Collection<Entity> player = Collections.singletonList(sender);
                    TrackingService.removeEntityCommand(sender, player);
                });
    }

    /**
     * List teams
     *
     * @return CommandAPICommand
     */
    public static CommandAPICommand teams() {
        return new CommandAPICommand("teams")
                .executes((sender, args) -> {
                    TrackingService.teams(sender);
                });
    }

}
