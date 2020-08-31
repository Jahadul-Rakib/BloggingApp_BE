package com.rakib.service;

import com.rakib.domain.UserInfo;
import com.rakib.service.dto.UserDTO;
import javassist.NotFoundException;
import org.omg.PortableInterceptor.ORBInitInfoPackage.DuplicateName;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserService {
	UserInfo saveUser(UserDTO userInfo) throws DuplicateName;
	UserInfo getUserByEmail(String email);
	Page<UserInfo> getUsers(Pageable pageable);
	UserInfo updateUser(long id, UserDTO userDTO) throws Exception;

	String deleteUser(long id) throws NotFoundException;
}
