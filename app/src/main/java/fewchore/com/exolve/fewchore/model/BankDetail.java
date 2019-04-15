
package fewchore.com.exolve.fewchore.model;

import com.google.gson.annotations.SerializedName;

@SuppressWarnings("unused")
public class BankDetail {

    @SerializedName("bank_name")
    private String bankName;
    @SerializedName("userbank_accno")
    private String userbankAccno;
    @SerializedName("userbank_accttype")
    private String userbankAccttype;
    @SerializedName("userbank_id")
    private String userbankId;

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getUserbankAccno() {
        return userbankAccno;
    }

    public void setUserbankAccno(String userbankAccno) {
        this.userbankAccno = userbankAccno;
    }

    public String getUserbankAccttype() {
        return userbankAccttype;
    }

    public void setUserbankAccttype(String userbankAccttype) {
        this.userbankAccttype = userbankAccttype;
    }

    public String getUserbankId() {
        return userbankId;
    }

    public void setUserbankId(String userbankId) {
        this.userbankId = userbankId;
    }

}
