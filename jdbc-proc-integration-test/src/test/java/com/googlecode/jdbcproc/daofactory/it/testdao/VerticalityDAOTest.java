package com.googlecode.jdbcproc.daofactory.it.testdao;

import com.googlecode.jdbcproc.daofactory.it.DatabaseAwareTest;
import com.googlecode.jdbcproc.daofactory.it.testdao.dao.VerticalityDao;
import com.googlecode.jdbcproc.daofactory.it.testdao.model.Carabiner;
import com.googlecode.jdbcproc.daofactory.it.testdao.model.DynamicRope;
import com.googlecode.jdbcproc.daofactory.it.testdao.model.Harness;
import com.googlecode.jdbcproc.daofactory.it.testdao.model.ChalkBag;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.ArrayList;
import java.util.Date;


public class VerticalityDAOTest  extends DatabaseAwareTest {
    
    private VerticalityDao verticalityDao;

    public void setVerticalityDao(VerticalityDao verticalityDao) {
        this.verticalityDao = verticalityDao;
    }

    public void testProcedureWithListParameters() {
        List<Harness> harnesses = new ArrayList<Harness>(2);
        Harness harness1 = new Harness.Builder("HIRUNDOS").
            color("orange/silver").
            size("S").
            weight(280).
            build();
        Harness harness2 = new Harness.Builder("CORAX").
            color("dark gray").
            size("1").
            weight(510).
            build();
        
        harnesses.add(harness1);
        harnesses.add(harness2);
        
        verticalityDao.uploadHarnesses(harnesses);
        
        verticalityDao.uploadHarnessesCollection(Collections.unmodifiableCollection(harnesses));
    }
  
    public void testProcedureWithListAndNonListParameters() {
        List<Carabiner> carabiners = new ArrayList<Carabiner>(3);
        Carabiner carabiner1 = new Carabiner.Builder("ATTACHE 3D", 55.0).
            breakingStrengthMajorAxis(22).
            breakingStrengthOpenGate(6).
            breakingStrengthCrossLoaded(7).
            gateOpening(22).
            build();
        Carabiner carabiner2 = new Carabiner.Builder("LOCKER", 63.0).
            breakingStrengthMajorAxis(24).
            breakingStrengthOpenGate(8).
            breakingStrengthCrossLoaded(9).
            gateOpening(21).
            build();
        Carabiner carabiner3 = new Carabiner.Builder("Am'D", 74.0).
            breakingStrengthMajorAxis(28).
            breakingStrengthOpenGate(8).
            breakingStrengthCrossLoaded(7).
            gateOpening(22).
            build();
        Carabiner carabiner4 = new Carabiner.Builder("ATTACHE", 80.0).
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
        verticalityDao.uploadCarabiners(uploadDate, carabiners);
        int entityId = verticalityDao.uploadCarabiners(uploadDate, carabiners, uploadDate);
        assertEquals(1, entityId);
        entityId = verticalityDao.uploadCarabiners(null, carabiners, uploadDate);
        assertEquals(1, entityId);
        verticalityDao.uploadCarabinersWithMetaLoginInfo(uploadDate, carabiners);
        verticalityDao.uploadDynamicRopes(dynamicRopes, uploadDate);
        verticalityDao.uploadDynamicRopesWithMetaLoginInfo(dynamicRopes, uploadDate);
        verticalityDao.uploadVerticality(uploadDate, carabiners,  dynamicRopes);
        verticalityDao.uploadVerticalityWithMetaLoginInfo(uploadDate, carabiners,  dynamicRopes);
      
        Collection<Carabiner> carabinersCollection = Collections.unmodifiableCollection(carabiners);
        Collection<DynamicRope> dynamicRopeCollection 
            = Collections.unmodifiableCollection(dynamicRopes);
      
        verticalityDao.uploadCarabinersCollection(uploadDate, carabinersCollection);
        entityId = verticalityDao
            .uploadCarabinersCollection(uploadDate, carabinersCollection, uploadDate);
        assertEquals(1, entityId);
        entityId = verticalityDao
            .uploadCarabinersCollection(null, carabinersCollection, uploadDate);
        assertEquals(1, entityId);
        verticalityDao.uploadCarabinersWithMetaLoginInfoCollection(uploadDate, carabinersCollection);
        verticalityDao.uploadDynamicRopesCollection(dynamicRopeCollection, uploadDate);
        verticalityDao
            .uploadDynamicRopesWithMetaLoginInfoCollection(dynamicRopeCollection, uploadDate);
        verticalityDao
            .uploadVerticalityCollection(uploadDate, carabinersCollection, dynamicRopeCollection);
        verticalityDao.uploadVerticalityWithMetaLoginInfoCollection(uploadDate, carabinersCollection
            , dynamicRopeCollection);
    }
    
    public void testReturnEntityWithBigintTypeField() {
        ChalkBag chalkBag1 = new ChalkBag();
        chalkBag1.setName("BANDI");
        chalkBag1.setColor("TOPO print Charlet orange");
        chalkBag1.setMaterials("polyester and nylon");
        ChalkBag chalkBag2 = new ChalkBag();
        chalkBag2.setName("KODA");
        chalkBag2.setColor("TOPO print sandstone");
        chalkBag2.setMaterials("polyester and nylon");
        
        verticalityDao.createChalkBag(chalkBag1);
        verticalityDao.createChalkBag(chalkBag2);
        
        List<ChalkBag> chalkBags = verticalityDao.getChalkBags();
        assertEquals(2, chalkBags.size());
    }
}
