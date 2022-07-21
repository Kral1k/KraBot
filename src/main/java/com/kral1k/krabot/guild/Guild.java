package com.kral1k.krabot.guild;

import com.kral1k.krabot.Bot;
import com.kral1k.krabot.game.GameManager;
import com.kral1k.krabot.guild.member.Member;
import com.kral1k.krabot.guild.member.MemberCache;
import com.kral1k.krabot.guild.member.MemberData;
import com.kral1k.krabot.permission.PermissionRole;
import com.kral1k.krabot.permission.PermissionManager;
import com.kral1k.krabot.player.music.MusicPlayer;
import com.kral1k.krabot.player.tts.GoogleTextToSpeech;
import com.kral1k.krabot.utils.GuildDirectory;
import com.kral1k.krabot.utils.jsonconfig.JsonConfig;
import com.kral1k.krabot.utils.jsonconfig.JsonConfigSaver;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.managers.AudioManager;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.nio.file.Path;

public class Guild {
    private final Bot bot;
    private net.dv8tion.jda.api.entities.Guild jdaGuild;
    private final Path path;
    private final JsonConfigSaver<GuildData> dataSaver;
    private final GuildProperties properties;
    private final PermissionManager permissionManager;
    private final MemberCache memberCache = new MemberCache();
    private final MusicPlayer musicPlayer;
    private final GoogleTextToSpeech gTts;
    private final GameManager gameManager;


    public Guild(Bot bot, net.dv8tion.jda.api.entities.Guild jdaGuild) {
        this.bot = bot;
        this.jdaGuild = jdaGuild;
        this.path = Bot.getGuildDirectory(jdaGuild.getId(), GuildDirectory.ROOT);
        this.properties = GuildProperties.load(path.resolve("guild.properties"));
        this.dataSaver = JsonConfig.load(GuildData.class, path.resolve("data.json"));
        this.permissionManager = new PermissionManager(this);
        this.musicPlayer = new MusicPlayer(this);
        this.gTts = GoogleTextToSpeech.create(this);
        this.gameManager = new GameManager(this);
    }

    public Bot getBot() {
        return bot;
    }

    public void update(net.dv8tion.jda.api.entities.Guild jdaGuild) {
        this.jdaGuild = jdaGuild;
    }

    public net.dv8tion.jda.api.entities.Guild getJdaGuild() {
        return jdaGuild;
    }

    public GuildProperties getProperties() {
        return properties;
    }

    public JsonConfigSaver<GuildData> getDataSaver() {
        return dataSaver;
    }

    public void saveData() {
        dataSaver.save();
    }

    public GuildData getData() {
        return dataSaver.get();
    }

    public String getPermissionRoleId(PermissionRole permissionRole) {
        return permissionManager.getRoleId(permissionRole);
    }

    @NotNull
    public Member getMember(@NotNull net.dv8tion.jda.api.entities.Member jdaMember) {
        Member member = memberCache.get(jdaMember.getId());
        if (member != null) member.update(jdaMember);
        else {
            Path path = this.getDirectory(GuildDirectory.MEMBERDATA).resolve(jdaMember.getId() + ".json");
            JsonConfigSaver<MemberData> dataSaver = JsonConfig.load(MemberData.class, path);
            member = new Member(this, jdaMember, dataSaver);
            memberCache.put(member);
        }
        return member;
    }

    public MemberCache getMemberCache() {
        return memberCache;
    }

    @Nullable
    public Member getMember(User jdaUser) {
        return getMember(jdaUser.getId());
    }

    @Nullable
    public Member getMember(String memberId) {
        return memberCache.get(memberId);
    }

    public MusicPlayer getMusicPlayer() {
        return musicPlayer;
    }

    public GoogleTextToSpeech getGTts() {
        return gTts;
    }

    public GameManager getGameManager() {
        return gameManager;
    }

    public String getId() {
        return jdaGuild.getId();
    }

    public AudioManager getAudioManager() {
        return jdaGuild.getAudioManager();
    }

    public Path getDirectory(GuildDirectory directory) {
        Path path = this.path.resolve(directory.toString());
        return Bot.initializePath(path);
    }
}
