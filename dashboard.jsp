<%-- dashboard.jsp --%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    // --- Security Check ---
    // This scriptlet ensures that only logged-in users can see this page.
    // It checks if the "userEmail" attribute exists in the session.
    String userEmail = (String) session.getAttribute("userEmail");
    if (userEmail == null) {
        // If the user is not logged in, redirect them to the login page.
        response.sendRedirect("login.html");
        // return; prevents the rest of the page from loading
        return;
    }
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Dashboard - Online Voting System</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@400;600;700&display=swap" rel="stylesheet">
    <style>
        body {
            font-family: 'Poppins', sans-serif;
            background: linear-gradient(135deg, #6a11cb, #2575fc);
            min-height: 100vh;
            display: flex;
            align-items: center;
            justify-content: center;
            color: white;
            text-align: center;
            padding: 20px;
        }
        .dashboard-card {
            background: rgba(255, 255, 255, 0.1);
            backdrop-filter: blur(10px);
            padding: 40px;
            border-radius: 20px;
            box-shadow: 0 8px 32px 0 rgba(0, 0, 0, 0.37);
            border: 1px solid rgba(255, 255, 255, 0.2);
            max-width: 600px;
        }
        .dashboard-card h1 {
            font-weight: 700;
            margin-bottom: 15px;
        }
        .btn-vote {
            font-size: 1.2rem;
            font-weight: 600;
            padding: 12px 30px;
            border-radius: 30px;
            transition: transform 0.2s;
        }
        .btn-vote:hover {
            transform: translateY(-3px);
        }
        .logout-link {
            display: inline-block;
            margin-top: 25px;
            color: #d1d1d1;
            text-decoration: none;
        }
        .logout-link:hover {
            color: #ffffff;
            text-decoration: underline;
        }
    </style>
</head>
<body>

    <div class="dashboard-card">
        <h1>Welcome, <%= userEmail %>!</h1>
        <p class="lead">You have successfully logged in to the Secure Voting System.</p>
        <hr style="color: rgba(255,255,255,0.5);">
        <p>Ready to cast your vote? Click the button below to proceed to the voting panel.</p>

        <a href="user.jsp" class="btn btn-light btn-vote mt-3">🗳️ Proceed to Vote</a>

        <a href="index.html" class="logout-link">Logout</a>
    </div>

</body>
</html>