package me.levyuri.anticheatbase.managers.profile;

import com.github.retrooper.packetevents.protocol.player.ClientVersion;
import me.levyuri.anticheatbase.Anticheat;
import me.levyuri.anticheatbase.checks.CheckHolder;
import me.levyuri.anticheatbase.enums.Permissions;
import me.levyuri.anticheatbase.exempt.Exempt;
import me.levyuri.anticheatbase.files.Config;
import me.levyuri.anticheatbase.managers.threads.ProfileThread;
import me.levyuri.anticheatbase.playerdata.data.impl.*;
import me.levyuri.anticheatbase.processors.Packet;
import me.levyuri.anticheatbase.utils.ChatUtils;
import me.levyuri.anticheatbase.utils.TaskUtils;
import me.levyuri.anticheatbase.utils.VersionUtils;
import org.bukkit.entity.Player;

import java.util.UUID;

public class Profile {

    //-------------------------------------------
    private final ActionData actionData;
    private final CombatData combatData;
    private final ConnectionData connectionData;
    private final MovementData movementData;
    private final RotationData rotationData;
    private final TeleportData teleportData;
    private final VelocityData velocityData;
    private final VehicleData vehicleData;
    //-------------------------------------------

    //--------------------------------------
    private final CheckHolder checkHolder;
    //--------------------------------------

    //--------------------------------------
    private final ClientVersion version;
    private String client = "Unknown";
    private final boolean bypass;
    //--------------------------------------

    //------------------------------------------
    private final ProfileThread profileThread;
    private final Player player;
    private final UUID uuid;
    //------------------------------------------

    //---------------------------
    private final Exempt exempt;
    //---------------------------

    public Profile(final Player player) {

        //Player Object
        this.player = player;

        //UUID
        this.uuid = player.getUniqueId();

        //Version
        this.version = VersionUtils.getClientVersion(player);

        //Bypass
        this.bypass = !Config.Setting.DISABLE_BYPASS_PERMISSION.getBoolean() && player.hasPermission(Permissions.BYPASS.getPermission());

        //Data
        this.actionData = new ActionData(this);
        this.combatData = new CombatData();
        this.connectionData = new ConnectionData();
        this.movementData = new MovementData(this);
        this.rotationData = new RotationData(this);
        this.teleportData = new TeleportData();
        this.velocityData = new VelocityData();
        this.vehicleData = new VehicleData();

        //Check Holder
        this.checkHolder = new CheckHolder(this);

        //Exempt
        this.exempt = new Exempt(this);

        //Thread
        this.profileThread = Anticheat.getInstance().getThreadManager().getAvailableProfileThread();

        //Initialize Checks
        reloadChecks();
    }

    public boolean isBypassing() {
        return bypass;
    }

    public void handle(final Packet packet) {

        if (this.player == null) return;

        this.connectionData.process(packet);
        this.actionData.process(packet);
        this.combatData.process(packet);
        this.movementData.process(packet);
        this.rotationData.process(packet);
        this.teleportData.process(packet);
        this.velocityData.process(packet);
        this.vehicleData.process(packet);

        if (skip(packet.getTimeStamp())) return;

        this.exempt.handleExempts(packet.getTimeStamp());

        this.checkHolder.runChecks(packet);
    }

    public void kick(final String reason) {

        if (this.player == null) return;

        TaskUtils.task(() -> this.player.kickPlayer(ChatUtils.format(reason)));
    }

    public ClientVersion getVersion() {
        return version;
    }

    public String getClient() {
        return client;
    }

    public void setClient(final String client) {
        this.client = client;
    }

    public Player getPlayer() {
        return this.player;
    }

    public UUID getUUID() {
        return this.uuid;
    }

    public void reloadChecks() {
        this.checkHolder.registerAll();
    }

    private boolean skip(final long currentTime) {
        return this.bypass;
    }

    public void handleTick(final long currentTime) {
    }

    public TeleportData getTeleportData() {
        return teleportData;
    }

    public ActionData getActionData() {
        return actionData;
    }

    public CombatData getCombatData() {
        return combatData;
    }

    public ConnectionData getConnectionData() {
        return connectionData;
    }

    public MovementData getMovementData() {
        return movementData;
    }

    public RotationData getRotationData() {
        return rotationData;
    }

    public VelocityData getVelocityData() {
        return velocityData;
    }

    public VehicleData getVehicleData() {
        return vehicleData;
    }

    public CheckHolder getCheckHolder() {
        return checkHolder;
    }

    public Exempt isExempt() {
        return exempt;
    }

    public ProfileThread getProfileThread() {
        return profileThread;
    }
}