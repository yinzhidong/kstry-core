package cn.kstry.framework.test.role.service;

import cn.kstry.framework.core.annotation.CustomRole;
import cn.kstry.framework.core.annotation.TaskService;
import cn.kstry.framework.core.role.Role;
import cn.kstry.framework.core.util.AssertUtil;
import cn.kstry.framework.core.util.PermissionUtil;

/**
 *
 * @author lykan
 */
@SuppressWarnings("unused")
@CustomRole(name = "custom-role-test")
public class CustomRoleService {

    @TaskService(name = "custom-role-service")
    public void custom(Role role) {
        AssertUtil.notNull(role);
        role.addPermission(PermissionUtil.permissionList("r:say_number"));
    }
}
