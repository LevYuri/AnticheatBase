package me.levyuri.anticheatbase.checks.types;

import me.levyuri.anticheatbase.checks.enums.CheckType;
import me.levyuri.anticheatbase.managers.profile.Profile;
import me.levyuri.anticheatbase.processors.Packet;

// AbstractCheck related
public abstract class Check extends AbstractCheck {

    public Check(final Profile profile, final CheckType check, final String type, final String description) {
        super(profile, check, type, description);
    }

    public Check(final Profile profile, final CheckType check, final String description) {
        super(profile, check, "", description);
    }

    public abstract void handle(final Packet packet);
}