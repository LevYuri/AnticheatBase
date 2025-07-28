package me.levyuri.anticheatbase.nms;

import me.levyuri.anticheatbase.utils.VersionUtils;

public class NMSManager {

    private final NMSInstance nmsInstance;

    public NMSManager() {

        switch (VersionUtils.getServerVersion()) {

            default:
                this.nmsInstance = new InstanceDefault();
                break;

            // TODO: code versions
        }
    }

    public NMSInstance getNmsInstance() {
        return nmsInstance;
    }
}