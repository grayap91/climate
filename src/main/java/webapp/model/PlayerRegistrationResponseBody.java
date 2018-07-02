package webapp.model;

/**
 * Created by Gautam on 2018-05-28.
 */
public class PlayerRegistrationResponseBody {

    public String getGameId() {
        return gameId;
    }

    public void setGameId(String gameId) {
        this.gameId = gameId;
    }

    String gameId;
    boolean registered;
    String msg;

    public boolean isRegistered() {
        return registered;
    }

    public void setRegistered(boolean registered) {
        this.registered = registered;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
