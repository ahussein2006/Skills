package com.code.dal.entities.setup;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import com.code.dal.entities.base.BaseEntity;
import com.code.enums.QueryConfigConstants;

import lombok.Data;

@NamedQueries({
	@NamedQuery(
		name = QueryConfigConstants.SP_Module_GetModules,
		query = " select m from Module m" +
			" where (:P_CODE = :P_ESC_SEARCH_STR or m.code = :P_CODE) " +
			" order by m.code")
})

@Data
@Entity
@Table(name = "SP_MODULES")
public class Module implements BaseEntity {

    @Id
    @Column(name = "ID")
    private Long id;

    @Column(name = "CODE")
    private String code;

    @Column(name = "DESCRIPTION")
    private String description;

}