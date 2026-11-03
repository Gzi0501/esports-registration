package com.esports.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

/**
 * Servlet 公共父类：提供参数处理、页面转发、重定向和提示消息等通用方法
 */
public class BaseServlet extends HttpServlet {

    /**
     * 把字符串转换成 int，转换失败时返回默认值
     */
    protected int getInt(String value, int defaultValue) {
        if (value == null || value.trim().isEmpty()) {
            return defaultValue;
        }
        try {
            return Integer.parseInt(value.trim());
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    /**
     * 获取请求参数并去掉首尾空格，参数为空时返回空字符串
     */
    protected String getString(HttpServletRequest request, String name) {
        String value = request.getParameter(name);
        return value == null ? "" : value.trim();
    }

    /**
     * 转发到 /WEB-INF/views/ 下的 JSP 页面
     */
    protected void forward(HttpServletRequest request, HttpServletResponse response, String view)
            throws ServletException, IOException {
        request.getRequestDispatcher("/WEB-INF/views/" + view).forward(request, response);
    }

    /**
     * 重定向到当前项目下的某个路径
     */
    protected void redirect(HttpServletRequest request, HttpServletResponse response, String path)
            throws IOException {
        response.sendRedirect(request.getContextPath() + path);
    }

    /**
     * 把提示消息放入 Session，由 header.jsp 显示一次后自动移除
     */
    protected void flash(HttpServletRequest request, String type, String message) {
        request.getSession().setAttribute("flashType", type);
        request.getSession().setAttribute("flashMsg", message);
    }

    protected void flashSuccess(HttpServletRequest request, String message) {
        flash(request, "success", message);
    }

    protected void flashError(HttpServletRequest request, String message) {
        flash(request, "error", message);
    }
}
