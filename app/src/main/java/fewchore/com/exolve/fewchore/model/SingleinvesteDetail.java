
package fewchore.com.exolve.fewchore.model;

import java.util.List;
import com.google.gson.annotations.Expose;

@SuppressWarnings("unused")
public class SingleinvesteDetail {

    @Expose
    private List<InvestmentDetails> msg;
    @Expose
    private Boolean status;

    public List<InvestmentDetails> getMsg() {
        return msg;
    }

    public void setMsg(List<InvestmentDetails> msg) {
        this.msg = msg;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

}
