package com.rakib.service;

import com.rakib.domain.UserInfo;
import com.rakib.service.dto.UserDTO;
import org.omg.PortableInterceptor.ORBInitInfoPackage.DuplicateName;

import java.util.List;

public interface UserService {
	UserInfo saveUser(UserDTO userInfo) throws DuplicateName;
	UserInfo getUserByEmail(String email);
	List<UserInfo> getUsers();
	UserInfo updateUser(long id, UserDTO userDTO) throws Exception;
}
