package com.googlecode.jdbcproc.daofactory.impl.block.impl;

import com.googlecode.jdbcproc.daofactory.impl.block.IResultSetConverterBlock;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.CallableStatement;
import java.util.*;

import org.springframework.util.Assert;

/**
 * For list entity with @OneToMany 2x annotation
 */
public class ResultSetConverterBlockEntityOneToMany2xList implements IResultSetConverterBlock {

    public ResultSetConverterBlockEntityOneToMany2xList(List<OneToManyLink> aOneToManyLinks) {
        theOneToManyLinks = aOneToManyLinks;
    }

    public Object convertResultSet(ResultSet aResultSet, CallableStatement aStmt) throws SQLException {
        Assert.notNull(aResultSet, "ResultSet is null");

        List<Object> ret = new LinkedList<Object>();

        List<Holder> holders = createHolders();

        while(aResultSet.next()) {
            // loads all entity from result set
            for (Holder holder : holders) {
                holder.loadEntity(aResultSet);
            }

            // adds to ret list
            Holder firstLevelHolder = holders.get(0);
            if(firstLevelHolder.isNew()) {
                firstLevelHolder.clearChildren();
                ret.add(firstLevelHolder.getLoadedEntity());
            }

            for(int i= 1; i< holders.size(); i++) {
                Holder holder = holders.get(i);
                Holder parentHolder = holders.get(i-1);

                if(holder.isNotEmpty() && holder.isNew()) {
                    holder.clearChildren();
                    parentHolder.addToList(holder.getLoadedEntity());
                }
            }



        }
        return Collections.unmodifiableList(ret);

    }


    public String toString() {
        return "ResultSetConverterBlockEntityOneToMany2xList{" +
                "theOneToManyLinks=" + theOneToManyLinks +
                '}';
    }

    private List<Holder> createHolders() {
        ArrayList<Holder> ret = new ArrayList<Holder>(theOneToManyLinks.size());
        for (OneToManyLink link : theOneToManyLinks) {
            ret.add(new Holder(link));
        }
        return ret;
    }

    private static class Holder {

        public Holder(OneToManyLink aLink) {
            theLink  = aLink;
            theEmptyEntity = aLink.createEmptyEntity();
        }

        public void loadEntity(ResultSet aResultSet) {
            thePreviousEntity = theLoadedEntity;
            theLoadedEntity = theLink.loadEntity(aResultSet);
        }

        public boolean isNotEmpty() {
            return !theEmptyEntity.equals(theLoadedEntity);
        }

        public boolean isNew() {
            if(theLoadedEntity!=null) {
                return thePreviousEntity == null || !thePreviousEntity.equals(theLoadedEntity);
            } else {
                throw new IllegalStateException("Loaded entity is null");
            }
        }

        public Object getLoadedEntity() {
            return theLoadedEntity;
        }

        public void addToList(Object aEntity) {
            if(isNew()) {
                theList.add(aEntity);
                theLink.setChildren(theLoadedEntity, Collections.unmodifiableList(theList));
            } else {
                theList.add(aEntity);
                theLink.setChildren(thePreviousEntity, Collections.unmodifiableList(theList));
            }
        }

        public void clearChildren() {
            theList = new LinkedList<Object>();
//            if(thePreviousEntity!=null) {
//                List<Object> list = Collections.unmodifiableList(theList);
//                theLink.setChildren(thePreviousEntity, list);
//            } else {
//                if(theList.size()>0) {
//                    throw new IllegalStateException("List with children has no parent");
//                }
//            }
        }

        private Object thePreviousEntity = null ;
        private Object theLoadedEntity = null;
        private final OneToManyLink theLink;
        private final Object theEmptyEntity;
        private List<Object> theList = new LinkedList<Object>();


    }


    private final List<OneToManyLink> theOneToManyLinks;

}