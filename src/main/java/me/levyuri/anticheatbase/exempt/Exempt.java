package me.levyuri.anticheatbase.exempt;

import com.github.retrooper.packetevents.manager.server.ServerVersion;
import me.levyuri.anticheatbase.managers.profile.Profile;
import me.levyuri.anticheatbase.playerdata.data.impl.MovementData;
import me.levyuri.anticheatbase.playerdata.data.impl.RotationData;
import org.bukkit.GameMode;

public class Exempt {

    private final Profile profile;

    public Exempt(final Profile profile) {
        this.profile = profile;
    }

    private boolean aim, autoclicker, cinematic, elytra, jesus, movement, slime, velocity, vehicle, isvoid, spectator;

    public void handleExempts(final long timeStamp) {

        MovementData movementData = profile.getMovementData();
        RotationData rotationData = profile.getRotationData();

        this.slime = movementData.getSinceSlimeTicks() < 20;

        this.cinematic = rotationData.getCinematicProcessor().isCinematic();

        this.movement = movementData.getDeltaXZ() == 0D && movementData.getDeltaY() == 0D;

        this.isvoid = ServerVersion.getLatest().isNewerThan(ServerVersion.V_1_18_1) ? (movementData.getLocation().getY() < -70.0) : (movementData.getLocation().getY() < 0.0);

        this.spectator = ServerVersion.getLatest().isNewerThan(ServerVersion.V_1_8) && (profile.getPlayer().getGameMode() == GameMode.SPECTATOR || profile.getPlayer().getGameMode() == GameMode.SPECTATOR);
    }

    public boolean aim() {
        return this.aim;
    }

    public boolean autoclicker() {
        return this.autoclicker;
    }

    public boolean cinematic() {
        return this.cinematic;
    }

    public boolean elytra() {
        return this.elytra;
    }

    public boolean jesus() {
        return this.jesus;
    }

    public boolean movement() {
        return this.movement;
    }

    public boolean slime() {
        return this.slime;
    }

    public boolean velocity() {
        return this.velocity;
    }

    public boolean vehicle() {
        return this.vehicle;
    }

    public boolean isvoid() {
        return this.isvoid;
    }

    public boolean spectator() {
        return this.spectator;
    }
}