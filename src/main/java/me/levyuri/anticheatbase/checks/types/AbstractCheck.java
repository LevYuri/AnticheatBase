package me.levyuri.anticheatbase.checks.types;

import me.levyuri.anticheatbase.Anticheat;
import me.levyuri.anticheatbase.api.events.AnticheatViolationEvent;
import me.levyuri.anticheatbase.checks.annotations.Development;
import me.levyuri.anticheatbase.checks.annotations.Disabled;
import me.levyuri.anticheatbase.checks.annotations.Experimental;
import me.levyuri.anticheatbase.checks.enums.CheckCategory;
import me.levyuri.anticheatbase.checks.enums.CheckType;
import me.levyuri.anticheatbase.files.Config;
import me.levyuri.anticheatbase.files.commentedfiles.CommentedFileConfiguration;
import me.levyuri.anticheatbase.managers.profile.Profile;
import me.levyuri.anticheatbase.utils.BetterStream;
import me.levyuri.anticheatbase.utils.MiscUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.LinkedHashSet;
import java.util.Set;

public abstract class AbstractCheck {

    protected final Profile profile;

    private final boolean enabled;

    private final Set<String> commands = new LinkedHashSet<>();

    private final boolean enabledSetback;

    private final String checkName;
    private final String checkType;
    private final String description;
    private final boolean development, disabled, experimental;
    private final CheckCategory checkCategory;
    private final CheckType check;
    private int vl;
    private final int maxVl;
    private float buffer;
    private String verbose = "";

    public AbstractCheck(final Profile profile, final CheckType check, final String type, final String description) {

        this.profile = profile;
        this.check = check;
        this.checkName = check.getCheckName();
        this.checkType = type;
        this.description = description;

        final CommentedFileConfiguration config = Anticheat.getInstance().getChecks();
        final String codeName = this.checkName.toLowerCase();
        final String checkType = type.toLowerCase().replace(" ", "_");

        this.enabledSetback = !Config.Setting.GHOST_MODE.getBoolean()
                && (check == CheckType.SPEED || check == CheckType.FLY || check == CheckType.MOTION);

        this.enabled = type.isEmpty()
                ? config.getBoolean(codeName + ".enabled")
                : config.getBoolean(codeName + "." + checkType + ".enabled", config.getBoolean(codeName + "." + checkType));

        this.maxVl = config.getInt(codeName + ".max_vl");

        if (profile != null) {
            this.commands.addAll(
                    BetterStream.applyAndGet(config.getStringList(codeName + ".commands"),
                            command -> command.replace("%player%", profile.getPlayer().getName())
                    )
            );
        }

        final Class<? extends AbstractCheck> clazz = this.getClass();

        this.development = clazz.isAnnotationPresent(Development.class);

        this.disabled = clazz.isAnnotationPresent(Disabled.class);

        this.experimental = clazz.isAnnotationPresent(Experimental.class);

        this.checkCategory = check.getCheckCategory();
    }

    public String getVerbose() {
        return verbose;
    }

    protected void debug(final Object info) {
        Bukkit.broadcastMessage(String.valueOf(info));
    }

    public void fail(final String verbose) {

        this.verbose = verbose;

        fail();
    }

    public void fail() {

        //Development
        if (this.disabled) return;

        //Just to make sure
        if (this.vl < 0) this.vl = 0;

        final Player player = profile.getPlayer();

        if (player == null) return;

        final AnticheatViolationEvent violationEvent = new AnticheatViolationEvent(
                player,
                this.check,
                this.description,
                this.checkType,
                verbose,
                //Increase the violations here
                this.vl++,
                this.maxVl,
                this.experimental);

        Bukkit.getPluginManager().callEvent(violationEvent);

        if (violationEvent.isCancelled()) {

            this.vl--;

            return;
        }

        if (this.enabledSetback) profile.getMovementData().getSetbackProcessor().setback(true);

        if (this.vl >= this.maxVl && !development) {

            MiscUtils.consoleCommand(this.commands);

            this.vl = 0;
            this.buffer = 0;
        }
    }

    public CheckCategory getCategory() {
        return checkCategory;
    }

    public void resetVl() {
        this.vl = 0;
    }

    public int getVl() {
        return this.vl;
    }

    public void setVl(final int vl) {
        this.vl = vl;
    }

    protected float increaseBuffer() {
        return this.buffer++;
    }

    protected float increaseBufferBy(final double amount) {
        return this.buffer += (float) amount;
    }

    protected float decreaseBuffer() {
        return this.buffer == 0 ? 0 : (this.buffer = Math.max(0, this.buffer - 1));
    }

    protected float decreaseBufferBy(final double amount) {
        return this.buffer == 0 ? 0 : (this.buffer = (float) Math.max(0, this.buffer - amount));
    }

    public void resetBuffer() {
        this.buffer = 0;
    }

    protected float getBuffer() {
        return this.buffer;
    }

    public boolean isEnabled() {
        return this.enabled;
    }

    public String getCheckName() {
        return this.checkName;
    }

    public String getCheckType() {
        return this.checkType;
    }

    public String getDescription() {
        return this.description;
    }
}