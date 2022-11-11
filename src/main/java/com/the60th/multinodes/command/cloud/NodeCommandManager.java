package com.the60th.multinodes.command.cloud;

import cloud.commandframework.annotations.AnnotationParser;
import cloud.commandframework.arguments.parser.StandardParameters;
import cloud.commandframework.bukkit.BukkitCaptionRegistryFactory;
import cloud.commandframework.bukkit.CloudBukkitCapabilities;
import cloud.commandframework.captions.FactoryDelegatingCaptionRegistry;
import cloud.commandframework.execution.CommandExecutionCoordinator;
import cloud.commandframework.meta.CommandMeta;
import cloud.commandframework.paper.PaperCommandManager;
import com.the60th.multinodes.MultiNodes;
import com.the60th.multinodes.command.CmdNodes;
import com.the60th.multinodes.command.map.CmdMap;
import org.bukkit.command.CommandSender;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.function.Function;

public class NodeCommandManager {
    private final MultiNodes plugin;
    private final PaperCommandManager<CommandSender> manager;
    private final CmdNodes cmdNodes;

    private final CmdMap cmdMap;
    public NodeCommandManager(MultiNodes plugin){
        this.plugin = plugin;
        try {
            manager = new PaperCommandManager<>(plugin, CommandExecutionCoordinator.simpleCoordinator(),
                    Function.identity(), Function.identity());
        }catch (Exception e){
            throw new RuntimeException("Failed to set up cloud commands");
        }

        if(manager.hasCapability(CloudBukkitCapabilities.BRIGADIER))
            manager.registerBrigadier();
        if (manager.hasCapability(CloudBukkitCapabilities.ASYNCHRONOUS_COMPLETION))
            manager.registerAsynchronousCompletions();


        cmdNodes = new CmdNodes(plugin,this);
        cmdMap = new CmdMap(plugin,this);

        AnnotationParser<CommandSender> nodeParser = new AnnotationParser<>(
                manager,
                CommandSender.class,
                params -> CommandMeta.simple()
                        .with(CommandMeta.DESCRIPTION, params.get(StandardParameters.DESCRIPTION, "Default description for node command"))
                        .build()
        );

        AnnotationParser<CommandSender> mapParser = new AnnotationParser<>(
                manager,
                CommandSender.class,
                params -> CommandMeta.simple()
                        .with(CommandMeta.DESCRIPTION, params.get(StandardParameters.DESCRIPTION, "Default description for map command"))
                        .build()
        );
        //TODO Use this later?
        /*manager.parameterInjectorRegistry().registerInjector(
                Persona.class,
                (context, annotations) -> {
                    if (context.getSender() instanceof Player player)
                        return PersonaAPI.online().getActivePersona(player);
                    else
                        throw new OfflinePlayerException();
                }
        );*/

        // captions (basically custom, error messages)
        //captionFactory.registerMessageFactory();
        // register commands
        nodeParser.parse(cmdNodes);
        mapParser.parse(cmdMap);

    }


    public @NonNull PaperCommandManager<CommandSender> getManager() {
        return manager;
    }
}
