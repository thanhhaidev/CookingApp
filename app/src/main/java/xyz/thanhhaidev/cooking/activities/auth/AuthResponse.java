package xyz.thanhhaidev.cooking.activities.auth;

import com.google.gson.annotations.SerializedName;

/**
 * Created by ThanhHaiDev on 30/05/18.
 */

public class AuthResponse {

    @SerializedName("hasura_id")
    int hasuraId;

    @SerializedName("auth_token")
    String authToken;

    @SerializedName("roles")
    String[] roles;

    public int getHasuraId() {
        return hasuraId;
    }

    public String getAuthToken() {
        return authToken;
    }

    public String[] getRoles() {
        return roles;
    }
}
