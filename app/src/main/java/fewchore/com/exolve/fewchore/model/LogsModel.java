
package fewchore.com.exolve.fewchore.model;

import com.google.gson.annotations.Expose;

@SuppressWarnings("unused")
public class LogsModel {

    @Expose
    private String msg;
    @Expose
    private Boolean status;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

}
