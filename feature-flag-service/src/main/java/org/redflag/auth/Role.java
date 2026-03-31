package org.redflag.auth;

import lombok.Getter;

import java.util.Arrays;

@Getter
public enum Role {
    CREATE_FEATURE_FLAG_ROLE(Role.CREATE_FEATURE_FLAG_ROLE_NAME),
    READ_FEATURE_FLAG_ROLE(Role.READ_FEATURE_FLAG_ROLE_NAME),
    UPDATE_FEATURE_FLAG_ROLE(Role.UPDATE_FEATURE_FLAG_ROLE_NAME),
    DELETE_FEATURE_FLAG_ROLE(Role.DELETE_FEATURE_FLAG_ROLE_NAME),
    CREATE_EMPLOYEE_ROLE(Role.CREATE_EMPLOYEE_ROLE_NAME),
    READ_EMPLOYEE_ROLE(Role.READ_EMPLOYEE_ROLE_NAME),
    UPDATE_EMPLOYEE_ROLE(Role.UPDATE_EMPLOYEE_ROLE_NAME),
    DELETE_EMPLOYEE_ROLE(Role.DELETE_EMPLOYEE_ROLE_NAME),
    CREATE_DEPARTMENT_ROLE(Role.CREATE_DEPARTMENT_ROLE_NAME),
    READ_DEPARTMENT_ROLE(Role.READ_DEPARTMENT_ROLE_NAME),
    UPDATE_DEPARTMENT_ROLE(Role.UPDATE_DEPARTMENT_ROLE_NAME),
    DELETE_DEPARTMENT_ROLE(Role.DELETE_DEPARTMENT_ROLE_NAME),;

    public static final String CREATE_FEATURE_FLAG_ROLE_NAME = "CREATE_FEATURE_FLAG_ROLE";
    public static final String READ_FEATURE_FLAG_ROLE_NAME = "READ_FEATURE_FLAG_ROLE";
    public static final String UPDATE_FEATURE_FLAG_ROLE_NAME = "UPDATE_FEATURE_FLAG_ROLE";
    public static final String DELETE_FEATURE_FLAG_ROLE_NAME = "DELETE_FEATURE_FLAG_ROLE";
    public static final String CREATE_EMPLOYEE_ROLE_NAME = "CREATE_EMPLOYEE_ROLE";
    public static final String READ_EMPLOYEE_ROLE_NAME = "READ_EMPLOYEE_ROLE";
    public static final String UPDATE_EMPLOYEE_ROLE_NAME = "UPDATE_EMPLOYEE_ROLE";
    public static final String DELETE_EMPLOYEE_ROLE_NAME = " DELETE_EMPLOYEE_ROLE";
    public static final String CREATE_DEPARTMENT_ROLE_NAME = "CREATE_DEPARTMENT_ROLE";
    public static final String READ_DEPARTMENT_ROLE_NAME = "READ_DEPARTMENT_ROLE";
    public static final String UPDATE_DEPARTMENT_ROLE_NAME = "UPDATE_DEPARTMENT_ROLE";
    public static final String DELETE_DEPARTMENT_ROLE_NAME = "DELETE_DEPARTMENT_ROLE";


    private final String name;
    Role(String name) {
        this.name = name;
    }

    public static Role getRoleByName(String name){
        return Arrays.stream(values()).filter(role -> role.name.equals(name))
                .findAny().get();
    }
}
