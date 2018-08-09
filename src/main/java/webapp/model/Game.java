package webapp.model;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;

/**
 * Created by Gautam on 2018-05-23.
 */
public class Game {

    public static final int numPeriods = 10;

    public static final int numGoodsPerPeriod = 3;

    public static final int numPlayersMax = 5;

    public Set<Player> getPlayerList() {
        return playerList;
    }

    public void setPlayerList(Set<Player> playerList) {
        this.playerList = playerList;
    }

    Set<Player> playerList;

    BiddingHistory biddingHistory;

    AllocationHistory allocationHistory;

    public Game(String gameId)
    {
        this.gameId = gameId;
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
}
