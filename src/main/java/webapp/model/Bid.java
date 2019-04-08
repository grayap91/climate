package webapp.model;

/**
 * Created by Gautam on 2018-05-15.
 */
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;

public class Bid {

    public int getRound() {
        return round;
    }

    public void setRound(int round) {
        this.round = round;
    }

    @NotNull(message = "round can't empty!")
    int round;

    @NotBlank(message = "bid can't empty!")
    String bid1;

    @NotBlank(message = "bid can't empty!")
    String bid2;

    @NotBlank(message = "bid can't empty!")
    String bid3;

    @NotBlank(message = "userId can't empty!")
    String userId;

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

    @NotBlank(message = "gameId can't empty!")
    String gameId;

    public String getBid1() {
        return bid1;
    }

    public int getBid1Num()    {
        try{
            return (int)Double.parseDouble(bid1);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return 0;
        }
    }

    public int getBid2Num()    {
        try{
            return (int)Double.parseDouble(bid2);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return 0;
        }
    }

    public int getBid3Num()    {
        try{
            return (int)Double.parseDouble(bid3);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return 0;
        }
    }

    public void setBid1(String bid1) {
        this.bid1 = bid1;
    }

    public String getBid2() {
        return bid2;
    }

    public void setBid2(String bid2) {
        this.bid2 = bid2;
    }

    public String getBid3() {
        return bid3;
    }

    public void setBid3(String bid3) {
        this.bid3 = bid3;
    }
}