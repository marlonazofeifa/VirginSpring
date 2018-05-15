
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
            if(!earlyThan(resetDateFormat(row.cells[5].innerHTML),stringDate)) {
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
            if(!earlyThan(resetDateFormat(row.cells[5].innerHTML),stringDate)) {
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
            if(!olderThanOneDay(resetDateFormat(row.cells[5].innerHTML),stringDate)) {
                return false;
            }
        }
    }
    return true;
}

function resetDateFormat(date) {
    var months = [
        "Enero", "Febrero", "Marzo",
        "Abril", "Mayo", "Junio", "Julio",
        "Agosto", "Septiembre", "Octubre",
        "Noviembre", "Diciembre"
    ];
    var monthsNumbers = ["01","02","03","04","05","06","07","08","09","10","11","12"]
    var aux = date.split(" ");
    var day = aux[0];
    var year = aux[2];
    var index = 0;
    for (var i = 0; i < 12; i++) {
        if(months[i].toString() == aux[1].toString()) {
            index = i;
            break;
        }
    }
    return year+"-"+ monthsNumbers[index].toString() +"-"+day;
}

function earlyThan(dateCompared, dateConstraint) {
    var dateComparedSplit = dateCompared.split("-");
    var dateConstraintSplit = dateConstraint.split("-");
    var dateComparedValue = (parseInt(dateComparedSplit[0])*12*31)+(parseInt(dateComparedSplit[1]*31))+(parseInt(dateComparedSplit[2]));
    var dateConstraintValue = (parseInt(dateConstraintSplit[0])*12*31)+(parseInt(dateConstraintSplit[1]*31))+(parseInt(dateConstraintSplit[2]));
    if(dateComparedValue <= dateConstraintValue) {
        return true;
    }
    return false;
}

function olderThanOneDay(dateCompared, dateConstraint) {
    var dateComparedSplit = dateCompared.split("-");
    var dateConstraintSplit = dateConstraint.split("-");
    var dateComparedValue = (parseInt(dateComparedSplit[0])*12*31)+(parseInt(dateComparedSplit[1]*31))+(parseInt(dateComparedSplit[2]));
    var dateConstraintValue = (parseInt(dateConstraintSplit[0])*12*31)+(parseInt(dateConstraintSplit[1]*31))+(parseInt(dateConstraintSplit[2]));
    if(dateComparedValue > dateConstraintValue) {
        return true;
    }
    return false;
}

window.onload = function () {

    // Date shortcuts buttons.
    var yearButton = document.getElementById("year");
    var lastMonthButton = document.getElementById("mes-pasado");
    var monthButton = document.getElementById("mes");
    var yesterdayButton = document.getElementById("ayer");
    var todayButton = document.getElementById("hoy");
    var tomorrowButton = document.getElementById("tomorrow");

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
    var arrivalTime = $("#arrival-time");

    $(confirmationContainer).hide();

    // Date elements that must be parsed
    var dateElements = document.getElementsByClassName("date-element");

    // As soon as we load our window, we parse our dates.
    window.onload = parseDate();
    window.onload = modifyTimeObjects();

    // Parse our String date-elements with the following format: "yyyy-mm-dd" to "dd-MMM-yyyy".
    // This means our month will be shown with it's name.
    function parseDate(){
        for(var i = 0; i<dateElements.length; i++) {
            dateElements[i].innerHTML = stringToDate(dateElements[i].innerHTML, "yyyy-mm-dd", "-");
        }
    }

    function modifyTimeObjects() {
        $('.time').each(function (i,obj) {
            var position = $(obj).html().lastIndexOf(":");
            $(obj).html($(obj).html().substring(0,position));
        })
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
        tomorrowButton.classList.remove("light-selected");
    }

    yearButton.onclick = function() {
        var initDate = new Date(new Date().getFullYear(), 0, 1);
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

    tomorrowButton.onclick = function() {
        var initDate = new Date();
        var endDate = new Date();
        initDate.setDate(initDate.getDate()+1);
        endDate.setDate(endDate.getDate()+1);
        document.getElementById("initial-date").value = getDatePicker(initDate);
        document.getElementById("end-date").value = getDatePicker(endDate);
        disableSelectedClass();
        tomorrowButton.className += " light-selected";
    };

    initDate.onchange = function () {
        disableSelectedClass();
    };

    endDate.onchange = function () {
        disableSelectedClass();
    };

    actionButton.onclick = function() {
        if (!validate(action)){
            swal({
                title: '¡Acción no valida!',
                text: "Por favor, seleccione una acción.",
                type: 'warning'
            });
        } else {
            if (boxesChecked()) {
                switch (action.value) {
                    case "eliminar":
                        if(tryToDelete(reservationTable)) {
                            swal({
                                title: '¡Cuidado!',
                                text: "¿Esta seguro que desea realizar la operación de eliminar?",
                                type: 'warning',
                                showCancelButton: true,
                                confirmButtonColor: '#3085d6',
                                cancelButtonColor: '#d33',
                                cancelButtonText:"No",
                                confirmButtonText: 'Sí, eliminar'
                            }).then((result) => {
                                if (result.value) {
                                    form.action = "/admin/eliminar-reservas";
                                    form.submit();
                                }
                            })
                        } else {
                            swal({
                                title: 'Revisar las fechas.',
                                text: "No se pueden eliminar reservas pasadas.",
                                type: 'error'
                            });
                        }
                        break;
                    case "pendiente":
                        form.action = "/admin/pendiente-reservas";
                        form.submit();
                        break;
                    case "cancelar":
                        if(tryToCancel(reservationTable)) {
                            form.action = "/admin/cancelar-reservas";
                            form.submit();
                        } else {
                            swal({
                                title: 'Revisar las fechas.',
                                text: "No se pueden cancelar reservas que no han llegado.",
                                type: 'error'
                            });
                        }
                        break;
                }
            } else {
                swal({
                    title: '¡Acción no valida!',
                    text: "Seleccione por lo menos una reserva para modificar.",
                    type: 'warning'
                });
            }
        }
    }
    
    confirmButton.onclick = function () {
        if(boxesChecked()) {
            if(!tryToConfirm(reservationTable)) {
                swal({
                    title: 'Revisar las fechas.',
                    text: "No se pueden confirmar reservas que no han llegado.",
                    type: 'error'
                });
            } else {
                if(workerList.value != null && workerList.value != "") {
                    var input = document.createElement("input");
                    input.setAttribute("type","hidden");
                    input.setAttribute("name","workerId");
                    input.setAttribute("value",workerList.value);
                    form.appendChild(input);
                    var inputArrivalTime = document.createElement("input");
                    inputArrivalTime.setAttribute("type","hidden");
                    inputArrivalTime.setAttribute("name","arrival-time");
                    inputArrivalTime.setAttribute("value",arrivalTime.val());
                    form.appendChild(inputArrivalTime);
                    form.action = "/admin/confirmar-reservas";
                    form.submit();
                } else {
                    swal({
                        title: 'Falta funcionario.',
                        text: "Por favor, selecione un funcionario para continuar.",
                        type: 'warning'
                    });
                }
            }
        } else {
            swal({
                title: '¡Acción no valida!',
                text: "Seleccione por lo menos una reserva para modificar.",
                type: 'warning'
            });
        }
    }

    action.onchange = function() {
        if(action.value == "confirmar") {
            $(confirmationContainer).show(100);
            $(actionButton).hide();
        } else {
            $(confirmationContainer).hide(100);
            $(actionButton).show();
        }
    }
    
    selectAllBox.onclick = function () {
        var checkBoxes = document.getElementsByName("ids");
        for (var  i=0; i<checkBoxes.length; i++) {
            checkBoxes[i].checked = selectAllBox.checked;
        }
    }
};