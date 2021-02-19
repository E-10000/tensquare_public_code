package com.tensquare.friend.controller;

import com.tensquare.friend.client.UserClient;
import com.tensquare.friend.serivce.FriendService;
import entity.Result;
import entity.StatusCode;
import io.jsonwebtoken.Claims;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import util.JwtUtil;

import javax.servlet.http.HttpServletRequest;

/**
 * @description：TODO
 */
@RestController
@RequestMapping("/friend")
public class FriendController {

    @Autowired
    private HttpServletRequest httpServletRequest;

    @Autowired
    private FriendService friendService;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserClient userClient;

    /**
     * test
     * @param
     * @return entity.Result
     */

    @GetMapping(value = "")
    public Result findAll(){
        return new Result(true,StatusCode.OK,"查询成功",friendService.findAll());
    }



    /*
     *
     * @param friendid 对方ID
     * @param type 1喜欢，2不喜欢
     * @return entity.Result
     */
    @PutMapping(value = "like/{friendid}/{type}")
    public Result addFriend(@PathVariable String friendid,@PathVariable String type){
        //验证登陆
        String token = (String) httpServletRequest.getAttribute("claims_user");
        if (token==null || "".equals(token)){
            //如果当前用户没有user角色
            return new Result(false, StatusCode.ERROR,"无权限操作");
        }
        //将token转义
        Claims claims = jwtUtil.parseJWT(token);
        String userid = claims.getId();

        //判断是添加好友好事添加非好友
        if (type!=null) {//非空
            //添加好友
            if (type.equals("1")) {

                //根据不同情况添加好友
                int flag = friendService.addFriend(userid, friendid);
                //重复添加好友
                if (flag==0){
                    return new Result(false, StatusCode.ERROR,"重复添加好友");
                }
                //添加成功
                if (flag == 1) {
                    userClient.updateMyFollowcountAndHisFanscount(userid,friendid,1);
                    return new Result(true, StatusCode.OK,"添加成功");
                }

            }
            //添加非好友
            else if (type.equals("2")){
                int flag = friendService.addNoFriend(userid, friendid);
                //重复添加非好友
                if (flag==0){
                    return new Result(false, StatusCode.ERROR,"重复点击了不喜欢");
                }
                //添加成功
                if (flag == 1) {
                    return new Result(true, StatusCode.OK,"添加成功");
                }
            }
            //如果都不是
            else return new Result(false, StatusCode.ERROR,"参数异常");


        }
        //如果type为空
        return new Result(false, StatusCode.ERROR,"参数异常");
    }

    /*
     *删除好友，在friend表中删除对应数据，更新双方关注数和粉丝数，在nofriend表中添加数据
     * @param friendid
     * @return entity.Result
     */
    @DeleteMapping("{friendid}")
    public Result deleteFriend(@PathVariable("friendid") String friendid){
        //验证登陆
        String token = (String) httpServletRequest.getAttribute("claims_user");
        if (token==null || "".equals(token)){
            //如果当前用户没有user角色
            return new Result(false, StatusCode.ERROR,"无权限操作");
        }
        //将token转义
        Claims claims = jwtUtil.parseJWT(token);
        String userid = claims.getId();


        friendService.deleteFriend(userid,friendid);
        userClient.updateMyFollowcountAndHisFanscount(userid,friendid,-1);
        return new Result(true,StatusCode.OK,"删除成功");

    }
}
