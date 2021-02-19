package com.tensquare.friend.dao;

import com.tensquare.friend.pojo.Friend;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface FriendDao extends JpaRepository<Friend,String> {
    
    /**
     * 根据用户ID与被关注用户ID查询记录个数，作用是为了查找该用户是否关注了另外一用户
     * @param userid
	 * @param friendid 
     * @return int
     */
    public Friend findByUseridAndFriendid(String userid,String friendid);

    /**
     * 更新为互相喜欢
     * @param userid
	 * @param friendid
	 * @param isLike 
     * @return void
     */
    @Modifying
    @Query(value="update tb_friend f set f.islike=?3 where f.userid = ?1 and f.friendid=?2",nativeQuery=true)
    public void updateLike(String userid,String friendid,String isLike);

    /*
     *根据userid和friendid进行删除
     * @param userid
     * @param friendid
     * @return void
     */
    @Query(value="delete from tb_friend where userid = ?1 and friendid=?2",nativeQuery=true)
    @Modifying
    public void deleteFriend(String userid, String friendid);
}
