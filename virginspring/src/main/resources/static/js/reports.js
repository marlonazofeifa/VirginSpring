function createDailyReport() {
    var initInitDate = new Date();
    initInitDate.setDate(initInitDate.getDate()+1);
    var endInitDate = new Date();
    endInitDate.setDate(endInitDate.getDate()+1);
    document.getElementById("selected-button-hidden").value = "daily";
    document.getElementById("init-date-hidden").value = getDatePicker(initInitDate);
    document.getElementById("end-date-hidden").value = getDatePicker(endInitDate);
    document.getElementById("date-picker-container").style.display = "none";
    document.getElementById("airline-container").style.display = "none";
}

function showIncludePaxContainer() {
    var x = document.getElementById("include-pax-container");
    x.style.display = "block";
}

function hideIncludePaxContainer() {
    var x = document.getElementById("include-pax-container");
    x.style.display = "none";
}

function showIncludeNotReceivedContainer() {
    var x = document.getElementById("include-not-received-container");
    x.style.display = "block";
}

function hideIncludeNotReceivedContainer() {
    var x = document.getElementById("include-not-received-container");
    x.style.display = "none";
}

function switchToYearPicker() {
    var year = document.getElementById("year-picker-container");
    var date = document.getElementById("date-picker-container");
    var day = document.getElementById("day-picker-container");
    date.style.display = "none";
    year.style.display = "block";
    day.style.display = "none";
    document.getElementById("year-picker").value = new Date().getFullYear();
}

function switchToDatePicker() {
    var year = document.getElementById("year-picker-container");
    var date = document.getElementById("date-picker-container");
    var day = document.getElementById("day-picker-container");
    year.style.display = "none";
    date.style.display = "block";
    day.style.display = "none";
}

function switchToDayPicker() {
    var year = document.getElementById("year-picker-container");
    var date = document.getElementById("date-picker-container");
    var day = document.getElementById("day-picker-container");
    year.style.display = "none";
    date.style.display = "none";
    day.style.display = "block";
    var tomorrow = new Date();
    tomorrow.setDate(tomorrow.getDate() + 1);
    document.getElementById("day-picker").value = getDatePicker(tomorrow);
}

function includePaxTouched() {
    if(document.getElementById("include-pax").checked){
        document.getElementById("include-pax-hidden").value = "t";
    }
    else{
        document.getElementById("include-pax-hidden").value = "f";
    }
}

function includeNotReceivedTouched() {
    if(document.getElementById("include-not-received").checked){
        document.getElementById("include-not-received-hidden").value = "t";
    }
    else{
        document.getElementById("include-not-received-hidden").value = "f";
    }
}

function iniDateChanged() {
    document.getElementById("init-date-hidden").value = document.getElementById("initial-date").value;
}

function endDateChanged() {
    /*
    if(document.getElementById("selected-button-hidden").value === "annual"){
        var initDate = getDateFromString(document.getElementById("end-date").value);
        initDate.setDate(initDate.getDate() - 365);
        document.getElementById("initial-date").value = getDatePicker(initDate);
        /*document.getElementById("init-date-hidden").value = initDate.value;
    }*/
    document.getElementById("end-date-hidden").value = document.getElementById("end-date").value;
}

function yearPickerChanged() {
    var yearChosed = document.getElementById("year-picker").value;
    var initDate = getDateFromString(yearChosed + "-01-01");
    document.getElementById("init-date-hidden").value = getDatePicker(initDate);
    var endDate = getDateFromString(yearChosed + "-12-31");
    document.getElementById("end-date-hidden").value = getDatePicker(endDate);
}

function dayPickerChanged() {
    document.getElementById("init-date-hidden").value = document.getElementById("day-picker").value;
    document.getElementById("end-date-hidden").value = document.getElementById("day-picker").value;
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
    document.getElementById("day-picker").value = getDatePicker(initInitDate);
    document.getElementById("selected-button-hidden").value = "daily";
    document.getElementById("init-date-hidden").value = getDatePicker(initInitDate);
    document.getElementById("end-date-hidden").value = getDatePicker(endInitDate);
    document.getElementById("partner-hidden").value = "";
    document.getElementById("include-pax-hidden").value = "f";
    document.getElementById("include-not-received-hidden").value = "f";
    document.getElementById("year-picker-container").style.display = "none";
    document.getElementById("date-picker-container").style.display = "none";
    document.getElementById("include-not-received-container").style.display = "none";

    // Function that disables the on click effect of the date shortcut buttons.
    // It simply disable the "tab-selector-selected" class.
    function disableSelectedClass(){
        dailyButton.classList.remove("tab-selector-selected");
        annualButton.classList.remove("tab-selector-selected");
        customButton.classList.remove("tab-selector-selected");
    }

    function enableCustom() {
        var today = new Date();
        var firstDayOfPreviousMonth = new Date(today.getFullYear(), today.getMonth() - 1, 1);
        var lastDayOfPreviousMonth = new Date(today.getFullYear(), today.getMonth(), 0);

        initDate.value = getDatePicker(firstDayOfPreviousMonth);
        endDate.value = getDatePicker(lastDayOfPreviousMonth);

        document.getElementById("init-date-hidden").value = initDate.value;
        document.getElementById("end-date-hidden").value = endDate.value;

        customButton.classList.add("tab-selector-selected");
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
        var date = new Date();
        date.setDate(date.getDate()+1);
        document.getElementById("init-date-hidden").value = getDatePicker(date);
        document.getElementById("end-date-hidden").value = getDatePicker(date);
        showIncludePaxContainer();
        hideIncludeNotReceivedContainer();
        disableSelectedClass();
        disableCustomsParams();
        switchToDayPicker();
        dailyButton.className += " tab-selector-selected";
    };

    annualButton.onclick = function() {
        selectedButton.value = "annual";

        var yearChosed = new Date().getFullYear();
        var initDate = getDateFromString(yearChosed + "-01-01");
        document.getElementById("init-date-hidden").value = getDatePicker(initDate);
        var endDate = getDateFromString(yearChosed + "-12-31");
        document.getElementById("end-date-hidden").value = getDatePicker(endDate);

        hideIncludePaxContainer();
        hideIncludeNotReceivedContainer();
        disableSelectedClass();
        disableCustomsParams();
        switchToYearPicker();
        annualButton.className += " tab-selector-selected";
    };

    customButton.onclick = function () {
        selectedButton.value = "custom";
        hideIncludePaxContainer();
        showIncludeNotReceivedContainer();
        disableSelectedClass();
        switchToDatePicker();
        enableCustom();
    };
};