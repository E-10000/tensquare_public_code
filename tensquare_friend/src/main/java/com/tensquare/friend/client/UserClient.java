package com.tensquare.friend.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;

@FeignClient("tensquare-user")
public interface UserClient {
    /*
     * 关注或者删除别人，自己的关注数+或者-1，别人的粉丝数+或者-1
     * @param userid
     * @param friendid
     * @param x +1自己关注别人，-1自己删除别人
     * @return void
     */
    @PutMapping("/user/{userid}/{friendid}/{x}")
    public void updateMyFollowcountAndHisFanscount(@PathVariable("userid") String userid, @PathVariable("friendid") String friendid, @PathVariable("x") int x);
}
