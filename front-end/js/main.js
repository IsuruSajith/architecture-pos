/* global bootstrap: false */
(() => {
    'use strict'
    const tooltipTriggerList = Array.from(document.querySelectorAll('[data-bs-toggle="tooltip"]'))
    tooltipTriggerList.forEach(tooltipTriggerEl => {
      new bootstrap.Tooltip(tooltipTriggerEl)
    })
  })();

export function showProgress(xhr){
    const progressBar =  $("#progress-bar");
    xhr.addEventListener('loadstart', ()=> progressBar.width('5%'));
    xhr.addEventListener('progress', (eventData)=> {
        const downloadedBytes = eventData.loaded;
        const totalBytes = eventData.total;
        const progress = downloadedBytes / totalBytes * 100;
        progressBar.width(`${progress}%`);
    });
    xhr.addEventListener('loadend', ()=> {
        progressBar.width('100%');
        setTimeout(()=> progressBar.width('0%'), 500);
    });
}

export function showToast(toastType, header, message) {
    const toast = $("#toast .toast");
    toast.removeClass("text-bg-success text-bg-warning text-bg-danger");
    switch (toastType) {
        case 'success':
            toast.addClass('text-bg-success');
            break;
        case 'warning':
            toast.addClass('text-bg-warning');
            break;
        case 'error':
            toast.addClass('text-bg-danger');
            break;
        default:
    }
    $("#toast .toast-header > strong").text(header);
    $("#toast .toast-body").text(message);
    toast.toast('show');
}