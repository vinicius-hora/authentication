package com.rasmoo.client.financescontroll.entity;

import javax.persistence.Embeddable;

import lombok.Getter;

@Embeddable
@Getter
public class Credential {
	
	private String email;

	private String senha;
}
