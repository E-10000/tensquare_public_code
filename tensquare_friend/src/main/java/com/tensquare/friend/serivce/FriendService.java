package com.tensquare.friend.serivce;

import com.tensquare.friend.dao.FriendDao;
import com.tensquare.friend.dao.NoFriendDao;
import com.tensquare.friend.pojo.Friend;
import com.tensquare.friend.pojo.NoFriend;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @description：TODO
 */
@Service
@Transactional
public class FriendService {

    @Autowired
    private FriendDao friendDao;

    @Autowired
    private NoFriendDao noFriendDao;

    public int addFriend(String userid,String friendid){
        //如果已经加为好友了
        if (friendDao.findByUseridAndFriendid(userid, friendid)!=null){
            return 0;
        }
        //向喜欢表添加记录
        Friend friend = new Friend();
        friend.setUserid(userid);
        friend.setFriendid(friendid);
        friend.setIslike("0");
        friendDao.save(friend);

        //判断对方是否喜欢你，如果喜欢，就把isLike变成1
        if (friendDao.findByUseridAndFriendid(friendid,userid)!=null){
            friendDao.updateLike(userid,friendid,"1");
            friendDao.updateLike(friendid,userid,"1");
        }
        return 1;

    }

    /*
     *
     * @param userid
     * @param friendid
     * @return 0重复数据，1保存成功
     */
    public int addNoFriend(String userid, String friendid) {
        //判断表中是否已经有该数据了
        NoFriend noFriend = noFriendDao.findByUseridAndFriendid(userid, friendid);
        if (noFriend!=null){
            return 0;
        }

        noFriend = new NoFriend();
        noFriend.setUserid(userid);
        noFriend.setFriendid(friendid);
        noFriendDao.save(noFriend);
        return 1;
    }


    public void deleteFriend(String userid,String friendid) {
        //删除friend表中的数据
        friendDao.deleteFriend(userid,friendid);
        //更新friendid到userid的数据为0
        friendDao.updateLike(friendid,userid,"0");
        //nofriend表中添加数据
        NoFriend noFriend = new NoFriend();
        noFriend.setUserid(userid);
        noFriend.setFriendid(friendid);
        noFriendDao.save(noFriend);
    }

    public List<Friend> findAll() {
        return friendDao.findAll();
    }
}
