window.onload = function () {

    var obj;
    $('.partner-state').on('change', function () {
        $('.partner-state').attr('disabled', true);
        var state;
        obj = $(this);
        if ($(obj).attr('checked')) {
            state = false;
        } else {
            state = true;
        }
        $.ajax({
            url: "/admin/partner/cambiar-estado",
            type: 'GET',
            data: {
                numId: $(this).val(),
                state: state
            }, success: function () {
                if (state) {
                    $(obj).attr('checked', 'checked');
                    obj.checked = true;
                } else {
                    $(obj).removeAttr('checked');
                    obj.checked = false;
                }
                $('.partner-state').attr('disabled', false);
            }, error: function () {
                swal({
                    title: "Error. No se pudo modificar.",
                    text: "No se pudo modificar el estado del afiliado. Refresque la págima e intente de nuevo. De seguir fallando, comunicarse con alguno de los técnicos..",
                    type: 'error'
                });
                $(obj).checked = !$(this).checked;
                $('.partner-state').attr('disabled', false);
            }
        })
    });

    $('.partner-row').each(function (index, row) {

        var id = row.getElementsByClassName('id')[0].innerText;
        var href = '/admin/editar-afiliado?id='.concat(id);
        var selector = row.getElementsByClassName('partner-selector');
        for (var i = 0; i < selector.length; i++) {
            selector[i].onclick = function () {
                window.location.href = href;
            };
        }
    });
};