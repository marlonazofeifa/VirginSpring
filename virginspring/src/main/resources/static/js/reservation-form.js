/**
 * Created by alaincruzcasanova on 11/21/17.
 */
jQuery(document).ready(function() {

    /*
    * SECTION: Delete button.
    * */

    var deleteForm = document.getElementById("delete-reservation");
    var deleteButton = document.getElementById("delete-button");
    var arrivalDate = $("#arrivalDate");
    var airline = $("#airline");
    var flights = $("#flight-number");
    var schedule = $("#flight-arrival-time");
    var arrivalTime = $("#arrival-time");

    deleteButton.onclick = function() {
        swal({
            title: '¡Cuidado!',
            text: "¿Seguro que desea eliminar la reserva?",
            type: 'warning',
            showCancelButton: true,
            confirmButtonColor: '#3085d6',
            cancelButtonColor: '#d33',
            cancelButtonText:"No",
            confirmButtonText: 'Sí, eliminar'
        }).then((result) => {
            if (result.value) {
                var currentUrl = window.location.pathname;
                var finalUrl = currentUrl.concat("/eliminar");
                deleteForm.action = finalUrl;
                deleteForm.submit();
            }
        })
    }

    /*
     * SECTION: Change state of the reservation.
     * */

    var workersContainer = document.getElementById("workers-container");
    var workerList = document.getElementById("workers-list");
    var actionSelector = document.getElementById("action-selector");
    var noWorkerMessage = document.getElementById("no-worker");
    var changeStateButton = document.getElementById("change-state");
    var actionForm = document.getElementById("modify-state");
    $(workersContainer).hide(100);
    $(noWorkerMessage).hide(100);

    actionSelector.onchange = function() {
        if(actionSelector.value == "confirmar") {
            $(workersContainer).show(100);
        } else {
            $(workersContainer).hide(100);
            $(noWorkerMessage).hide(100);
        }
    }

    changeStateButton.onclick = function() {
        if((workerList.value != null && workerList.value != "") || (actionSelector.value != "confirmar")) {
            $(noWorkerMessage).hide(100);
            switch(actionSelector.value) {
                case "eliminar":
                    swal({
                        title: '¡Cuidado!',
                        text: "¿Seguro que desea eliminar la reserva?",
                        type: 'warning',
                        showCancelButton: true,
                        confirmButtonColor: '#3085d6',
                        cancelButtonColor: '#d33',
                        cancelButtonText:"No",
                        confirmButtonText: 'Sí, eliminar'
                    }).then((result) => {
                        if (result.value) {
                            var currentUrl = window.location.pathname;
                            var finalUrl = currentUrl.concat("/eliminar");
                            deleteForm.action = finalUrl;
                            deleteForm.submit();
                        }
                    })
                    break;
                case "cancelar":
                    var currentUrl = window.location.pathname;
                    var finalUrl = currentUrl.concat("/cancelar");
                    actionForm.action = finalUrl;
                    actionForm.submit();
                    break;
                case "pendiente":
                    var currentUrl = window.location.pathname;
                    var finalUrl = currentUrl.concat("/pendiente");
                    actionForm.action = finalUrl;
                    actionForm.submit();
                    break;
                case "confirmar":
                    var input = document.createElement("input");
                    input.setAttribute("type","hidden");
                    input.setAttribute("name","workerId");
                    input.setAttribute("value",workerList.value);
                    actionForm.appendChild(input);
                    var inputArrivalTime = document.createElement("input");
                    inputArrivalTime.setAttribute("type","hidden");
                    inputArrivalTime.setAttribute("name","arrival-time");
                    inputArrivalTime.setAttribute("value",arrivalTime.val());
                    actionForm.appendChild(inputArrivalTime);
                    var currentUrl = window.location.pathname;
                    var finalUrl = currentUrl.concat("/confirmar");
                    actionForm.action = finalUrl;
                    actionForm.submit();
                    break;
            }
        } else {
            if(actionSelector.value == "confirmar") {
                $(noWorkerMessage).show(100);
            }
        }
    }

    /*
    * SECTION: Update reservation information.
    * */

    var updateForm = document.getElementById("reservation-info-section");
    var updateButton = document.getElementById("update-button");

    updateButton.onclick = function() {
        var check = true;
        var element = null;
        $('.required').each(function(i,obj) {
            if (obj.value == "" || obj.value == null) {
                check = false;
                $(obj).addClass("red-outline");
                if(element == null) {
                    element = obj;
                }
            } else {
                $(obj).removeClass("red-outline");
            }
        })
        if(element != null) {
            $('html, body').animate({
                scrollTop: $(element).offset().top - 200
            }, 1000);
        }
        if(check) {
            var passengers = getPassengersNames();
            var annotations = getAnnotations();
            submitUpdate(passengers, annotations)
        }
    }

    function submitUpdate(passengers, annotations) {
        var inputInitials = document.createElement("input");
        inputInitials.setAttribute("type","hidden");
        inputInitials.setAttribute("name","airline");
        inputInitials.setAttribute("value",airline.val());
        var inputFlightNumber = document.createElement("input");
        inputFlightNumber.setAttribute("type","hidden");
        inputFlightNumber.setAttribute("name","flight");
        inputFlightNumber.setAttribute("value",flights.val());
        var inputAnnotations = document.createElement("input");
        inputAnnotations.setAttribute("type","hidden");
        inputAnnotations.setAttribute("name","annotations");
        inputAnnotations.setAttribute("value",annotations);
        var inputPassengers = document.createElement("input");
        inputPassengers.setAttribute("type","hidden");
        inputPassengers.setAttribute("name","passengers");
        inputPassengers.setAttribute("value",passengers);
        var scheduleInput = document.createElement("input");
        scheduleInput.setAttribute("type","hidden");
        scheduleInput.setAttribute("name","arrivalTime");
        scheduleInput.setAttribute("value",schedule.val());
        updateForm.appendChild(scheduleInput);
        updateForm.appendChild(inputInitials);
        updateForm.appendChild(inputFlightNumber);
        updateForm.appendChild(inputAnnotations);
        updateForm.appendChild(inputPassengers);
        updateForm.action = "/admin/reservas/modificar";
        updateForm.submit();
    }

    function getPassengersNames() {
        var passengersString = "";
        $('.passenger-row').each(function(i,obj) {
            if(i == 0) {
                if(($(obj).find(".passenger-first-name").val() != "") && ($(obj).find(".passenger-last-name").val() != "")
                    && ($(obj).find(".passenger-last-name").val() != null) && ($(obj).find(".passenger-first-name").val() != "")) {
                    passengersString += $(obj).find(".passenger-first-name").val().replace(",","").replace(":","");
                    passengersString += ":"+$(obj).find(".passenger-last-name").val().replace(",","").replace(":","");
                }
            } else {
                passengersString += ",";
                passengersString += $(obj).find(".passenger-first-name").val().replace(",","").replace(":","");
                passengersString += ":"+$(obj).find(".passenger-last-name").val().replace(",","").replace(":","");
            }
        });
        return passengersString;
    }

    function getAnnotations() {
        var annotations = "";
        $('.tag').each(function(i,obj) {
            if(i == 0) {
                annotations += $(obj).text();
            } else {
                annotations += ","+$(obj).text();
            }
        })
        return annotations;
    }

    /*
    * SECTION: Airline's flights select.
    * */

    arrivalDate.on("change",function() {
        changeFlights();
    })

    airline.on("change",function() {
        changeFlights();
    });

    flights.on("change", function() {
        schedule.val('Cargando');
        schedule.addClass('loading');
        $.ajax({
            url: '/admin/reservas/estado-horario',
            type: 'GET',
            data: {
                initials: airline.val(),
                flight: flights.val()
            },
            success: function (results) {
                if(results) {
                    $('#schedule-background').addClass('back');
                    $('#schedule-background').removeClass('back-disabled');
                    $('#summer-schedule').text("Horario de verano");
                } else {
                    $('#schedule-background').removeClass('back');
                    $('#schedule-background').addClass('back-disabled');
                    $('#summer-schedule').text("Horario normal");
                }
                $.ajax({
                    url: '/admin/reservas/horario',
                    type: 'GET',
                    data: {
                        arrivalDate: arrivalDate.val(),
                        initials: airline.val(),
                        flight: flights.val()
                    }, success: function(results) {
                        if($(results).length != 0) {
                            schedule.val(results.arrivalHour);
                            schedule.removeClass('loading');
                        } else {
                            schedule.val('');
                            schedule.removeClass('loading');
                        }
                    }, error: function () {
                        schedule.val('');
                        schedule.removeClass('loading');
                        swal({
                            title: "Opps, algo paso.",
                            text: "No se pudo cargar la hora de arrivo. Intente de nuevo.",
                            type: 'error'
                        });
                    }
                })
            }, error: function() {
                $('#schedule-background').removeClass('back');
                $('#schedule-background').addClass('back-disabled');
                schedule.val('');
                swal({
                    title: "Opps, algo paso.",
                    text: "No se pudo cargar la hora de arrivo. Intente de nuevo.",
                    type: 'error'
                });
            }
        })
    })

    function changeFlights() {
        flights.find("option")
            .remove()
            .end()
            .append('<option selected="selected" value="">Cargando</option>');
        flights.addClass("loading");
        flights.removeAttr('disabled');
        schedule.val('');
        $.ajax({
            url: '/admin/reservas/buscar-vuelos',
            type: 'GET',
            data: {
                arrivalDate: arrivalDate.val(),
                airlineSelected: airline.val()
            },
            success: function (results) {
                flights.find("option")
                    .remove();
                if(results.length != 0){
                    $(results).each(function (i, obj) {
                        var newOption = $('<option value=' + obj.flightNumber + '>' + obj.flightNumber + '</option>')
                        flights.append(newOption);
                        if (i == 0) {
                            schedule.val(obj.arrivalHour);
                            $.ajax({
                                url: '/admin/reservas/estado-horario',
                                type: 'GET',
                                data: {
                                    initials: obj.initials,
                                    flight: obj.flightNumber
                                },
                                success: function (results) {
                                    if(results) {
                                        $('#schedule-background').addClass('back');
                                        $('#schedule-background').removeClass('back-disabled');
                                        $('#summer-schedule').text("Horario de verano");
                                    } else {
                                        $('#schedule-background').removeClass('back');
                                        $('#schedule-background').addClass('back-disabled');
                                        $('#summer-schedule').text("Horario normal");
                                    }
                                }, error: function() {
                                    $('#schedule-background').removeClass('back');
                                    $('#schedule-background').addClass('back-disabled');
                                    schedule.val('');
                                    swal({
                                        title: "Opps, algo paso.",
                                        text: "No se pudo cargar la hora de arrivo. Intente de nuevo.",
                                        type: 'error'
                                    });
                                }
                            })
                        }
                    })
                    flights.removeClass("loading");
                } else {
                    flights.find("option")
                        .remove()
                        .end()
                        .append('<option disabled="disabled" selected="selected" value="">No hay vuelos disponibles.</option>');
                    flights.removeClass("loading").end();
                    flights.attr('disabled','disabled');
                }
            },
            error: function () {
                swal({
                    title: "Opps, algo paso.",
                    text: "No se pudo cargar los vuelos. Intente de nuevo",
                    type: 'error'
                });
                flights.find("option")
                    .remove()
                    .end()
                    .append('<option selected="selected" value="">No hay vuelos disponibles.</option>');
                flights.attr('disabled','disabled');
                flights.removeClass("loading");
            }
        })
    }

    /*
     * SECTION: Favorite tags.
     * */

    $(document).on("keypress", "#reservation-info-section", function(event) {
        return event.keyCode != 13;
    });
    for (let textarea of document.querySelectorAll('form textarea')) {
        tagsInput(textarea);
    }

    function updateTags(){
        $(".favorite-tags").empty();
        var a = $("#id-partner").val();
        $.ajax({
            url: 'obtenerfavoritos',
            type: 'GET',
            data: {
                partId:a
            },
            success:function (data) {
                for ( let i = 0; i < data.length;i++){
                    let element = "<span class=\"fav-tag\" data-tag=\""+ data[i] +"\">"+ data[i] +"</span>"
                    $(".favorite-tags").append(element);
                }
                $(".fav-tag").click(function () {
                    var span = $(".tag[data-tag='" + $(this).text() + "']");
                    if (span.length == 0){
                        $(".tags-input input").before("<span class=\"tag\" data-tag=\""+ $(this).text() +"\">"+ $(this).text() +"</span>");
                    }
                });
            },
            error:function () {
                console.log('Error occured');
            }
        })
    }

    /*
    * SECTION: Extra passengers.
    * */

    $(".btn_add_passenger").click(function () {
        var numElement = $("#passengers-container div").length + 1;
        var newElement = "<div class=\"passenger-row compound full-size\">\n" +
            "\n" +
            "                                                <label class=\"control-label form-label passenger-index\">"+numElement+"</label>\n" +
            "                                                <input class=\"field form-control field-passenger-name margin-top passenger-first-name\" type=\"text\" placeholder=\"Nombre\" id=\"passengername1\"/>\n" +
            "                                                <input class=\"field form-control field-passenger-name margin-top passenger-last-name\" type=\"text\" placeholder=\"Apellidos\" id=\"passengerlast1\"/>\n" +
            "                                                <button type='button' class='btn-remove-passenger'>\n" +
            "                                                    <svg width=\"2%\" height = \"2em\" version=\"1.1\" id=\"remove1\" xmlns=\"http://www.w3.org/2000/svg\" xmlns:xlink=\"http://www.w3.org/1999/xlink\" x=\"0px\" y=\"0px\" viewBox=\"0 0 50 50\" style=\"enable-background:new 0 0 50 50;\" xml:space=\"preserve\">\n" +
            "                                                    <circle style=\"fill:#D75A4A;\" cx=\"25\" cy=\"25\" r=\"25\"/>\n" +
            "                                                    <polyline style=\"fill:none;stroke:#FFFFFF;stroke-width:2;stroke-linecap:round;stroke-miterlimit:10;\" points=\"16,34 25,25 34,16\"/>\n" +
            "                                                    <polyline style=\"fill:none;stroke:#FFFFFF;stroke-width:2;stroke-linecap:round;stroke-miterlimit:10;\" points=\"16,16 25,25 34,34\"/>\n" +
            "                                                </svg>\n" +
            "                                                </button>\n" +
            "                                            </div>";
        $("#passengers-container").append(newElement);
        $(".btn-remove-passenger").click(function () {
            $(this).parent().remove();
            $("#passengers-container div").each(function (i) {
                $(this).children("label").text(i+1);
            });
            if ($("#passengers-container div").length == 0){
                $(".btn_add_passenger").empty();
                var svg = "<svg version=\"1.1\" id=\"Capa_1\" xmlns=\"http://www.w3.org/2000/svg\" xmlns:xlink=\"http://www.w3.org/1999/xlink\" x=\"0px\" y=\"0px\" height=\"3em\" \n" +
                    "\t viewBox=\"0 0 50 50\" style=\"enable-background:new 0 0 50 50;\" xml:space=\"preserve\">\n" +
                    "<circle style=\"fill:#43B05C;\" cx=\"25\" cy=\"25\" r=\"25\"/>\n" +
                    "<line style=\"fill:none;stroke:#FFFFFF;stroke-width:2;stroke-linecap:round;stroke-linejoin:round;stroke-miterlimit:10;\" x1=\"25\" y1=\"13\" x2=\"25\" y2=\"38\"/>\n" +
                    "<line style=\"fill:none;stroke:#FFFFFF;stroke-width:2;stroke-linecap:round;stroke-linejoin:round;stroke-miterlimit:10;\" x1=\"37.5\" y1=\"25\" x2=\"12.5\" y2=\"25\"/>\n" +
                    "</svg>\n" +
                    "<label style=\"margin-left:0.5em;margin-bottom:0.7em;font-size:small\">Añadir pasajero</label>";
                $(".btn_add_passenger").append(svg);
            }else{
                $(".btn_add_passenger").empty();
                var svg = "<svg version=\"1.1\" id=\"Capa_1\" xmlns=\"http://www.w3.org/2000/svg\" xmlns:xlink=\"http://www.w3.org/1999/xlink\" x=\"0px\" y=\"0px\" height=\"2em\" viewBox=\"0 0 792 306\" enable-background=\"new 0 0 792 306\" xml:space=\"preserve\">\n" +
                    "                                                <path fill=\"#43B05C\" d=\"M702,130c0,158.5-137.001,168-306,168S90,284.5,90,130C90,90.472,86.5,0,0,0s792,0,792,0S702,0,702,130z\"/>\n" +
                    "                                                <line fill=\"none\" stroke=\"#FFFFFF\" stroke-width=\"16\" stroke-linecap=\"round\" stroke-linejoin=\"round\" stroke-miterlimit=\"10\" x1=\"396\" y1=\"78.796\" x2=\"396\" y2=\"229.204\"/>\n" +
                    "                                                <line fill=\"none\" stroke=\"#FFFFFF\" stroke-width=\"16\" stroke-linecap=\"round\" stroke-linejoin=\"round\" stroke-miterlimit=\"10\" x1=\"471.204\" y1=\"150.992\" x2=\"320.796\" y2=\"150.992\"/>\n" +
                    "                                            </svg>";
                $(".btn_add_passenger").append(svg);
            }

        });
        $(".btn_add_passenger").empty();
        var svg = "<svg version=\"1.1\" id=\"Capa_1\" xmlns=\"http://www.w3.org/2000/svg\" xmlns:xlink=\"http://www.w3.org/1999/xlink\" x=\"0px\" y=\"0px\" height=\"2em\" viewBox=\"0 0 792 306\" enable-background=\"new 0 0 792 306\" xml:space=\"preserve\">\n" +
            "                                                <path fill=\"#43B05C\" d=\"M702,130c0,158.5-137.001,168-306,168S90,284.5,90,130C90,90.472,86.5,0,0,0s792,0,792,0S702,0,702,130z\"/>\n" +
            "                                                <line fill=\"none\" stroke=\"#FFFFFF\" stroke-width=\"16\" stroke-linecap=\"round\" stroke-linejoin=\"round\" stroke-miterlimit=\"10\" x1=\"396\" y1=\"78.796\" x2=\"396\" y2=\"229.204\"/>\n" +
            "                                                <line fill=\"none\" stroke=\"#FFFFFF\" stroke-width=\"16\" stroke-linecap=\"round\" stroke-linejoin=\"round\" stroke-miterlimit=\"10\" x1=\"471.204\" y1=\"150.992\" x2=\"320.796\" y2=\"150.992\"/>\n" +
            "                                            </svg>";
        $(".btn_add_passenger").append(svg);

        //Ultimo numero de los pasajeros
    });

    $(".btn-remove-passenger").click(function () {
        $(this).parent().remove();
        $("#passengers-container div").each(function (i) {
            $(this).children("label").text(i+1);
        });
        if ($("#passengers-container div").length == 0){
            $(".btn_add_passenger").empty();
            var svg = "<svg version=\"1.1\" id=\"Capa_1\" xmlns=\"http://www.w3.org/2000/svg\" xmlns:xlink=\"http://www.w3.org/1999/xlink\" x=\"0px\" y=\"0px\" height=\"3em\" \n" +
                "\t viewBox=\"0 0 50 50\" style=\"enable-background:new 0 0 50 50;\" xml:space=\"preserve\">\n" +
                "<circle style=\"fill:#43B05C;\" cx=\"25\" cy=\"25\" r=\"25\"/>\n" +
                "<line style=\"fill:none;stroke:#FFFFFF;stroke-width:2;stroke-linecap:round;stroke-linejoin:round;stroke-miterlimit:10;\" x1=\"25\" y1=\"13\" x2=\"25\" y2=\"38\"/>\n" +
                "<line style=\"fill:none;stroke:#FFFFFF;stroke-width:2;stroke-linecap:round;stroke-linejoin:round;stroke-miterlimit:10;\" x1=\"37.5\" y1=\"25\" x2=\"12.5\" y2=\"25\"/>\n" +
                "</svg>\n" +
                "<label style=\"margin-left:0.5em;margin-bottom:0.7em;font-size:small\">Añadir pasajero</label>";
            $(".btn_add_passenger").append(svg);
        }else{
            $(".btn_add_passenger").empty();
            var svg = "<svg version=\"1.1\" id=\"Capa_1\" xmlns=\"http://www.w3.org/2000/svg\" xmlns:xlink=\"http://www.w3.org/1999/xlink\" x=\"0px\" y=\"0px\" height=\"2em\" viewBox=\"0 0 792 306\" enable-background=\"new 0 0 792 306\" xml:space=\"preserve\">\n" +
                "                                                <path fill=\"#43B05C\" d=\"M702,130c0,158.5-137.001,168-306,168S90,284.5,90,130C90,90.472,86.5,0,0,0s792,0,792,0S702,0,702,130z\"/>\n" +
                "                                                <line fill=\"none\" stroke=\"#FFFFFF\" stroke-width=\"16\" stroke-linecap=\"round\" stroke-linejoin=\"round\" stroke-miterlimit=\"10\" x1=\"396\" y1=\"78.796\" x2=\"396\" y2=\"229.204\"/>\n" +
                "                                                <line fill=\"none\" stroke=\"#FFFFFF\" stroke-width=\"16\" stroke-linecap=\"round\" stroke-linejoin=\"round\" stroke-miterlimit=\"10\" x1=\"471.204\" y1=\"150.992\" x2=\"320.796\" y2=\"150.992\"/>\n" +
                "                                            </svg>";
            $(".btn_add_passenger").append(svg);
        }

    });

    updateTags();


});