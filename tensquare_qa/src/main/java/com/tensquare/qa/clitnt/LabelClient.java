package com.tensquare.qa.clitnt;

import com.tensquare.qa.clitnt.impl.LabelClientImpl;
import entity.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(value = "tensquare-base",fallback = LabelClientImpl.class)
public interface LabelClient {

    //这里要补全请求路径
    @GetMapping("/label/{labelId}")
    public Result findById(@PathVariable("labelId") String labelId);
}
