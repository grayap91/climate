package webapp.model;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Gautam on 2018-06-24.
 */

public class BiddingHistory {

    //multi unit auction, each player submits a set of bids for each time period

    public Map<Integer, Map<Player, Bid>> bids = new HashMap<>(10);
    //might need some kind of synchronization here

    public void addBid(Player player, Bid bid)
    {
        int round = bid.getRound();
        if (bids.containsKey(round))
        {
            bids.get(round).put(player, bid);
        }
        else
        {
            Map<Player, Bid> map = new HashMap<>();
            map.put(player, bid);
            bids.put(round, map);
        }

    }

    public Bid getBid(Player p, int round)
    {
        return bids.get(round).get(p);
    }

    public Map<Player, Bid> getBidMap(int round) { return bids.get(round); }


}
