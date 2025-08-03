package org.example;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession; // <-- Important: Add this import

import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/register")
public class RegisterServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        // 1. Get parameters and create a User object to hold them
        User user = new User();
        // Assuming user's email is entered in the 'mobile' field of the form
        // If you have a separate email field, use request.getParameter("email_field_name")
        String email = request.getParameter("mobile");

        user.setName(request.getParameter("name"));
        user.setMobile(email); // Storing email in the mobile field for now
        user.setPassword(request.getParameter("pass"));
        user.setAddress(request.getParameter("add"));
        user.setRole(request.getParameter("role"));

        String confirmPassword = request.getParameter("cpass");

        // 2. Basic Validation: Check if passwords match
        if (!user.getPassword().equals(confirmPassword)) {
            out.println("<h2>Error: Passwords do not match!</h2><a href='index.html'>Try Again</a>");
            return; // Stop further execution
        }

        // 3. Generate an OTP and send it via email
        String otp = EmailUtil.generateOtp();
        boolean emailSent = EmailUtil.sendOtpEmail(email, otp);

        // 4. If email was sent successfully, store data in session and redirect
        if (emailSent) {
            // Get the current session
            HttpSession session = request.getSession();

            // Store the user object and the generated OTP in the session
            session.setAttribute("userDetails", user);
            session.setAttribute("generatedOtp", otp);

            System.out.println("User details and OTP stored in session. Redirecting to verification page.");

            // Redirect the user to the OTP verification page
            response.sendRedirect("verify.html");
        } else {
            // Handle email sending failure
            out.println("<h2>Error: Could not send OTP. Please check server logs and try again.</h2>");
        }
    }
}