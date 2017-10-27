package com.jkxy.tang.main;

import java.util.List;
import java.util.Scanner;
import java.util.Set;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.jkxy.tang.entity.Poet;
import com.jkxy.tang.entity.Poetry;
import com.jkxy.tang.util.HibernateUtils;

public class Main {

	public static void main(String[] args) {
		//输出数据中名字为三个字的诗人列表及其诗词的数量统计。格式如：姓名 － 诗词数量
		getPoetAndCountNum();
		
		//获取诗歌总数量
//		int[] countAndId=getPoetryCount("李白");
//		System.out.println(countAndId[0]+" "+countAndId[1]);
		
		//分页输出诗人李白所有诗词的标题，要求每10个标题分为1页
//		pagingOutput("李白", 10);
		
		//输出某一个诗人所有的诗词前15个字（包括标点符号），这个诗人的名字要求用户输入，敲回车后进行查询操作。
//		outputFirst15();
	}
	
	public static void getPoetAndCountNum(){
		Transaction tx=null;
		Session session=null;
		System.out.println("［使用HQL实现］输出数据中名字为三个字的诗人列表及其诗词的数量统计。格式如：姓名 － 诗词数量");
		
		try {
			session=HibernateUtils.getSession();
			tx=session.beginTransaction();
			String hql="from Poet p where length(p.name)=9";
			
			Query query=session.createQuery(hql);
			List<Poet> listP=(List<Poet>)query.list();
			for (Poet poet : listP) {
				if(poet.getName().length()==3){
					System.out.println(poet.getName()+" - "+poet.getSetPoetries().size());
				}
					
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
	
	public static void pagingOutput(String name,int pageSize){
		Transaction tx=null;
		Session session=null;
		int count=getPoetryCount(name)[1];
		int totalPage=0;
		if(getPoetryCount(name)[1]%pageSize==0){
			totalPage=getPoetryCount(name)[1]/pageSize;
		}else{
			totalPage=getPoetryCount(name)[1]/pageSize+1;
		}
		
		System.out.println("诗人"+name+"相关的数据总共 "+totalPage+"页。");
		
		for (int i = 1; i <= totalPage; i++) {
			try {
				session=HibernateUtils.getSession();
				tx=session.beginTransaction();
				String hql="from Poetry p where p.poet.name=:name order by p.id";
				if (i==totalPage) {
					System.out.println("最后一页内容：");
				}else{
					System.out.println("第"+(i)+"页内容：");
				}
				
				
				Query query=session.createQuery(hql).setFirstResult((i-1)*pageSize).setMaxResults(pageSize);
				query.setString("name", name);
				List<Poetry> list=(List<Poetry>)query.list();
					
				for (Poetry poetry : list) {
					System.out.println("《"+poetry.getTitle()+"》");
	
				}
	
				tx.commit();
			} catch (Exception e) {
				if(tx!=null){
					tx.rollback();
				}
				
			}finally {
				if(session!=null){
					HibernateUtils.closeSession(session);
				}
				
			}
		}
	}
	
	public static void outputFirst15(){
		Transaction tx=null;
		Session session=null;
		System.out.println("输入诗人的名字:");
		Scanner scanner=new Scanner(System.in);
		String pName=scanner.nextLine();
		System.out.println(pName);
		
		try {
			session=HibernateUtils.getSession();
			tx=session.beginTransaction();
			String hql="from Poetry p where p.poet.name=:name order by p.id";
			
			Query query=session.createQuery(hql);
			query.setString("name",pName.trim());
			List<Poetry> list=(List<Poetry>)query.list();
				
			for (Poetry poetry : list) {
//				System.out.println("《"+poetry.getTitle()+"》");
				System.out.println(poetry.getContent().substring(0, 15));
			}

			tx.commit();
		} catch (Exception e) {
			if(tx!=null){
				tx.rollback();
			}
			
		}finally {
			if(session!=null){
				HibernateUtils.closeSession(session);
			}
			
		}
		scanner.close();
	}
	
	public static int[] getPoetryCount(String name){
		Transaction tx=null;
		Session session=null;
		int[] countAndId = new int[2];
		
		try {
			session=HibernateUtils.getSession();
			tx=session.beginTransaction();
			String hql="select distinct p from Poet p inner join fetch p.setPoetries where p.name=?";
			
			Query query=session.createQuery(hql);
			query.setString(0, name);
			List<Poet> list=(List<Poet>)query.list();
			for (Poet poet : list) {
				countAndId[0]=poet.getId();
				countAndId[1]=poet.getSetPoetries().size();
//				System.out.println(poet.getName()+poet.getId()+poet.getSetPoetries().size());
			}
			
			tx.commit();
		} catch (Exception e) {
			if(tx!=null){
				tx.rollback();
			}
			
		}finally {
			if(session!=null){
				HibernateUtils.closeSession(session);
			}
			
		}
		
		return countAndId;
	}
	

		
}