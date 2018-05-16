package webapp.model;

/**
 * Created by Gautam on 2018-05-15.
 */
import java.util.List;

public class AjaxResponseBody {

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public List<User> getResult() {
        return result;
    }

    public void setResult(List<User> result) {
        this.result = result;
    }

    String msg;
    List<User> result;
}