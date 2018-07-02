package webapp.model;

import java.util.Map;

/**
 * Created by Gautam on 2018-06-24.
 */
public class AllocationHistory {

    Map<Player, Integer> allocationMap;

    public int getPlayerAllocation(Player player)
    {
        return allocationMap.get(player);
    }

    public void initializePlayer(Player player)
    {
        allocationMap.put(player, 0);
    }

    public void incrementPlayerAllocation(Player player)
    {
        allocationMap.put(player, allocationMap.get(player)+1);
    }
}
