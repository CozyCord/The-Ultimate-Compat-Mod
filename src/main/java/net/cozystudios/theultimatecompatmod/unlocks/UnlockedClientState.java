package net.cozystudios.theultimatecompatmod.unlocks;

import net.minecraft.util.Identifier;

import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

public final class UnlockedClientState {

    private UnlockedClientState() {}

    private static final Set<Identifier> UNLOCKED = new HashSet<>();

    public static synchronized void setAll(Set<Identifier> items) {
        UNLOCKED.clear();
        UNLOCKED.addAll(items);
    }

    public static synchronized Set<Identifier> addAll(Set<Identifier> items) {
        Set<Identifier> newlyAdded = new LinkedHashSet<>();
        for (Identifier id : items) {
            if (UNLOCKED.add(id)) newlyAdded.add(id);
        }
        return newlyAdded;
    }

    public static synchronized boolean isUnlocked(Identifier id) {
        return UNLOCKED.contains(id);
    }

    public static synchronized Set<Identifier> snapshot() {
        return Collections.unmodifiableSet(new HashSet<>(UNLOCKED));
    }

    public static synchronized void clear() {
        UNLOCKED.clear();
    }
}
