package com.tensquare.qa.dao;

import org.hibernate.query.NativeQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.tensquare.qa.pojo.Problem;
import org.springframework.data.jpa.repository.Query;

import java.lang.annotation.Native;
import java.util.List;

/**
 * 数据访问接口
 * @author Administrator
 *
 */
public interface ProblemDao extends JpaRepository<Problem,String>,JpaSpecificationExecutor<Problem>{

    @Query(value ="SELECT * FROM `tb_problem` as `p`,`tb_pl` as `pl` where p.id = pl.problemid and pl.labelid = ? ORDER BY replytime DESC",nativeQuery=true)
    public Page<Problem> newList(String id, Pageable pageable);

    @Query(value ="SELECT * FROM `tb_problem` as `p`,`tb_pl` as `pl` where p.id = pl.problemid and pl.labelid = ? ORDER BY reply DESC",nativeQuery=true)
    public Page<Problem> hotList(String id,Pageable pageable);

    @Query(value ="SELECT * FROM `tb_problem` as `p`,`tb_pl` as `pl` where p.id = pl.problemid and pl.labelid = ? and p.reply = 0  ORDER BY replytime DESC",nativeQuery=true)
    public Page<Problem> waitList(String id,Pageable pageable);

}
