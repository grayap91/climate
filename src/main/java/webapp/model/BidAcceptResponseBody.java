package webapp.model;

/**
 * Created by Gautam on 2018-05-15.
 */
import java.util.List;

public class BidAcceptResponseBody {

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }



    String msg;

    int numAllocated;

    public int getNumAllocated() {
        return numAllocated;
    }

    public void setNumAllocated(int numAllocated) {
        this.numAllocated = numAllocated;
    }
}