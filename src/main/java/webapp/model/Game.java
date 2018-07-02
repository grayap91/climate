package webapp.model;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by Gautam on 2018-05-23.
 */
public class Game {

    public static final int numPeriods = 10;

    public static final int numGoodsPerPeriod = 3;

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
