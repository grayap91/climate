package webapp.model;

import java.util.List;

/**
 * Created by Gautam on 2018-08-14.
 */
public class UserRoundHistory {

    private int allocation;

    private List<Integer> price;

    private List<Integer> bids;

    public int getAllocation() {
        return allocation;
    }

    public void setAllocation(int allocation) {
        this.allocation = allocation;
    }

    public List<Integer> getPrice() {
        return price;
    }

    public void setPrice(List<Integer> price) {
        this.price = price;
    }

    public List<Integer> getBids() {
        return bids;
    }

    public void setBids(List<Integer> bids) {
        this.bids = bids;
    }
}
