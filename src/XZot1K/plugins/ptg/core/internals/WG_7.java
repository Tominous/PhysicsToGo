package XZot1K.plugins.ptg.core.internals;

import XZot1K.plugins.ptg.PhysicsToGo;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import com.sk89q.worldguard.protection.regions.RegionQuery;
import org.bukkit.Location;

import java.util.ArrayList;
import java.util.List;

public class WG_7 {

    public static boolean passedWorldGuardHook(Location location) {
        boolean isBlacklist = PhysicsToGo.getPluginInstance().getConfig().getBoolean("hooks-options.world-guard.blacklist"),
                blockInAll = PhysicsToGo.getPluginInstance().getConfig().getBoolean("hooks-options.world-guard.block-in-all");
        RegionQuery query = WorldGuard.getInstance().getPlatform().getRegionContainer().createQuery();
        com.sk89q.worldedit.util.Location worldEditLocation = BukkitAdapter.adapt(location);

        ApplicableRegionSet regionsAtLocation = query.getApplicableRegions(worldEditLocation);
        List<ProtectedRegion> regionList = new ArrayList<>(regionsAtLocation.getRegions());
        List<String> regionWhitelist = PhysicsToGo.getPluginInstance().getConfig().getStringList("hooks-options.world-guard.region-list");
        if (blockInAll && regionList.size() > 0) {
            return false;
        }

        if (!blockInAll) {
            for (int i = -1; ++i < regionList.size(); ) {
                ProtectedRegion protectedRegion = regionList.get(i);

                boolean isInList = isInList(regionWhitelist, protectedRegion.getId());
                if (isInList) return !isBlacklist;
            }

            return isBlacklist;
        } else return true;
    }

    private static boolean isInList(List<String> stringList, String string) {
        for (int i = -1; ++i < stringList.size(); ) if (stringList.get(i).equalsIgnoreCase(string)) return true;
        return false;
    }
}
