package com.rakib.service.implimentation;
import org.springframework.stereotype.Service;
import com.rakib.domain.UserRole;
import com.rakib.domain.repo.UserRoleRepo;
import com.rakib.service.RoleService;
@Service
public class RoleServiceImpl implements RoleService {
	UserRoleRepo roleRepo;
	public RoleServiceImpl(UserRoleRepo roleRepo) {
		this.roleRepo = roleRepo;
	}


	@Override
	public UserRole saveRole(UserRole userRole) {
		UserRole role = new UserRole();
		role.setUserRole(userRole.getUserRole());
		return roleRepo.save(role);
	}


}
