package com.lostsys.data.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class MainController {
	private Long ver=new Date().getTime();	
	
	@PersistenceContext
	private EntityManager em;
	
    @RequestMapping(value="/")
    @ResponseBody
    ModelAndView home() {
    	ModelAndView mav=new ModelAndView();
    	
    	mav.setViewName("index");
    	mav.addObject( "ver", ver);
    	mav.addObject( "videos", getVideos() );
    	
        return mav;
    	}	
    
    @RequestMapping(value="/api")
    @ResponseBody
    HashMap<String,Object> api(
    		@RequestParam(value="from") String from1,
    		@RequestParam(value="to") String to1,
    		@RequestParam(value="video") String video1
    		) {
    	HashMap<String,Object> r=new HashMap<String,Object>();
    	
    	r.put( "subsdia" , getSubscriptoresDia( from1, to1, video1) );
       	r.put( "subssem" , getSubscriptoresSemana( from1, to1, video1) );
        
    	return r;
    	}
    
    private List<Object[]> getSubscriptoresSemana(String from1, String to1, String video1) {
    	String sql="SELECT DAYNAME(date),sum(subscribers) FROM youtube.subscribers ";
    	String where=getConditionalSql(from1, to1, video1);
    	
    	sql+=where+" group by WEEKDAY(date)";
               
        return getSqlValues(sql,from1, to1, video1);
    	}
    
    private List<Object[]> getSubscriptoresDia(String from1, String to1, String video1) {
    	String sql="SELECT date,sum(subscribers) FROM youtube.subscribers ";
    	String where=getConditionalSql(from1, to1, video1);
    	
    	sql+=where+" group by date order by date desc";
               
        return getSqlValues(sql,from1, to1, video1);
    	} 
    
    private List<Object[]> getSqlValues(String sql,String from1, String to1, String video1) {
    	Query query = em.createNativeQuery(sql);
       	
    	if ( !from1.equals("") ) query.setParameter("from", from1);
    	if ( !to1.equals("") ) query.setParameter("to", to1);
    	if ( !video1.equals("") ) query.setParameter("video", video1);
               
        return query.getResultList();    	
    	}

    private String getConditionalSql(String from1, String to1, String video1) {
    	String where="";
    	if ( !from1.equals("") ) where+=" date >= :from ";

    	if ( !to1.equals("") ) {
    		if ( !where.equals("") ) where+=" and ";
    		where+=" date <= :to ";
    		}

    	if ( !video1.equals("") ) {
    		if ( !where.equals("") ) where+=" and ";
    		where+=" video = :video ";
    		}
    	
    	if ( !where.equals("") ) where=" where "+where;
    	
    	return where;
    	}    
    
    private List<Object[]> getVideos() {
    	Query query = em.createNativeQuery("select distinct video, video_title from subscribers;");
    	return query.getResultList();
    	}    
	
	}
