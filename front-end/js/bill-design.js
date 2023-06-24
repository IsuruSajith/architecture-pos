import {Big} from '../node_modules/big.js/big.mjs';
import {formatNumber, formatPrice} from "./place-order.js";

export function getBillDesignHTML(cart, orderId) {
    return `<!doctype html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport"
          content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">
    <title>POS Bill</title>
    <link rel="stylesheet" href="node_modules/bootstrap/dist/css/bootstrap.min.css">
    <link rel="stylesheet" href="css/bill.css">
</head>
<body class="d-flex justify-content-center p-2 bg-secondary-subtle">
<div id="bill">
    <div id="bill-header" class="d-flex align-items-center flex-column gap-1 p-2 border-bottom">
        <svg xmlns="http://www.w3.org/2000/svg" width="40" height="32"
             class="bi bi-bag-check-fill" viewBox="0 0 16 16">
            <path fill-rule="evenodd"
                  d="M10.5 3.5a2.5 2.5 0 0 0-5 0V4h5v-.5zm1 0V4H15v10a2 2 0 0 1-2 2H3a2 2 0 0 1-2-2V4h3.5v-.5a3.5 3.5 0 1 1 7 0zm-.646 5.354a.5.5 0 0 0-.708-.708L7.5 10.793 6.354 9.646a.5.5 0 1 0-.708.708l1.5 1.5a.5.5 0 0 0 .708 0l3-3z" />
        </svg>
        <h2 class="my-0">POS System</h2>
        <small>dep10@ijse.lk</small>
    </div>
    <div id="bill-details" class="p-2 border-bottom text-center">
        <div><span class="fw-bold">Order ID:</span> OD${orderId.toString().padStart(3, '0')}</div>
        <div><span class="fw-bold">Customer:</span> ${cart.customer ? 'C' + cart.customer.id.toString().padStart(3, '0') + " - " + cart.customer.name : 'Walk-in Customer'}</div>
        <div><span class="fw-bold">Order Date:</span> ${cart.dateTime}</div>
    </div>
    <div id="bill-items">
        <table>
            <thead>
                <tr>
                    <th>No</th>
                    <th>Item</th>
                    <th>Qty.</th>
                    <th>Price</th>
                    <th>Amount</th>
                </tr>
            </thead>
            <tbody>
            ${generateRows(cart)}
            </tbody>
        </table>
        <h6 class="d-flex justify-content-between p-2 border-bottom">
            <span>Net Total</span>
            <span>${formatPrice(cart.getTotal())}</span>
        </h6>
    </div>
    <div id="bill-footer" class="text-center">
        <small>Thank you, come agan!</small>
        <div>
            <small class="fw-bold">IMPORTANT NOTICE</small>
            <p class="p-1">
                <small>
                    In case of a price discrepancy, return the item & bill within
                    7 days to refund the difference
                </small>
            </p>
        </div>
    </div>
</div>
</body>
</html>`;
}

function generateRows(cart){
    let html = '';
    cart.itemList.forEach((item, index) => {
        html += `
            <tr>
               <td>${index + 1}</td>
               <td colspan="4">${item.code} : ${item.description}</td>
            </tr>
            <tr>
                <td></td>
                <td></td>
                <td>${item.qty}</td>
                <td>${formatNumber(item.unitPrice)}</td>
                <td>${formatNumber(Big(item.qty).times(Big(item.unitPrice)))}</td>
            </tr>`;
    });
    return html;
}