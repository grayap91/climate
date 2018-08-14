package webapp.model;

/**
 * Created by Gautam on 2018-08-14.
 */
public class HistoryRequest {

    private String userId;

    private String gameId;

    private int round;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getGameId() {
        return gameId;
    }

    public void setGameId(String gameId) {
        this.gameId = gameId;
    }

    public int getRound() {
        return round;
    }

    public void setRound(int round) {
        this.round = round;
    }
}
