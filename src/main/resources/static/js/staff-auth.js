document.addEventListener('DOMContentLoaded', () => {
    const userJson = localStorage.getItem('user');
    if (!userJson) {
        window.location.href = "/login";
        return;
    }
    const user = JSON.parse(userJson);
    const roleName = user.role && user.role.name ? user.role.name : null;
    if (roleName !== 'STAFF') {
        window.location.href = "/client/dashboard";
        return;
    }
    const staffNameEl = document.getElementById('staffName');
    const fullNameEl = document.getElementById('fullName');
    const emailEl = document.getElementById('email');
    if (staffNameEl) staffNameEl.textContent = user.firstName;
    if (fullNameEl) fullNameEl.textContent = `${user.firstName} ${user.lastName}`;
    if (emailEl) emailEl.textContent = user.email;
    const logoutBtn = document.getElementById('logoutBtn');
    if (logoutBtn) {
        logoutBtn.addEventListener('click', () => {
            localStorage.removeItem('user');
            window.location.href = "/login";
        });
    }
    const sidebarMenu = document.getElementById('sidebarMenu');
    const sections = {
        rooms: document.getElementById('section-rooms'),
        reservations: document.getElementById('section-reservations'),
        guests: document.getElementById('section-guests')
    };
    if (sidebarMenu) {
        sidebarMenu.addEventListener('click', (e) => {
            const link = e.target.closest('a[data-section]');
            if (!link) return;
            e.preventDefault();
            sidebarMenu.querySelectorAll('.nav-link').forEach(a => a.classList.remove('active'));
            link.classList.add('active');
            const sectionKey = link.getAttribute('data-section');
            Object.values(sections).forEach(sec => sec && sec.classList.remove('active'));
            const target = sections[sectionKey];
            if (target) target.classList.add('active');
        });
    }
});