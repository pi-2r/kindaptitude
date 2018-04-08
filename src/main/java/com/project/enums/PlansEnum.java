package com.project.enums;

/**
 * Created by zen on 12/03/17.
 */
public enum PlansEnum {

    USER(1, "ROLE_USER"),
    ADMIN(2, "ROLE_ADMIN");



    private final int id;

    private final String planName;

    PlansEnum(int id, String planName) {
        this.id = id;
        this.planName = planName;
    }

    public int getId() {
        return id;
    }

    public String getPlanName() {
        return planName;
    }
}
