
function getDatePicker(date){
    dateString = date.getFullYear()+"-";
    if((date.getMonth()+1)<10){
        dateString += "0"+(date.getMonth()+1)+"-";
    } else {
        dateString += (date.getMonth()+1)+"-";
    }
    if(date.getDate()<10){
        dateString += "0"+date.getDate();
    } else {
        dateString += date.getDate();
    }
    return dateString;
};

function validate(actionSelected) {
    if (actionSelected.value == "notselected") {
        return false;
    }
    return true;
}

function boxesChecked() {
    var checkBoxes = document.getElementsByName("ids");
    for(var i = 0; i<checkBoxes.length; i++) {
        if(checkBoxes[i].checked) {
            return true;
        }
    }
    return false;
}

function tryToConfirm(table) {
    for (var i = 1, row; row = table.rows[i]; i++) {
        if($(row.cells[0]).find("input").is(":checked")) {
            var date = new Date();
            var stringDate = getDatePicker(date);
            if(!earlyThan(row.cells[5].innerHTML,stringDate)) {
                return false;
            }
        }
    }
    return true;
}

function tryToCancel(table) {
    for (var i = 1, row; row = table.rows[i]; i++) {
        if($(row.cells[0]).find("input").is(":checked")) {
            var date = new Date();
            var stringDate = getDatePicker(date);
            if(!earlyThan(row.cells[5].innerHTML,stringDate)) {
                return false;
            }
        }
    }
    return true;
}

function tryToDelete(table) {
    for (var i = 1, row; row = table.rows[i]; i++) {
        if($(row.cells[0]).find("input").is(":checked")) {
            var date = new Date();
            date.setDate(date.getDate());
            var stringDate = getDatePicker(date);
            if(!olderThanOneDay(row.cells[5].innerHTML,stringDate)) {
                return false;
            }
        }
    }
    return true;
}

function earlyThan(dateCompared, dateConstraint) {
    if(dateCompared <= dateConstraint) {
        return true;
    }
    return false;
}

function olderThanOneDay(dateCompared, dateConstraint) {
    if(dateCompared >= dateConstraint) {
        return true;
    }
    return false;
}

function addHiddenInput(form, key, value) {
    // Create a hidden input element, and append it to the form:
    var input = document.createElement('input');
    input.type = 'hidden';
    input.name = key; // 'the key/name of the attribute/field that is sent to the server
    input.value = value;
    form.appendChild(input);
}

window.onload = function () {

    // Date shortcuts buttons.
    var yearButton = document.getElementById("year");
    var lastMonthButton = document.getElementById("mes-pasado");
    var monthButton = document.getElementById("mes");
    var yesterdayButton = document.getElementById("ayer");
    var todayButton = document.getElementById("hoy");

    // Filter values
    var initDate = document.getElementById("initial-date");
    var endDate = document.getElementById("end-date");

    // Form and button use to update our reservations
    var action = document.getElementById("action-selector");
    var form = document.getElementById("reservation-list");
    var actionButton = document.getElementById("check-action");
    var selectAllBox = document.getElementById("selectAll");
    var reservationTable = document.getElementById("reservation-table");
    var confirmationContainer = document.getElementById("workers-container");
    var confirmButton = document.getElementById("confirm-action");
    var workerList = document.getElementById("workers-list");

    $(confirmationContainer).hide();

    // Date elements that must be parsed
    var dateElements = document.getElementsByClassName("date-element");

    // As soon as we load our window, we parse our dates.
    window.onload = parseDate();

    // Parse our String date-elements with the following format: "yyyy-mm-dd" to "dd-MMM-yyyy".
    // This means our month will be shown with it's name.
    function parseDate(){
        for(var i = 0; i<dateElements.length; i++) {
            dateElements[i].innerHTML = stringToDate(dateElements[i].innerHTML, "yyyy-mm-dd", "-");
        }
    }

    // Function that help us parsing our String to dates
    function stringToDate(date, format, delimiter) {
        var stringDate = date.toString();
        var formatItems= format.split(delimiter);
        var dateItems= stringDate.split(delimiter);
        var monthIndex= formatItems.indexOf("mm");
        var dayIndex= formatItems.indexOf("dd");
        var yearIndex= formatItems.indexOf("yyyy");
        var month= parseInt(dateItems[monthIndex]);
        month-=1;
        var formatedDate = new Date(dateItems[yearIndex],month,dateItems[dayIndex]);
        return formatDate(formatedDate);
    }

    // Format our dates to "dd-MMM-yyyy"
    function formatDate(date) {
        var monthNames = [
            "Enero", "Febrero", "Marzo",
            "Abril", "Mayo", "Junio", "Julio",
            "Agosto", "Septiembre", "Octubre",
            "Noviembre", "Diciembre"
        ];
        var day = date.getDate();
        var monthIndex = date.getMonth();
        var year = date.getFullYear();

        return day + ' ' + monthNames[monthIndex] + ' ' + year;
    }

    // Function that disables the on click effect of the date shortcut buttons.
    // It simply disable the "light-selected" class.
    function disableSelectedClass(){
        yearButton.classList.remove("light-selected");
        lastMonthButton.classList.remove("light-selected");
        monthButton.classList.remove("light-selected");
        yesterdayButton.classList.remove("light-selected");
        todayButton.classList.remove("light-selected");
    }

    yearButton.onclick = function() {
        var initDate = new Date(new Date().getFullYear(), 1, 1);
        var endDate = new Date();
        document.getElementById("initial-date").value = getDatePicker(initDate);
        document.getElementById("end-date").value = getDatePicker(endDate);
        disableSelectedClass();
        yearButton.className += " light-selected";
    };

    lastMonthButton.onclick = function () {
        var initDate = new Date();
        initDate.setDate(initDate.getDate()-30);
        var endDate = new Date();
        document.getElementById("initial-date").value = getDatePicker(initDate);
        document.getElementById("end-date").value = getDatePicker(endDate);
        disableSelectedClass();
        lastMonthButton.className += " light-selected";
    };

    monthButton.onclick = function () {
        var initDate = new Date();
        initDate.setDate(initDate.getDate()-7);
        var endDate = new Date();
        document.getElementById("initial-date").value = getDatePicker(initDate);
        document.getElementById("end-date").value = getDatePicker(endDate);
        disableSelectedClass();
        monthButton.className += " light-selected";
    };


    yesterdayButton.onclick = function() {
        var initDate = new Date();
        initDate.setDate(initDate.getDate()-1);
        var endDate = new Date();
        endDate.setDate(endDate.getDate()-1);
        document.getElementById("initial-date").value = getDatePicker(initDate);
        document.getElementById("end-date").value = getDatePicker(endDate);
        disableSelectedClass();
        yesterdayButton.className += " light-selected";
    };

    todayButton.onclick = function() {
        var initDate = new Date();
        var endDate = new Date();
        document.getElementById("initial-date").value = getDatePicker(initDate);
        document.getElementById("end-date").value = getDatePicker(endDate);
        disableSelectedClass();
        todayButton.className += " light-selected";
    };

    initDate.onchange = function () {
        disableSelectedClass();
    }

    endDate.onchange = function () {
        disableSelectedClass();
    }

    actionButton.onclick = function() {
        if (!validate(action)){
            alert("Por favor, seleccione una acción.");
        } else {
            if (boxesChecked()) {
                switch (action.value) {
                    case "eliminar":
                        if(tryToDelete(reservationTable)) {
                            if (confirm("¿Esta seguro que desea realizar la operación de eliminar?") == true) {
                                form.action = "/partner/eliminar-reservas";
                                form.submit();
                            }
                        } else {
                            alert("No se pueden eliminar reservas pasadas. Revisar las fechas.");
                        }
                        break;
                    case "pendiente":
                        form.action = "/partner/pendiente-reservas";
                        form.submit();
                        break;
                    case "cancelar":
                        if(tryToCancel(reservationTable)) {
                            form.action = "/partner/cancelar-reservas";
                            form.submit();
                        } else {
                            alert("No se pueden cancelar reservas que no han llegado. Revisar las fechas.");
                        }
                        break;
                }
            } else {
                alert("Seleccione por lo menos una reserva para modificar.");
            }
        }
    }
    
    confirmButton.onclick = function () {
        if(boxesChecked()) {
            if(!tryToConfirm(reservationTable)) {
                alert("No se pueden confirmar reservas que no han llegado. Revisar las fechas.");
            } else {
                if(workerList.value != null && workerList.value != "") {
                    var input = document.createElement("input");
                    input.setAttribute("type","hidden");
                    input.setAttribute("name","workerId");
                    input.setAttribute("value",workerList.value);
                    form.appendChild(input);
                    form.action = "/partner/confirmar-reservas";
                    form.submit();
                } else {
                    alert("Selecione un funcionario para continuar.");
                }
            }
        } else {
            alert("Seleccione por lo menos una reserva para modificar.");
        }
    }

    action.onchange = function() {
        if(action.value == "confirmar") {
            $(confirmationContainer).show(100);
            $(actionButton).hide();
        } else {
            $(confirmationContainer).hide(100);
            $(actionButton).show();
            //$("#workers-container").removeClass('show-workers').addClass('hide-workers');
        }
    }
    
    function deleteIds() {
        var checkDates = document.getElementById("reservation-table");
    }
    
    selectAllBox.onclick = function () {
        var checkBoxes = document.getElementsByName("ids");
        for (var  i=0; i<checkBoxes.length; i++) {
            checkBoxes[i].checked = selectAllBox.checked;
        }
    }
};