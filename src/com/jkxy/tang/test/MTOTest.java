package com.jkxy.tang.test;

import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.junit.Test;

import com.jkxy.tang.entity.Poet;
import com.jkxy.tang.entity.Poetry;
import com.jkxy.tang.util.HibernateUtils;

public class MTOTest {
	
	/**
	 * 生成DDL语句
	 */
/*	@Test
	public void sechmDDL(){
		Configuration cfg=new Configuration().configure();
		SchemaExport schema=new SchemaExport(cfg);
		schema.create(true, true);
	}*/
	
	@Test	
	public void add(){
		Transaction tx=null;
		Session session=null;
		
		try {
			session=HibernateUtils.getSession();
			tx=session.beginTransaction();
			//在单向的多对一中，要先添加少的一端
			Poet poet=new Poet();
			poet.setName("俞振翔");
			session.save(poet);
			
			Poetry poetry=new Poetry();
			poetry.setPoet(poet);
			poetry.setTitle("222");
			poetry.setContent("333");
			session.save(poetry);

			
			tx.commit();
		} catch (Exception e) {
			if(tx!=null){
				tx.rollback();
			}
		}finally {
			HibernateUtils.closeSession(session);
		}
	}
	
	@Test
	public void get(){
		Transaction tx=null;
		Session session=null;
		
		try {
			session=HibernateUtils.getSession();
			tx=session.beginTransaction();
			
			Poetry poetry=(Poetry)session.get(Poetry.class, 2532);
			System.out.println(poetry.getTitle()+poetry.getContent());
//			Hibernate.initialize(poetry.getPoet());
			System.out.println(poetry.getPoet().getName());
			
			tx.commit();
		} catch (Exception e) {
			if(tx!=null){
				tx.rollback();
			}
		}finally {
			HibernateUtils.closeSession(session);
		}
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
