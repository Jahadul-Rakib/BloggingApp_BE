package com.rakib.service.implimentation;
import org.springframework.stereotype.Service;
import com.rakib.domain.Role;
import com.rakib.domain.repo.UserRoleRepo;
import com.rakib.service.RoleService;
@Service
public class RoleServiceImpl implements RoleService {
	UserRoleRepo roleRepo;
	public RoleServiceImpl(UserRoleRepo roleRepo) {
		this.roleRepo = roleRepo;
	}

	@Override
	public Role saveRole(Role userRole) {
		Role role = new Role();
		role.setName(userRole.getName());
		return roleRepo.save(role);
	}


}
