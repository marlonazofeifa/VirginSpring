//DO NOT MODIFIED
window.onload = function () {

    var changePasswordButton = document.getElementById('change-pass');
    var popUpWindow = document.getElementById('pop-up-window');
    var closeButton = document.getElementById('close-popup-button');
    var errorMessageConfirmed = document.getElementById("not-confirmed");
    var errorMessageNotValid = document.getElementById("not-valid");
    $(errorMessageConfirmed).hide();
    $(errorMessageNotValid).hide();


    changePasswordButton.onclick = function () {
        popUpWindow.style.visibility = "visible";
    };

    closeButton.onclick = function () {
        popUpWindow.style.visibility = "hidden";
        clearContent()
    };

    window.onclick = function (event) {
        if (event.target == popUpWindow) {
            popUpWindow.style.visibility = "hidden";
            clearContent()
        }
    }

    $("#btn-save-pass").click(function (e, returnedValue) {
        $(errorMessageConfirmed).hide();
        $(errorMessageNotValid).hide();
        var lastPassword = document.getElementById("last-password").value;
        var newPassword = document.getElementById("new-password").value;
        var verificationPassword = document.getElementById("new-password-verification").value;

        e.preventDefault();

        if(verificationPassword == newPassword) {
            $.ajax({
                type: "POST",
                url: "/admin/cambiar-contrasenna",
                data: {lastPassword: lastPassword, newPassword: newPassword}, // parameters
                success: function (response) {
                    if (response.toString().localeCompare("error") == 0) {
                        $(errorMessageNotValid).show();
                    } else if (response.toString().localeCompare("invalidPassword") == 0) {
                        $(errorMessageNotValid).show();
                    } else {
                        popUpWindow.style.display = "none";
                        swal({
                            title: "Éxito.",
                            text: "La contraseña fue modificada.",
                            type: 'success'
                        });
                    }
                },
                error: function () {
                    swal({
                        title: "Algo fallo.",
                        text: "No se pudo cambiar la contraseña. Intentelo de nuevo.",
                        type: 'error'
                    });
                }
            });
        } else {
            $(errorMessageConfirmed).show();
        }
    });

    $('pop-up-form').keypress(function (e) {
        if (e.keyCode == 13)
            $('#btn-save-pass').click();
    });

    function clearContent() {
        document.getElementById("last-password").value = "";
        document.getElementById("new-password").value = "";
        document.getElementById("new-password-verification").value = "";
    }
};

