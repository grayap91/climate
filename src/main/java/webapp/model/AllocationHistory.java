package webapp.model;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Gautam on 2018-06-24.
 */
public class AllocationHistory {

    Map<Integer, Map<Player, Integer>> allocationMap = new ConcurrentHashMap<>();
    //key by round

    public int getAllocationForPlayer(Player player, int round)
    {
        return allocationMap.get(round).get(player);
    }

    private void setAllocationForPlayer(Player player, int round, int allocation)
    {
        if(allocationMap.containsKey(round))
        {
            Map map = allocationMap.get(round);
            map.put(player, allocation);
        }
        else
        {
            Map map = new HashMap();
            map.put(player, allocation);
            allocationMap.put(round, map);
        }
    }

    public void initializeAllocationForPlayer(Player player, int round)
    {
        setAllocationForPlayer(player, round, 0);
    }

    public void incrementAllocationForPlayer(Player player, int round)
    {
        if(allocationMap.containsKey(round))
        {
            Map<Player, Integer> map = allocationMap.get(round);
            if(map.containsKey(player))
            {
                int allocation = map.get(player)+1;
                map.put(player, allocation);
            }
            else
            {
                map.put(player, 1);
                allocationMap.put(round, map);
            }
        }
        else
        {
            Map map = new HashMap();
            map.put(player, 1);
            allocationMap.put(round, map);
        }
    }

    public int getTotalAllocation(Player player)
    {
        int total = 0;
        int i=0;
        while(i<=10)
        {
            if(allocationMap.containsKey(i)) {
                total += getAllocationForPlayer(player, i);
            }
            i++;
        }
        return total;
    }


}
