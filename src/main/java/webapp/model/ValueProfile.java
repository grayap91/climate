package webapp.model;

import java.util.List;

/**
 * Created by Gautam on 2018-08-30.
 */
public class ValueProfile {

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    String msg;

    public List<Integer> getValues() {
        return values;
    }

    public void setValues(List<Integer> values) {
        this.values = values;
    }

    List<Integer> values;
}
