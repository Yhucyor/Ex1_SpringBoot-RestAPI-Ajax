const productContextPath = ($('meta[name="context-path"]').attr('content') || '/').replace(/\/$/, '');
const productApi = `${productContextPath}/api/product`;
const productCategoryApi = `${productContextPath}/api/category`;
let productModal;

$(function () {
    productModal = new bootstrap.Modal(document.getElementById('productModal'));
    loadProductCategories();
    loadProducts();

    $('#openCreateProduct').on('click', function () {
        resetProductForm();
        productModal.show();
    });

    $('#productForm').on('submit', function (event) {
        event.preventDefault();
        const isUpdate = Boolean($('#productId').val());
        $.ajax({
            url: `${productApi}/${isUpdate ? 'updateProduct' : 'addProduct'}`,
            type: isUpdate ? 'PUT' : 'POST',
            data: new FormData(this),
            contentType: false,
            processData: false
        }).done(function (response) {
            productModal.hide();
            showProductMessage(response.message || 'Lưu Product thành công.', 'success');
            loadProducts();
        }).fail(showProductAjaxError);
    });

    $('#productTableBody').on('click', '.edit-product', function () {
        const id = $(this).data('id');
        $.post(`${productApi}/getProduct`, {id})
            .done(function (response) {
                const product = productResponseBody(response);
                $('#productId').val(product.productId);
                $('#productName').val(product.productName);
                $('#categoryId').val(product.category ? product.category.categoryId : '');
                $('#quantity').val(product.quantity);
                $('#unitPrice').val(product.unitPrice);
                $('#discount').val(product.discount);
                $('#status').val(product.status);
                $('#description').val(product.description);
                $('#imageFile').val('');
                $('#productModalTitle').text('Cập nhật Product');
                $('#saveProductButton').text('Lưu thay đổi');
                $('#imageHelp').text('Để trống nếu muốn giữ ảnh hiện tại.');
                productModal.show();
            })
            .fail(showProductAjaxError);
    });

    $('#productTableBody').on('click', '.delete-product', function () {
        const id = $(this).data('id');
        if (!window.confirm('Bạn có chắc muốn xóa Product này?')) {
            return;
        }
        $.ajax({url: `${productApi}/deleteProduct`, type: 'DELETE', data: {productId: id}})
            .done(function (response) {
                showProductMessage(response.message || 'Xóa Product thành công.', 'success');
                loadProducts();
            })
            .fail(showProductAjaxError);
    });
});

function loadProductCategories() {
    $.getJSON(productCategoryApi)
        .done(function (response) {
            const categories = productResponseBody(response) || [];
            const options = ['<option value="">Chọn Category</option>'];
            categories.forEach(function (category) {
                options.push(`<option value="${category.categoryId}">${productEscapeHtml(category.categoryName)}</option>`);
            });
            $('#categoryId').html(options.join(''));
            $('#openCreateProduct').prop('disabled', categories.length === 0);
            if (!categories.length) {
                showProductMessage('Hãy tạo ít nhất một Category trước khi thêm Product.', 'danger');
            }
        })
        .fail(showProductAjaxError);
}

function loadProducts() {
    $.getJSON(productApi)
        .done(function (response) {
            const products = productResponseBody(response) || [];
            const rows = products.map(function (product) {
                const image = product.images
                    ? `<img class="thumb" src="${productContextPath}/uploads/${encodeURIComponent(product.images)}" alt="Ảnh ${productEscapeHtml(product.productName)}">`
                    : '<span class="text-secondary">Không có ảnh</span>';
                const categoryName = product.category ? product.category.categoryName : 'Chưa phân loại';
                const status = Number(product.status) === 1
                    ? '<span class="badge text-bg-success">Đang bán</span>'
                    : '<span class="badge text-bg-secondary">Ngừng bán</span>';
                return `<tr>
                    <td class="ps-4">${product.productId}</td>
                    <td>${image}</td>
                    <td><div class="fw-medium">${productEscapeHtml(product.productName)}</div><small class="text-secondary description-cell d-block">${productEscapeHtml(product.description)}</small></td>
                    <td>${productEscapeHtml(categoryName)}</td>
                    <td>${product.quantity}</td>
                    <td>${formatMoney(product.unitPrice)}</td>
                    <td>${formatMoney(product.discount)}</td>
                    <td>${status}</td>
                    <td class="text-end pe-4 text-nowrap">
                        <button class="btn btn-sm btn-outline-warning edit-product" data-id="${product.productId}" title="Sửa"><i class="fa-solid fa-pen"></i></button>
                        <button class="btn btn-sm btn-outline-danger delete-product ms-1" data-id="${product.productId}" title="Xóa"><i class="fa-solid fa-trash"></i></button>
                    </td>
                </tr>`;
            });
            $('#productTableBody').html(rows.length
                ? rows.join('')
                : '<tr><td colspan="9" class="text-center py-5 text-secondary">Chưa có Product.</td></tr>');
        })
        .fail(showProductAjaxError);
}

function resetProductForm() {
    $('#productForm')[0].reset();
    $('#productId').val('');
    $('#productModalTitle').text('Thêm Product');
    $('#saveProductButton').text('Thêm mới');
    $('#imageHelp').text('Có thể để trống.');
}

function productResponseBody(response) {
    return response && Object.prototype.hasOwnProperty.call(response, 'body') ? response.body : response;
}

function showProductAjaxError(xhr) {
    const response = xhr.responseJSON;
    const message = response && response.message
        ? response.message
        : (typeof response === 'string' ? response : 'Không thể thực hiện yêu cầu.');
    showProductMessage(message, 'danger');
}

function showProductMessage(message, type) {
    $('#productAlert')
        .removeClass('d-none alert-success alert-danger')
        .addClass(`alert-${type}`)
        .text(message);
}

function productEscapeHtml(value) {
    return $('<div>').text(value == null ? '' : value).html();
}

function formatMoney(value) {
    return Number(value || 0).toLocaleString('vi-VN') + ' đ';
}
