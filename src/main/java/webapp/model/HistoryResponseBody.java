package webapp.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Gautam on 2018-08-14.
 */
public class HistoryResponseBody {

    private String msg;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }


    public List<UserRoundHistory> getList() {
        return list;
    }

    public void setList(List<UserRoundHistory> list) {
        this.list = list;
    }

    List<UserRoundHistory> list = new ArrayList<>();


}
