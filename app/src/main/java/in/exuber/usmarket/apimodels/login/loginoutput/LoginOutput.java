
package in.exuber.usmarket.apimodels.login.loginoutput;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LoginOutput {

    @SerializedName("statusCode")
    @Expose
    private Integer statusCode;
    @SerializedName("data")
    @Expose
    private Data data;
    @SerializedName("error")
    @Expose
    private Boolean error;

    public Integer getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(Integer statusCode) {
        this.statusCode = statusCode;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public Boolean getError() {
        return error;
    }

    public void setError(Boolean error) {
        this.error = error;
    }

}
