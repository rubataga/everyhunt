package me.rubataga.everyhunt.commands;

import me.rubataga.everyhunt.services.TargetService;
import me.rubataga.everyhunt.services.TargetManager;
import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.EntitySelectorArgument;
import dev.jorel.commandapi.arguments.PlayerArgument;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.Collections;

/**
 * Class containing commands relating to {@link TargetManager}
 */
public class TargetManagerCommands {

    private TargetManagerCommands() {
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
                    TargetService.addRunner(sender, player);
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
                    TargetService.addRunner(sender, player);
                });
    }

    /**
     * Command to add multiple players as runners
     *
     * @return CommandAPICommand
     */
    public static CommandAPICommand addRunnerMultiple() {
        return new CommandAPICommand("addrunner")
                .withArguments(new EntitySelectorArgument("players", EntitySelectorArgument.EntitySelector.MANY_ENTITIES))
                .executes((sender, args) -> {
                    // AP CSA : Casting
                    TargetService.addRunner(sender, (Collection) args[0]);
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
                    Collection<Entity> player = Collections.singletonList((Player) args[0]);
                    TargetService.addHunter(sender, player);
                });
    }

    /**
     * Command to add multiple entities as hunters
     *
     * @return CommandAPICommand
     */
    public static CommandAPICommand addHunterMultiple() {
        return new CommandAPICommand("addhunter")
                .withArguments(new EntitySelectorArgument("players", EntitySelectorArgument.EntitySelector.MANY_ENTITIES))
                .executes((sender, args) -> {
                    TargetService.addRunner(sender, (Collection) args[0]);
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
                    Collection<Entity> players = Collections.singletonList(sender);
                    TargetService.addHunter(sender, players);
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
                    TargetService.removeEntity(sender, player);
                });
    }

    /**
     * Remove entity as target
     *
     * @return CommandAPICommand
     */
    public static CommandAPICommand removeEntity() {
        return new CommandAPICommand("remove")
                .withArguments(new EntitySelectorArgument("entity", EntitySelectorArgument.EntitySelector.MANY_ENTITIES))
                .executes((sender, args) -> {
                    Collection<Entity> entities = (Collection) args[0];
                    TargetService.removeEntity(sender, entities);
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
                    TargetService.removeEntity(sender, player);
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
                    TargetService.teams(sender);
                });
    }

    public static CommandAPICommand sumSelf() {
        return new CommandAPICommand("sum")
                .executesPlayer((sender, args) -> {
                    Collection<Entity> player = Collections.singletonList(sender);
                    TargetService.sum(sender, player);
                });
    }

    public static CommandAPICommand sum() {
        return new CommandAPICommand("sum")
                .withArguments(new EntitySelectorArgument("entity", EntitySelectorArgument.EntitySelector.MANY_ENTITIES))
                .executes((sender, args) -> {
                    Collection<Entity> entities = (Collection) args[0];
                    TargetService.sum(sender, entities);
                });
    }

}
