const ui = {
    productModal: null,
    productForm: null,
    modalTitle: null,
    productIdField: null,
    nameInput: null,
    priceInput: null,
    imageUrlInput: null,
    submitButton: null,
    tableBody: null,
    paginationControls: null,
    addProductBtn: null,
    closeModalBtn: null,
    tableHeader: null,
};

const state = {
    currentPage: 0,
    pageSize: 5,
    sortBy: 'id',
    order: 'asc',
};

const openModal = () => ui.productModal.style.display = 'block';
const closeModal = () => ui.productModal.style.display = 'none';


document.addEventListener('DOMContentLoaded', () => {
    ui.productModal = document.getElementById('product-modal');
    ui.productForm = document.getElementById('product-form');
    ui.modalTitle = document.getElementById('modal-title');
    ui.productIdField = document.getElementById('product-id');
    ui.nameInput = document.getElementById('name');
    ui.priceInput = document.getElementById('price');
    ui.imageUrlInput = document.getElementById('imageUrl');
    ui.submitButton = document.getElementById('submit-button');
    ui.tableBody = document.querySelector("#product-table tbody");
    ui.paginationControls = document.getElementById('pagination-controls');
    ui.addProductBtn = document.getElementById('add-product-btn');
    ui.closeModalBtn = document.querySelector('.close-button');
    ui.tableHeader = document.querySelector("#product-table thead");

    ui.addProductBtn.addEventListener('click', setupAddModal);
    ui.closeModalBtn.addEventListener('click', closeModal);

    window.addEventListener('click', (event) => {
        if (event.target === ui.productModal) {
            closeModal();
        }
    });

    ui.productForm.addEventListener('submit', handleFormSubmit);
    ui.tableBody.addEventListener('click', handleTableClick);
    ui.tableHeader.addEventListener('click', handleSort);

    getProducts();
});


function handleFormSubmit(event) {
    event.preventDefault();

    const productData = {
        name: ui.nameInput.value,
        price: parseInt(ui.priceInput.value, 10),
        imageUrl: ui.imageUrlInput.value,
    };
    const id = ui.productIdField.value;

    if (id) {
        updateProduct(id, productData);
    } else {
        addNewProduct(productData);
    }
}

function handleTableClick(event) {
    const target = event.target;
    if (target.classList.contains('edit-btn')) {
        const productId = target.dataset.id;
        openEditModal(productId);
    }
    if (target.classList.contains('delete-btn')) {
        const productId = target.dataset.id;
        deleteProduct(productId);
    }
}

function setupAddModal() {
    ui.productForm.reset();
    ui.modalTitle.textContent = '새 상품 추가';
    ui.productIdField.value = '';
    ui.submitButton.textContent = '저장';
    openModal();
}

function setupEditModal(product) {
    ui.modalTitle.textContent = '상품 수정';
    ui.productIdField.value = product.id;
    ui.nameInput.value = product.name;
    ui.priceInput.value = product.price;
    ui.imageUrlInput.value = product.imageUrl;
    ui.submitButton.textContent = '수정';
    openModal();
}


function getProducts(page = state.currentPage) {
    state.currentPage = page;
    const { pageSize, sortBy, order } = state; // 'sort' -> 'sortBy'
    const sortParam = `${sortBy},${order}`; // 'sort' -> 'sortBy'

    axios.get(`/api/products?page=${state.currentPage}&size=${pageSize}&sort=${sortParam}`)
        .then(response => {
            const pageData = response.data;
            renderTable(pageData.content);
            renderPagination(pageData);
            updateSortIndicator();
        })
        .catch(error => {
            handleApiError(error, '상품 목록을 불러오는 데 실패했습니다.');
            renderTable([]);
            renderPagination({ totalPages: 0 });
        });
}

function renderTable(products) {
    ui.tableBody.innerHTML = '';

    products.forEach(product => {
        const row = document.createElement('tr');
        row.innerHTML = `
            <td>${product.id}</td>
            <td>${product.name}</td>
            <td>${product.price}</td>
            <td><img src="${product.imageUrl}" alt="${product.name}" width="80"></td>
            <td>
                <button class="edit-btn" data-id="${product.id}">수정</button>
                <button class="delete-btn" data-id="${product.id}">삭제</button>
            </td>
        `;
        ui.tableBody.appendChild(row);
    });
}

function renderPagination(pageData) {
    ui.paginationControls.innerHTML = '';
    const totalPages = pageData.totalPages;
    const currentPage = pageData.number;

    if (totalPages === 0) return;

    const createButton = (text, page, enabled = true, isCurrent = false) => {
        const button = document.createElement('button');
        button.innerText = text;
        button.disabled = !enabled;
        if (isCurrent) {
            button.classList.add('current');
        }
        button.onclick = () => getProducts(page);
        return button;
    };

    ui.paginationControls.appendChild(createButton('<<', 0, currentPage > 0));
    ui.paginationControls.appendChild(createButton('<', currentPage - 1, pageData.hasPrevious));

    // 페이지 번호 로직 개선
    const maxPageButtons = 5;
    let startPage = Math.max(0, currentPage - Math.floor(maxPageButtons / 2));
    let endPage = Math.min(totalPages - 1, startPage + maxPageButtons - 1);
    startPage = Math.max(0, endPage - maxPageButtons + 1);

    if (startPage > 0) {
        const dots = document.createElement('span');
        dots.innerText = '...';
        ui.paginationControls.appendChild(dots);
    }

    for (let i = startPage; i <= endPage; i++) {
        ui.paginationControls.appendChild(createButton(i + 1, i, true, i === currentPage));
    }

    if (endPage < totalPages - 1) {
        const dots = document.createElement('span');
        dots.innerText = '...';
        ui.paginationControls.appendChild(dots);
    }

    ui.paginationControls.appendChild(createButton('>', currentPage + 1, pageData.hasNext));
    ui.paginationControls.appendChild(createButton('>>', totalPages - 1, currentPage < totalPages - 1));
}

function handleApiError(error, defaultMessage) {
    console.error(defaultMessage, error);
    console.log('서버로부터 받은 실제 에러 응답:', error.response);

    const data = error.response?.data;
    let errorMessage = defaultMessage;

    if (data) {
        if (data.errors && data.errors.length > 0) {
            errorMessage = data.errors.map(err => err.detail).join('\\n');
        } else if (data.detail) {
            errorMessage = data.detail;
        }
    }

    alert(errorMessage);
}

function addNewProduct(productData) {
    axios.post('/api/products', productData)
        .then(response => {
            if (response.status === 201) {
                alert('상품이 성공적으로 추가되었습니다.');
                closeModal();
                getProducts();
            }
        })
        .catch(error => {
            handleApiError(error, '상품 추가에 실패했습니다.');
        });
}

function openEditModal(id) {
    axios.get(`/api/products/${id}`)
        .then(response => {
            setupEditModal(response.data);
        })
        .catch(error => {
            handleApiError(error, '상품 정보를 불러오는 데 실패했습니다.');
        });
}

function updateProduct(id, productData) {
    axios.put(`/api/products/${id}`, productData)
        .then(response => {
            if (response.status === 204) {
                alert('상품이 성공적으로 수정되었습니다.');
                closeModal();
                getProducts();
            }
        })
        .catch(error => {
            handleApiError(error, '상품 수정에 실패했습니다.');
        });
}

function deleteProduct(id) {
    if (confirm(`정말로 이 상품(ID: ${id})을 삭제하시겠습니까?`)) {
        axios.delete(`/api/products/${id}`)
            .then(response => {
                if (response.status === 204) {
                    alert('상품이 성공적으로 삭제되었습니다.');
                    getProducts();
                }
            })
            .catch(error => {
                handleApiError(error, '상품 삭제에 실패했습니다.');
            });
    }
}

function handleSort(event) {
    const target = event.target;
    if (target.tagName !== 'TH' || !target.dataset.sort) {
        return;
    }

    const newSortBy = target.dataset.sort;
    if (state.sortBy === newSortBy) {
        state.order = state.order === 'asc' ? 'desc' : 'asc';
    } else {
        state.sortBy = newSortBy;
        state.order = 'asc';
    }
    getProducts();
}

function updateSortIndicator() {
    ui.tableHeader.querySelectorAll('th').forEach(th => {
        th.classList.remove('sorted-asc', 'sorted-desc');
        if (th.dataset.sort === state.sortBy) {
            th.classList.add(state.order === 'asc' ? 'sorted-asc' : 'sorted-desc');
        }
    });
}
