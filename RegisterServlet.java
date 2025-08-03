package org.example;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/register")
public class RegisterServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        String email = request.getParameter("mobile"); // Assuming 'mobile' field is for email

        User user = new User();
        user.setName(request.getParameter("name"));
        user.setMobile(email);
        user.setPassword(request.getParameter("pass"));
        user.setAddress(request.getParameter("add"));
        user.setRole(request.getParameter("role"));

        String confirmPassword = request.getParameter("cpass");

        if (!user.getPassword().equals(confirmPassword)) {
            out.println("<h2>Error: Passwords do not match!</h2><a href='index.html'>Try Again</a>");
            return;
        }

        String otp = EmailUtil.generateOtp();
        boolean emailSent = EmailUtil.sendOtpEmail(email, otp);

        if (emailSent) {
            HttpSession session = request.getSession();
            session.setAttribute("userDetails", user);
            session.setAttribute("generatedOtp", otp);
            response.sendRedirect("verify.html");
        } else {
            out.println("<h2>Error: Could not send OTP. Please check server logs.</h2>");
        }
    }
}