package com.kral1k.krabot;

import com.kral1k.krabot.button.ButtonManager;
import com.kral1k.krabot.command.CommandManager;
import com.kral1k.krabot.guild.Guild;
import com.kral1k.krabot.guild.GuildCache;
import com.kral1k.krabot.modal.ModalManager;
import com.kral1k.krabot.user.User;
import com.kral1k.krabot.user.UserCache;
import com.kral1k.krabot.user.UserData;
import com.kral1k.krabot.utils.Directory;
import com.kral1k.krabot.utils.GuildDirectory;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.ChunkingFilter;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.security.auth.login.LoginException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class Bot {
    private static final Path DIRECTORY = Path.of("");
    private JDA jda;
    private final UserCache userCache = new UserCache();
    private final GuildCache guildCache = new GuildCache();
    private final CommandManager commandManager = new CommandManager();
    private final ButtonManager buttonManager = new ButtonManager();
    private final ModalManager modalManager = new ModalManager();

    public void start() throws LoginException {
        BotProperties properties = BotProperties.load(DIRECTORY.resolve("krabot.properties"));
        JDABuilder jdaBuilder = JDABuilder.createDefault(properties.token);
        jdaBuilder.setChunkingFilter(ChunkingFilter.ALL);
        jdaBuilder.setMemberCachePolicy(MemberCachePolicy.ALL);
        jdaBuilder.enableIntents(GatewayIntent.GUILD_MEMBERS);
        jdaBuilder.setActivity(Activity.of(properties.activityType, properties.activityName, properties.activityUrl));
        jdaBuilder.addEventListeners(new JdaEvents(this));
        this.jda = jdaBuilder.build();


    }
    @NotNull
    public User getUser(@NotNull net.dv8tion.jda.api.entities.User jdaUser) {
        User user = userCache.get(jdaUser.getId());
        if (user != null) user.update(jdaUser);
        else {
            Path path = getDirectory(Directory.USERDATA).resolve(jdaUser.getId() + ".json");
            UserData userData = UserData.initialize(path);
            user = new User(this, jdaUser, userData);
            userCache.put(user);
        }
        return user;
    }

    @Nullable
    public User getUser(String userId) {
        return userCache.get(userId);
    }

    @NotNull
    public Guild getGuild(@NotNull net.dv8tion.jda.api.entities.Guild jdaGuild) {
        Guild guild = guildCache.get(jdaGuild.getId());
        if (guild != null) guild.update(jdaGuild);
        else {
            guild = new Guild(this, jdaGuild);
            guildCache.put(guild);
        }
        return guild;
    }

    @Nullable
    public Guild getGuild(String guildId) {
        return guildCache.get(guildId);
    }

    public CommandManager getCommandManager() {
        return commandManager;
    }

    public ButtonManager getButtonManager() {
        return buttonManager;
    }

    public ModalManager getModalManager() {
        return modalManager;
    }

    public int updateCommands() {
        List<SlashCommandData> commands = commandManager.getJdaCommands();
        jda.updateCommands().addCommands(commands).queue();
        return commands.size();
    }

    public void stop() {
        jda.shutdownNow();
    }

    public static Path getDirectory(Directory directory) {
        Path path = DIRECTORY.resolve(directory.toString());
        return initializePath(path);
    }

    public static Path getGuildDirectory(String guildId, GuildDirectory directory) {
        Path path = DIRECTORY.resolve(Directory.GUILDS + "/" + guildId + "/" + directory);
        return initializePath(path);
    }

    public static Path initializePath(Path path) {
        if (Files.exists(path)) return path;
        try {
            return Files.createDirectories(path);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
