package com.dopenkov.sandbox.websockettest.app;

import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.InjectionPoint;
import javax.inject.Singleton;
import javax.json.Json;
import javax.json.JsonBuilderFactory;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Collections;
import java.util.logging.Logger;

/**
 * @author Dmitry Openkov
 */
public class Resources {
    @Produces
    @PersistenceContext
    private EntityManager em;

    @Produces
    @Singleton
    public JsonBuilderFactory createFactory() {
        return Json.createBuilderFactory(Collections.EMPTY_MAP);
    }

    @Produces
    public Logger produceLog(InjectionPoint injectionPoint) {
        return Logger.getLogger(injectionPoint.getMember().getDeclaringClass().getName());
    }
}
