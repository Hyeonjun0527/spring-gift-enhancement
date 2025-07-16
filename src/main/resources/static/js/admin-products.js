const ui = {
    productModal: null,
    productForm: null,
    modalTitle: null,
    productIdField: null,
    nameInput: null,
    priceInput: null,
    imageUrlInput: null,
    tableBody: null,
    paginationControls: null,
    addProductBtn: null,
    closeModalBtn: null,
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
    ui.tableBody = document.querySelector("#product-table tbody");
    ui.paginationControls = document.getElementById('pagination-controls');
    ui.addProductBtn = document.getElementById('add-product-btn');
    ui.closeModalBtn = document.querySelector('.close-button');

    ui.addProductBtn.addEventListener('click', setupAddModal);
    ui.closeModalBtn.addEventListener('click', closeModal);

    window.addEventListener('click', (event) => {
        if (event.target === ui.productModal) {
            closeModal();
        }
    });

    ui.productForm.addEventListener('submit', handleFormSubmit);
    ui.tableBody.addEventListener('click', handleTableClick);

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
    openModal();
}

function setupEditModal(product) {
    ui.modalTitle.textContent = '상품 수정';
    ui.productIdField.value = product.id;
    ui.nameInput.value = product.name;
    ui.priceInput.value = product.price;
    ui.imageUrlInput.value = product.imageUrl;
    openModal();
}


function getProducts(page = 0, size = 5) {
    axios.get(`/api/products?page=${page}&size=${size}`)
        .then(response => {
            const pageData = response.data;
            renderTable(pageData.content);
            renderPagination(pageData);
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

    if (pageData.totalPages === 0) return;

    if (pageData.totalPages === 1) {
        const pageButton = document.createElement('button');
        pageButton.innerText = '1';
        pageButton.classList.add('current');
        ui.paginationControls.appendChild(pageButton);
        return;
    }

    const currentPageNumber = pageData.number;

    if (pageData.hasPrevious) {
        const prevButton = document.createElement('button');
        prevButton.innerText = '이전';
        prevButton.onclick = () => getProducts(currentPageNumber - 1);
        ui.paginationControls.appendChild(prevButton);
    }

    for (let i = 0; i < pageData.totalPages; i++) {
        const pageButton = document.createElement('button');
        pageButton.innerText = i + 1;
        if (i === currentPageNumber) {
            pageButton.classList.add('current');
        }
        pageButton.onclick = () => getProducts(i);
        ui.paginationControls.appendChild(pageButton);
    }

    if (pageData.hasNext) {
        const nextButton = document.createElement('button');
        nextButton.innerText = '다음';
        nextButton.onclick = () => getProducts(currentPageNumber + 1);
        ui.paginationControls.appendChild(nextButton);
    }
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
