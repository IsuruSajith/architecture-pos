import {DateTimeFormatter, LocalDateTime} from '../node_modules/@js-joda/core/dist/js-joda.esm.js';
import {Big} from '../node_modules/big.js/big.mjs';
import {Cart} from "./cart.js";
import {showProgress, showToast} from "./main.js";
import {getBillDesignHTML} from "./bill-design.js";

/* Module Level Variables, Constants */

const REST_API_BASE_URL = 'http://localhost:8080/pos';
const WS_API_BASE_URL = 'ws://localhost:8080/pos';
const orderDateTimeElm = $("#order-date-time");
const tbodyElm = $("#tbl-order tbody");
const tFootElm = $("#tbl-order tfoot");
const netTotalElm = $("#net-total");
const itemInfoElm = $("#item-info");
const customerNameElm = $("#customer-name");
const frmOrder = $("#frm-order");
const btnPlaceOrder = $("#btn-place-order");
const txtCustomer = $("#txt-customer");
const txtCode = $("#txt-code");
const txtQty = $("#txt-qty");
let customer = null;
let item = null;
let socket = null;
let cart = new Cart((total)=> netTotalElm.text(formatPrice(total)));

/* Initialization Logic */

setDateTime();
tbodyElm.empty();
socket = new WebSocket(`${WS_API_BASE_URL}/customers-ws`);
updateOrderDetails();

/* Event Handlers & Timers */

setInterval(setDateTime, 1000);
$("#btn-clear-customer").on('click', () => {
    customer = null;
    cart.setCustomer(customer);
    customerNameElm.text("Walk-in Customer");
    txtCustomer.val("");
    txtCustomer.removeClass("is-invalid");
    txtCustomer.trigger("focus");
});
socket.addEventListener('message', (eventData) => {
    customer = JSON.parse(eventData.data);
    cart.setCustomer(customer);
    customerNameElm.text(customer.name);
});
txtCustomer.on('input', () => findCustomer());
txtCustomer.on('blur', () => {
    if (txtCustomer.val() && !customer) {
        txtCustomer.addClass("is-invalid");
    }
});
txtCode.on('input', () => {
    itemInfoElm.addClass('d-none');
    frmOrder.addClass('d-none');
})
txtCode.on('change', () => findItem());
txtQty.on('input', () => txtQty.removeClass('is-invalid'));
frmOrder.on('submit', (eventData) => {
    eventData.preventDefault();

    if (+txtQty.val() <= 0 || +txtQty.val() > item.qty) {
        txtQty.addClass("is-invalid");
        txtQty.trigger("select");
        return;
    }
    item.qty = +txtQty.val();

    if (cart.containItem(item.code)) {
        const codeElm = Array.from(tbodyElm.find("tr td:first-child .code")).find(codeElm => $(codeElm).text() === item.code);
        const qtyElm = $(codeElm).parents("tr").find("td:nth-child(2)");
        const priceElm = $(codeElm).parents("tr").find("td:nth-child(4)");

        cart.updateItemQty(item.code, cart.getItem(item.code).qty + item.qty);
        qtyElm.text(cart.getItem(item.code).qty);
        priceElm.text(formatNumber(Big(cart.getItem(item.code).qty).times(item.unitPrice)));
    } else {
        addItemToTable(item);
        cart.addItem(item);
    }

    itemInfoElm.addClass("d-none");
    frmOrder.addClass("d-none");
    txtCode.val("");
    txtCode.trigger("focus");
    txtQty.val("1");
});
tbodyElm.on('click', 'svg.delete', (eventData) => {
    const trElm = $(eventData.target).parents("tr");
    const code = trElm.find("td:first-child .code").text();
    cart.deleteItem(code);
    trElm.remove();
    txtCode.val("");
    txtCode.trigger('input');
    if (!cart.itemList.length) {
        tFootElm.show();
        txtCode.trigger('focus');
    }
});
btnPlaceOrder.on('click', ()=> placeOrder());

/* Functions */

function placeOrder(){
    if (!cart.itemList.length) return;

    cart.dateTime = orderDateTimeElm.text();
    btnPlaceOrder.attr('disabled', true);
    const xhr = new XMLHttpRequest();

    const jqxhr = $.ajax(`${REST_API_BASE_URL}/orders`, {
        method: 'POST',
        contentType: 'application/json',
        data: JSON.stringify(cart),
        xhr: ()=> xhr
    });

    showProgress(xhr);

    jqxhr.done((orderId)=> {
        printBill(orderId);
        cart.clear();
        $("#btn-clear-customer").trigger('click');
        txtCode.val("");
        txtCode.trigger("input");
        tbodyElm.empty();
        tFootElm.show();
        showToast('success', 'Success', 'Order has been placed successfully');
    });
    jqxhr.fail(()=> {
        showToast('error', 'Failed', "Failed to place the order, try again!");
    });
    jqxhr.always(()=> btnPlaceOrder.removeAttr('disabled'));
}

function printBill(orderId) {
    const billWindow = open("", `_blank`, "popup=true,width=200");
    billWindow.document.write(getBillDesignHTML(cart, orderId));
}

function updateOrderDetails() {
    const id = cart.customer?.id.toString().padStart(3, '0');
    txtCustomer.val(id ? 'C' + id : '');
    customerNameElm.text(cart.customer?.name);
    cart.itemList.forEach(item => addItemToTable(item));
    netTotalElm.text(formatPrice(cart.getTotal()));
}

function addItemToTable(item) {
    tFootElm.hide();
    const trElm = $(`<tr>
                    <td>
                        <div class="d-flex justify-content-between align-items-center">
                            <div>
                                <div class="fw-bold code">${item.code}</div>
                                <div>${item.description}</div>
                            </div>
                            <svg data-bs-toggle="tooltip" data-bs-title="Remove Item" xmlns="http://www.w3.org/2000/svg"
                                 width="32" height="32" fill="currentColor" class="bi bi-trash delete"
                                 viewBox="0 0 16 16">
                                <path d="M5.5 5.5A.5.5 0 0 1 6 6v6a.5.5 0 0 1-1 0V6a.5.5 0 0 1 .5-.5Zm2.5 0a.5.5 0 0 1 .5.5v6a.5.5 0 0 1-1 0V6a.5.5 0 0 1 .5-.5Zm3 .5a.5.5 0 0 0-1 0v6a.5.5 0 0 0 1 0V6Z"/>
                                <path d="M14.5 3a1 1 0 0 1-1 1H13v9a2 2 0 0 1-2 2H5a2 2 0 0 1-2-2V4h-.5a1 1 0 0 1-1-1V2a1 1 0 0 1 1-1H6a1 1 0 0 1 1-1h2a1 1 0 0 1 1 1h3.5a1 1 0 0 1 1 1v1ZM4.118 4 4 4.059V13a1 1 0 0 0 1 1h6a1 1 0 0 0 1-1V4.059L11.882 4H4.118ZM2.5 3h11V2h-11v1Z"/>
                            </svg>
                        </div>
                    </td>
                    <td>
                        ${item.qty}
                    </td>
                    <td>
                        ${formatNumber(item.unitPrice)}
                    </td>
                    <td>
                        ${formatNumber(Big(item.unitPrice).times(Big(item.qty)))}
                    </td>
                </tr>`);
    tbodyElm.append(trElm);
}

function findItem() {
    const description = $("#description");
    const stock = $("#stock span");
    const unitPrice = $("#unit-price");
    const itemInfo = itemInfoElm;
    const code = txtCode.val().trim();

    description.text("");
    stock.text("");
    unitPrice.text("");
    itemInfo.addClass("d-none");
    frmOrder.addClass("d-none");
    txtCode.removeClass("is-invalid");
    txtQty.val("1");
    item = null;

    if (!code) return;

    const jqxhr = $.ajax(`${REST_API_BASE_URL}/items/${code}`);
    txtCode.attr('disabled', true);
    jqxhr.done((data) => {
        item = data;
        description.text(item.description);
        if (cart.containItem(item.code)) {
            item.qty -= cart.getItem(code).qty;
        }
        stock.text(item.qty ? `In Stock: ${item.qty}` : 'Out of Stock');
        !item.qty ? stock.addClass("out-of-stock") : stock.removeClass("out-of-stock");
        unitPrice.text(formatPrice(item.unitPrice));
        itemInfo.removeClass("d-none");
        if (item.qty) {
            frmOrder.removeClass("d-none");
            txtQty.trigger("select");
        }
    });
    jqxhr.fail(() => {
        txtCode.addClass("is-invalid");
        txtCode.trigger('select');
    });
    jqxhr.always(() => {
        txtCode.removeAttr("disabled");
        if (!item?.qty) {
            txtCode.trigger("select");
        }
    });
}

function findCustomer() {
    const idOrContact = txtCustomer.val().trim().replace('C', '');

    txtCustomer.removeClass("is-invalid");
    if (!idOrContact) return;
    customer = null;
    customerNameElm.text("Walk-in Customer");
    cart.setCustomer(null);

    if (socket.readyState === socket.OPEN) socket.send(idOrContact);
}

export function formatPrice(price) {
    return new Intl.NumberFormat('en-LK', {
        style: 'currency',
        currency: 'LKR',
        minimumFractionDigits: 2,
        maximumFractionDigits: 2
    }).format(price);
}

export function formatNumber(number) {
    return new Intl.NumberFormat('en-LK', {
        style: 'decimal',
        minimumFractionDigits: 2,
        maximumFractionDigits: 2
    }).format(number);
}

function setDateTime() {
    const now = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    orderDateTimeElm.text(now);
}


