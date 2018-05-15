jQuery(document).ready(function () {

    var table = document.getElementById("schedule-table");
    if (table != null) {
        for (var i = 1; i < table.rows.length; i++) {

            table.rows[i].onclick = function () {
                var id = this.cells[0].text;
                redirectInfo(this.cells[0].innerHTML);
            }
        }
    }

    function redirectInfo(day) {
        window.location.href= "horario-seleccionado?day="+day;
    }

})