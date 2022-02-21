package com.code.dal.entities;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

import com.code.dal.entities.base.CommonEntity;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
@Entity
@Table(name = "Taco_Order")
public class Order extends CommonEntity {

	@NotBlank(message = "Name is required")
	@Column(name = "name")
	private String fullName;

	@NotNull(message = "Street is required")
	private String street;

	@Length(min = 3, max = 10, message = "City length between 3 and 10")
	private String city;

	@Length(min = 2, max = 2, message = "State length equals 2")
	private String state;

	@Digits(integer = 3, fraction = 0, message = "Invalid zip")
	private String zip;

	@Column(name = "placedat")
	private Date placedAt;

	@ManyToMany(targetEntity = Taco.class)
	@JoinTable(
	        joinColumns=@JoinColumn(name="tacoorder"),
	        inverseJoinColumns=@JoinColumn(name="taco")
	    )
	private List<Taco> tacos = new ArrayList<>();

	public void addDesign(Taco design) {
		this.tacos.add(design);
	}

	@PrePersist
	private void placedAt() {
		this.placedAt = new Date();
	}

}