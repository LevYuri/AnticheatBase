package me.levyuri.anticheatbase.utils.custom;

import me.levyuri.anticheatbase.utils.SampleList;
import org.bukkit.Location;
import org.bukkit.util.Vector;

import java.util.LinkedList;
import java.util.List;

public class PastLocations {

    private final SampleList<CustomLocation> pastLocations = new SampleList<>(20, true);

    public List<CustomLocation> getEstimatedLocationsFromPing(final long currentTime, final long playerPing, final long locationsNumber) {

        final List<CustomLocation> locations = new LinkedList<>();

        final long deltaTime = currentTime - playerPing;

        for (final CustomLocation location : this.pastLocations) {

            if (Math.abs(deltaTime - location.getTimeStamp()) < locationsNumber) {

                locations.add(location);
            }
        }

        if (locations.isEmpty()) locations.add(this.pastLocations.getLast());

        return locations;
    }

    public List<Vector> getEstimatedVectorsFromPing(final long currentTime, final long playerPing, final long locationsNumber) {

        final List<Vector> locations = new LinkedList<>();

        final long deltaTime = currentTime - playerPing;

        for (final CustomLocation location : this.pastLocations) {

            if (Math.abs(deltaTime - location.getTimeStamp()) < locationsNumber) {

                locations.add(location.toVector());
            }
        }

        if (locations.isEmpty()) locations.add(this.pastLocations.getLast().toVector());

        return locations;
    }

    public boolean isCollected() {
        return this.pastLocations.isCollected();
    }

    public void clear() {
        this.pastLocations.clear();
    }

    public void addLocation(final Location location) {
        this.pastLocations.add(new CustomLocation(location));
    }

    public SampleList<CustomLocation> getPastLocations() {
        return pastLocations;
    }
}
