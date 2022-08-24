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
		name = QueryConfigConstants.SP_Message_GetMessages,
		query = " select m from Message m" +
			" where m.moduleIds like :P_MODULE_IDS " +
			" order by m.messageKey")
})

@Data
@Entity
@Table(name = "SP_MESSAGES")
public class Message implements BaseEntity {

    @Id
    @Column(name = "ID")
    private Long id;

    @Column(name = "MODULE_IDS")
    private String moduleIds;

    @Column(name = "MESSAGE_TYPE")
    private String messageType;

    @Column(name = "MESSAGE_KEY")
    private String messageKey;

    @Column(name = "AR_VALUE")
    private String arValue;

    @Column(name = "EN_VALUE")
    private String enValue;

}
