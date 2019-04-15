
package fewchore.com.exolve.fewchore.model;

import com.google.gson.annotations.SerializedName;

@SuppressWarnings("unused")
public class KeyModel {

    @SerializedName("api_key")
    private String apiKey;
    @SerializedName("secret_key")
    private String secretKey;

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

}
