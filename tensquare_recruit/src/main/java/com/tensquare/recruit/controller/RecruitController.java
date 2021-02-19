package com.tensquare.recruit.controller;
import java.util.List;
import java.util.Map;

import com.tensquare.recruit.dao.RecruitDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import com.tensquare.recruit.pojo.Recruit;
import com.tensquare.recruit.service.RecruitService;

import entity.PageResult;
import entity.Result;
import entity.StatusCode;
/**
 * 控制器层
 * @author Administrator
 *
 */
@RestController
@CrossOrigin
@RequestMapping("/recruit")
public class RecruitController {

	@Autowired
	private RecruitService recruitService;
	
	
	/**
	 * 查询全部数据
	 * @return
	 */
	@GetMapping("")
	public Result findAll(){
		return new Result(true,StatusCode.OK,"查询成功",recruitService.findAll());
	}
	
	/**
	 * 根据ID查询
	 * @param id ID
	 * @return
	 */
	@GetMapping("{id}")
	public Result findById(@PathVariable("id") String id){
		return new Result(true,StatusCode.OK,"查询成功",recruitService.findById(id));
	}


	/**
	 * 分页+多条件查询
	 * @param searchMap 查询条件封装
	 * @param page 页码
	 * @param size 页大小
	 * @return 分页结果
	 */
	@PostMapping("search/{page}/{size}")
	public Result findSearch(@RequestBody Map searchMap , @PathVariable("page") int page, @PathVariable("size") int size){
		System.out.println(searchMap);
		System.out.println();
		Page<Recruit> pageList = recruitService.findSearch(searchMap, page, size);
		return  new Result(true,StatusCode.OK,"查询成功",  new PageResult<Recruit>(pageList.getTotalElements(), pageList.getContent()) );
	}

	/**
     * 根据条件查询
     * @param searchMap
     * @return
     */
	@PostMapping("search")
    public Result findSearch(@RequestBody Map searchMap){
        return new Result(true,StatusCode.OK,"查询成功",recruitService.findSearch(searchMap));
    }
	
	/**
	 * 增加
	 * @param recruit
	 */
	@PostMapping("")
	public Result add(@RequestBody Recruit recruit){
		recruitService.add(recruit);
		return new Result(true,StatusCode.OK,"增加成功");
	}
	
	/**
	 * 修改
	 * @param recruit
	 */
	@PutMapping("{id}")
	//未
	public Result update(@RequestBody Recruit recruit, @PathVariable("id") String id ){
		recruit.setId(id);
		recruitService.update(recruit);		
		return new Result(true,StatusCode.OK,"修改成功");
	}
	
	/**
	 * 删除
	 * @param id
	 */
	@DeleteMapping("{id}")
	public Result delete(@PathVariable("id") String id ){
		recruitService.deleteById(id);
		return new Result(true,StatusCode.OK,"删除成功");
	}

	@GetMapping("search/recommend")
	public Result findTop6ByStateOrderByCreatetimeDesc(){
		List<Recruit> recruits = recruitService.findTop6ByStateOrderByCreatetimeDesc();
		return new Result(true,StatusCode.OK,"查询成功",recruits);
	}

	@GetMapping("search/newlist")
	public Result findTop6ByStateNotOrderByCreatetimeDesc(){
		List<Recruit> recruits = recruitService.findTop6ByStateNotOrderByCreatetimeDesc();
		return new Result(true,StatusCode.OK,"查询成功",recruits);
	}

}
