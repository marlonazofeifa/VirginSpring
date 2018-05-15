jQuery(document).ready(function () {
    $("#btnPrueba").click(function () {
        $.ajax({
            url: "prueba-cors",
            type: 'POST',
            crossDomain: true,
            data: {"mensaje": "hola"},
            success:function (data) {
                if (data == "respondido") alert("El mensaje fue respondido con " + data);
            },
            error:function (data) {
                alert("ERROR: " + data);
            }
        })
    });
});