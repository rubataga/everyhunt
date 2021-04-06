package me.rubataga.everyhunt.commands;

import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.EntitySelectorArgument;
import dev.jorel.commandapi.arguments.PlayerArgument;
import me.rubataga.everyhunt.GameManager;
import me.rubataga.everyhunt.services.GameManagerService;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.Collections;

public class GameManagerCommands {

    public static CommandAPICommand newGame() {
        return new CommandAPICommand("newgame")
                .executes((sender, args) -> {
                    GameManager.newGame();
                });
    }

    public static CommandAPICommand startGame() {
        return new CommandAPICommand("startgame")
                .executes((sender, args) -> {
                    GameManager.startGame();
                });
    }

    public static CommandAPICommand stopGame() {
        return new CommandAPICommand("stopgame")
                .executes((sender, args) -> {
                    GameManager.stopGame();
                });
    }

    public static CommandAPICommand joinPlayer() {
        return new CommandAPICommand("join")
                .withArguments(new PlayerArgument("player"))
                .executes((sender, args) -> {
                    Collection<Player> player = Collections.singletonList((Player) args[0]);
                    GameManagerService.playerJoinGame(sender,player);
                });
    }

    @SuppressWarnings("unchecked")
    public static CommandAPICommand joinPlayerMultiple() {
        return new CommandAPICommand("join")
                .withArguments(new EntitySelectorArgument("players", EntitySelectorArgument.EntitySelector.MANY_PLAYERS))
                .executes((sender, args) -> {
                    GameManagerService.playerJoinGame(sender, (Collection<Player>) args[0]);
                });
    }

    public static CommandAPICommand joinSelf() {
        return new CommandAPICommand("join")
                .executesPlayer((sender, args) -> {
                    Collection<Player> player = Collections.singletonList(sender);
                    GameManagerService.playerJoinGame(sender,player);
                });
    }

}
