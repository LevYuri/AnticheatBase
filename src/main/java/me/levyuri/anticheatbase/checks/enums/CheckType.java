package me.levyuri.anticheatbase.checks.enums;

public enum CheckType {
    AIM("Aim", CheckCategory.COMBAT),
    AUTOCLICKER("AutoClicker", CheckCategory.COMBAT),
    BADPACKETS("BadPackets", CheckCategory.PLAYER),
    FLY("Fly", CheckCategory.MOVEMENT),
    GROUND("Ground", CheckCategory.PLAYER),
    GROUNDSPOOF("GroundSpoof", CheckCategory.MOVEMENT),
    KILLAURA("KillAura", CheckCategory.COMBAT),
    SCAFFOLD("Scaffold", CheckCategory.PLAYER),
    STEP("Step", CheckCategory.MOVEMENT),
    SPEED("Speed", CheckCategory.MOVEMENT),
    MOTION("Motion", CheckCategory.MOVEMENT),
    NOFALL("NoFall", CheckCategory.MOVEMENT),
    JESUS("Jesus", CheckCategory.MOVEMENT),
    VEHICLE("Vehicle", CheckCategory.MOVEMENT),
    ELYTRA("Elytra", CheckCategory.MOVEMENT),
    TIMER("Timer", CheckCategory.PLAYER),
    OMNISPRINT("OmniSprint", CheckCategory.MOVEMENT),
    NOSLOW("NoSlow", CheckCategory.MOVEMENT),
    REACH("Reach", CheckCategory.COMBAT),
    VELOCITY("Velocity", CheckCategory.COMBAT),
    INVENTORY("Inventory", CheckCategory.PLAYER),
    INTERACT("Interact", CheckCategory.PLAYER),
    FASTCLIMB("FastClimb", CheckCategory.MOVEMENT),
    HITBOX("Hitbox", CheckCategory.COMBAT);

    private final String checkName;
    private final CheckCategory checkCategory;

    CheckType(final String checkName, final CheckCategory checkCategory) {
        this.checkName = checkName;
        this.checkCategory = checkCategory;
    }

    public String getCheckName() {
        return checkName;
    }

    public CheckCategory getCheckCategory() {
        return checkCategory;
    }
}