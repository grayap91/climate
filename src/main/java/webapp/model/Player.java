package webapp.model;

import webapp.util.Util;

/**
 * Created by Gautam on 2018-05-23.
 */
public class Player {

    PlayerType playerType;

    public Player(String username, PlayerType playerType)
    {
        this.username = username;
        this.userId = Util.getId4Name(username);
        this.playerType = playerType;
    }

    public Player(String username)
    {
        this.username = username;
        this.userId = Util.getId4Name(username);
    }

    String username;

    public String getGameId() {
        return gameId;
    }

    public void setGameId(String gameId) {
        this.gameId = gameId;
    }

    String gameId;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    String userId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Player player = (Player) o;

        return username.equals(player.username);
    }

    @Override
    public int hashCode() {
        return username.hashCode();
    }


}
