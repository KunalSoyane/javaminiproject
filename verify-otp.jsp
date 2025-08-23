<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Verify Your Email</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <style>
        body {
            background: linear-gradient(135deg, #6a11cb, #2575fc);
            display: flex;
            align-items: center;
            justify-content: center;
            height: 100vh;
        }
        .verify-card {
            background: white;
            padding: 40px;
            border-radius: 15px;
            text-align: center;
            max-width: 450px;
        }
    </style>
</head>
<body>
    <div class="verify-card">
        <h3>Check Your Email</h3>
        <p class="text-muted">We've sent a 6-digit OTP to your email address. Please enter it below to complete your registration.</p>

        <%
            String error = request.getParameter("error");
            if (error != null && "1".equals(error)) {
        %>
            <div class="alert alert-danger" role="alert">
                Invalid OTP. Please try again.
            </div>
        <% } %>

        <form action="verify-otp" method="post">
            <div class="mb-3">
                <input type="text" name="otp" class="form-control form-control-lg" placeholder="Enter OTP" required maxlength="6">
            </div>
            <button type="submit" class="btn btn-primary w-100">Verify & Register</button>
        </form>
    </div>
</body>
</html>