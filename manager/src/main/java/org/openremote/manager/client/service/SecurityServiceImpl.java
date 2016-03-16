package org.openremote.manager.client.service;

import com.google.gwt.user.client.Window;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import elemental.json.JsonObject;
import org.openremote.manager.client.event.UserChangeEvent;
import org.openremote.manager.client.interop.keycloak.*;
import org.openremote.manager.shared.Consumer;
import org.openremote.manager.shared.Runnable;

public class SecurityServiceImpl implements SecurityService {

    private Keycloak keycloak;
    private CookieService cookieService;
    private EventBus eventBus;
    private String username = null;

    @Inject
    public SecurityServiceImpl(Keycloak keycloak,
                               CookieService cookieService,
                               EventBus eventBus) {
        this.keycloak = keycloak;
        this.cookieService = cookieService;
        this.eventBus = eventBus;

         // TODO Add event handlers for security service?
         onAuthSuccess(() -> {
          eventBus.fireEvent(new UserChangeEvent(getUsername()));
         });

        onAuthLogout(() -> {
            eventBus.fireEvent(new UserChangeEvent(null));
        });

        // Update username
        updateUsername();
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public void login() {
        keycloak.login();
    }

    @Override
    public void login(LoginOptions options) {
        keycloak.login(options);
    }

    @Override
    public void logout() {
        // Keycloak generates an invalid redirect URI as redirects are defined
        // against client in keycloak admin console only valid URI is /master
        LogoutOptions opts = new LogoutOptions();
        opts.redirectUri = "/master";
        keycloak.logout(opts);
    }

    @Override
    public void logout(LogoutOptions options) {
        keycloak.logout(options);
    }

    @Override
    public void register() {
        keycloak.register();
    }

    @Override
    public void register(LoginOptions options) {
        keycloak.register(options);
    }

    @Override
    public boolean hasRealmRole(String role) {
        return keycloak.hasRealmRole(role);
    }

    @Override
    public boolean hasResourceRole(String role, String resource) {
        return keycloak.hasResourceRole(role, resource);
    }

    @Override
    public boolean isTokenExpired() {
        return keycloak.isTokenExpired();
    }

    @Override
    public boolean isTokenExpired(int minValiditySeconds) {
        return keycloak.isTokenExpired(minValiditySeconds);
    }

    @Override
    public void clearToken() {
        keycloak.clearToken();
    }

    @Override
    public void onTokenExpired(Runnable fn) {
        keycloak.onTokenExpired(fn);
    }

    @Override
    public void onAuthSuccess(Runnable fn) {
        keycloak.onAuthSuccess = fn;
    }

    @Override
    public void onAuthLogout(Runnable fn) {
        keycloak.onAuthLogout = fn;
    }

    @Override
    public void updateToken(Consumer<Boolean> successFn, Runnable errorFn) {
        KeycloakCallback kcCallback = keycloak.updateToken();
        kcCallback.success(successFn);
        kcCallback.error(errorFn);
    }

    @Override
    public void updateToken(int minValiditySeconds, Consumer<Boolean> successFn, Runnable errorFn) {
        KeycloakCallback kcCallback = keycloak.updateToken(minValiditySeconds);
        kcCallback.success(successFn);
        kcCallback.error(errorFn);
    }

    @Override
    public String getRealm() {
        return keycloak.realm;
    }

    @Override
    public String getToken() {
        return keycloak.token;
    }

    @Override
    public JsonObject getParsedToken() {
        return keycloak.tokenParsed;
    }

    @Override
    public String getRefreshToken() {
        return keycloak.refreshToken;
    }

    private void updateUsername() {
        username = null;

        if (!isTokenExpired()) {
            JsonObject token = getParsedToken();
            username = token.getString("preferred_username");
        }
    }
}
