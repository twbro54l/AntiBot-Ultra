package cm.dasp.antibotultra;

import cm.dasp.antibotultra.utils.WebUtils;
import cm.dasp.antibotultra.utils.ZipUtils;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;

public final class AntiBotUltra extends JavaPlugin {
    @Override
    public void onEnable() {
        // Plugin startup logic
        /*
         create a scheduler to run if file does not exist or is older than
         24 hours, 7 days, 30 days, 90 days, 180 days (add this in a config (better yet, make this plugin user configurable)).
         */
        try {
            WebUtils.downloadUsingNIO("https://www.stopforumspam.com/downloads/listed_ip_180.zip", "listed_ip_180.zip");
            ZipUtils.unzipFile(this.getDataFolder().getAbsolutePath() + File.separator + "listed_ip_180.zip", this.getDataFolder().getAbsolutePath());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
