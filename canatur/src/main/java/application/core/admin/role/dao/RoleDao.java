package application.core.admin.role.dao;

import application.model.Role;

public interface RoleDao {
    Role findByRole(String role);
    Role findByID(String role);

}
