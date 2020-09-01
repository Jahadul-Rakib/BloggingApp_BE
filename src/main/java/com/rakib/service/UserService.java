package com.rakib.service;

import com.rakib.domain.UserInfo;
import com.rakib.service.dto.UserDTO;
import com.rakib.service.dto.response.UserResponseDTO;
import javassist.NotFoundException;
import org.omg.PortableInterceptor.ORBInitInfoPackage.DuplicateName;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserService {
	UserResponseDTO saveUser(UserDTO userInfo) throws DuplicateName;
	UserResponseDTO getUserByEmail(String email);
	Page<UserResponseDTO> getUsers(Pageable pageable);
	UserResponseDTO updateUser(long id, UserDTO userDTO) throws Exception;

	String deleteUser(long id) throws NotFoundException;
}
