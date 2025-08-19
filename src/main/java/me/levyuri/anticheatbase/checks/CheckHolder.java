package me.levyuri.anticheatbase.checks;

import me.levyuri.anticheatbase.checks.annotations.Disabled;
import me.levyuri.anticheatbase.checks.impl.aim.*;
import me.levyuri.anticheatbase.checks.impl.badpackets.*;
import me.levyuri.anticheatbase.checks.impl.groundspoof.*;
import me.levyuri.anticheatbase.checks.types.Check;
import me.levyuri.anticheatbase.managers.profile.Profile;
import me.levyuri.anticheatbase.processors.Packet;

import java.util.Arrays;

public class CheckHolder {

    private final Profile profile;
    private Check[] checks;
    private int checksSize;
    private boolean testing; //Used for testing new checks

    public CheckHolder(final Profile profile) {
        this.profile = profile;
    }

    public void runChecks(final Packet packet) {
        for (int i = 0; i < this.checksSize; i++) this.checks[i].handle(packet);
    }

    public void registerAll() {

        addChecks(

                new AimA(this.profile),
                new AimB(this.profile),

                new BadPacketsA(this.profile),

                new GroundSpoofA(this.profile)

        );

        // TODO: Make disabled: disable check
    }

    private void addChecks(final Check... checks) {

        this.checks = new Check[0];

        this.checksSize = 0;

        for (final Check check : checks) {

            //if (this.profile != null && (!check.isEnabled() || isDisabled(check))) continue; // TODO: fix (it just doesn't register any checks if enabled)

            this.checks = Arrays.copyOf(this.checks, this.checksSize + 1);

            this.checks[this.checksSize] = check;

            this.checksSize++;
        }
    }

    private boolean isDisabled(final Check check) {

        if (this.testing) return true;

        if (check.getClass().isAnnotationPresent(Disabled.class)) this.testing = true;

        return false;
    }

    public Check[] getChecks() {
        return checks;
    }
}