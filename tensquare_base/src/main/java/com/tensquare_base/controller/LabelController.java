package com.tensquare_base.controller;

import com.tensquare_base.pojo.Label;
import com.tensquare_base.service.LabelService;
import entity.PageResult;
import entity.Result;
import entity.StatusCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Scanner;

@RestController
@CrossOrigin
@RequestMapping("/label")//基础请求都是localhost:9001/label
@RefreshScope
public class LabelController {

    @Value("${test}")
    private String test;

    @Autowired
    private LabelService labelService;

    @Autowired
    private HttpServletRequest httpServletRequest;

    @GetMapping("")
    public Result findAll(){
        //测试
        String header = httpServletRequest.getHeader("Authorization");
        System.out.println(header);
        System.out.println(test);
        return new Result(true, StatusCode.OK,"查询成功",labelService.findAll());
    }

    @GetMapping("{labelId}")
    public Result findById(@PathVariable("labelId") String labelId){
        return new Result(true, StatusCode.OK,"查询成功",labelService.findById(labelId));
    }

    @PostMapping("")
    public Result save(@RequestBody Label label){
        labelService.save(label);
        return new Result(true, StatusCode.OK,"添加成功");
    }

    @PutMapping("{labelId}")
    public Result update(@RequestBody Label label,@PathVariable("labelId") String labelId){
        label.setId(labelId);
        labelService.update(label);
        return new Result(true, StatusCode.OK,"更新成功");
    }

    @DeleteMapping("{labelId}")
    public Result deleteById(@PathVariable("labelId") String labelId){
        labelService.deleteById(labelId);
        return new Result(true, StatusCode.OK,"删除成功");
    }

    @PostMapping("search")
    public Result findSearch(@RequestBody Label label){
        List<Label> list = labelService.findSearch(label);
        return new Result(true,StatusCode.OK,"查询成功",list);
    }

    @PostMapping("search/{page}/{size}")
    public Result findSearchPage(@RequestBody Label label,@PathVariable("page") int page,@PathVariable("size") int size){
        Page<Label> pageData = labelService.findSearchPage(label,page,size);
        //返回的data为PageResult类，其第一个是总数量，第二个是数据,而且PageResult<T>刚好符合
        return new Result(true,StatusCode.OK,"查询成功",new PageResult<Label>(pageData.getTotalElements(),pageData.getContent()));
    }


}
