// Doctor Dashboard JavaScript
document.addEventListener('DOMContentLoaded', function() {
    // Set current date (if present)
    const currentDateElement = document.getElementById('current-date');
    if (currentDateElement) {
        const currentDate = new Date();
        const dateOptions = { weekday: 'long', year: 'numeric', month: 'long', day: 'numeric' };
        currentDateElement.textContent = currentDate.toLocaleDateString('en-US', dateOptions);
    }

    // Add New Patient button
    const addPatientBtn = document.getElementById('add-patient');
    if (addPatientBtn) {
        addPatientBtn.addEventListener('click', () => {
            alert('Open form to add a new patient (not implemented).');
        });
    }

    // Handle 'View Details' links in schedule table
    const scheduleLinks = document.querySelectorAll('.schedule-table a');
    scheduleLinks.forEach(link => {
        link.addEventListener('click', (e) => {
            e.preventDefault();
            const row = e.target.closest('tr');
            const patient = row.querySelector('td').textContent.trim();
            alert(`Show details for ${patient} (not implemented).`);
        });
    });

    // 'View All Feedback' link
    const viewAll = document.querySelector('.view-all');
    if (viewAll) {
        viewAll.addEventListener('click', (e) => {
            e.preventDefault();
            alert('Navigate to full feedback page (not implemented).');
        });
    }

    // Sidebar navigation
    const navItems = document.querySelectorAll('.nav-menu li');
    navItems.forEach(item => {
        item.addEventListener('click', () => {
            navItems.forEach(i => i.classList.remove('active'));
            item.classList.add('active');
        });
    });
});