package com.tensquare.friend.dao;

import com.tensquare.friend.pojo.Friend;
import com.tensquare.friend.pojo.NoFriend;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface NoFriendDao extends JpaRepository<NoFriend,String> {
    
    /**
     * 根据用户ID与被关注用户ID查询记录个数，作用是为了查找该用户是否关注了另外一用户
     * @param userid
	 * @param friendid 
     * @return int
     */
    public NoFriend findByUseridAndFriendid(String userid,String friendid);

}
