
package fewchore.com.exolve.fewchore.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@SuppressWarnings("unused")
public class Card {

    @Expose
    private String identifier;
    @SerializedName("masked_pan")
    private String maskedPan;
    @SerializedName("require_cvv")
    private Boolean requireCvv;
    @SerializedName("require_expiry")
    private Boolean requireExpiry;
    @SerializedName("require_pin")
    private Boolean requirePin;

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public String getMaskedPan() {
        return maskedPan;
    }

    public void setMaskedPan(String maskedPan) {
        this.maskedPan = maskedPan;
    }

    public Boolean getRequireCvv() {
        return requireCvv;
    }

    public void setRequireCvv(Boolean requireCvv) {
        this.requireCvv = requireCvv;
    }

    public Boolean getRequireExpiry() {
        return requireExpiry;
    }

    public void setRequireExpiry(Boolean requireExpiry) {
        this.requireExpiry = requireExpiry;
    }

    public Boolean getRequirePin() {
        return requirePin;
    }

    public void setRequirePin(Boolean requirePin) {
        this.requirePin = requirePin;
    }

}
