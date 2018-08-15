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

    public Map<Integer, Map<Player, List<Integer>>> prices = new HashMap<>(10);

    public void addPrice(Player player, int round, int price)
    {
        Map<Player, List<Integer>> map;
        if(prices.containsKey(round))
        {
            map = prices.get(round);
            if(map.containsKey(player))
            {
                map.get(player).add(price);
            }
            else
            {
                List<Integer> list = new ArrayList<>();
                list.add(price);
                map.put(player, list);
            }
        }
        else
        {
            map = new HashMap<>();
            List<Integer> list = new ArrayList<>();
            list.add(price);
            map.put(player, list);
            prices.put(round, map);
        }


    }

    public List<Integer> getPrices(Player player, int round)
    {
        List<Integer> list = new ArrayList<>();
        if(prices.containsKey(round))
        {
            if(prices.get(round).containsKey(player))
            {
                return prices.get(round).get(player);
            }
        }
        return list;
    }


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
