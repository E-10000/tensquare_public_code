package com.tensquare.user.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.tensquare.user.pojo.User;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

/**
 * 数据访问接口
 * @author Administrator
 *
 */
@Transactional
public interface UserDao extends JpaRepository<User,String>,JpaSpecificationExecutor<User>{
	public User findByMobile(String mobile);

	/*
	 * 跟新自己的关注数
	 * @param userid
	 * @param x
	 * @return void
	 */
	@Modifying
	@Query(value="update tb_user set followcount=followcount+?2 where id =?1",nativeQuery=true)
	public void updateFollowcount(String userid, int x);

	/*
	 * 更新别人的粉丝数
	 * @param userid
	 * @param x
	 * @return void
	 */
	@Modifying
	@Query(value="update tb_user set fanscount=fanscount+?2 where id =?1",nativeQuery=true)
	public void updateFanscount(String friendid, int x);
}
