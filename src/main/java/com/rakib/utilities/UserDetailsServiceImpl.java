package com.rakib.utilities;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import com.rakib.domain.UserInfo;
import com.rakib.domain.repo.UserInfoRepo;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

	private final UserInfoRepo userInfo;

	public UserDetailsServiceImpl(UserInfoRepo userInfo) {
		this.userInfo = userInfo;
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		UserInfo user = userInfo.getUserInfoByUserEmail(username);

		if (user != null) {
			System.out.println(user);
			return user;
		} else {
			throw new UsernameNotFoundException("user not found by email "+ user.getUserEmail());
		}
	}

}
