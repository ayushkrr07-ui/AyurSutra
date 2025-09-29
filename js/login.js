// Login Page JavaScript
document.addEventListener('DOMContentLoaded', function() {
    const loginForm = document.getElementById('loginForm');
    
    // Set current date
    const currentDate = new Date();
    const dateOptions = { weekday: 'long', year: 'numeric', month: 'long', day: 'numeric' };
    
    // Handle form submission
    if (loginForm) {
        loginForm.addEventListener('submit', function(e) {
            e.preventDefault();
            
            const username = document.getElementById('username').value;
            const password = document.getElementById('password').value;
            const userType = document.getElementById('userType').value;
            
            // Simple validation
            if (!username || !password) {
                alert('Please enter both username and password');
                return;
            }
            
            // In a real application, you would send this data to a server for authentication
            // For demo purposes, we'll just redirect based on user type
            if (userType === 'doctor') {
                window.location.href = 'doctor-dashboard.html';
            } else if (userType === 'clinic') {
                window.location.href = 'clinic-dashboard.html';
            }
        });
    }
});