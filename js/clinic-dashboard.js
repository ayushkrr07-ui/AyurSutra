// Clinic Dashboard JavaScript
document.addEventListener('DOMContentLoaded', function() {
    // Set current date
    const currentDateElement = document.getElementById('current-date');
    if (currentDateElement) {
        const currentDate = new Date();
        const dateOptions = { weekday: 'long', year: 'numeric', month: 'long', day: 'numeric' };
        currentDateElement.textContent = currentDate.toLocaleDateString('en-US', dateOptions);
    }
    
    // Update availability button functionality
    const updateAvailabilityBtn = document.querySelector('.update-availability-btn');
    if (updateAvailabilityBtn) {
        updateAvailabilityBtn.addEventListener('click', function() {
            alert('Update availability functionality will be implemented here.');
        });
    }
    
    // Next token button functionality
    const nextTokenBtn = document.querySelector('.next-token-btn');
    if (nextTokenBtn) {
        nextTokenBtn.addEventListener('click', function() {
            alert('Next token functionality will be implemented here.');
        });
    }
    
    // Alert message button functionality
    const alertMessageBtn = document.querySelector('.alert-message-btn');
    if (alertMessageBtn) {
        alertMessageBtn.addEventListener('click', function() {
            alert('Send alert message functionality will be implemented here.');
        });
    }
    
    // Sidebar menu item click handling
    const sidebarMenuItems = document.querySelectorAll('.sidebar-menu li a');
    sidebarMenuItems.forEach(item => {
        item.addEventListener('click', function(e) {
            // If not the logout link
            if (!this.getAttribute('href').includes('index.html')) {
                e.preventDefault();
                
                // Remove active class from all menu items
                sidebarMenuItems.forEach(menuItem => {
                    menuItem.parentElement.classList.remove('active');
                });
                
                // Add active class to clicked menu item
                this.parentElement.classList.add('active');
                
                // In a real application, you would load the appropriate content here
                alert(`Navigating to ${this.textContent.trim()} section`);
            }
        });
    });
});