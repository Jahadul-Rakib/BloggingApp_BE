package com.rakib.domain;
import com.rakib.domain.enums.Roles;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;


@Data
@Entity
public class Role implements GrantedAuthority {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
	private Roles name;

	@Override
	public String getAuthority() {
		return String.valueOf(this.name);
	}
}
