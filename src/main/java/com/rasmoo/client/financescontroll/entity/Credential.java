package com.rasmoo.client.financescontroll.entity;

import javax.persistence.Embeddable;

import lombok.Data;

import java.io.Serializable;

@Embeddable
@Data
public class Credential implements Serializable {

	private static final long serialVersionUID = -3944562429656505268L;
	private String email;

	private String senha;
	
}
