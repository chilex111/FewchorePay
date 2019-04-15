
package fewchore.com.exolve.fewchore.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@SuppressWarnings("unused")
public class ChargeOnepipeModel {

    @SerializedName("charge_token")
    private String chargeToken;
    @Expose
    private String code;
    @Expose
    private String description;
    @Expose
    private Object error;
    @Expose
    private Object errors;
    @SerializedName("transaction_ref")
    private String transactionRef;

    public String getChargeToken() {
        return chargeToken;
    }

    public void setChargeToken(String chargeToken) {
        this.chargeToken = chargeToken;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Object getError() {
        return error;
    }

    public void setError(Object error) {
        this.error = error;
    }

    public Object getErrors() {
        return errors;
    }

    public void setErrors(Object errors) {
        this.errors = errors;
    }

    public String getTransactionRef() {
        return transactionRef;
    }

    public void setTransactionRef(String transactionRef) {
        this.transactionRef = transactionRef;
    }

}
