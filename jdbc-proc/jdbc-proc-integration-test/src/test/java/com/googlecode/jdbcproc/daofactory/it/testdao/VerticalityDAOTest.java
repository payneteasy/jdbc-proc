package com.googlecode.jdbcproc.daofactory.it.testdao;

import com.googlecode.jdbcproc.daofactory.it.DatabaseAwareTest;
import com.googlecode.jdbcproc.daofactory.it.testdao.model.Carabiner;
import com.googlecode.jdbcproc.daofactory.it.testdao.model.DynamicRope;
import com.googlecode.jdbcproc.daofactory.it.testdao.model.Carabiner.Builder;
import com.googlecode.jdbcproc.daofactory.it.testdao.dao.VerticalityDAO;

import java.util.List;
import java.util.ArrayList;
import java.util.Date;


public class VerticalityDAOTest  extends DatabaseAwareTest {
    
    private VerticalityDAO verticalityDAO;

    public void setVerticalityDAO(VerticalityDAO verticalityDAO) {
        this.verticalityDAO = verticalityDAO;
    }

    public void test() {
        List<Carabiner> carabiners = new ArrayList<Carabiner>(3);
        Carabiner carabiner1 = new Builder("ATTACHE 3D", 55.0).
            breakingStrengthMajorAxis(22).
            breakingStrengthOpenGate(6).
            breakingStrengthCrossLoaded(7).
            gateOpening(22).
            build();
        Carabiner carabiner2 = new Builder("LOCKER", 63.0).
            breakingStrengthMajorAxis(24).
            breakingStrengthOpenGate(8).
            breakingStrengthCrossLoaded(9).
            gateOpening(21).
            build();
        Carabiner carabiner3 = new Builder("Am'D", 74.0).
            breakingStrengthMajorAxis(28).
            breakingStrengthOpenGate(8).
            breakingStrengthCrossLoaded(7).
            gateOpening(22).
            build();
        Carabiner carabiner4 = new Builder("ATTACHE", 80.0).
            breakingStrengthMajorAxis(23).
            breakingStrengthOpenGate(6).
            breakingStrengthCrossLoaded(7).
            gateOpening(20).
            build();

        carabiners.add(carabiner1);
        carabiners.add(carabiner2);
        carabiners.add(carabiner3);
        carabiners.add(carabiner4);
        
        List<DynamicRope> dynamicRopes = new ArrayList<DynamicRope>(2);
        DynamicRope dynamicRope1 = new DynamicRope.Builder("8.2 Dragonfly").
            diameter(8.2).
            lengths(70).
            type("half rope").
            weight(41.8).
            build();
        DynamicRope dynamicRope2 = new DynamicRope.Builder("9.8 Nomad").
            diameter(9.2).
            lengths(70).
            type("single rope").
            weight(63.0).
            build();
        
        dynamicRopes.add(dynamicRope1);
        dynamicRopes.add(dynamicRope2);
        
        Date uploadDate = new Date();
        verticalityDAO.uploadCarabinders(uploadDate, carabiners);
        verticalityDAO.uploadDynamicRopes(dynamicRopes, uploadDate);
        verticalityDAO.uploadVerticality(uploadDate, carabiners,  dynamicRopes);
    }
}