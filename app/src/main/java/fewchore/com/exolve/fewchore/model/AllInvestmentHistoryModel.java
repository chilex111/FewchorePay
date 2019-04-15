
package fewchore.com.exolve.fewchore.model;

import java.util.List;
import com.google.gson.annotations.Expose;

@SuppressWarnings("unused")
public class AllInvestmentHistoryModel {

    @Expose
    private List<Investments> msg;
    @Expose
    private Boolean status;

    public List<Investments> getMsg() {
        return msg;
    }

    public void setMsg(List<Investments> msg) {
        this.msg = msg;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

}
