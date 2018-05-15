jQuery(document).ready(function() {

    // For the success and failure messages, we must be able to have the following ids in our html.
    var message = $("#message-alert").html();
    var type = $("#message-alert-type").html();

    if(message != "") {
        if(type == "success") {
            swal({
                title: "¡Éxito!",
                text: message,
                type: 'success'
        })
        } else if (type == "error") {
            swal({
                title: "Algo falló.",
                text: message,
                type: 'error'
            })
        }
    }

})