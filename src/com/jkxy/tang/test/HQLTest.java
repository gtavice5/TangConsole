package com.jkxy.tang.test;

import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.junit.Test;

import com.jkxy.tang.entity.Poet;
import com.jkxy.tang.entity.Poetry;
import com.jkxy.tang.util.HibernateUtils;

public class HQLTest {

	@Test
	public void select1(){
		Transaction tx=null;
		Session session=null;
		
		try {
			session=HibernateUtils.getSession();
			tx=session.beginTransaction();
			String hql="select poet from Poet as poet order by poet.id desc";
			
			Query query=session.createQuery(hql);
			List<Poet> listP=(List<Poet>)query.list();
			for (Poet poet : listP) {
				System.out.println(poet);
			}
			
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
