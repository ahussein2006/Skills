package com.code.dal.repositories.base;

//@Aspect
//@Component
//@Configurable
public class AuditRepository {

//	private static final Long moduleId = 1L;

	/*@Pointcut("execution(* org.springframework.data.repository.CrudRepository+.save(*))")
	public void whenSaveOrUpdate() {
	};
	
	@Before("whenSaveOrUpdate()")
	public void beforeSaveOrUpdate(JoinPoint joinPoint) {
		System.out.println(joinPoint);
	}*/
	
//	@Pointcut("this(org.springframework.data.repository.Repository)")
//	public void inDATAExec(){}
//
//	@Before("execution(* *.*(..))")
//	public void before(JoinPoint joinPoint) {
//		System.out.println(joinPoint);
//	}

//	@Autowired
//	private AuditLogRepository auditLogRepository;

	/*
	 * public void audit(BaseEntity bean, OperationsEnum operation, String userId)
	 * throws RepositoryException { if (bean instanceof AuditEntity) { AuditEntity
	 * auditableBean = (AuditEntity) bean;
	 * 
	 * if (!auditableBean.isPreventAuditFlag()) {
	 * 
	 * if (userId == null || userId.isEmpty()) throw new
	 * RepositoryException("User cannot be null while auditing.");
	 * 
	 * AuditLog log = new AuditLog(); log.setModuleId(moduleId);
	 * 
	 * if (userId.matches("\\d+")) log.setUserId(Long.parseLong(userId)); else
	 * log.setSystemName(userId);
	 * 
	 * log.setOperation(operation.toString()); log.setOperationGregDate(new Date());
	 * log.setContentEntity(auditableBean.getClass().getCanonicalName());
	 * log.setContentId(auditableBean.getId());
	 * log.setContent(auditableBean.calculateContent());
	 * auditLogRepository.save(log); } } }
	 */

}