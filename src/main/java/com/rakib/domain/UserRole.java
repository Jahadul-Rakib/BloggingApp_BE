package com.rakib.domain;
import com.rakib.enums.Roles;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;


@Data
@Entity
@Table
public class UserRole implements GrantedAuthority {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
	private Roles userRole;

	@Override
	public String getAuthority() {
		return String.valueOf(this.userRole);
	}
}
