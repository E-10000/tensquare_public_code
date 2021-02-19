package com.tensquare.search.controller;

import com.tensquare.search.pojo.Article;
import com.tensquare.search.service.ArticleService;
import entity.PageResult;
import entity.Result;
import entity.StatusCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import util.IdWorker;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/article")
public class ArticleController {
    @Autowired
    private ArticleService articleService;

//    @GetMapping("")
//    public Result findAll(){
//        List<Article> all = articleService.findAll();
//        return new Result(true,StatusCode.OK,"查找成功",all);
//    }


    @PostMapping("")
    public Result save(@RequestBody Article article){
        articleService.add(article);
        return new Result(true, StatusCode.OK,"添加成功");
    }

    @DeleteMapping("{id}")
    public Result deleteById(@PathVariable String id){
        articleService.deleteById(id);
        return new Result(true,StatusCode.OK,"删除成功");
    }

    @GetMapping("search/{keyword}/{page}/{size}")
    public Result findByTitleOrContentLike(@PathVariable String keyword,@PathVariable int page,@PathVariable int size){
        Page<Article> articles = articleService.findByTitleOrContentLike(keyword, page, size);
        return new Result(true,StatusCode.OK,"查询成功",new PageResult<Article>(articles.getTotalElements(),articles.getContent()));
    }
}
