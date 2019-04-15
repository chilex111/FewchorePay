
package fewchore.com.exolve.fewchore.model;

import com.google.gson.annotations.SerializedName;

@SuppressWarnings("unused")
public class Investments {

    @SerializedName("investment_amount")
    private String investmentAmount;
    @SerializedName("investment_created")
    private String investmentCreated;
    @SerializedName("investment_duedate")
    private String investmentDuedate;
    @SerializedName("investment_id")
    private String investmentId;
    @SerializedName("investment_name")
    private String investmentName;
    @SerializedName("investment_total")
    private String investmentTotal;
    @SerializedName("status_title")
    private String statusTitle;

    public String getInvestmentAmount() {
        return investmentAmount;
    }

    public void setInvestmentAmount(String investmentAmount) {
        this.investmentAmount = investmentAmount;
    }

    public String getInvestmentCreated() {
        return investmentCreated;
    }

    public void setInvestmentCreated(String investmentCreated) {
        this.investmentCreated = investmentCreated;
    }

    public String getInvestmentDuedate() {
        return investmentDuedate;
    }

    public void setInvestmentDuedate(String investmentDuedate) {
        this.investmentDuedate = investmentDuedate;
    }

    public String getInvestmentId() {
        return investmentId;
    }

    public void setInvestmentId(String investmentId) {
        this.investmentId = investmentId;
    }

    public String getInvestmentName() {
        return investmentName;
    }

    public void setInvestmentName(String investmentName) {
        this.investmentName = investmentName;
    }

    public String getInvestmentTotal() {
        return investmentTotal;
    }

    public void setInvestmentTotal(String investmentTotal) {
        this.investmentTotal = investmentTotal;
    }

    public String getStatusTitle() {
        return statusTitle;
    }

    public void setStatusTitle(String statusTitle) {
        this.statusTitle = statusTitle;
    }

}
