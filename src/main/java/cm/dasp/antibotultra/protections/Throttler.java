package cm.dasp.antibotultra.protections;

import cm.dasp.antibotultra.AntiBotUltra;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitTask;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.logging.Level;

public class Throttler {
    private static final AntiBotUltra ANTI_BOT_ULTRA;

    /*
     * Using injection to get the instance of the plugin
     */
    static {
        ANTI_BOT_ULTRA = (AntiBotUltra) Bukkit.getServer().getPluginManager().getPlugin("AntiBot-Ultra");
    }

    private final int duration;
    private final int maxLogins;
    private final int interval;
    private final boolean botProtectionEnabled;
    private Status status;
    private int flaggedConnections;
    private Instant lastFlagged;
    private BukkitTask antiBotTask;

    public Throttler() {
        this.duration = ANTI_BOT_ULTRA.getConfig().getInt("throttler.duration");
        this.maxLogins = ANTI_BOT_ULTRA.getConfig().getInt("throttler.max-logins");
        this.interval = ANTI_BOT_ULTRA.getConfig().getInt("throttler.intervaL");
        this.botProtectionEnabled = ANTI_BOT_ULTRA.getConfig().getBoolean("throttler.enabled");
        this.antiBotTask = null;
        this.flaggedConnections = 0;
        this.preapare();
    }

    private void preapare() {
        stop();
        status = Status.DISABLED;
        if (!botProtectionEnabled) return;
        status = Status.WAITING;
    }

    public void start() {
        stop();
        status = Status.ACTIVE;
        antiBotTask = Bukkit.getScheduler().runTaskLater(ANTI_BOT_ULTRA, this::stop, duration * 20L);
    }

    public void stop() {
        if (status != Status.ACTIVE) {
            return;
        }
        status = Status.WAITING;

        antiBotTask.cancel();
        antiBotTask = null;

        ANTI_BOT_ULTRA.getLogger().log(Level.INFO, "Task has stopped");
        flaggedConnections = 0;
    }

    public boolean isAllowed() {
        switch (status) {
            case DISABLED:
                return true;
            case ACTIVE:
                return false;
        }
        if (lastFlagged == null) lastFlagged = Instant.now();
        if (ChronoUnit.SECONDS.between(lastFlagged, Instant.now()) <= interval) {
            flaggedConnections++;
        } else {
            flaggedConnections = 1;
            lastFlagged = null;
        }
        if (flaggedConnections > maxLogins) {
            start();
            return false;
        }
        return true;
    }

    private enum Status {
        ACTIVE, WAITING, DISABLED
    }

}

