package jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

import java.text.SimpleDateFormat;

public class ParseJwtTest {
    public static void main(String[] args) {
        String Jwt = "eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiI2NjYiLCJzdWIiOiLlsI_lvLoiLCJpYXQiOjE2MDY0NjgyNzAsImV4cCI6MTYwNjQ2ODMzMCwicm9sZXMiOiJ1c2VyIn0.yWPMXOPcxPYvbjwkmOSUdzpGZJXpOsPJaqNjMHwpsFw";
        Claims claims = Jwts.parser()//开始解析
                .setSigningKey("tensquare")//加密盐
                .parseClaimsJws(Jwt)//那串JWT
                .getBody();

        System.out.println("用户ID："+claims.getId());
        System.out.println("用户名："+claims.getSubject());
        System.out.println("登陆时间："+new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(claims.getIssuedAt()));
        System.out.println("过期时间："+new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(claims.getExpiration()));
        System.out.println("用户角色："+claims.get("roles"));
    }
}
