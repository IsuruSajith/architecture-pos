import {showProgress, showToast} from "./main.js";

const tbodyElm = $("#tbl-customers tbody");
const modalElm = $("#new-customer-modal");
const txtId = $("#txt-id");
const txtName = $("#txt-name");
const txtContact = $("#txt-contact");
const txtAddress = $("#txt-address");
const btnSave = $("#btn-save");

tbodyElm.empty();

function formatCustomerId(id) {
    return `C${id.toString().padStart(3, '0')}`;
}

[txtName, txtContact, txtAddress].forEach(txtElm =>
    $(txtElm).addClass('animate__animated'));

btnSave.on('click', () => {
    if (!validateData()) {
        return false;
    }

    const id = txtId.val().trim();
    const name = txtName.val().trim();
    const contact = txtContact.val().trim();
    const address = txtAddress.val().trim();

    let customer = {
        name, contact, address
    };

    /* Todo: Send a request to the server to save the customer */

    /* 1. Create xhr object */
    const xhr = new XMLHttpRequest();

    /* 2. Set an event listener to listen readystatechange */
    xhr.addEventListener('readystatechange', ()=> {
        if (xhr.readyState === 4){
            [txtName, txtAddress, txtContact, btnSave].forEach(elm => elm.removeAttr('disabled'));
            $("#loader").css('visibility', 'hidden');
            if (xhr.status === 201){
                customer = JSON.parse(xhr.responseText);
                getCustomers();
                resetForm(true);
                txtName.trigger('focus');
                showToast('success', 'Saved', 'Customer has been saved successfully');
            }else{
                const errorObj = JSON.parse(xhr.responseText);
                showToast('error', 'Failed to save', errorObj.message);
            }
        }
    });

    /* 3. Let's open the request */
    xhr.open('POST', 'http://localhost:8080/pos/customers', true);

    /* 4. Let's set some request headers */
    xhr.setRequestHeader('Content-Type', 'application/json');

    showProgress(xhr);

    /* 5. Okay, time to send the request */
    xhr.send(JSON.stringify(customer));

    [txtName, txtAddress, txtContact, btnSave].forEach(elm => elm.attr('disabled', 'true'));
    $("#loader").css('visibility', 'visible');

});

function validateData() {
    const address = txtAddress.val().trim();
    const contact = txtContact.val().trim();
    const name = txtName.val().trim();
    let valid = true;
    resetForm();

    if (!address) {
        valid = invalidate(txtAddress, "Address can't be empty");
    } else if (!/.{3,}/.test(address)) {
        valid = invalidate(txtAddress, 'Invalid address');
    }

    if (!contact) {
        valid = invalidate(txtContact, "Contact number can't be empty");
    } else if (!/^\d{3}-\d{7}$/.test(contact)) {
        valid = invalidate(txtContact, 'Invalid contact number');
    }

    if (!name) {
        valid = invalidate(txtName, "Name can't be empty");
    } else if (!/^[A-Za-z ]+$/.test(name)) {
        valid = invalidate(txtName, "Invalid name");
    }

    return valid;
}

function invalidate(txt, msg) {
    setTimeout(() => txt.addClass('is-invalid animate__shakeX'), 0);
    txt.trigger('select');
    txt.next().text(msg);
    return false;
}

function resetForm(clearData) {
    [txtId, txtName, txtAddress, txtContact].forEach(txt => {
        txt.removeClass("is-invalid animate__shakeX");
        if (clearData) txt.val('');
    });
}

modalElm.on('show.bs.modal', () => {
    resetForm(true);
    txtId.parent().hide();
    setTimeout(() => txtName.trigger('focus'), 500);
});

function getCustomers(){
    const xhr = new XMLHttpRequest();

    xhr.addEventListener('readystatechange', ()=> {
        if (xhr.readyState === 4){
            if (xhr.status === 200){
                tbodyElm.empty();
                const customerList = JSON.parse(xhr.responseText);
                customerList.forEach(customer => {
                    tbodyElm.append(`
                    <tr>
                        <td class="text-center">${formatCustomerId(customer.id)}</td>
                        <td>${customer.name}</td>
                        <td class="d-none d-xl-table-cell">${customer.address}</td>
                        <td class="contact text-center">${customer.contact}</td>
                        <td>
                            <div class="actions d-flex gap-3 justify-content-center">
                                <svg data-bs-toggle="tooltip" data-bs-title="Edit Customer" xmlns="http://www.w3.org/2000/svg" width="20" height="20" fill="currentColor"
                                    class="bi bi-pencil-square edit" viewBox="0 0 16 16">
                                    <path
                                        d="M15.502 1.94a.5.5 0 0 1 0 .706L14.459 3.69l-2-2L13.502.646a.5.5 0 0 1 .707 0l1.293 1.293zm-1.75 2.456-2-2L4.939 9.21a.5.5 0 0 0-.121.196l-.805 2.414a.25.25 0 0 0 .316.316l2.414-.805a.5.5 0 0 0 .196-.12l6.813-6.814z" />
                                    <path fill-rule="evenodd"
                                        d="M1 13.5A1.5 1.5 0 0 0 2.5 15h11a1.5 1.5 0 0 0 1.5-1.5v-6a.5.5 0 0 0-1 0v6a.5.5 0 0 1-.5.5h-11a.5.5 0 0 1-.5-.5v-11a.5.5 0 0 1 .5-.5H9a.5.5 0 0 0 0-1H2.5A1.5 1.5 0 0 0 1 2.5v11z" />
                                </svg>
                                <svg data-bs-toggle="tooltip" data-bs-title="Delete Customer" xmlns="http://www.w3.org/2000/svg" width="20" height="20" fill="currentColor"
                                    class="bi bi-trash delete" viewBox="0 0 16 16">
                                    <path
                                        d="M5.5 5.5A.5.5 0 0 1 6 6v6a.5.5 0 0 1-1 0V6a.5.5 0 0 1 .5-.5Zm2.5 0a.5.5 0 0 1 .5.5v6a.5.5 0 0 1-1 0V6a.5.5 0 0 1 .5-.5Zm3 .5a.5.5 0 0 0-1 0v6a.5.5 0 0 0 1 0V6Z" />
                                    <path
                                        d="M14.5 3a1 1 0 0 1-1 1H13v9a2 2 0 0 1-2 2H5a2 2 0 0 1-2-2V4h-.5a1 1 0 0 1-1-1V2a1 1 0 0 1 1-1H6a1 1 0 0 1 1-1h2a1 1 0 0 1 1 1h3.5a1 1 0 0 1 1 1v1ZM4.118 4 4 4.059V13a1 1 0 0 0 1 1h6a1 1 0 0 0 1-1V4.059L11.882 4H4.118ZM2.5 3h11V2h-11v1Z" />
                                </svg>
                            </div>
                        </td>
                    </tr>
                `);
                });
                const tooltipTriggerList = document.querySelectorAll('[data-bs-toggle="tooltip"]')
                const tooltipList = [...tooltipTriggerList].map(tooltipTriggerEl => new bootstrap.Tooltip(tooltipTriggerEl));
                if (!customerList.length){
                    $("#tbl-customers tfoot").show();
                }else {
                    $("#tbl-customers tfoot").hide();
                }
            }else{
                tbodyElm.empty();
                $("#tbl-customers tfoot").show();
                showToast('error', 'Failed', 'Failed to fetch customers');
                console.log(JSON.parse(xhr.responseText));
            }
        }
    });

    const searchText = $("#txt-search").val().trim();
    const query = (searchText) ? `?q=${searchText}`: "";

    xhr.open('GET', 'http://localhost:8080/pos/customers' + query, true);

    const tfoot = $("#tbl-customers tfoot tr td:first-child");
    xhr.addEventListener('loadstart', ()=> tfoot.text('Please wait!'));
    xhr.addEventListener('loadend', ()=> tfoot.text('No customer records are found!'));

    xhr.send();
}

getCustomers();
$("#txt-search").on('input', ()=> getCustomers());

tbodyElm.on('click', ".delete", (eventData)=> {
    /* XHR -> jQuery AJAX */
    const id = +$(eventData.target).parents("tr").children("td:first-child").text().replace('C', '');
    const xhr = new XMLHttpRequest();
    const jqxhr = $.ajax(`http://localhost:8080/pos/customers/${id}`, {
        method: 'DELETE',
        xhr: ()=> xhr           // This is a hack to obtain the xhr that is used by jquery
    });
    showProgress(xhr);
    jqxhr.done(()=> {
        showToast('success', 'Deleted', 'Customer has been deleted successfully');
        $(eventData.target).tooltip('dispose');
        getCustomers();
    });
    jqxhr.fail(()=> {
        showToast('error', 'Failed', "Failed to delete the customer, try again!");
    });

    /*
    *   const jqxhr = $.ajax(url, {
    *               method: 'GET',
    *               contentType: 'application/json',
    *               data: 'Request Body'
    *           });
    *
    *   jqxhr.done((response, status)=> {});
    *   jqxhr.fail(()=> {});
    *   jqxhr.always(()=> {});
    *
    * */
});