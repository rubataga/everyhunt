package me.rubataga.everyhunt.roles;

public enum RoleEnum {
    HUNTER,
    TARGET,
    RUNNER;

    public RoleEnum getEnum(String key) {
        if (key.equalsIgnoreCase("hunter")) {
            return HUNTER;
        } else if (key.equalsIgnoreCase("target")){
            return TARGET;
        } else if (key.equalsIgnoreCase("runner")){
            return RUNNER;
        } else {
            return null;
        }
    }

}
