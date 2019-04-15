
package fewchore.com.exolve.fewchore.model;

import com.google.gson.annotations.SerializedName;

@SuppressWarnings("unused")
public class CardDetails {

    @SerializedName("card_authorization_code")
    private String cardAuthorizationCode;
    @SerializedName("card_bank")
    private String cardBank;
    @SerializedName("card_card_type")
    private String cardCardType;
    @SerializedName("card_created")
    private String cardCreated;
    @SerializedName("card_cvv")
    private Object cardCvv;
    @SerializedName("card_expiry_month")
    private String cardExpiryMonth;
    @SerializedName("card_expiry_year")
    private String cardExpiryYear;
    @SerializedName("card_id")
    private String cardId;
    @SerializedName("card_modified")
    private String cardModified;
    @SerializedName("card_number")
    private String cardNumber;
    @SerializedName("card_reference")
    private String cardReference;
    @SerializedName("card_user_id")
    private String cardUserId;

    public String getCardAuthorizationCode() {
        return cardAuthorizationCode;
    }

    public void setCardAuthorizationCode(String cardAuthorizationCode) {
        this.cardAuthorizationCode = cardAuthorizationCode;
    }

    public String getCardBank() {
        return cardBank;
    }

    public void setCardBank(String cardBank) {
        this.cardBank = cardBank;
    }

    public String getCardCardType() {
        return cardCardType;
    }

    public void setCardCardType(String cardCardType) {
        this.cardCardType = cardCardType;
    }

    public String getCardCreated() {
        return cardCreated;
    }

    public void setCardCreated(String cardCreated) {
        this.cardCreated = cardCreated;
    }

    public Object getCardCvv() {
        return cardCvv;
    }

    public void setCardCvv(Object cardCvv) {
        this.cardCvv = cardCvv;
    }

    public String getCardExpiryMonth() {
        return cardExpiryMonth;
    }

    public void setCardExpiryMonth(String cardExpiryMonth) {
        this.cardExpiryMonth = cardExpiryMonth;
    }

    public String getCardExpiryYear() {
        return cardExpiryYear;
    }

    public void setCardExpiryYear(String cardExpiryYear) {
        this.cardExpiryYear = cardExpiryYear;
    }

    public String getCardId() {
        return cardId;
    }

    public void setCardId(String cardId) {
        this.cardId = cardId;
    }

    public String getCardModified() {
        return cardModified;
    }

    public void setCardModified(String cardModified) {
        this.cardModified = cardModified;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getCardReference() {
        return cardReference;
    }

    public void setCardReference(String cardReference) {
        this.cardReference = cardReference;
    }

    public String getCardUserId() {
        return cardUserId;
    }

    public void setCardUserId(String cardUserId) {
        this.cardUserId = cardUserId;
    }

}
