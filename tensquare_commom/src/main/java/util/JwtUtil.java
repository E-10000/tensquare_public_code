package util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * Created by Administrator on 2018/4/11.
 */
//@ConfigurationProperties("jwt.config")
@ConfigurationProperties("jwt.config")
@Data
public class JwtUtil {

    private String key ;

    private long ttl ;//一个小时

    /**
     * 生成JWT
     *
     * @param id 用户ID
     * @param subject 用户名
     * @param roles 角色名
     * @return JWT字符串
     */
    public String createJWT(String id, String subject, String roles) {
        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);
        JwtBuilder builder = Jwts.builder()
                .setId(id)
                .setSubject(subject)
                .setIssuedAt(now)
                .signWith(SignatureAlgorithm.HS256, key)
                .claim("roles", roles);
        if (ttl > 0) {
            builder.setExpiration( new Date( nowMillis + ttl));
        }
        return builder.compact();
    }

    /**
     * 解析JWT
     * @param jwtStr
     * @return
     */
    public Claims parseJWT(String jwtStr){
        return  Jwts.parser()
                //key就是盐值
                .setSigningKey(key)
                //JWT字符串
                .parseClaimsJws(jwtStr)
                .getBody();
    }

}
