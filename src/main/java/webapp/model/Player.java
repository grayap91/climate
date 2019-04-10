package webapp.model;

import webapp.util.Util;

/**
 * Created by Gautam on 2018-05-23.
 */
public class Player {

    PlayerType playerType;

    public ValueType getValueType() {
        return valueType;
    }

    public double getRatio() {
        return ratio;
    }

    public void setRatio(double ratio) {
        this.ratio = ratio;
    }

    private double ratio;

    public double getRatioDeriv() {
        return ratioDeriv;
    }

    public void setRatioDeriv(double ratioDeriv) {
        this.ratioDeriv = ratioDeriv;
    }

    private double ratioDeriv;

    //robot params

    public void setValueType(ValueType valueType) {
        this.valueType = valueType;
    }

    ValueType valueType;

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

        if (playerType != player.playerType) return false;
        if (username != null ? !username.equals(player.username) : player.username != null) return false;
        return userId != null ? userId.equals(player.userId) : player.userId == null;
    }

    @Override
    public int hashCode() {
        int result = playerType != null ? playerType.hashCode() : 0;
        result = 31 * result + (username != null ? username.hashCode() : 0);
        result = 31 * result + (userId != null ? userId.hashCode() : 0);
        return result;
    }
}
