package com.googlecode.jdbcproc.daofactory.impl.block.impl;

import java.sql.ResultSet;
import java.util.List;

/**
 * Utils to convert ResultSet to Entity
 */
public class ResultSetConverterBlockUtil {

    /**
     * Creates entity from result set
     * @param aResultSet              result set
     * @param aEntityType             entity type
     * @param aEntityPropertySetters  property setters
     * @param aOneToOneLinks          one to one links
     * @return entity
     */
    public static Object createEntity(ResultSet aResultSet
            , Class aEntityType
            , List<EntityPropertySetter> aEntityPropertySetters
            , List<OneToOneLink> aOneToOneLinks) {
        
        Object entity = createEntityObject(aEntityType);
        // sets simple properties
        for (EntityPropertySetter propertySetter : aEntityPropertySetters) {
            try {
                propertySetter.fillProperty(entity, aResultSet);
            } catch (Exception e) {
                throw new IllegalStateException("Unable to set property: "+e.getMessage(), e);
            }
        }

        // sets OneToOne and ManyToOne properties
        for(OneToOneLink oneToOneLink : aOneToOneLinks) {
        	Object oneToOneEntity = oneToOneLink.getBlock().createEntity(aResultSet);
        	try {
				oneToOneLink.fillProperty(entity, oneToOneEntity);
            } catch (Exception e) {
                throw new IllegalStateException("Unable to set property: "+e.getMessage(), e);
            }
        }

        return entity;
    }

    private static Object createEntityObject(Class aEntityType) {
        try {
            return aEntityType.newInstance();
        } catch (Exception e) {
            throw new IllegalStateException("Could not create entity "+aEntityType.getSimpleName(), e);
        }
    }

}
