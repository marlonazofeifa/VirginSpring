jQuery(document).ready(function () {

    //Cuando se preciona el boton eliminar muestra ventana de confirmación
    $("#delete-button").click(function () {
        showConfirmationWindow();
    });

    //Al cancelar oculpa la ventana de confirmación
    $("#cancel-button").click(function () {
        hideConfirmationWindow();
    });

    //Al tocar la cortina gris se cancela la operación.
    $(".dark-curtain").click(function () {
        hideConfirmationWindow();
    });

    function hideConfirmationWindow() {
        document.getElementsByClassName("dark-curtain")[0].style.visibility = "hidden";
        document.getElementsByClassName("popup-window")[0].style.visibility = "hidden";
    }

    function showConfirmationWindow() {
        document.getElementsByClassName("dark-curtain")[0].style.visibility = "visible";
        document.getElementsByClassName("popup-window")[0].style.visibility = "visible";
    }
})