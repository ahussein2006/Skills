package com.code.dal.entities;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.PrePersist;
import javax.persistence.Version;

import com.code.dal.QueryNames;
import com.code.dal.entities.audit.AuditEntity;
import com.code.exceptions.RepositoryException;

import lombok.Data;
import lombok.EqualsAndHashCode;

@NamedQueries({
	@NamedQuery(name = QueryNames.TACO_GET_TACO_BY_ID,
			query = "select t from Taco t where t.id= :P_ID"),
	@NamedQuery(name = QueryNames.TACO_GET_TACO_BY_NAME,
		query = "select t from Taco t where t.name= :P_NAME"),
	@NamedQuery(name = QueryNames.TACO_GET_RECENT_TACOS,
		query = "select t from Taco t order by t.insertedAt desc")
})

@Data
@EqualsAndHashCode(callSuper = false)
@Entity
public class Taco extends AuditEntity {
		
	private String name;

	@Column(name = "insertedat")
	private Date insertedAt;

	@Version
	private Long version;
	
	@PrePersist
	private void insertedAt() throws RepositoryException {
		this.insertedAt = new Date();
	}
}
