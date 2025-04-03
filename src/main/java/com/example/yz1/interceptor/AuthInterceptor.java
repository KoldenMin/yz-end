package com.example.yz1.interceptor;

import com.example.yz1.common.Result;
import com.example.yz1.utils.JwtUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 认证拦截器
 */
@Component
public class AuthInterceptor implements HandlerInterceptor {
    
    @Autowired
    private JwtUtil jwtUtil;
    
    @Autowired
    private ObjectMapper objectMapper;
    
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 获取请求头中的token
        String token = request.getHeader("Authorization");
        
        // 如果token不存在，返回未认证错误
        if (!StringUtils.hasText(token)) {
            return unauthorized(response);
        }
        
        // 如果token格式不正确，返回未认证错误
        if (!token.startsWith("Bearer ")) {
            return unauthorized(response);
        }
        
        // 提取token
        token = token.substring(7);
        
        // 验证token是否有效
        if (!jwtUtil.validateToken(token)) {
            return unauthorized(response);
        }
        
        // 将用户ID和是否管理员放入请求属性中
        request.setAttribute("userId", jwtUtil.getUserIdFromToken(token));
        request.setAttribute("isAdmin", jwtUtil.isAdminFromToken(token));
        
        return true;
    }
    
    /**
     * 返回未认证错误
     *
     * @param response 响应
     * @return false
     * @throws IOException IO异常
     */
    private boolean unauthorized(HttpServletResponse response) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write(objectMapper.writeValueAsString(Result.error(401, "未认证或认证已过期")));
        return false;
    }
}