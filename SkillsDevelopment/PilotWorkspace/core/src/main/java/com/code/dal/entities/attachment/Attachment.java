package com.code.dal.entities.attachment;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.code.dal.entities.base.AuditeeEntity;
import com.code.enums.QueryConfigConstants;

import lombok.Data;
import lombok.EqualsAndHashCode;

@NamedQueries({
	@NamedQuery(
		name = QueryConfigConstants.GN_Attachment_GetAttachments,
		query = " select a from Attachment a " +
			" where (:P_ID = :P_ESC_SEARCH_LONG or a.id = :P_ID) " +
			"   and (:P_ATTACHMENTS_KEY = :P_ESC_SEARCH_STR or a.attachmentsKey = :P_ATTACHMENTS_KEY) " +
			"   and (:P_FILE_NAME = :P_ESC_SEARCH_STR or a.fileName like :P_FILE_NAME) " +
			"   and (:P_FILE_METADATA = :P_ESC_SEARCH_STR or a.fileMetadata like :P_FILE_METADATA) " +
			" order by a.fileName ")
})
@Data
@EqualsAndHashCode(callSuper = false)
@Entity
@Table(name = "GN_ATTACHMENTS")
public class Attachment extends AuditeeEntity {
    @SequenceGenerator(name = "GNAttachmentSeq", sequenceName = "GN_ATTACHMENT_SEQ", allocationSize = 1)
    @GeneratedValue(generator = "GNAttachmentSeq", strategy = GenerationType.SEQUENCE)
    @Id
    @Column(name = "ID")
    private Long id;

    @Column(name = "ATTACHMENTS_KEY")
    private String attachmentsKey;

    @Column(name = "FILE_ID")
    private String fileId;

    @Column(name = "FILE_NAME")
    private String fileName;

    @Column(name = "FILE_METADATA")
    private String fileMetadata;

    @Override
    public String caculateContentId() {
	return id.toString();
    }
}
