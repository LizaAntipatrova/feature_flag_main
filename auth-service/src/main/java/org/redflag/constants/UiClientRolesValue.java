package org.redflag.constants;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class UiClientRolesValue {
    public static final String CREATE_FEATURE_FLAG = "CREATE_FEATURE_FLAG_ROLE";
    public static final String READ_FEATURE_FLAG = "READ_FEATURE_FLAG_ROLE";
    public static final String UPDATE_FEATURE_FLAG = "UPDATE_FEATURE_FLAG_ROLE";
    public static final String DELETE_FEATURE_FLAG = "DELETE_FEATURE_FLAG_ROLE";

    public static final String CREATE_EMPLOYEE = "CREATE_EMPLOYEE_ROLE";
    public static final String READ_EMPLOYEE = "READ_EMPLOYEE_ROLE";
    public static final String UPDATE_EMPLOYEE = "UPDATE_EMPLOYEE_ROLE";
    public static final String DELETE_EMPLOYEE = "DELETE_EMPLOYEE_ROLE";

    public static final String CREATE_DEPARTMENT = "CREATE_DEPARTMENT_ROLE";
    public static final String READ_DEPARTMENT = "READ_DEPARTMENT_ROLE";
    public static final String UPDATE_DEPARTMENT = "UPDATE_DEPARTMENT_ROLE";
    public static final String DELETE_DEPARTMENT = "DELETE_DEPARTMENT_ROLE";
}
