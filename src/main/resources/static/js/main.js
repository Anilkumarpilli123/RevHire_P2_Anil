document.addEventListener('DOMContentLoaded', () => {
    const sidebar = document.getElementById('sidebar');
    const menuToggle = document.getElementById('menuToggle');
    const sidebarClose = document.getElementById('sidebarClose');
    const sidebarOverlay = document.getElementById('sidebarOverlay');

    if (menuToggle && sidebar) {
        menuToggle.addEventListener('click', () => {
            sidebar.classList.add('active');
            sidebarOverlay.classList.add('active');
            document.body.style.overflow = 'hidden';
        });
    }

    const closeSidebar = () => {
        if (sidebar) sidebar.classList.remove('active');
        if (sidebarOverlay) sidebarOverlay.classList.remove('active');
        document.body.style.overflow = '';
    };

    if (sidebarClose) {
        sidebarClose.addEventListener('click', closeSidebar);
    }

    if (sidebarOverlay) {
        sidebarOverlay.addEventListener('click', closeSidebar);
    }

    // Close sidebar on escape key
    document.addEventListener('keydown', (e) => {
        if (e.key === 'Escape') closeSidebar();
    });

    // Reset body overflow if window is resized beyond mobile break point
    window.addEventListener('resize', () => {
        if (window.innerWidth > 1024) {
            closeSidebar();
        }
    });

    // Auto-hide messages after 5 seconds
    const messages = document.querySelectorAll('.alert, .error-text');
    messages.forEach(msg => {
        // Only auto-hide error-text if it's a flash message (not persistent form validation)
        // For now, we apply it to all as per user request
        setTimeout(() => {
            msg.classList.add('fading');
            setTimeout(() => {
                msg.style.display = 'none';
            }, 500);
        }, 4500);
    });

    // Notification Polling
    function updateNotificationBadge() {
        const badge = document.getElementById('notifBadge');
        if (!badge) return;

        fetch('/api/notifications/unread-count')
            .then(response => response.json())
            .then(data => {
                if (data.status === 'success') {
                    const count = data.data;
                    if (count > 0) {
                        badge.textContent = count;
                        badge.style.display = 'flex';
                    } else {
                        badge.style.display = 'none';
                    }
                }
            })
            .catch(err => console.error('Error fetching unread count:', err));
    }

    // Initial call and set interval
    updateNotificationBadge();
    setInterval(updateNotificationBadge, 30000); // Poll every 30 seconds
});
