package webapp.model;

/**
 * Created by Gautam on 2019-01-07.
 */
public class OutputRow {
    String userId;
    String value1;
    String value2;
    String value3;
    String bid1;
    String bid2;
    String bid3;


    public int getProfit() {
        return profit;
    }

    public void setProfit(int profit) {
        this.profit = profit;
    }

    int profit;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
    public int getAllocation() {
        return allocation;
    }

    public void setAllocation(int allocation) {
        this.allocation = allocation;
    }

    public int getRound() {
        return round;
    }

    public void setRound(int round) {
        this.round = round;
    }

    int allocation;

    public String getValue1() {
        return value1;
    }

    public void setValue1(String value1) {
        this.value1 = value1;
    }

    public String getValue2() {
        return value2;
    }

    public void setValue2(String value2) {
        this.value2 = value2;
    }

    public String getValue3() {
        return value3;
    }

    public void setValue3(String value3) {
        this.value3 = value3;
    }

    public String getBid1() {
        return bid1;
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

    int round;
    int price;

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }
}
