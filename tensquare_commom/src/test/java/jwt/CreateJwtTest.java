package jwt;

import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import javax.swing.*;
import javax.xml.crypto.Data;
import java.util.Date;

public class CreateJwtTest {
    public static void main(String[] args) {

        //获取当前时间戳
        long now = System.currentTimeMillis();
        //过期时间,加一分钟
        long exp = now + 1*60*1000;

        JwtBuilder jwtBuilder = Jwts.builder()//开始构建
                .setId("666")//ID
                .setSubject("小强")//用户名
                .setIssuedAt(new Date(now))//创建时间
                .setExpiration(new Date(exp))//过期时间
                .claim("roles","user")//添加自定义键值
                //signWith(加密算法,加密盐)
                .signWith(SignatureAlgorithm.HS256, "tensquare");

        //jwtBuilder.compact() 转化为String类型
        System.out.println(jwtBuilder.compact());
    }
}
