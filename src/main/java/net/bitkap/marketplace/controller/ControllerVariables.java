package net.bitkap.marketplace.controller;

public interface ControllerVariables {

    String[] devAntPatterns = new String[]{
            "/swagger/**",
            "/h2-console/**"
    };

    String[] publicAntPatterns = new String[]{
            "/v1/productsList/**"
    };

    String[] userAntPatterns = new String[]{
            "/v1/user/**"
    };

    String[] adminAntPatterns = new String[]{
            "/v1/admin/**"
    };

    String[] rootAntPatterns = new String[]{
            "/v1/root/**"
    };

    String ROOT_ROLE_NAME = "root";

    String ADMIN_ROLE_NAME = "admin";

    String USER_ROLE_NAME = "user";

    String ROLE_PREFIX = "ROLE_";

}
