
package fewchore.com.exolve.fewchore.model;

import com.google.gson.annotations.Expose;

@SuppressWarnings("unused")
public class LoanTimModel {

    @Expose
    private String duration;
    @Expose
    private String id;

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

}
