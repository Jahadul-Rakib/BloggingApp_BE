package com.rakib.service.implimentation;

import com.rakib.domain.Role;
import com.rakib.service.dto.UserDTO;
import com.rakib.domain.repo.UserRoleRepo;
import com.rakib.service.dto.response.UserResponseDTO;
import com.rakib.service.mapper.UserMapper;
import javassist.NotFoundException;
import org.omg.PortableInterceptor.ORBInitInfoPackage.DuplicateName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.rakib.domain.UserInfo;
import com.rakib.domain.repo.UserInfoRepo;
import com.rakib.service.UserService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static java.util.Objects.nonNull;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    UserMapper userMapper;

    private final UserInfoRepo userInfoRepo;
    private final UserRoleRepo userRoleRepo;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserInfoRepo userinfo, UserRoleRepo userRoleRepo, PasswordEncoder passwordEncoder) {
        this.userInfoRepo = userinfo;
        this.userRoleRepo = userRoleRepo;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserResponseDTO saveUser(UserDTO user) throws DuplicateName {
        List<Role> roles = new ArrayList<>();
        for (Long value : user.getRoleId()) {
            Role role = userRoleRepo.getOne(value);
            roles.add(role);
        }

        Optional<UserInfo> byUserName = userInfoRepo.getUserInfoByUserName(user.getUserName());
        if (byUserName.isPresent()) {
            throw new DuplicateName("User Email Already Exist.");
        }

        UserInfo userInfo = new UserInfo();
        userInfo.setUserName(user.getUserName());
        userInfo.setUserEmail(user.getUserEmail());
        userInfo.setUserPhone(user.getUserPhone());
        userInfo.setUserPassword(passwordEncoder.encode(user.getUserPassword()));
        userInfo.setActive(false);
        userInfo.setRole(roles);

        return userMapper.toDTO(userInfoRepo.save(userInfo));
    }

    @Override
    public UserResponseDTO getUserByEmail(String email) {
        return userMapper.toDTO(userInfoRepo.getUserInfoByUserEmail(email));
    }

    @Override
    public Page<UserResponseDTO> getUsers(Pageable pageable) {
        Page<UserInfo> all = userInfoRepo.findAll(pageable);
        List<UserResponseDTO> responseDTOS = new ArrayList<>();
        all.forEach(info -> {
            responseDTOS.add(userMapper.toDTO(info));
        });
        return new PageImpl<UserResponseDTO>(responseDTOS, pageable, responseDTOS.size());
    }

    @Override
    public UserResponseDTO updateUser(long id, UserDTO userDTO) throws Exception {
        Optional<UserInfo> userInfo = userInfoRepo.findById(id);
        if (userInfo.isPresent()) {
            if (nonNull(userDTO.getUserName())) {
                userInfo.get().setUserName(userDTO.getUserName());
            }
            if (nonNull(userDTO.getUserEmail())) {
                userInfo.get().setUserEmail(userDTO.getUserEmail());
            }
            if (nonNull(userDTO.getUserPassword())) {
                userInfo.get().setUserPassword(userDTO.getUserPassword());
            }
            if (userDTO.isActive()) {
                userInfo.get().setActive(userDTO.isActive());
            }
            UserInfo save = userInfoRepo.save(userInfo.get());
            return userMapper.toDTO(save);
        }
        throw new Exception("User Not Found.");
    }

    @Override
    public String deleteUser(long id) throws NotFoundException {
        Optional<UserInfo> blog = userInfoRepo.findById(id);
        if (blog.isPresent()) {
            userInfoRepo.deleteById(id);
        } else {
            throw new NotFoundException("User not found by " + id);
        }
        return "Deleted Successfully.";
    }

}
