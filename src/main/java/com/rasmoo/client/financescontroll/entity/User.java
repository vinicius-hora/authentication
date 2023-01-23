package com.rasmoo.client.financescontroll.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

@Data
@Entity
@Table(name = "USER_FINANCE")
public class User implements Serializable {

	private static final long serialVersionUID = -2614874437766945384L;
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	@Column(name = "id")
	private Long id;
	
	@Column(name = "name")
	private String nome;

	@Column(name = "role")
	private String role = "ROLE_COSTUMER";

	@JsonIgnore
	@Column(name = "credential")
	private Credential credencial = new Credential();

	@OneToMany(
		mappedBy = "user",
		cascade = CascadeType.ALL,
		orphanRemoval = true
	)
	// @JoinTable(name = "user_has_entries", joinColumns = { @JoinColumn(name = "user_id") }, inverseJoinColumns = {
	// 		@JoinColumn(name = "entry_id") })
	@Column(name = "entry")
	private List<Entry> lancamento = new ArrayList<>();

}
