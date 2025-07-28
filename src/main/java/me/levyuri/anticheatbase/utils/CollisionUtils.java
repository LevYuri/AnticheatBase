package me.levyuri.anticheatbase.utils;

import me.levyuri.anticheatbase.Anticheat;
import me.levyuri.anticheatbase.nms.NMSInstance;
import me.levyuri.anticheatbase.utils.custom.CustomLocation;
import org.bukkit.Material;
import org.bukkit.block.Block;

import java.util.ArrayList;
import java.util.List;

public final class CollisionUtils {

    private CollisionUtils() {
    }

    // location.getY() % SERVER_GROUND_DIVISOR
    public static final double SERVER_GROUND_DIVISOR = .015625D;

    // Get all blocks horizontally
    private static final double EXPAND_HORIZONTAL = .300001D;

    // Blocks on top and bellow
    private static final double EXPAND_ADDITIONAL = 2.000000000002E-6;

    /*
    At edge of block
    Math.abs(location.getX() % 1)
    Math.abs(location.getZ() % 1)
     */
    private static final double[] EDGE_MODULOS = {
            .7D,
            .72D,
            .28D,
            .3D
    };

    /*
    Against wall
    Math.abs(location.getX() % 1)
    Math.abs(location.getZ() % 1)
     */
    private static final double[] WALL_MODULOS = {
            // Full Blocks
            .699999988079071D,
            .30000001192092896D,

            // Glass Panes
            .13749998807907104D,
            .862500011920929D,

            // Cobblestone Walls
            .050000011920928955D,
            .949999988079071D,
            .012499988079071045D,
            .987500011920929D,

            // Fences
            .07499998807907104D,
            .925000011920929D,

            // Chests
            .23750001192092896D,
            .762499988079071D,

            // Heads
            .19999998807907104D,
            .800000011920929D,

            // Chains
            .10624998807907104D,
            .893750011920929D,

            // Bamboo
            .9895833283662796D,
            .35624998807907104D,
            .7770833522081375D,
            .14375001192092896D,

            // Anvils
            .824999988079071D,
            .17500001192092896D,
            .11250001192092896D,
            .887499988079071D
    };

    public static boolean isNearWall(final CustomLocation location) {

        final double x = Math.abs(location.getX() % 1);
        final double z = Math.abs(location.getZ() % 1);

        for (final double modulo : WALL_MODULOS) {

            final double moduloX = Math.abs(x - modulo);
            final double moduloZ = Math.abs(z - modulo);

            if (moduloX < 1.706E-13 || moduloZ < 1.706E-13) return true;
        }

        return false;
    }

    public static boolean isNearEdge(final CustomLocation location) {

        final double x = Math.abs(location.getX() % 1);
        final double z = Math.abs(location.getZ() % 1);

        return (x > EDGE_MODULOS[0] && x < EDGE_MODULOS[1])
                || (x > EDGE_MODULOS[2] && x < EDGE_MODULOS[3])
                || (z > EDGE_MODULOS[0] && z < EDGE_MODULOS[1])
                || (z > EDGE_MODULOS[2] && z < EDGE_MODULOS[3]);
    }

    public static float getBlockSlipperiness(final Material type) {

        switch (type) {

            case SLIME_BLOCK:

                return .8F;

            case FROSTED_ICE:
            case ICE:
            case PACKED_ICE:

                return .98F;

            case BLUE_ICE:

                return .989F;

            default:

                return MoveUtils.FRICTION_FACTOR;
        }
    }

    public static boolean isServerGround(final double y) {
        return Math.abs(y) % SERVER_GROUND_DIVISOR == 0D;
    }

    // Block under WITHOUT touching it
    public static boolean hasBlockUnder(final CustomLocation location, final CustomLocation blockLocation) {

        final double locationX = location.getX();
        final double locationY = location.getY();
        final double locationZ = location.getZ();

        final double blockX = blockLocation.getX();
        final double blockY = blockLocation.getY();
        final double blockZ = blockLocation.getZ();

        final double deltaX = MathUtils.getAbsoluteDelta(blockX, locationX);
        final double deltaY = blockY - locationY;
        final double deltaZ = MathUtils.getAbsoluteDelta(blockZ, locationZ);

        return deltaX < 1.3D && deltaY < 0D && deltaZ < 1.3D;
    }

    public static boolean isChunkLoaded(final CustomLocation location) {
        return Anticheat.getInstance().getNmsManager().getNmsInstance().isChunkLoaded(
                location.getWorld(), location.getBlockX(), location.getBlockZ()
        );
    }

    private static Block getBlockAsync(final CustomLocation location) {
        return isChunkLoaded(location) ? location.getBlock() : null;
    }

    public static Block getBlock(final CustomLocation location, final boolean async) {
        return async ? getBlockAsync(location) : location.getBlock();
    }

    // Blocks around player
    public static NearbyBlocksResult getNearbyBlocks(final CustomLocation location, final boolean async) {

        final NearbyBlocksResult result = new NearbyBlocksResult();

        final NMSInstance nms = Anticheat.getInstance().getNmsManager().getNmsInstance();

        // Anti duplicate
        final List<Block> blockPositions = new ArrayList<>();

        final double locationX = location.getX();
        final double locationY = location.getY();
        final double locationZ = location.getZ();

        final double aboveY = locationY + 1.9D;
        final double middleY = locationY + 1D;
        final double underY = locationY - .500001D;

        final CustomLocation cloned = location.clone();

        for (double x = -EXPAND_HORIZONTAL; x <= EXPAND_HORIZONTAL; x += EXPAND_HORIZONTAL) {

            for (double z = -EXPAND_HORIZONTAL; z <= EXPAND_HORIZONTAL; z += EXPAND_HORIZONTAL) {

                final double additionalX = x > 0D ? -EXPAND_ADDITIONAL : EXPAND_ADDITIONAL;
                final double additionalZ = z > 0D ? -EXPAND_ADDITIONAL : EXPAND_ADDITIONAL;

                final double expandX = locationX + x;
                final double expandZ = locationZ + z;

                cloned.setX(expandX + additionalX);
                cloned.setZ(expandZ + additionalZ);

                above:
                {

                    cloned.setY(aboveY);

                    final Block above = getBlock(cloned, async);

                    if (above == null) break above;

                    if (!blockPositions.contains(above)) {

                        result.handle(above, BlockPosition.ABOVE, nms);

                        blockPositions.add(above);
                    }
                }

                under:
                {

                    cloned.setY(underY);

                    final Block under = getBlock(cloned, async);

                    if (under == null) break under;

                    if (!blockPositions.contains(under)) {

                        result.handle(under, BlockPosition.UNDER, nms);

                        blockPositions.add(under);
                    }
                }

                cloned.setX(expandX);
                cloned.setZ(expandZ);

                middle:
                {

                    cloned.setY(middleY);

                    final Block middle = getBlock(cloned, async);

                    if (middle == null) break middle;

                    if (!blockPositions.contains(middle)) {

                        result.handle(middle, BlockPosition.MIDDLE, nms);

                        blockPositions.add(middle);
                    }
                }

                below:
                {

                    cloned.setY(locationY);

                    final Block below = getBlock(cloned, async);

                    if (below == null) break below;

                    if (!blockPositions.contains(below)) {

                        result.handle(below, BlockPosition.BELOW, nms);

                        blockPositions.add(below);
                    }
                }
            }
        }

        return result;
    }

    private enum BlockPosition {
        ABOVE,
        MIDDLE,
        BELOW,
        UNDER
    }

    public static class NearbyBlocksResult {

        private final List<Material> blockTypes = new ArrayList<>();

        private boolean nearGround, blockAbove, nearWaterLogged;

        private void handle(final Block block, final BlockPosition blockPosition, final NMSInstance nms) {

            Material type = nms.getType(block);

            if (type == null) return;

            if (type.isSolid()) {

                switch (blockPosition) {

                    case ABOVE:

                        if (!this.blockAbove) this.blockAbove = true;

                        break;

                    case UNDER:

                        if (!this.nearGround) this.nearGround = true;

                        break;
                }
            }

            if (!this.nearWaterLogged) this.nearWaterLogged = nms.isWaterLogged(block);

            // Anti duplicate
            if (this.blockTypes.contains(type)) return;

            this.blockTypes.add(type);
        }

        public List<Material> getBlockTypes() {
            return blockTypes;
        }

        public boolean isNearGround() {
            return nearGround;
        }

        public boolean hasBlockAbove() {
            return blockAbove;
        }

        public boolean isNearWaterLogged() {
            return nearWaterLogged;
        }
    }
}
