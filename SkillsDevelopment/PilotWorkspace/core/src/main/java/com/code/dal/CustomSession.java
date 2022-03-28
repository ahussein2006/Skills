package com.code.dal;

import org.hibernate.Session;

public class CustomSession {
    private static final ThreadLocal<CustomSession> sessionsThreadLocal = new ThreadLocal<CustomSession>();

    private final Session session;
    private final String sessionOwner;

    CustomSession(Session session, String sessionOwner) {
	this.session = session;
	this.sessionOwner = sessionOwner;
    }

    static void setCurrentSession(CustomSession customSession) {
	sessionsThreadLocal.set(customSession);
    }

    static CustomSession getCurrentSession() {
	return sessionsThreadLocal.get();
    }

    static void removeCurrentSession() {
	sessionsThreadLocal.remove();
    }

    Session getSession() {
	return session;
    }

    String getSessionOwner() {
	return sessionOwner;
    }
}