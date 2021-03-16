package me.rubataga.everyhunt.commands;

import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.EntitySelectorArgument;
import dev.jorel.commandapi.arguments.StringArgument;
import me.rubataga.everyhunt.services.AdminService;
import me.rubataga.everyhunt.utils.Debugger;
import org.bukkit.entity.Entity;

import java.util.Collection;
import java.util.Collections;

public class AdminCommands {

    public static CommandAPICommand config() {
        return new CommandAPICommand("config")
                .executes((sender, args) -> {
                    AdminService.config(sender);
                });
    }

    public static CommandAPICommand loadConfig() {
        return new CommandAPICommand("loadconfig")
                .withArguments(new StringArgument("filename.yml"))
                .executes((sender, args) -> {
                    AdminService.loadConfig(sender,(String)args[0]);
                });
    }

    public static CommandAPICommand configGui() {
        return new CommandAPICommand("configgui")
                .withAliases("cfgui")
                .executesPlayer((sender, args) -> {
                    AdminService.configGui(sender);
                });
    }

    public static CommandAPICommand sumSelf() {
        return new CommandAPICommand("sum")
                .executesPlayer((sender, args) -> {
                    Collection<Entity> player = Collections.singletonList(sender);
                    AdminService.sum(sender, player);
                });
    }

    @SuppressWarnings("unchecked")
    public static CommandAPICommand sum() {
        return new CommandAPICommand("sum")
                .withArguments(new EntitySelectorArgument("entity", EntitySelectorArgument.EntitySelector.MANY_ENTITIES))
                .executes((sender, args) -> {
                    Collection<Entity> entities = (Collection<Entity>) args[0];
                    AdminService.sum(sender, entities);
                });
    }

    public static CommandAPICommand dummy() {
        return new CommandAPICommand("dummy")
                .executes((sender, args) -> {
                    Debugger.send("DUMMY!");
                    sender.sendMessage("DUMMY!");
                });
    }

}
