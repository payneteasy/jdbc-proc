package com.googlecode.jdbcproc.daofactory.it.testdao.dao;

import com.googlecode.jdbcproc.daofactory.annotation.AMetaLoginInfo;
import com.googlecode.jdbcproc.daofactory.it.testdao.model.Carabiner;
import com.googlecode.jdbcproc.daofactory.it.testdao.model.DynamicRope;
import com.googlecode.jdbcproc.daofactory.it.testdao.model.Harness;
import com.googlecode.jdbcproc.daofactory.it.testdao.model.ChalkBag;
import com.googlecode.jdbcproc.daofactory.annotation.AStoredProcedure;

import java.util.Date;
import java.util.List;

public interface VerticalityDao {
    
    @AStoredProcedure(name = "upload_carabiners")
    void uploadCarabiners(Date uploadDate, List<Carabiner> carabiners);
    
    @AStoredProcedure(name = "upload_carabiners_2")
    int uploadCarabiners(Date uploadDate, List<Carabiner> carabiners, Date uploadDate2);
    
    @AMetaLoginInfo
    @AStoredProcedure(name = "upload_carabiners_with_meta_login_info")
    void uploadCarabinersWithMetaLoginInfo(Date uploadDate, List<Carabiner> carabiners);
    
    @AStoredProcedure(name = "upload_dynamic_ropes")
    void uploadDynamicRopes(List<DynamicRope> dynamicRopes, Date uploadDate);
    
    @AMetaLoginInfo
    @AStoredProcedure(name = "upload_dynamic_ropes_with_meta_login_info")
    void uploadDynamicRopesWithMetaLoginInfo(List<DynamicRope> dynamicRopes, Date uploadDate);
    
    @AStoredProcedure(name = "upload_verticality")
    void uploadVerticality(Date uploadDate, List<Carabiner> carabiners, List<DynamicRope> dynamicRopes);
  
    @AMetaLoginInfo
    @AStoredProcedure(name = "upload_verticality_with_meta_login_info")
    void uploadVerticalityWithMetaLoginInfo(Date uploadDate, List<Carabiner> carabiners, List<DynamicRope> dynamicRopes);
  
    @AStoredProcedure(name = "upload_harnesses")
    void uploadHarnesses(List<Harness> harnesses);
  
    @AStoredProcedure(name = "create_chalk_bag")
    void createChalkBag(ChalkBag chalkBag);
    
    @AStoredProcedure(name = "get_chalk_bags")
    List<ChalkBag> getChalkBags();
}
