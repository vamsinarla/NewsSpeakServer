package com.vn.newsspeak;

import javax.jdo.JDOHelper;
import javax.jdo.PersistenceManagerFactory;

/**
 * This singleton wrapper class makes and stores within itself 
 * a single instance of the {@code PersistenceManagerFactory} class
 * to prevent multiple initializations of the factory in a single app.
 * 
 * This is required to satisfy AppEngine's requirement that each app uses only
 * a single instance of {@code PersistenceManagerFactory} to get instances
 * of {@code PersistenceManager} class for interacting with the data store.
 */
public final class PMF {
    private static final PersistenceManagerFactory pmfInstance =
        JDOHelper.getPersistenceManagerFactory("transactions-optional");

    private PMF() {}

    /**
     * Returns the singleton instance of {@code PersistenceManagerFactory}
     * maintained for this app.
     * @return the singleton instance of {@code PersistenceManagerFactory}
     */
    public static PersistenceManagerFactory get() {
        return pmfInstance;
    }
}
