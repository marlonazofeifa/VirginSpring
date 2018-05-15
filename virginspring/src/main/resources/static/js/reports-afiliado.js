function checkboxTouched() {
    if(document.getElementById("include-pax").checked){
        document.getElementById("include-pax-hidden").value = "t";
    }
    else{
        document.getElementById("include-pax-hidden").value = "f";
    }
}

function iniDateChanged() {
    document.getElementById("init-date-hidden").value = document.getElementById("initial-date").value;
}

function endDateChanged() {
    if(document.getElementById("selected-button-hidden").value === "annual"){
        var initDate = getDateFromString(document.getElementById("end-date").value);
        initDate.setDate(initDate.getDate() - 365);
        document.getElementById("initial-date").value = getDatePicker(initDate);
        /*document.getElementById("init-date-hidden").value = initDate.value;*/
    }
    document.getElementById("end-date-hidden").value = document.getElementById("end-date").value;
}

function partnerChanged() {
    document.getElementById("partner-hidden").value = document.getElementById("select-partner").value;
}

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

function getDateFromString(dateStr) {
    date = new Date();
    date.setYear(parseInt(dateStr.substring(0,4)));
    date.setMonth(parseInt(dateStr.substring(5,7))-1);
    date.setDate(parseInt(dateStr.substring(8)));
    return date;
}

window.onload = function () {

    // Date shortcuts buttons.
    var dailyButton = document.getElementById("daily-report");
    var annualButton = document.getElementById("annual-report");
    var customButton = document.getElementById("custom-report");

    // Filter values
    var initDate = document.getElementById("initial-date");
    var endDate = document.getElementById("end-date");
    var selectPartner = document.getElementById("select-partner");
    var selectedButton = document.getElementById("selected-button-hidden");

    var initInitDate = new Date();
    initInitDate.setDate(initInitDate.getDate()+1);
    var endInitDate = new Date();
    endInitDate.setDate(endInitDate.getDate()+1);
    document.getElementById("initial-date").value = getDatePicker(initInitDate);
    document.getElementById("end-date").value = getDatePicker(endInitDate);
    document.getElementById("selected-button-hidden").value = "daily";
    document.getElementById("init-date-hidden").value = getDatePicker(initInitDate);
    document.getElementById("end-date-hidden").value = getDatePicker(endInitDate);
    document.getElementById("partner-hidden").value = "";
    document.getElementById("include-pax-hidden").value = "f";

    // Function that disables the on click effect of the date shortcut buttons.
    // It simply disable the "light-selected" class.
    function disableSelectedClass(){
        dailyButton.classList.remove("light-selected");
        annualButton.classList.remove("light-selected");
        customButton.classList.remove("light-selected");
    }

    function enableCustom() {
        customButton.classList.add("light-selected");
        initDate.disabled = false;
        endDate.disabled = false;
        selectPartner.value = "";
        selectPartner.placeholder = "Id de Afiliado";
        selectPartner.disabled = false;
    }

    function disableCustomsParams() {
        initDate.disabled = true;
        endDate.disabled = true;
        selectPartner.disabled = true;
        selectPartner.value = "";
        selectPartner.placeholder = "Todos";
    }
    
    dailyButton.onclick = function() {
        selectedButton.value = "daily";
        var initDate = new Date();
        initDate.setDate(initDate.getDate()+1);
        var endDate = new Date();
        endDate.setDate(endDate.getDate()+1);
        document.getElementById("initial-date").value = getDatePicker(initDate);
        document.getElementById("init-date-hidden").value = getDatePicker(initDate);
        document.getElementById("end-date").value = getDatePicker(endDate);
        document.getElementById("end-date-hidden").value = getDatePicker(endDate);
        document.getElementById("include-pax-container").style.visibility = 'visible';
        disableSelectedClass();
        disableCustomsParams();
        dailyButton.className += " light-selected";
    };

    annualButton.onclick = function() {
        selectedButton.value = "annual";
        var initDate = new Date();
        initDate.setDate(initDate.getDate()-365);
        var endDate = new Date();
        document.getElementById("initial-date").value = getDatePicker(initDate);
        document.getElementById("init-date-hidden").value = getDatePicker(initDate);
        document.getElementById("end-date").value = getDatePicker(endDate);
        document.getElementById("end-date-hidden").value = getDatePicker(endDate);
        document.getElementById("include-pax-container").style.visibility = 'hidden';
        disableSelectedClass();
        disableCustomsParams();
        document.getElementById("end-date").disabled = false;
        selectPartner.value = "";
        selectPartner.placeholder = "Id de Afiliado";
        selectPartner.disabled = false;
        annualButton.className += " light-selected";
    };

    customButton.onclick = function () {
        selectedButton.value = "custom";
        document.getElementById("include-pax-container").style.visibility = 'hidden';
        disableSelectedClass();
        enableCustom();
    };
};