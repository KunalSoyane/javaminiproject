<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    // 1. --- SECURITY CHECK: Ensure user is logged in ---
    String userEmail = (String) session.getAttribute("userEmail");
    if (userEmail == null) {
        response.sendRedirect("login.html");
        return; // Stop the page from loading further
    }
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>User Dashboard - Online Voting System</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <style>
        body {
            background: linear-gradient(135deg, #6a11cb, #2575fc);
            min-height: 100vh;
            padding: 20px;
            color: white;
        }
        .card {
            background: white;
            color: black;
            border-radius: 15px;
            padding: 20px;
        }
        .party-card {
            border: 1px solid #ddd;
            border-radius: 10px;
            padding: 15px;
            margin-bottom: 15px;
            background: #f8f9fa;
        }
    </style>
</head>
<body>
    <div class="container">
        <h2 class="text-center mb-4">Welcome, <%= userEmail %> 👋</h2>

        <div id="votingSection" class="card">
            <h4 class="text-center">Select Your Party 🗳</h4>
            <hr>

            <form action="vote" method="post">
                <div class="party-card d-flex justify-content-between align-items-center">
                    <span>Party A - People's Choice</span>
                    <input type="hidden" name="party" value="Party A - People's Choice">
                    <button type="submit" class="btn btn-primary">Vote</button>
                </div>
            </form>

            <form action="vote" method="post">
                <div class="party-card d-flex justify-content-between align-items-center">
                    <span>Party B - Progressive Front</span>
                    <input type="hidden" name="party" value="Party B - Progressive Front">
                    <button type="submit" class="btn btn-primary">Vote</button>
                </div>
            </form>

            <form action="vote" method="post">
                <div class="party-card d-flex justify-content-between align-items-center">
                    <span>Party C - National Unity</span>
                    <input type="hidden" name="party" value="Party C - National Unity">
                    <button type="submit" class="btn btn-primary">Vote</button>
                </div>
            </form>
        </div>
    </div>
</body>
</html>