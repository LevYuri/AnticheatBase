package me.levyuri.anticheatbase.utils.custom;

import org.bukkit.block.BlockFace;

public class PlacedBlock {

    private final CustomLocation location;
    private final BlockFace face;

    public PlacedBlock(final CustomLocation location, final BlockFace face) {
        this.location = location;
        this.face = face;
    }

    public CustomLocation getLocation() {
        return location;
    }

    public BlockFace getFace() {
        return face;
    }
}
