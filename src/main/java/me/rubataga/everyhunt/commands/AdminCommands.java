package me.rubataga.everyhunt.commands;

import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.EntitySelectorArgument;
import me.rubataga.everyhunt.services.AdminService;
import me.rubataga.everyhunt.services.TargetService;
import org.bukkit.entity.Entity;

import java.util.Collection;
import java.util.Collections;

public class AdminCommands {

    public static CommandAPICommand config() {
        return new CommandAPICommand("config")
                .executes((sender,args) -> {
                    AdminService.config(sender);
                });
    }

    public static CommandAPICommand sumSelf() {
        return new CommandAPICommand("sum")
                .executesPlayer((sender, args) -> {
                    Collection<Entity> player = Collections.singletonList(sender);
                    AdminService.sum(sender, player);
                });
    }

    public static CommandAPICommand sum() {
        return new CommandAPICommand("sum")
                .withArguments(new EntitySelectorArgument("entity", EntitySelectorArgument.EntitySelector.MANY_ENTITIES))
                .executes((sender, args) -> {
                    Collection<Entity> entities = (Collection) args[0];
                    AdminService.sum(sender, entities);
                });
    }
}
