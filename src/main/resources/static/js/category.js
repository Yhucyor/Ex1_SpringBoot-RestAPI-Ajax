const categoryContextPath = ($('meta[name="context-path"]').attr('content') || '/').replace(/\/$/, '');
const categoryApi = `${categoryContextPath}/api/category`;
let createCategoryModal;
let updateCategoryModal;

$(function () {
    createCategoryModal = new bootstrap.Modal(document.getElementById('createCategoryModal'));
    updateCategoryModal = new bootstrap.Modal(document.getElementById('updateCategoryModal'));
    loadCategories();

    $('#openCreateCategory').on('click', function () {
        $('#addCategoryForm')[0].reset();
        createCategoryModal.show();
    });

    $('#addCategoryForm').on('submit', function (event) {
        event.preventDefault();
        submitCategoryForm(`${categoryApi}/addCategory`, 'POST', new FormData(this), createCategoryModal);
    });

    $('#updateCategoryForm').on('submit', function (event) {
        event.preventDefault();
        submitCategoryForm(`${categoryApi}/updateCategory`, 'PUT', new FormData(this), updateCategoryModal);
    });

    $('#categoryTableBody').on('click', '.edit-category', function () {
        const id = $(this).data('id');
        $.post(`${categoryApi}/getCategory`, {id})
            .done(function (response) {
                const category = responseBody(response);
                $('#updateCategoryId').val(category.categoryId);
                $('#updateCategoryName').val(category.categoryName);
                $('#updateCategoryIcon').val('');
                updateCategoryModal.show();
            })
            .fail(showAjaxError);
    });

    $('#categoryTableBody').on('click', '.delete-category', function () {
        const id = $(this).data('id');
        if (!window.confirm('Bạn có chắc muốn xóa Category này? Các Product liên quan cũng có thể bị xóa.')) {
            return;
        }
        $.ajax({url: `${categoryApi}/deleteCategory`, type: 'DELETE', data: {categoryId: id}})
            .done(function (response) {
                showCategoryMessage(response.message || 'Xóa Category thành công.', 'success');
                loadCategories();
            })
            .fail(showAjaxError);
    });
});

function loadCategories() {
    $.getJSON(categoryApi)
        .done(function (response) {
            const categories = responseBody(response) || [];
            const rows = categories.map(function (category) {
                const icon = category.icon
                    ? `<img class="thumb" src="${categoryContextPath}/uploads/${encodeURIComponent(category.icon)}" alt="Icon ${escapeHtml(category.categoryName)}">`
                    : '<span class="text-secondary">Không có ảnh</span>';
                return `<tr>
                    <td class="ps-4">${category.categoryId}</td>
                    <td>${icon}</td>
                    <td class="fw-medium">${escapeHtml(category.categoryName)}</td>
                    <td class="text-end pe-4">
                        <button class="btn btn-sm btn-outline-warning edit-category" data-id="${category.categoryId}" title="Sửa"><i class="fa-solid fa-pen"></i></button>
                        <button class="btn btn-sm btn-outline-danger delete-category ms-1" data-id="${category.categoryId}" title="Xóa"><i class="fa-solid fa-trash"></i></button>
                    </td>
                </tr>`;
            });
            $('#categoryTableBody').html(rows.length
                ? rows.join('')
                : '<tr><td colspan="4" class="text-center py-5 text-secondary">Chưa có Category.</td></tr>');
        })
        .fail(showAjaxError);
}

function submitCategoryForm(url, method, formData, modal) {
    $.ajax({
        url,
        type: method,
        data: formData,
        contentType: false,
        processData: false
    }).done(function (response) {
        modal.hide();
        showCategoryMessage(response.message || 'Lưu Category thành công.', 'success');
        loadCategories();
    }).fail(showAjaxError);
}

function responseBody(response) {
    return response && Object.prototype.hasOwnProperty.call(response, 'body') ? response.body : response;
}

function showAjaxError(xhr) {
    const response = xhr.responseJSON;
    const message = response && response.message
        ? response.message
        : (typeof response === 'string' ? response : 'Không thể thực hiện yêu cầu.');
    showCategoryMessage(message, 'danger');
}

function showCategoryMessage(message, type) {
    $('#categoryAlert')
        .removeClass('d-none alert-success alert-danger')
        .addClass(`alert-${type}`)
        .text(message);
}

function escapeHtml(value) {
    return $('<div>').text(value == null ? '' : value).html();
}
