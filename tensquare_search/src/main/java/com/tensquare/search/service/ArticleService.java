package com.tensquare.search.service;

import com.tensquare.search.dao.ArticleDao;
import com.tensquare.search.pojo.Article;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import util.IdWorker;

import java.util.List;

@Service
public class ArticleService {
    @Autowired
    private ArticleDao articleDao;
    @Autowired
    private IdWorker idWorker;

    public void add(Article article){
        article.setId(idWorker.nextId()+"");
        articleDao.save(article);
    }

    public void deleteById(String id){
        articleDao.deleteById(id);
    }

    public Page<Article> findByTitleOrContentLike(String keyword,int page,int size){
        Pageable pageable = PageRequest.of(page-1, size);
        return  articleDao.findByTitleOrContentLike(keyword,keyword,pageable);
    }


}
