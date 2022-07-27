package com.dentalportal.utils;

import java.util.Arrays;
import java.util.List;

import static com.dentalportal.utils.Permissions.*;

public enum Roles {
    DOCTOR      (Arrays.asList(
        CREATE_PATIENT,
        MODIFY_PATIENT,
        GET_PATIENT,
        GET_PATIENTS,
        MODIFY_DOCTOR,
        CREATE_NURSE,
        CREATE_ASSISTANT,
        MODIFY_NURSE,
        MODIFY_ASSISTANT,
        DELETE_NURSE,
        DELETE_ASSISTANT
    )),
    PATIENT     (Arrays.asList(
        CREATE_PATIENT,
        MODIFY_PATIENT,
        GET_PATIENT
    )),
    ASSISTANT   (Arrays.asList(
        GET_ASSISTANT,
        MODIFY_ASSISTANT,
        GET_PATIENT,
        GET_PATIENTS
    )),
    NURSE       (Arrays.asList(
        GET_PATIENT,
        GET_PATIENTS,
        CREATE_NURSE,
        CREATE_ASSISTANT,
        GET_NURSE,
        GET_ASSISTANT,
        MODIFY_NURSE,
        MODIFY_ASSISTANT
    )),
    ADMIN       (Arrays.asList(ALL));

    private List<Permissions> permissions;

    Roles(List<Permissions> permissions) {
        this.permissions = permissions;
    }

    public List<Permissions> getPermissions() {
        return permissions;
    }
}
