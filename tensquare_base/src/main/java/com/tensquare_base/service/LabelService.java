package com.tensquare_base.service;

import com.tensquare_base.dao.LabelDao;
import com.tensquare_base.pojo.Label;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import util.IdWorker;

import javax.persistence.criteria.*;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional//保证数据库事务完整性
public class LabelService {
    @Autowired
    private LabelDao labelDao;

    @Autowired
    private IdWorker idWorker;

    public List<Label> findAll(){
        return labelDao.findAll();
    }

    public Label findById(String id){
        return labelDao.findById(id).get();
    }

    public void save(Label label){
        label.setId(idWorker.nextId()+"");
        labelDao.save(label);
    }

    public void update(Label label){
        labelDao.save(label);
    }

    public void deleteById(String id){
        labelDao.deleteById(id);
    }

    public List<Label> findSearch(Label label) {
        /*
        返回一个List集合，这个是模糊查找嘛，就labelname和state是有可能查找的，就这两个查找
         */
        return labelDao.findAll(new Specification<Label>() {
            @Override
            public Predicate toPredicate(Root<Label> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                //new一个list集合，用来存放所有条件，因为直接and不行，有if条件在
                List<Predicate> list = new ArrayList<>();

                //当labelname不为空的时候
                if (label.getLabelname()!=null&&!"".equals(label.getLabelname())){
                    Path<Object> labelname = root.get("labelname");
                    Predicate predicate1 = criteriaBuilder.like(labelname.as(String.class), "%" + label.getLabelname() + "%");//where labelname like xxx
                    list.add(predicate1);
                }

                //当state不为空的时候
                if (label.getState()!=null&&!"".equals(label.getState())){
                    Path<Object> state = root.get("state");
//                    Predicate predicate2 = criteriaBuilder.like(state.as(String.class), "%" + label.getState() + "%");//where state like xxx
                    Predicate predicate2 = criteriaBuilder.equal(state, label.getState());
                    list.add(predicate2);
                }

                //因为不知道Predicate长度，但是list不用知道长度，把list转换成数组就行
                Predicate[] parr = new Predicate[list.size()];
                parr = list.toArray(parr);

                //and()参数 Predicate[]
                return criteriaBuilder.and(parr);//where state like xxx and labelname like xxx

            }
        });
    }

    public Page<Label> findSearchPage(Label label, int page, int size) {
        Specification<Label> specification = new Specification<Label>() {
            @Override
            public Predicate toPredicate(Root<Label> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                //new一个list集合，用来存放所有条件，因为直接and不行，有if条件在
                List<Predicate> list = new ArrayList<>();

                //当labelname不为空的时候
                if (label.getLabelname()!=null&&!"".equals(label.getLabelname())){
                    Path<Object> labelname = root.get("labelname");
                    Predicate predicate1 = criteriaBuilder.like(labelname.as(String.class), "%" + label.getLabelname() + "%");//where labelname like xxx
                    list.add(predicate1);
                }

                //当state不为空的时候
                if (label.getState()!=null&&!"".equals(label.getState())){
                    Path<Object> state = root.get("state");
//                    Predicate predicate2 = criteriaBuilder.like(state.as(String.class), "%" + label.getState() + "%");//where state like xxx
                    Predicate predicate2 = criteriaBuilder.equal(state, label.getState());
                    list.add(predicate2);
                }

                //因为不知道Predicate长度，但是list不用知道长度，把list转换成数组就行
                Predicate[] parr = new Predicate[list.size()];
                parr = list.toArray(parr);

                //and()参数 Predicate[]
                return criteriaBuilder.and(parr);//where state like xxx and labelname like xxx
            }
        };
        Pageable pageable = PageRequest.of(page-1,size);//因为查询是从0开始的
        return labelDao.findAll(specification,pageable);
    }
}
