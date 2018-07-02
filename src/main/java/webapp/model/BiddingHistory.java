package webapp.model;

import java.util.List;

/**
 * Created by Gautam on 2018-06-24.
 */
public class BiddingHistory {

    //multi unit auction, each player submits a set of bids for each time period

    public List<List<Double>> getBids() {
        return bids;
    }

    public void setBids(List<List<Double>> bids) {
        this.bids = bids;
    }

    public void addBid(List<Double> bid)
    {
        bids.add(bid);
    }

    List<List<Double>> bids;
}
