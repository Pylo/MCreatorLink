package net.mcreator.minecraft.link.command;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import net.mcreator.minecraft.link.MCreatorLink;
import net.mcreator.minecraft.link.devices.AbstractDevice;

import java.util.Collection;
import java.util.Collections;
import java.util.concurrent.CompletableFuture;

public class LinkDeviceArgumentType implements ArgumentType<String> {

    @Override
    public String parse(StringReader reader) {
        String deviceName = reader.getRemaining();
        reader.setCursor(reader.getTotalLength());
        return deviceName;
    }

    @Override
    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
        for (AbstractDevice device : MCreatorLink.LINK.getAllDevices())
            builder.suggest(device.getName());
        return builder.buildFuture();
    }

    @Override
    public Collection<String> getExamples() {
        return Collections.emptyList();
    }

}
