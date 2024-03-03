package com.code.security;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletContext;

import org.springframework.beans.factory.annotation.Autowired;

import com.code.config.InjectionManager;
import com.code.enums.ConfigCodesEnum;
import com.code.util.BasicUtil;
import com.code.util.ConfigurationUtil;

import lombok.AllArgsConstructor;
import lombok.Data;

public class SecurityManager {

    @Autowired
    private ServletContext servletContext;

    private static SecurityManager instance;

    private static final String USERS_SECURITY_INFO = "USERS_SECURITY_INFO";

    static {
	instance = new SecurityManager();
	InjectionManager.wireServices(BasicUtil.convertObjectToSet(instance));
	instance.servletContext.setAttribute(USERS_SECURITY_INFO, new HashMap<String, SecurityInfo>());
    }

    private SecurityManager() {
    }

    @SuppressWarnings("unchecked")
    private static Map<String, SecurityInfo> getUsersSecurityInfo() {
	return (Map<String, SecurityInfo>) instance.servletContext.getAttribute(USERS_SECURITY_INFO);
    }

    // -----------------------------------------------------------------------------------------
    public static void setUserSecurityInfo(String username, Long userId) {
	getUsersSecurityInfo().put(username, new SecurityInfo(userId, new Date()));
    }

    public static Long getUserId(String username) {
	return getUsersSecurityInfo().get(username).getUserId();
    }

    // -----------------------------------------------------------------------------------------
    public static void notifyUserAccess(String username) {
	getUsersSecurityInfo().get(username).setLastAccess(new Date());
    }

    protected static void invalidateUsersSecurityInfo() {
	getUsersSecurityInfo().entrySet().stream().forEach(e -> {
	    if (new Date().getTime() - e.getValue().getLastAccess().getTime() > Long.parseLong(ConfigurationUtil.getConfigValue(ConfigCodesEnum.SEC_INFO_TIMEOUT)))
		getUsersSecurityInfo().remove(e.getKey());
	});
    }

    // -----------------------------------------------------------------------------------------
    @Data
    @AllArgsConstructor
    private static class SecurityInfo {
	private Long userId;
	private Date lastAccess;
    }
}
