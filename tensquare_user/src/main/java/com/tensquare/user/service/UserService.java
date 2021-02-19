package com.tensquare.user.service;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Selection;
import javax.servlet.http.HttpServletRequest;

import com.mysql.cj.protocol.WatchableOutputStream;
import com.tensquare.user.pojo.Admin;
import entity.Result;
import entity.StatusCode;
import io.jsonwebtoken.Claims;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import util.IdWorker;

import com.tensquare.user.dao.UserDao;
import com.tensquare.user.pojo.User;
import util.JwtUtil;

/**
 * 服务层
 * 
 * @author Administrator
 *
 */
@Service
public class UserService {

	@Autowired
	private UserDao userDao;
	
	@Autowired
	private IdWorker idWorker;

	@Autowired
	private RedisTemplate redisTemplate;

	@Autowired
	private RabbitTemplate rabbitTemplate;

	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	//消息头
	@Autowired
	private HttpServletRequest request;

	@Autowired
	private JwtUtil jwtUtil;

	/**
	 * 查询全部列表
	 * @return
	 */
	public List<User> findAll() {
		return userDao.findAll();
	}

	
	/**
	 * 条件查询+分页
	 * @param whereMap
	 * @param page
	 * @param size
	 * @return
	 */
	public Page<User> findSearch(Map whereMap, int page, int size) {
		Specification<User> specification = createSpecification(whereMap);
		PageRequest pageRequest =  PageRequest.of(page-1, size);
		return userDao.findAll(specification, pageRequest);
	}

	
	/**
	 * 条件查询
	 * @param whereMap
	 * @return
	 */
	public List<User> findSearch(Map whereMap) {
		Specification<User> specification = createSpecification(whereMap);
		return userDao.findAll(specification);
	}

	/**
	 * 根据ID查询实体
	 * @param id
	 * @return
	 */
	public User findById(String id) {
		return userDao.findById(id).get();
	}

	/**
	 * 增加
	 * @param user
	 */
	public void add(User user) {
		user.setId( idWorker.nextId()+"" );
		userDao.save(user);
	}

	/**
	 * 修改
	 * @param user
	 */
	public void update(User user) {
		userDao.save(user);
	}

	/**
	 * 删除需要具有管理员权限，权限放在消息头里面
	 * 删除
	 * @param id
	 */
	public void deleteById(String id) {
		//判断是否有claims_admin的消息头
		String token = (String) request.getAttribute("claims_admin");
		System.out.println(token);
		//如果为空
		if (token==null || "".equals(token)){
			throw new RuntimeException("权限不足");
		}

		userDao.deleteById(id);
	}

	/**
	 * 动态条件构建
	 * @param searchMap
	 * @return
	 */
	private Specification<User> createSpecification(Map searchMap) {

		return new Specification<User>() {

			@Override
			public Predicate toPredicate(Root<User> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				List<Predicate> predicateList = new ArrayList<Predicate>();
                // ID
                if (searchMap.get("id")!=null && !"".equals(searchMap.get("id"))) {
                	predicateList.add(cb.like(root.get("id").as(String.class), "%"+(String)searchMap.get("id")+"%"));
                }
                // 手机号码
                if (searchMap.get("mobile")!=null && !"".equals(searchMap.get("mobile"))) {
                	predicateList.add(cb.like(root.get("mobile").as(String.class), "%"+(String)searchMap.get("mobile")+"%"));
                }
                // 密码
                if (searchMap.get("password")!=null && !"".equals(searchMap.get("password"))) {
                	predicateList.add(cb.like(root.get("password").as(String.class), "%"+(String)searchMap.get("password")+"%"));
                }
                // 昵称
                if (searchMap.get("nickname")!=null && !"".equals(searchMap.get("nickname"))) {
                	predicateList.add(cb.like(root.get("nickname").as(String.class), "%"+(String)searchMap.get("nickname")+"%"));
                }
                // 性别
                if (searchMap.get("sex")!=null && !"".equals(searchMap.get("sex"))) {
                	predicateList.add(cb.like(root.get("sex").as(String.class), "%"+(String)searchMap.get("sex")+"%"));
                }
                // 头像
                if (searchMap.get("avatar")!=null && !"".equals(searchMap.get("avatar"))) {
                	predicateList.add(cb.like(root.get("avatar").as(String.class), "%"+(String)searchMap.get("avatar")+"%"));
                }
                // E-Mail
                if (searchMap.get("email")!=null && !"".equals(searchMap.get("email"))) {
                	predicateList.add(cb.like(root.get("email").as(String.class), "%"+(String)searchMap.get("email")+"%"));
                }
                // 兴趣
                if (searchMap.get("interest")!=null && !"".equals(searchMap.get("interest"))) {
                	predicateList.add(cb.like(root.get("interest").as(String.class), "%"+(String)searchMap.get("interest")+"%"));
                }
                // 个性
                if (searchMap.get("personality")!=null && !"".equals(searchMap.get("personality"))) {
                	predicateList.add(cb.like(root.get("personality").as(String.class), "%"+(String)searchMap.get("personality")+"%"));
                }
				
				return cb.and( predicateList.toArray(new Predicate[predicateList.size()]));

			}
		};

	}

	public void sendSms(String mobile) {
		//生成6位数字验证码
		String checkcode = RandomStringUtils.randomNumeric(6);
		//向缓存保留一份,在缓存保留
		redisTemplate.opsForValue().set("checkcode_"+mobile,checkcode,5, TimeUnit.MINUTES);
		//给用户发一份（rabbitmq）
		//队列为sms，发送Map类型
		Map<String,Object> map = new HashMap<>();
		map.put("mobile",mobile);
		map.put("checkcode",checkcode);

		rabbitTemplate.convertAndSend("sms",map);
		//控制台输出
		System.out.println("验证码为"+checkcode);
	}

	public void register(User user, String code) {
		String checkcodeRedis = (String) redisTemplate.opsForValue().get("checkcode_" + user.getMobile());
		if (checkcodeRedis==null){
			throw new RuntimeException("请先获取手机验证码");
		}
		if (!checkcodeRedis.equals(code)){
			throw new RuntimeException("验证码错误");
		}

		user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
		user.setId(idWorker.nextId()+"");
		user.setRegdate(new Date());
		user.setUpdatedate(new Date());
		user.setLastdate(new Date());
		user.setOnline(0l);
		user.setFanscount(0);
		user.setFollowcount(0);

		userDao.save(user);
	}

	public User login(User user){
		User userLogin = userDao.findByMobile(user.getMobile());
		if (userLogin!=null && bCryptPasswordEncoder.matches(user.getPassword(),userLogin.getPassword())){
			return userLogin;
		}
		return null;
	}

	/*
	 * 关注或者删除别人，自己的关注数+或者-1，别人的粉丝数+或者-1
	 * @param userid
	 * @param friendid
	 * @param x +1自己关注别人，-1自己删除别人
	 * @return void
	 */
    public void updateMyFollowcountAndHisFanscount(String userid, String friendid, int x) {
		userDao.updateFollowcount(userid,x);
		userDao.updateFanscount(friendid,x);
    }
}
