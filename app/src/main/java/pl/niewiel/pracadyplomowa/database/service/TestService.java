package pl.niewiel.pracadyplomowa.database.service;

import org.json.JSONException;
import org.json.JSONObject;
import pl.niewiel.pracadyplomowa.apiClients.ApiClient;

public class TestService {
    private ApiClient apiClient = new ApiClient();

    private String status;
    private String code;
    private String message;


    public void test() {
        try {
            JSONObject reader = new JSONObject(apiClient.get("test").getBody());

            status = reader.getString("status");
            code = reader.getJSONObject("result").getString("code");
            message = reader.getJSONObject("result").getJSONObject("content").getString("message");
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public String toString() {
        return "status: " + status +
                "\ncode: " + code +
                "\nmessage: " + message;
    }

    public String getStatus() {
        return status;
    }

    private void setStatus(String status) {
        this.status = status;
    }

    public String getCode() {
        return code;
    }

    private void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    private void setMessage(String message) {
        this.message = message;
    }
}
