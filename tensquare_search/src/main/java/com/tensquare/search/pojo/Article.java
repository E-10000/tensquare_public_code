package com.tensquare.search.pojo;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;

import java.io.Serializable;

//对应elasticsearch的index 和 type
@Document(indexName = "tensquare_article",type = "article")
@Data
public class Article implements Serializable {
    @Id
    private String id;

    /*
    @Field干嘛的
    是否索引，就是看该域是否能被搜索（面试回答）
    是否分词，就表示搜索的时候是整体匹配还是单词匹配
    是否存储，就是是否在页面上显示
     */
    @Field(index = true,analyzer = "ik_max_word",searchAnalyzer = "ik_max_word")
    private String title;

    @Field(index = true,analyzer = "ik_max_word",searchAnalyzer = "ik_max_word")
    private String content;

    private String state;
}
