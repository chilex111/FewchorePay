
package fewchore.com.exolve.fewchore.model;

import com.google.gson.annotations.SerializedName;

@SuppressWarnings("unused")
public class TransactionModel {

    @SerializedName("charge_token")
    private String mChargeToken;
    @SerializedName("code")
    private String mCode;
    @SerializedName("description")
    private String mDescription;
    @SerializedName("error")
    private Object mError;
    @SerializedName("errors")
    private Object mErrors;
    @SerializedName("transaction_ref")
    private String mTransactionRef;

    public String getChargeToken() {
        return mChargeToken;
    }

    public void setChargeToken(String chargeToken) {
        mChargeToken = chargeToken;
    }

    public String getCode() {
        return mCode;
    }

    public void setCode(String code) {
        mCode = code;
    }

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String description) {
        mDescription = description;
    }

    public Object getError() {
        return mError;
    }

    public void setError(Object error) {
        mError = error;
    }

    public Object getErrors() {
        return mErrors;
    }

    public void setErrors(Object errors) {
        mErrors = errors;
    }

    public String getTransactionRef() {
        return mTransactionRef;
    }

    public void setTransactionRef(String transactionRef) {
        mTransactionRef = transactionRef;
    }

}
