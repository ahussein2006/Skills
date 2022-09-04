package com.code.business;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.code.dal.RepositoryManager;
import com.code.dal.entities.employees.EmployeeData;
import com.code.dal.entities.missions.Mission;
import com.code.dal.entities.missions.MissionDetail;
import com.code.dal.entities.missions.MissionDetailData;
import com.code.enums.ChronologyTypesEnum;
import com.code.enums.ConfigCodesEnum;
import com.code.enums.ErrorMessageCodesEnum;
import com.code.enums.FlagsEnum;
import com.code.enums.QueryConfigConstants;
import com.code.enums.SeparatorsEnum;
import com.code.exceptions.BusinessException;
import com.code.exceptions.RepositoryException;
import com.code.util.BasicUtil;
import com.code.util.ConfigurationUtil;
import com.code.util.ExceptionUtil;
import com.code.util.MultiChronologyCalendarUtil;

@Service
public class MissionsBusiness {

    @Autowired
    private EmployeesBusiness employeesBusiness;

    @Autowired
    private RepositoryManager repositoryManager;

    // ----------------------------------- Missions Management ---------------------------------
    public void addMissionAndDetails(Mission mission, List<MissionDetailData> missionDetailsData, long transactionalUserId) throws BusinessException {
	validateMissionAndDetails(mission, missionDetailsData);

	try {
	    repositoryManager.beginTransaction();

	    addMission(mission, transactionalUserId);
	    addMissionDetails(mission, missionDetailsData, transactionalUserId);

	    repositoryManager.commitTransaction();
	} catch (Exception e) {
	    repositoryManager.rollbackTransaction();
	    throw ExceptionUtil.handleException(e, transactionalUserId);
	}
    }

    private void addMission(Mission mission, long transactionalUserId) throws BusinessException {
	try {
	    mission.setDecreeNumber(((new Random()).nextInt(1000) + 1) + "");
	    mission.setDecreeHijriDate(MultiChronologyCalendarUtil.getSysDate(ChronologyTypesEnum.HIJRI));
	    mission.setEflag(FlagsEnum.ON.getValue());
	    mission.setMigFlag(FlagsEnum.OFF.getValue());

	    repositoryManager.insertEntity(mission, transactionalUserId);
	} catch (Exception e) {
	    throw ExceptionUtil.handleException(e, transactionalUserId);
	}
    }

    private void addMissionDetails(Mission mission, List<MissionDetailData> missionDetailsData, long transactionalUserId) throws BusinessException {
	try {
	    for (MissionDetailData missionDetailData : missionDetailsData) {
		missionDetailData.setMissionId(mission.getId());
		repositoryManager.insertEntity(missionDetailData.getMissionDetail(), transactionalUserId);
		missionDetailData.setId(missionDetailData.getMissionDetail().getId());
	    }
	} catch (Exception e) {
	    throw ExceptionUtil.handleException(e, transactionalUserId);
	}
    }

    public void modifyMissionDetailWithActualData(long missionDetailId, Date actualStartHijriDate, int actualPeriod, int absenceFlag, String absenceReasons, long transactionalUserId) throws BusinessException {
	MissionDetail missionDetail = getMissionDetailById(missionDetailId);
	Mission mission = getMissionById(missionDetail.getMissionId());

	validateMissionDetailActualData(mission, missionDetail, actualStartHijriDate, actualPeriod, absenceFlag, absenceReasons);

	try {
	    repositoryManager.beginTransaction();

	    missionDetail.setActualPeriod(actualPeriod);
	    missionDetail.setActualStartHijriDate(actualStartHijriDate);
	    missionDetail.setActualEndHijriDate(MultiChronologyCalendarUtil.addSubDateDays(actualStartHijriDate, (actualPeriod + missionDetail.getRoadPeriod() - 1), ChronologyTypesEnum.HIJRI));
	    missionDetail.setAbsenceFlag(absenceFlag);
	    missionDetail.setAbsenceReasons(absenceReasons);

	    repositoryManager.updateEntity(missionDetail, transactionalUserId);

	    repositoryManager.commitTransaction();
	} catch (Exception e) {
	    repositoryManager.rollbackTransaction();
	    throw ExceptionUtil.handleException(e, transactionalUserId);
	}
    }

    public void modifyMissionDetailWithJoining(long missionDetailId, Date joiningHijriDate, long transactionalUserId) throws BusinessException {
	MissionDetail missionDetail = getMissionDetailById(missionDetailId);
	validateMissionDetailJoining(missionDetail, joiningHijriDate);

	try {
	    repositoryManager.beginTransaction();

	    missionDetail.setJoiningHijriDate(joiningHijriDate);
	    repositoryManager.updateEntity(missionDetail, transactionalUserId);

	    repositoryManager.commitTransaction();
	} catch (Exception e) {
	    repositoryManager.rollbackTransaction();
	    throw ExceptionUtil.handleException(e, transactionalUserId);
	}
    }

    // ----------------------------------- Missions Operations ---------------------------------
    public Integer calculateEmployeeMissionsBalance(long employeeId, Date missionHijriDate) throws BusinessException {
	return (Integer.parseInt(ConfigurationUtil.getConfigValue(ConfigCodesEnum.MSN_BALANCE)) - getEmployeeConsumedMissionsBalance(employeeId, missionHijriDate));
    }

    // ----------------------------------- Missions Validations --------------------------------
    public void validateMissionAndDetails(Mission mission, List<MissionDetailData> missionDetailsData) throws BusinessException {
	if (mission == null)
	    throw new BusinessException(ErrorMessageCodesEnum.NO_DATA_FOUND);

	if (BasicUtil.isNullOrEmpty(missionDetailsData))
	    throw new BusinessException(ErrorMessageCodesEnum.MSN_ONE_EMPLOYEE_MANDATORY);

	validateMission(mission);

	validateMissionDetails(mission, missionDetailsData);
    }

    private void validateMission(Mission mission) throws BusinessException {
	if (!BasicUtil.isFlag(mission.getLocationFlag()) || !BasicUtil.isFlag(mission.getHajjFlag()) || !BasicUtil.isFlag(mission.getDoubleBalanceFlag()))
	    throw new BusinessException(ErrorMessageCodesEnum.MSN_MAIN_FIELDS_MNDATORY);

	if (BasicUtil.isNullOrEmpty(mission.getLocation()) || BasicUtil.isNullOrEmpty(mission.getDestination()) || BasicUtil.isNullOrEmpty(mission.getPurpose())
		|| !BasicUtil.isPositive(mission.getPeriod()) || mission.getStartHijriDate() == null || mission.getEndHijriDate() == null)
	    throw new BusinessException(ErrorMessageCodesEnum.MSN_MAIN_FIELDS_MNDATORY);

	if (mission.getLocationFlag().equals(FlagsEnum.ON.getValue()) && (!BasicUtil.isPositive(mission.getRoadPeriod()) || BasicUtil.isNullOrEmpty(mission.getMinisteryApprovalNumber()) || mission.getMinisteryApprovalHijriDate() == null))
	    throw new BusinessException(ErrorMessageCodesEnum.MSN_MAIN_FIELDS_MNDATORY);
	else if (mission.getLocationFlag().equals(FlagsEnum.OFF.getValue()) && (mission.getRoadPeriod() != 0 || mission.getMinisteryApprovalNumber() != null || mission.getMinisteryApprovalHijriDate() != null))
	    throw new BusinessException(ErrorMessageCodesEnum.MSN_MAIN_FIELDS_MNDATORY);

	if (!mission.getEndHijriDate().equals(MultiChronologyCalendarUtil.addSubDateDays(mission.getStartHijriDate(), (mission.getPeriod() + mission.getRoadPeriod() - 1), ChronologyTypesEnum.HIJRI)))
	    throw new BusinessException(ErrorMessageCodesEnum.INVALID_DATE);
    }

    private void validateMissionDetails(Mission mission, List<MissionDetailData> missionDetailsData) throws BusinessException {
	Set<Long> employeesIds = new HashSet<Long>();

	for (MissionDetailData missionDetailData : missionDetailsData) {
	    if (missionDetailData.getEmployeeId() == null)
		throw new BusinessException(ErrorMessageCodesEnum.NO_DATA_FOUND);

	    EmployeeData employeeData = employeesBusiness.getEmployeeDataById(missionDetailData.getEmployeeId());

	    if (!BasicUtil.isPositive(missionDetailData.getPeriod()) || missionDetailData.getStartHijriDate() == null || missionDetailData.getEndHijriDate() == null)
		throw new BusinessException(ErrorMessageCodesEnum.MSN_DETAIL_FIELDS_MNDATORY, new Object[] { employeeData.getName() });

	    if (!missionDetailData.getPeriod().equals(missionDetailData.getActualPeriod()) || !missionDetailData.getStartHijriDate().equals(missionDetailData.getActualStartHijriDate()) || !missionDetailData.getEndHijriDate().equals(missionDetailData.getActualEndHijriDate()))
		throw new BusinessException(ErrorMessageCodesEnum.MSN_DETAIL_FIELDS_MNDATORY, new Object[] { employeeData.getName() });

	    if (mission.getLocationFlag().equals(FlagsEnum.ON.getValue()) && !BasicUtil.isPositive(missionDetailData.getRoadPeriod()))
		throw new BusinessException(ErrorMessageCodesEnum.MSN_DETAIL_FIELDS_MNDATORY, new Object[] { employeeData.getName() });
	    else if (mission.getLocationFlag().equals(FlagsEnum.OFF.getValue()) && missionDetailData.getRoadPeriod() != 0)
		throw new BusinessException(ErrorMessageCodesEnum.MSN_DETAIL_FIELDS_MNDATORY, new Object[] { employeeData.getName() });

	    if (!missionDetailData.getEndHijriDate().equals(MultiChronologyCalendarUtil.addSubDateDays(missionDetailData.getStartHijriDate(), (missionDetailData.getPeriod() + missionDetailData.getRoadPeriod() - 1), ChronologyTypesEnum.HIJRI)))
		throw new BusinessException(ErrorMessageCodesEnum.INVALID_DATE, new Object[] { employeeData.getName() });

	    if ((BasicUtil.isNullOrEmpty(missionDetailData.getExceptionalApprovalNumber()) && missionDetailData.getExceptionalApprovalHijriDate() != null) || (!BasicUtil.isNullOrEmpty(missionDetailData.getExceptionalApprovalNumber()) && missionDetailData.getExceptionalApprovalHijriDate() == null))
		throw new BusinessException(ErrorMessageCodesEnum.MSN_DETAIL_FIELDS_MNDATORY, new Object[] { employeeData.getName() });

	    if (missionDetailData.getAbsenceFlag() != FlagsEnum.OFF.getValue() || missionDetailData.getAbsenceReasons() != null || missionDetailData.getJoiningHijriDate() != null || missionDetailData.getClosingAttachmentsKey() != null)
		throw new BusinessException(ErrorMessageCodesEnum.MSN_DETAIL_FIELDS_MNDATORY, new Object[] { employeeData.getName() });

	    if (!employeesIds.add(missionDetailData.getEmployeeId()))
		throw new BusinessException(ErrorMessageCodesEnum.MSN_EMPLOYEE_REPETITION, new Object[] { employeeData.getName() });

	    validateMissionDetailDatesConflict(employeeData, missionDetailData.getStartHijriDate(), missionDetailData.getEndHijriDate(), null);

	    validateMissionDetailBalance(employeeData, missionDetailData.getStartHijriDate(), missionDetailData.getEndHijriDate(), missionDetailData.getPeriod(), missionDetailData.getExceptionalApprovalHijriDate(), missionDetailData.getExceptionalApprovalNumber(), missionDetailData.getRoadPeriod(), mission.getHajjFlag(), mission.getDoubleBalanceFlag());
	}
    }

    private void validateMissionDetailBalance(EmployeeData employeeData, Date startHijriDate, Date endHijriDate, int period, Date exceptionalApprovalDate, String exceptionalApprovalNumber, int roadPeriod, int hajjFlag, int doubleBalanceFlag) throws BusinessException {
	if (validateSameFinancialYear(startHijriDate, endHijriDate))
	    throw new BusinessException(ErrorMessageCodesEnum.MSN_START_END_SHOULD_BE_IN_SAME_YEAR, new Object[] { employeeData.getName() });

	if (exceptionalApprovalDate != null && !BasicUtil.isNullOrEmpty(exceptionalApprovalNumber))
	    return;

	if (hajjFlag != FlagsEnum.ON.getValue()) {

	    int remainingBalance = calculateEmployeeMissionsBalance(employeeData.getId(), startHijriDate);

	    if (doubleBalanceFlag == FlagsEnum.ON.getValue())
		remainingBalance += Integer.parseInt(ConfigurationUtil.getConfigValue(ConfigCodesEnum.MSN_DOUBLE_BALANCE));

	    if (remainingBalance - (period + roadPeriod) < 0)
		throw new BusinessException(ErrorMessageCodesEnum.MSN_NO_BALANCE, new Object[] { employeeData.getName() });
	}
    }

    private void validateMissionDetailDatesConflict(EmployeeData employeeData, Date startHijriDate, Date endHijriDate, Long excludedMissionId) throws BusinessException {
	if (startHijriDate.before(employeeData.getHiringHijriDate()))
	    throw new BusinessException(ErrorMessageCodesEnum.MSN_DATES_BEFORE_HIRING, new Object[] { employeeData.getName() });

	if (isMissionDetailHasDatesConflict(employeeData.getId(), startHijriDate, endHijriDate, excludedMissionId))
	    throw new BusinessException(ErrorMessageCodesEnum.MSN_DATES_CONFLICT, new Object[] { employeeData.getName() });
    }

    private void validateMissionDetailActualData(Mission mission, MissionDetail missionDetail, Date actualStartHijriDate, int actualPeriod, int absenceFlag, String absenceReasons) throws BusinessException {
	if (!BasicUtil.isFlag(absenceFlag) || actualStartHijriDate == null || !BasicUtil.isPositive(actualPeriod) || actualPeriod > missionDetail.getPeriod().intValue())
	    throw new BusinessException(ErrorMessageCodesEnum.MSN_ACTUAL_FIELDS_MNDATORY);

	if (absenceFlag != FlagsEnum.ON.getValue() && (!missionDetail.getPeriod().equals(actualPeriod) || !missionDetail.getStartHijriDate().equals(actualStartHijriDate))) {
	    if (validateSameFinancialYear(actualStartHijriDate, missionDetail.getStartHijriDate()))
		throw new BusinessException(ErrorMessageCodesEnum.MSN_ACTUAL_SHOULD_BE_IN_SAME_YEAR);

	    Date actualEndHijriDate = MultiChronologyCalendarUtil.addSubDateDays(actualStartHijriDate, (actualPeriod + missionDetail.getRoadPeriod() - 1), ChronologyTypesEnum.HIJRI);

	    if (validateSameFinancialYear(actualStartHijriDate, actualEndHijriDate))
		throw new BusinessException(ErrorMessageCodesEnum.MSN_START_END_SHOULD_BE_IN_SAME_YEAR);

	    validateMissionDetailDatesConflict(employeesBusiness.getEmployeeDataById(missionDetail.getEmployeeId()), actualStartHijriDate, actualEndHijriDate, mission.getId());
	}

	if (absenceFlag == FlagsEnum.ON.getValue() && (!missionDetail.getPeriod().equals(actualPeriod) || !missionDetail.getStartHijriDate().equals(actualStartHijriDate)))
	    throw new BusinessException(ErrorMessageCodesEnum.MSN_ACTUAL_SHOULD_NOT_CHANGE_IN_ABSENCE);

	if (absenceFlag != FlagsEnum.ON.getValue() && absenceReasons != null)
	    throw new BusinessException(ErrorMessageCodesEnum.MSN_ABSENCE_REASONS_NOT_ALLOWED);
    }

    private void validateMissionDetailJoining(MissionDetail missionDetail, Date joiningHijriDate) throws BusinessException {
	if (joiningHijriDate == null)
	    throw new BusinessException(ErrorMessageCodesEnum.MSN_JOINING_DATE_MANDATORY);

	if (missionDetail.getAbsenceFlag().equals(FlagsEnum.ON.getValue()))
	    throw new BusinessException(ErrorMessageCodesEnum.MSN_JOINING_DATE_NOT_ALLOWED);

	if (!joiningHijriDate.after(missionDetail.getActualEndHijriDate()))
	    throw new BusinessException(ErrorMessageCodesEnum.MSN_JOINING_DATE_INVALID);
    }

    // ----------------------------------- Missions Inquiries ----------------------------------
    public Mission getMissionById(long missionId) throws BusinessException {
	try {
	    return BasicUtil.getFirstItem(repositoryManager.getEntities(Mission.class, QueryConfigConstants.MSN_Mission_GetMissionById, QueryConfigConstants.MSN_Mission_GetMissionById_Params, missionId));
	} catch (RepositoryException e) {
	    throw ExceptionUtil.handleException(e, null);
	}
    }

    public List<Mission> getMissions(Integer locationFlag, String decreeNumber, Date fromHijriDate, Date toHijriDate, Long employeeId, int limit, int offset) throws BusinessException {
	try {
	    return repositoryManager.getEntitiesWithPaging(Mission.class, QueryConfigConstants.MSN_Mission_GetMissions, limit, offset, QueryConfigConstants.MSN_Mission_GetMissions_Params,
		    BasicUtil.getValueOrEscape(locationFlag), BasicUtil.getValueOrEscape(decreeNumber),
		    BasicUtil.getDateFlag(fromHijriDate), BasicUtil.getValueOrEscape(fromHijriDate), BasicUtil.getDateFlag(toHijriDate), BasicUtil.getValueOrEscape(toHijriDate),
		    BasicUtil.getValueOrEscape(employeeId));
	} catch (RepositoryException e) {
	    throw ExceptionUtil.handleException(e, null);
	}
    }

    public long getMissionsCount(Integer locationFlag, String decreeNumber, Date fromHijriDate, Date toHijriDate, Long employeeId) throws BusinessException {
	try {
	    return BasicUtil.getFirstItem(repositoryManager.getEntities(Long.class, QueryConfigConstants.MSN_Mission_GetMissionsCount, QueryConfigConstants.MSN_Mission_GetMissionsCount_Params,
		    BasicUtil.getValueOrEscape(locationFlag), BasicUtil.getValueOrEscape(decreeNumber),
		    BasicUtil.getDateFlag(fromHijriDate), BasicUtil.getValueOrEscape(fromHijriDate), BasicUtil.getDateFlag(toHijriDate), BasicUtil.getValueOrEscape(toHijriDate),
		    BasicUtil.getValueOrEscape(employeeId)));
	} catch (RepositoryException e) {
	    throw ExceptionUtil.handleException(e, null);
	}
    }

    public MissionDetail getMissionDetailById(long missionDetailId) throws BusinessException {
	try {
	    return BasicUtil.getFirstItem(repositoryManager.getEntities(MissionDetail.class, QueryConfigConstants.MSN_MissionDetail_GetMissionDetailById, QueryConfigConstants.MSN_Mission_GetMissionById_Params, missionDetailId));
	} catch (RepositoryException e) {
	    throw ExceptionUtil.handleException(e, null);
	}
    }

    private Integer getEmployeeConsumedMissionsBalance(long employeeId, Date missionHijriDate) throws BusinessException {
	try {
	    List<String> financialYearHijriDates = calculateFinancialYearHijriDates(missionHijriDate);

	    Long consumedBalance = BasicUtil.getFirstItem(repositoryManager.getEntities(Long.class, QueryConfigConstants.MSN_MissionDetail_GetConsumedBalance, QueryConfigConstants.MSN_MissionDetail_GetConsumedBalance_Params,
		    employeeId, financialYearHijriDates.get(0), financialYearHijriDates.get(1)));
	    return (int) consumedBalance.longValue();
	} catch (RepositoryException e) {
	    throw ExceptionUtil.handleException(e, null);
	}
    }

    private boolean isMissionDetailHasDatesConflict(long employeeId, Date startHijriDate, Date endHijriDate, Long excludedMissionId) throws BusinessException {
	try {
	    Long conflictCount = BasicUtil.getFirstItem(repositoryManager.getEntities(Long.class, QueryConfigConstants.MSN_MissionDetail_GetOverlap, QueryConfigConstants.MSN_MissionDetail_GetOverlap_PARAMS,
		    employeeId, MultiChronologyCalendarUtil.getDateString(startHijriDate, ChronologyTypesEnum.HIJRI), MultiChronologyCalendarUtil.getDateString(endHijriDate, ChronologyTypesEnum.HIJRI), BasicUtil.getValueOrEscape(excludedMissionId)));
	    return conflictCount.longValue() != 0L;
	} catch (RepositoryException e) {
	    throw ExceptionUtil.handleException(e, null);
	}
    }

    // ----------------------------------- Financial Years -------------------------------------
    private List<String> calculateFinancialYearHijriDates(Date missionHijriDate) {
	Date missionGregDate = MultiChronologyCalendarUtil.convertDate(missionHijriDate, ChronologyTypesEnum.HIJRI, ChronologyTypesEnum.GREGORIAN);
	int year = MultiChronologyCalendarUtil.getDateYear(missionGregDate, ChronologyTypesEnum.GREGORIAN);

	return List.of("01" + SeparatorsEnum.SLASH.getValue() + "01" + SeparatorsEnum.SLASH.getValue() + year, "31" + SeparatorsEnum.SLASH.getValue() + "12" + SeparatorsEnum.SLASH.getValue() + year);
    }

    private boolean validateSameFinancialYear(Date firstHijriDate, Date secondHijriDate) {
	Date firstGregDate = MultiChronologyCalendarUtil.convertDate(firstHijriDate, ChronologyTypesEnum.HIJRI, ChronologyTypesEnum.GREGORIAN);
	Date secondGregDate = MultiChronologyCalendarUtil.convertDate(secondHijriDate, ChronologyTypesEnum.HIJRI, ChronologyTypesEnum.GREGORIAN);

	return MultiChronologyCalendarUtil.getDateYear(firstGregDate, ChronologyTypesEnum.GREGORIAN) == MultiChronologyCalendarUtil.getDateYear(secondGregDate, ChronologyTypesEnum.GREGORIAN);
    }
}