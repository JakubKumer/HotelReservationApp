document.addEventListener('DOMContentLoaded', () => {
    const roomToastEl = document.getElementById('roomToast');
    const roomToastBody = document.getElementById('roomToastBody');
    const roomToast = roomToastEl ? new bootstrap.Toast(roomToastEl) : null;
    const roomsTableBody = document.getElementById('roomsTableBody');
    const roomsPagination = document.getElementById('roomsPagination');
    const roomsListCard = document.getElementById('roomsListCard');
    let allRooms = [];
    let currentPage = 1;
    const pageSize = 10;
    async function loadRooms() {
        if (!roomsTableBody) return;
        try {
            const response = await fetch('/api/rooms');
            if (!response.ok) {
                roomsTableBody.innerHTML =
                    '<tr><td colspan="7" class="text-danger small">Nie udało się pobrać listy pokoi.</td></tr>';
                if (roomsPagination) roomsPagination.innerHTML = '';
                return;
            }
            const rooms = await response.json();
            if (!rooms || rooms.length === 0) {
                allRooms = [];
                renderRoomsTable();
                return;
            }
            allRooms = rooms;
            currentPage = 1;
            renderRoomsTable();
        } catch (err) {
            roomsTableBody.innerHTML =
                '<tr><td colspan="7" class="text-danger small">Błąd połączenia przy pobieraniu pokoi.</td></tr>';
            if (roomsPagination) roomsPagination.innerHTML = '';
        }
    }
    function renderRoomsTable() {
        if (!roomsTableBody) return;
        roomsTableBody.innerHTML = '';
        if (!allRooms || allRooms.length === 0) {
            roomsTableBody.innerHTML =
                '<tr><td colspan="7" class="text-muted small">Brak pokoi w bazie.</td></tr>';
            if (roomsPagination) roomsPagination.innerHTML = '';
            return;
        }
        const totalPages = Math.ceil(allRooms.length / pageSize);
        if (currentPage > totalPages) currentPage = totalPages;
        if (currentPage < 1) currentPage = 1;
        const startIndex = (currentPage - 1) * pageSize;
        const endIndex = startIndex + pageSize;
        const roomsToShow = allRooms.slice(startIndex, endIndex);
        roomsToShow.forEach(room => {
            const tr = document.createElement('tr');
            const statusText = room.active ? 'Aktywny' : 'Nieaktywny';
            tr.innerHTML = `
                <td>${room.roomNumber}</td>
                <td>${room.floor}</td>
                <td>${room.capacity}</td>
                <td>${room.pricePerNight} zł</td>
                <td>${statusText}</td>
                <td>${room.description ? room.description : ''}</td>
                <td>
                    <img src="/api/rooms/${room.id}/image"
                         alt="Zdjęcie pokoju"
                         class="img-thumbnail mb-1"
                         style="max-height: 60px"
                         onerror="this.style.display='none'">
                    <br>
                    <button type="button"
                            class="btn btn-outline-secondary btn-sm mt-1 upload-image-btn"
                            data-room-id="${room.id}">
                        ${room.imageData ? 'Zmień zdjęcie' : 'Dodaj zdjęcie'}
                    </button>
                </td>
            `;
            roomsTableBody.appendChild(tr);
        });
        renderRoomsPagination(totalPages);
    }
    function renderRoomsPagination(totalPages) {
        if (!roomsPagination) return;
        roomsPagination.innerHTML = '';
        if (totalPages <= 1) return;
        const createPageItem = (label, page, disabled = false, active = false) => {
            const li = document.createElement('li');
            li.className = 'page-item';
            if (disabled) li.classList.add('disabled');
            if (active) li.classList.add('active');
            const a = document.createElement('a');
            a.className = 'page-link';
            a.href = '#';
            a.textContent = label;
            if (!disabled && !active) a.dataset.page = page;
            li.appendChild(a);
            return li;
        };
        roomsPagination.appendChild(
            createPageItem('«', currentPage - 1, currentPage === 1)
        );
        for (let p = 1; p <= totalPages; p++) {
            roomsPagination.appendChild(
                createPageItem(p, p, false, p === currentPage)
            );
        }
        roomsPagination.appendChild(
            createPageItem('»', currentPage + 1, currentPage === totalPages)
        );
    }
    if (roomsPagination) {
        roomsPagination.addEventListener('click', (e) => {
            const link = e.target.closest('a.page-link');
            if (!link || !link.dataset.page) return;
            e.preventDefault();
            currentPage = Number(link.dataset.page);
            renderRoomsTable();
        });
    }
    const showAddRoomBtn = document.getElementById('showAddRoomBtn');
    const addRoomCard = document.getElementById('addRoomCard');
    const addRoomForm = document.getElementById('addRoomForm');
    const addRoomMessage = document.getElementById('addRoomMessage');
    const roomNumberInput = document.getElementById('roomNumber');
    const floorInput = document.getElementById('floor');
    const capacityInput = document.getElementById('capacity');
    const priceInput = document.getElementById('pricePerNight');
    const descriptionInput = document.getElementById('description');
    const cancelAddRoomBtn = document.getElementById('cancelAddRoomBtn');
    let isAddFormVisible = false;
    if (showAddRoomBtn && addRoomCard && roomsListCard) {
        showAddRoomBtn.addEventListener('click', (e) => {
            e.preventDefault();
            if (addRoomMessage) {
                addRoomMessage.textContent = '';
                addRoomMessage.className = 'mt-2 small';
            }
            isAddFormVisible = !isAddFormVisible;
            if (isAddFormVisible) {
                addRoomCard.classList.remove('d-none');
                showAddRoomBtn.textContent = 'Lista pokoi';
                addRoomCard.scrollIntoView({ behavior: 'smooth' });
            } else {
                addRoomCard.classList.add('d-none');
                showAddRoomBtn.textContent = '+ Dodaj pokój';
                roomsListCard.scrollIntoView({ behavior: 'smooth' });
            }
        });
    }
    if (cancelAddRoomBtn && addRoomForm && addRoomCard) {
        cancelAddRoomBtn.addEventListener('click', () => {
            addRoomCard.classList.add('d-none');
            addRoomForm.reset();
            if (addRoomMessage) {
                addRoomMessage.textContent = '';
                addRoomMessage.className = 'mt-2 small';
            }
            isAddFormVisible = false;
            if (showAddRoomBtn) showAddRoomBtn.textContent = '+ Dodaj pokój';
        });
    }
    if (addRoomForm) {
        addRoomForm.addEventListener('submit', async (e) => {
            e.preventDefault();
            const body = {
                roomNumber: roomNumberInput.value,
                floor: Number(floorInput.value),
                capacity: Number(capacityInput.value),
                pricePerNight: Number(priceInput.value),
                description: descriptionInput.value
            };
            try {
                const response = await fetch('/api/rooms', {
                    method: 'POST',
                    headers: { 'Content-Type': 'application/json' },
                    body: JSON.stringify(body)
                });
                if (!response.ok) {
                    let text = 'Nie udało się dodać pokoju.';
                    try {
                        const err = await response.json();
                        if (err.fieldErrors?.length > 0) text = err.fieldErrors[0].message;
                        else if (err.message) text = err.message;
                    } catch (_) {}
                    if (addRoomMessage) {
                        addRoomMessage.textContent = text;
                        addRoomMessage.classList.add('text-danger');
                    }
                    return;
                }
                const room = await response.json();
                if (addRoomMessage) {
                    addRoomMessage.textContent = `Pokój został dodany (ID: ${room.id}).`;
                    addRoomMessage.classList.add('text-success');
                }
                if (roomToast) {
                    roomToastBody.textContent = `Pokój ${room.roomNumber} został dodany.`;
                    roomToast.show();
                }
                addRoomForm.reset();
                if (addRoomCard) addRoomCard.classList.add('d-none');
                isAddFormVisible = false;
                if (showAddRoomBtn) showAddRoomBtn.textContent = '+ Dodaj pokój';
                await loadRooms();
            } catch (err) {
                if (addRoomMessage) {
                    addRoomMessage.textContent = 'Błąd połączenia z serwerem.';
                    addRoomMessage.classList.add('text-danger');
                }
            }
        });
    }
    const roomImageFileInput = document.getElementById('roomImageFile');
    let currentRoomForImageId = null;
    if (roomsTableBody && roomImageFileInput) {
        roomsTableBody.addEventListener('click', (e) => {
            const btn = e.target.closest('.upload-image-btn');
            if (!btn) return;
            currentRoomForImageId = btn.dataset.roomId;
            roomImageFileInput.value = '';
            roomImageFileInput.click();
        });
        roomImageFileInput.addEventListener('change', async () => {
            if (!currentRoomForImageId) return;
            const file = roomImageFileInput.files[0];
            if (!file) return;
            const formData = new FormData();
            formData.append('file', file);
            try {
                const response = await fetch(`/api/rooms/${currentRoomForImageId}/image`, {
                    method: 'POST',
                    body: formData
                });
                if (!response.ok) {
                    if (roomToast) {
                        roomToastBody.textContent = 'Nie udało się zapisać zdjęcia pokoju.';
                        roomToastEl.classList.remove('text-bg-success');
                        roomToastEl.classList.add('text-bg-danger');
                        roomToast.show();
                    }
                    return;
                }
                if (roomToast) {
                    roomToastBody.textContent = 'Zdjęcie zapisane.';
                    roomToastEl.classList.remove('text-bg-danger');
                    roomToastEl.classList.add('text-bg-success');
                    roomToast.show();
                }
                await loadRooms();
            } catch (err) {
                if (roomToast) {
                    roomToastBody.textContent = 'Błąd połączenia.';
                    roomToastEl.classList.remove('text-bg-success');
                    roomToastEl.classList.add('text-bg-danger');
                    roomToast.show();
                }
            }
            currentRoomForImageId = null;
        });
    }
    loadRooms();
});