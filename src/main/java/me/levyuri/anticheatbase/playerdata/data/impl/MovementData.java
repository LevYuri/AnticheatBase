package me.levyuri.anticheatbase.playerdata.data.impl;

import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientPlayerPosition;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientPlayerPositionAndRotation;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientPlayerRotation;
import me.levyuri.anticheatbase.Anticheat;
import me.levyuri.anticheatbase.managers.profile.Profile;
import me.levyuri.anticheatbase.nms.NMSInstance;
import me.levyuri.anticheatbase.playerdata.data.Data;
import me.levyuri.anticheatbase.playerdata.processors.impl.PredictionProcessor;
import me.levyuri.anticheatbase.playerdata.processors.impl.SetbackProcessor;
import me.levyuri.anticheatbase.processors.Packet;
import me.levyuri.anticheatbase.utils.CollisionUtils;
import me.levyuri.anticheatbase.utils.MoveUtils;
import me.levyuri.anticheatbase.utils.custom.CustomLocation;
import me.levyuri.anticheatbase.utils.custom.Equipment;
import me.levyuri.anticheatbase.utils.fastmath.FastMath;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Vehicle;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class MovementData implements Data {

    private final Profile profile;

    private final Equipment equipment;

    private final SetbackProcessor setbackProcessor;
    private final PredictionProcessor predictionProcessor;

    private double deltaX, lastDeltaX, deltaZ, lastDeltaZ, deltaY, lastDeltaY, deltaXZ, lastDeltaXZ,
            accelXZ, lastAccelXZ, accelY, lastAccelY;

    private float fallDistance, lastFallDistance,
            baseGroundSpeed, baseAirSpeed,
            frictionFactor = MoveUtils.FRICTION_FACTOR, lastFrictionFactor = MoveUtils.FRICTION_FACTOR;

    private CustomLocation location, lastLocation;

    private final List<Material> nearbyBlocks = new ArrayList<>();

    private boolean onGround, lastOnGround, serverGround, lastServerGround, inAir, onIce, onSlime, blockNearHead;

    private int flyTicks, serverGroundTicks, lastServerGroundTicks, nearGroundTicks, lastNearGroundTicks, blocksAboveTicks, lastBlocksAboveTicks, halfBlocksTicks, lastHalfBlocksTicks,
            lastUnloadedChunkTicks = 100,
            clientGroundTicks, lastNearWallTicks,
            lastFrictionFactorUpdateTicks, lastNearEdgeTicks, airTicks, iceTicks, slimeTicks, sinceIceTicks, sinceSlimeTicks, blockNearHeadTicks, sinceBlockNearHeadTicks;

    public MovementData(final Profile profile) {
        this.profile = profile;

        this.equipment = new Equipment();
        this.setbackProcessor = new SetbackProcessor(profile);
        this.predictionProcessor = new PredictionProcessor(profile);

        this.location = this.lastLocation = new CustomLocation(profile.getPlayer().getLocation());
    }

    @Override
    public void process(final Packet packet) {

        final World world = profile.getPlayer().getWorld();

        final long currentTime = packet.getTimeStamp();

        switch (packet.getType()) {

            case PLAYER_POSITION:

                final WrapperPlayClientPlayerPosition move = packet.getPositionWrapper();

                this.lastOnGround = this.onGround;
                this.onGround = move.isOnGround();

                this.flyTicks = this.onGround ? 0 : this.flyTicks + 1;
                this.clientGroundTicks = this.onGround ? this.clientGroundTicks + 1 : 0;

                this.lastLocation = this.location;
                this.location = new CustomLocation(
                        world,
                        move.getLocation().getX(), move.getLocation().getY(), move.getLocation().getZ(),
                        this.location.getYaw(), this.location.getPitch(),
                        currentTime
                );

                processLocationData();

                break;

            case PLAYER_POSITION_AND_ROTATION:

                final WrapperPlayClientPlayerPositionAndRotation posLook = packet.getPositionLookWrapper();

                this.lastOnGround = this.onGround;
                this.onGround = posLook.isOnGround();

                this.flyTicks = this.onGround ? 0 : this.flyTicks + 1;
                this.clientGroundTicks = this.onGround ? this.clientGroundTicks + 1 : 0;

                this.lastLocation = this.location;
                this.location = new CustomLocation(
                        world,
                        posLook.getLocation().getX(), posLook.getLocation().getY(), posLook.getLocation().getZ(),
                        posLook.getYaw(), posLook.getPitch(),
                        currentTime
                );

                processLocationData();

                break;

            case PLAYER_ROTATION:

                final WrapperPlayClientPlayerRotation look = packet.getLookWrapper();

                this.lastOnGround = this.onGround;
                this.onGround = look.isOnGround();

                this.flyTicks = this.onGround ? 0 : this.flyTicks + 1;
                this.clientGroundTicks = this.onGround ? this.clientGroundTicks + 1 : 0;

                this.lastLocation = this.location;
                this.location = new CustomLocation(
                        world,
                        this.location.getX(), this.location.getY(), this.location.getZ(),
                        look.getYaw(), look.getPitch(),
                        currentTime
                );

                processLocationData();

                break;
        }
    }

    private void processLocationData() {

        final double lastDeltaX = this.deltaX;
        final double deltaX = this.location.getX() - this.lastLocation.getX();

        this.lastDeltaX = lastDeltaX;
        this.deltaX = deltaX;

        final double lastDeltaY = this.deltaY;
        final double deltaY = this.location.getY() - this.lastLocation.getY();

        this.lastDeltaY = lastDeltaY;
        this.deltaY = deltaY;

        final double lastAccelY = this.accelY;
        final double accelY = Math.abs(lastDeltaY - deltaY);

        this.lastAccelY = lastAccelY;
        this.accelY = accelY;

        final double lastDeltaZ = this.deltaZ;
        final double deltaZ = this.location.getZ() - this.lastLocation.getZ();

        this.lastDeltaZ = lastDeltaZ;
        this.deltaZ = deltaZ;

        final double lastDeltaXZ = this.deltaXZ;
        final double deltaXZ = FastMath.hypot(deltaX, deltaZ);

        this.lastDeltaXZ = lastDeltaXZ;
        this.deltaXZ = deltaXZ;

        final double lastAccelXZ = this.accelXZ;
        final double accelXZ = Math.abs(lastDeltaXZ - deltaXZ);

        this.lastAccelXZ = lastAccelXZ;
        this.accelXZ = accelXZ;

        //Process data
        processPlayerData();
    }

    private void handleNearbyBlocks() {

        final CollisionUtils.NearbyBlocksResult nearbyBlocksResult = CollisionUtils.getNearbyBlocks(this.location, false);

        // Near Ground

        this.lastNearGroundTicks = this.nearGroundTicks;

        this.nearGroundTicks = nearbyBlocksResult.isNearGround() ? 0 : this.nearGroundTicks + 1;

        // Block Above

        this.lastBlocksAboveTicks = this.blocksAboveTicks;

        this.blocksAboveTicks = nearbyBlocksResult.hasBlockAbove() ? 0 : this.blocksAboveTicks + 1;

        // Half Block

        this.lastHalfBlocksTicks = this.halfBlocksTicks;

        this.inAir = nearbyBlocksResult.getBlockTypes().stream().allMatch(block -> block == Material.AIR);
        this.onIce =  nearbyBlocksResult.getBlockTypes().stream().anyMatch(block -> block.toString().contains("ICE"));
        this.onSlime = nearbyBlocksResult.getBlockTypes().stream().anyMatch(block -> block.toString().equalsIgnoreCase("SLIME_BLOCK"));
        this.blockNearHead = nearbyBlocksResult.hasBlockAbove();
    }

    public void handleTick() {
        this.airTicks = inAir ? airTicks + 1 : 0;
        this.iceTicks = onIce ? iceTicks + 1 : 0;
        this.slimeTicks = onSlime ? slimeTicks + 1 : 0;
        this.blockNearHeadTicks = blockNearHead ? blockNearHeadTicks + 1 : 0;

        this.sinceIceTicks = onIce ? 0 : this.sinceIceTicks + 1;
        this.sinceSlimeTicks = onSlime ? 0 : this.sinceSlimeTicks + 1;
        this.sinceBlockNearHeadTicks = blockNearHead ? 0 : sinceBlockNearHeadTicks + 1;
    }

    private void processPlayerData() {

        final Player player = profile.getPlayer();

        final NMSInstance nms = Anticheat.getInstance().getNmsManager().getNmsInstance();

        // Chunk

        if ((this.lastUnloadedChunkTicks = nms.isChunkLoaded(
                this.location.getWorld(), this.location.getBlockX(), this.location.getBlockZ())
                ? this.lastUnloadedChunkTicks + 1 : 0) > 10) {

            // Nearby Blocks

            handleNearbyBlocks();

            // Friction Factor

            this.frictionFactor = CollisionUtils.getBlockSlipperiness(
                    nms.getType(this.location.clone().subtract(0D, .825D, 0D).getBlock())
            );

            this.lastFrictionFactorUpdateTicks = this.frictionFactor != this.lastFrictionFactor ? 0 : this.lastFrictionFactorUpdateTicks + 1;

            this.lastFrictionFactor = this.frictionFactor;
        }

        // Setbacks

        if (this.nearGroundTicks > 1) this.setbackProcessor.process();

        // Near Wall

        this.lastNearWallTicks = CollisionUtils.isNearWall(this.location) ? 0 : this.lastNearWallTicks + 1;

        // Near Edge

        this.lastNearEdgeTicks = this.lastNearGroundTicks == 0 && CollisionUtils.isNearEdge(this.location) ? 0 : this.lastNearEdgeTicks + 1;

        // Server Ground

        final boolean lastServerGround = this.serverGround;

        final boolean serverGround = CollisionUtils.isServerGround(this.location.getY());

        this.lastServerGround = lastServerGround;

        this.serverGround = serverGround;

        this.serverGroundTicks = serverGround ? this.serverGroundTicks + 1 : 0;

        this.lastServerGroundTicks = serverGround ? 0 : this.lastServerGroundTicks + 1;

        // Equipment

        this.equipment.handle(player);

        // Fall Distance

        this.lastFallDistance = this.fallDistance;

        this.fallDistance = nms.getFallDistance(player);

        // Base Speed

        this.baseGroundSpeed = MoveUtils.getBaseGroundSpeed(profile);

        this.baseAirSpeed = MoveUtils.getBaseAirSpeed(profile);

        // Prediction Processor
        this.predictionProcessor.process();
    }

    public int getLastNearEdgeTicks() {
        return lastNearEdgeTicks;
    }

    public int getLastFrictionFactorUpdateTicks() {
        return lastFrictionFactorUpdateTicks;
    }

    public float getFrictionFactor() {
        return frictionFactor;
    }

    public Equipment getEquipment() {
        return equipment;
    }

    public float getBaseAirSpeed() {
        return baseAirSpeed;
    }

    public float getBaseGroundSpeed() {
        return baseGroundSpeed;
    }

    public int getLastNearWallTicks() {
        return lastNearWallTicks;
    }

    public int getClientGroundTicks() {
        return clientGroundTicks;
    }

    public int getLastUnloadedChunkTicks() {
        return lastUnloadedChunkTicks;
    }

    public double getDeltaX() {
        return deltaX;
    }

    public double getLastDeltaX() {
        return lastDeltaX;
    }

    public double getDeltaZ() {
        return deltaZ;
    }

    public double getLastDeltaZ() {
        return lastDeltaZ;
    }

    public double getDeltaY() {
        return deltaY;
    }

    public double getLastDeltaY() {
        return lastDeltaY;
    }

    public double getDeltaXZ() {
        return deltaXZ;
    }

    public double getLastDeltaXZ() {
        return lastDeltaXZ;
    }

    public double getAccelXZ() {
        return accelXZ;
    }

    public double getLastAccelXZ() {
        return lastAccelXZ;
    }

    public double getAccelY() {
        return accelY;
    }

    public double getLastAccelY() {
        return lastAccelY;
    }

    public float getFallDistance() {
        return fallDistance;
    }

    public float getLastFallDistance() {
        return lastFallDistance;
    }

    public CustomLocation getLocation() {
        return location;
    }

    public CustomLocation getLastLocation() {
        return lastLocation;
    }

    public SetbackProcessor getSetbackProcessor() {
        return setbackProcessor;
    }

    public PredictionProcessor getPredictionProcessor() {
        return predictionProcessor;
    }

    public boolean isOnGround() {
        return onGround;
    }

    public boolean isServerGround() {
        return serverGround;
    }

    public int getFlyTicks() {
        return flyTicks;
    }

    public int getLastServerGroundTicks() {
        return lastServerGroundTicks;
    }

    public int getServerGroundTicks() {
        return serverGroundTicks;
    }

    public boolean isLastOnGround() {
        return lastOnGround;
    }

    public boolean isLastServerGround() {
        return lastServerGround;
    }

    public int getNearGroundTicks() {
        return nearGroundTicks;
    }

    public int getLastNearGroundTicks() {
        return lastNearGroundTicks;
    }

    public int getBlocksAboveTicks() {
        return blocksAboveTicks;
    }

    public int getLastBlocksAboveTicks() {
        return lastBlocksAboveTicks;
    }

    public int getHalfBlocksTicks() {
        return halfBlocksTicks;
    }

    public int getLastHalfBlocksTicks() {
        return lastHalfBlocksTicks;
    }

    public List<Material> getNearbyBlocks() {
        return nearbyBlocks;
    }
    public int getSlimeTicks() {
        return slimeTicks;
    }
    public int getIceTicks() {return iceTicks;}
    public int getSinceIceTicks() {return sinceIceTicks;}
    public int getSinceSlimeTicks() {return sinceSlimeTicks;}

    public boolean hasBlockNearHead() {return blockNearHead;}
    public int getBlockNearHeadTicks() {return blockNearHeadTicks;}
    public int getSinceBlockNearHeadTicks() {return sinceBlockNearHeadTicks;}

    public int getAirTicks() {
        return airTicks;
    }

    public boolean isNearVehicle() {
        for (final Entity entity : getEntitiesWithinRadius(profile.getPlayer().getLocation(), 2)) {
            if (entity instanceof Vehicle) return true;
        }

        return false;
    }

    public List<Entity> getEntitiesWithinRadius(final Location location, final double radius) {

        final double expander = 16.0D;

        final double x = location.getX();
        final double z = location.getZ();

        final int minX = (int) Math.floor((x - radius) / expander);
        final int maxX = (int) Math.floor((x + radius) / expander);

        final int minZ = (int) Math.floor((z - radius) / expander);
        final int maxZ = (int) Math.floor((z + radius) / expander);

        final World world = location.getWorld();

        List<Entity> entities = new LinkedList<>();

        for (int xVal = minX; xVal <= maxX; xVal++) {

            for (int zVal = minZ; zVal <= maxZ; zVal++) {

                if (!world.isChunkLoaded(xVal, zVal)) continue;

                for (Entity entity : world.getChunkAt(xVal, zVal).getEntities()) {
                    //We have to do this due to stupidness
                    if (entity == null) continue;

                    //Make sure the entity is within the radius specified
                    if (entity.getLocation().distanceSquared(location) > radius * radius) continue;

                    entities.add(entity);
                }
            }
        }

        return entities;
    }
}
