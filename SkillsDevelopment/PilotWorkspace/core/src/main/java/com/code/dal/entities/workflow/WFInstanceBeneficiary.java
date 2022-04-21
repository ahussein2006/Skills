package com.code.dal.entities.workflow;

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
		name = QueryConfigConstants.WF_InstanceBeneficiary_GetBeneficiariesByInstanceId,
		query = " select ib from WFInstanceBeneficiary ib " +
			" where ib.instanceId = :P_INSTANCE_ID " +
			" order by ib.beneficiaryId ")
})

@Data
@Entity
@Table(name = "WF_INSTANCE_BENEFICIARIES")
public class WFInstanceBeneficiary implements BaseEntity {
    @Id
    @Column(name = "INSTANCE_ID")
    private Long instanceId;

    @Id
    @Column(name = "BENEFICIARY_ID")
    private Long beneficiaryId;
}