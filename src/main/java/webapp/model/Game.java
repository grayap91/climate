package webapp.model;

import org.apache.commons.lang3.tuple.Pair;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Predicate;

/**
 * Created by Gautam on 2018-05-23.
 */
public class Game implements Runnable {

    public static final int numRounds = 10;

    public static final int numGoodsPerPeriod = 3;

    public static final int numPlayersMax = 5;

    private int round = 1;

    private boolean[] allocated = new boolean[numRounds];

    public Set<Player> getPlayerList() {
        return playerList;
    }

    public void setPlayerList(Set<Player> playerList) {
        this.playerList = playerList;
    }

    Set<Player> playerList = new HashSet<>();

    BiddingHistory biddingHistory = new BiddingHistory();

    AllocationHistory allocationHistory = new AllocationHistory();

    public Game(String gameId)
    {
        this.gameId = gameId;
        for(boolean b : allocated)
        {
            b = false;
        }
    }

    String gameId;

    public boolean isReady()
    {
        return playerList.size() == numPlayersMax;
    }

    public Player getPlayerById(String userId)
    {
        for(Player player : playerList)
        {
            if(player.getUserId().equals(userId))
            {
                return player;
            }
        }
        return null;
    }

    public boolean allBidsIn(int round)
    {
        Map map = biddingHistory.getBidMap(round);
        if(map == null)
        {
            return false;
        }
        boolean value = true;
        for(Player player : getPlayerList())
        {
            value = value && map.containsKey(player);
        }
        return value;
    }

    public boolean allHumanBidsIn(int round)
    {
        Map map = biddingHistory.getBidMap(round);
        if(map == null)
        {
            return false;
        }
        boolean value = true;
        for(Player player : getPlayerList())
        {
            if(player.playerType == PlayerType.HUMAN) {
                value = value && map.containsKey(player);
            }
        }
        return value;
    }

    public void processBid(String userId, Bid bid)
    {
        Player player = getPlayerById(userId);
        processBid(player, bid);

    }

    public void processBid(Player player, Bid bid)
    {
        if(playerList.contains(player))
        {
            biddingHistory.addBid(player, bid);
        }

    }

    private void submitRobotBids(int round)
    {
        for(Player player : playerList)
        {
            if(player.playerType == PlayerType.MBS)
            {
                //
                Bid bid = new Bid();
                bid.setGameId(gameId);
                bid.setRound(round);
                bid.setUserId(player.getUserId());
                bid.setBid1(Integer.toString(20));
                bid.setBid2(Integer.toString(20));
                bid.setBid3(Integer.toString(20));
                processBid(player, bid);
            }
        }
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Game game = (Game) o;

        return gameId != null ? gameId.equals(game.gameId) : game.gameId == null;
    }

    @Override
    public int hashCode() {
        return gameId != null ? gameId.hashCode() : 0;
    }

    @Override
    public void run() {
        //error handling to check that the game is in fact ready
        while(round <= numRounds)
        {
            //check if all bids are here
            if(allHumanBidsIn(round) && !allocated[round-1])
            {
                submitRobotBids(round);
                //if bids are here then allocate
                //only do this once
                allocate();
                allocated[round-1] = true;
            }
            else {
                continue;
            }

            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            round++;

        }
        //what do here?
    }

    private void allocate()
    {
        for(Player player : playerList)
        {
            allocationHistory.initializeAllocationForPlayer(player, round);
        }
        //this assumes the bidding history for every player is in
        Map<Player, Bid> map = biddingHistory.getBidMap(round);
        List<Pair<Player, Integer>> list = transformMap(map);
        for(int i = 0; i<= numGoodsPerPeriod-1; i++)
        {
            Player player = list.get(i).getLeft();
            allocationHistory.incrementAllocationForPlayer(player, round);
        }
    }

    private List<Pair<Player, Integer>> transformMap(Map<Player, Bid> map)
    {
        List<Pair<Player, Integer>> list = new ArrayList<>();
        for(Map.Entry<Player, Bid> entry : map.entrySet())
        {
            Pair<Player, Integer> p1 = Pair.of(entry.getKey(), Integer.parseInt(entry.getValue().getBid1()));
            Pair<Player, Integer> p2 = Pair.of(entry.getKey(), Integer.parseInt(entry.getValue().getBid2()));
            Pair<Player, Integer> p3 = Pair.of(entry.getKey(), Integer.parseInt(entry.getValue().getBid3()));
            list.add(p1);
            list.add(p2);
            list.add(p3);
        }
        Collections.sort(list, new Comparator<Pair<Player, Integer>>() {
            @Override
            public int compare(Pair<Player, Integer> o1, Pair<Player, Integer> o2) {
                if(o1.getRight() > o2.getRight())
                {
                    return -1;

                }
                else if(o1.getRight() == o2.getRight())
                {
                    return 0;
                }
                else {
                    return 1;
                }
            }
        });

        System.out.println(list);
        //this sorting part might still be inaccurate
        return list;
    }

    public int getAllocation(String userId, int round)
    {
        return allocationHistory.getAllocationForPlayer(getPlayerById(userId), round);
    }
}
