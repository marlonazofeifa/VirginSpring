
jQuery(document).ready(
    function($) {

        $(".btn_add_passenger").click(function () {
            var numElement = $("#passengers-container div").length + 1;
            var newElement = "<div class=\"field form-control-wide passenger-row\">\n" +
                "\n" +
                "                                                <label class=\"control-label form-label passengernumber\">"+numElement+"</label>\n" +
                "                                                <input class=\"field form-control field-passenger-name\" type=\"text\" placeholder=\"Nombre\" id=\"passengername1\"/>\n" +
                "                                                <input class=\"field form-control field-passenger-name\" type=\"text\" placeholder=\"Apellidos\" id=\"passengerlast1\"/>\n" +
                "                                                <button type='button' class='btn-remove-passenger'>\n" +
                "                                                    <svg width=\"1em\" height = \"2em\" version=\"1.1\" id=\"remove1\" xmlns=\"http://www.w3.org/2000/svg\" xmlns:xlink=\"http://www.w3.org/1999/xlink\" x=\"0px\" y=\"0px\" viewBox=\"0 0 50 50\" style=\"enable-background:new 0 0 50 50;\" xml:space=\"preserve\">\n" +
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
                        //this.children("label").text(this.value);
                        $(this).children("label").text(i+1);
                });
                if ($("#passengers-container div").length == 0){
                    $(".btn_add_passenger").empty();
                    var svg = "<svg version=\"1.1\" id=\"Capa_1\" xmlns=\"http://www.w3.org/2000/svg\" xmlns:xlink=\"http://www.w3.org/1999/xlink\" x=\"0px\" y=\"0px\" height=\"1.75em\" \n" +
                        "\t viewBox=\"0 0 50 50\" style=\"enable-background:new 0 0 50 50;\" xml:space=\"preserve\">\n" +
                        "<circle style=\"fill:#43B05C;\" cx=\"25\" cy=\"25\" r=\"25\"/>\n" +
                        "<line style=\"fill:none;stroke:#FFFFFF;stroke-width:2;stroke-linecap:round;stroke-linejoin:round;stroke-miterlimit:10;\" x1=\"25\" y1=\"13\" x2=\"25\" y2=\"38\"/>\n" +
                        "<line style=\"fill:none;stroke:#FFFFFF;stroke-width:2;stroke-linecap:round;stroke-linejoin:round;stroke-miterlimit:10;\" x1=\"37.5\" y1=\"25\" x2=\"12.5\" y2=\"25\"/>\n" +
                        "</svg>\n" +
                        "<label style=\"margin-left:0.5em;font-size:small\">Añadir pasajero</label>";
                    $(".btn_add_passenger").append(svg);
                }else{
                    $(".btn_add_passenger").empty();
                    var svg = "<svg version=\"1.1\" id=\"Capa_1\" xmlns=\"http://www.w3.org/2000/svg\" xmlns:xlink=\"http://www.w3.org/1999/xlink\" x=\"0px\" y=\"0px\" height=\"1.25em\" viewBox=\"0 0 792 306\" enable-background=\"new 0 0 792 306\" xml:space=\"preserve\">\n" +
                        "                                                <path fill=\"#43B05C\" d=\"M702,130c0,158.5-137.001,168-306,168S90,284.5,90,130C90,90.472,86.5,0,0,0s792,0,792,0S702,0,702,130z\"/>\n" +
                        "                                                <line fill=\"none\" stroke=\"#FFFFFF\" stroke-width=\"16\" stroke-linecap=\"round\" stroke-linejoin=\"round\" stroke-miterlimit=\"10\" x1=\"396\" y1=\"78.796\" x2=\"396\" y2=\"229.204\"/>\n" +
                        "                                                <line fill=\"none\" stroke=\"#FFFFFF\" stroke-width=\"16\" stroke-linecap=\"round\" stroke-linejoin=\"round\" stroke-miterlimit=\"10\" x1=\"471.204\" y1=\"150.992\" x2=\"320.796\" y2=\"150.992\"/>\n" +
                        "                                            </svg>";
                    $(".btn_add_passenger").append(svg);
                }

            });
            $(".btn_add_passenger").empty();
            var svg = "<svg version=\"1.1\" id=\"Capa_1\" xmlns=\"http://www.w3.org/2000/svg\" xmlns:xlink=\"http://www.w3.org/1999/xlink\" x=\"0px\" y=\"0px\" height=\"1.25em\" viewBox=\"0 0 792 306\" enable-background=\"new 0 0 792 306\" xml:space=\"preserve\">\n" +
                "                                                <path fill=\"#43B05C\" d=\"M702,130c0,158.5-137.001,168-306,168S90,284.5,90,130C90,90.472,86.5,0,0,0s792,0,792,0S702,0,702,130z\"/>\n" +
                "                                                <line fill=\"none\" stroke=\"#FFFFFF\" stroke-width=\"16\" stroke-linecap=\"round\" stroke-linejoin=\"round\" stroke-miterlimit=\"10\" x1=\"396\" y1=\"78.796\" x2=\"396\" y2=\"229.204\"/>\n" +
                "                                                <line fill=\"none\" stroke=\"#FFFFFF\" stroke-width=\"16\" stroke-linecap=\"round\" stroke-linejoin=\"round\" stroke-miterlimit=\"10\" x1=\"471.204\" y1=\"150.992\" x2=\"320.796\" y2=\"150.992\"/>\n" +
                "                                            </svg>";
            $(".btn_add_passenger").append(svg);

            //Ultimo numero de los pasajeros
        });

        //TODO: Habilitar campo de aerolínea únicamente cuando la fecha está seleccionada. Deshabilitar cuando la fecha es inválida.

        $("#airline-selector,#arrival-date").on({
            click: function () {
                var arrivalDate = $('#arrivalDate').val();
                var airline = $('#airline-selector').val();

                $.ajax({
                    url: '/partner/nuevareservacion/getFlights',
                    type: 'GET',
                    data: {
                        arrival_date: arrivalDate,
                        airline_selected: airline
                    },
                    success: function (data) {
                        $('#flight-selector').empty();
                        if (data.length != 0){
                            for (var i = 0; i < data.length; i++) {
                                var newOption = $('<option value=' + data[i].arrivalHour + ' summer-schedule=' + data[i].summerSchedule +' >' + data[i].flightNumber + '</option>');
                                $('#flight-selector').append(newOption);
                            }
                            $('#flight-selector').attr('disabled',false);
                            $('#flight-arrival-time').attr('placeholder', $('#flight-selector').find("option:selected").attr("value"));

                            if ( $('#flight-selector').find("option:selected").attr('summer-schedule') == "true"){
                                $('.back').css("background","hsla(34, 100%, 50%, 1)");
                                $('#summer-schedule').text("Horario de verano");
                            }else{
                                $('.back').css("background","grey");
                                $('#summer-schedule').text("Horario normal");
                            }

                        }else {
                            $('#flight-selector').attr('disabled',true);
                            $('#flight-selector').append('<option>No hay vuelos disponibles</option>');
                            $('#flight-arrival-time').attr('placeholder',"--:--");
                            $('.back').css("background","grey");
                            $('#summer-schedule').text("Horario normal")
                        }

                    },
                    error: function () {
                        console.log('Error occured');

                    }
                })
            },
            change: function () {

                $('#flight-arrival-time').attr('placeholder', $('#flight-selector').find("option:selected").attr("value"));
                if ($('#flight-selector').find("option:selected").attr('summer-schedule') == "true"){
                    $('.back').css("background","hsla(34, 100%, 50%, 1)");
                    $('#summer-schedule').text("Horario de verano")
                }else{
                    $('.back').css("background","grey");
                    $('#summer-schedule').text("Horario normal")
                }
            }
        });

        $("#flight-selector option").filter(function() {
            return $(this).val() != $("#flight-selector").val();
        }).attr('selected', true);

        $("#flight-selector").on("change", function() {

            $("#flight-arrival-time").attr('placeholder',$('#flight-selector').find("option:selected").attr("value"));
            if ($('#flight-selector').find("option:selected").attr('summer-schedule') == "true"){
                $('.back').css("background","hsla(34, 100%, 50%, 1)");
                $('#summer-schedule').text("Horario de verano")
            }else{
                $('.back').css("background","grey");
                $('#summer-schedule').text("Horario normal")
            }
        });


        
        function hideConfirmationWindow(emptySpaces) {//Si esta en true limpia todos los campos cuando se cierra
            $("#confirmation-window").remove();
            $(".confirmation-background").remove();
            $("#top-menu").css("filter","");
            $(".column-left").css("filter","");
            $(".column-right").css("filter","");
            $("footer").css("filter","");
            $(this).off("click");

            if (emptySpaces == true){
                $("#nameRepresent").val('');
                $("#lastname").val('');
                $("#arrivalDate").val('');
                $("#total-pax").val('0');
                $(".tags-input").val('');
            }
            
        }

        function showConfirmationWindow(){

            var firstName = $("#nameRepresent").val();
            var lastName = $("#lastname").val();
            var arrivalDate = $("#arrivalDate").val();
            var airlineSelected =  Array.from($("#airline-selector"))[0].selectedIndex;
            var airlineInitials = airlineSelected == "-1"?"":Array.from($("#airline-selector").children())[airlineSelected].value;
            var flightSelected =  Array.from($("#flight-selector"))[0].selectedIndex;
            var arrivalTime = flightSelected=="-1"?"":Array.from($("#flight-selector").children())[flightSelected].value;
            var flightNumber = flightSelected=="-1"?"":Array.from($("#flight-selector").children())[flightSelected].text;
            var totalPax = $("#total-pax").val();
            var tags = Array.from($(".tags-input").children());
            var annotations = "";
            for (let i = 0; i < tags.length - 1; i++){
                annotations += tags[i].innerText;
                if (i < tags.length - 2)
                    annotations += ",";
            }
            var passengersNamesStringReal = "";
            var passengersNamesString = "";
            var passengersNames = Array.from($(".field").filter(".form-control").filter(".field-passenger-name"));
            for (let i = 0; i < passengersNames.length; i++){
                if (i%2 == 0){
                    passengersNamesStringReal += passengersNames[i].value + ":";
                    passengersNamesString += passengersNames[i].value + " ";
                }
                else {
                    passengersNamesStringReal += passengersNames[i].value + ",";
                    passengersNamesString += passengersNames[i].value + ", ";
                }

            }
            passengersNamesStringReal = passengersNamesStringReal.substring(0,passengersNamesStringReal.length-1); //Esta la variableque se envía
            passengersNamesString = passengersNamesString.substring(0,passengersNamesString.length-2);

            // Limpiamos todos los campos
            $(".form-control").css("box-shadow", "inset 0 1px 1px rgba(0,0,0,.075)");
            $(".form-control").css("webkit-box-shadow", "inset 0 1px 1px rgba(0,0,0,.075)");

            if (firstName == "" ||
                firstName == " " ||
                firstName == "  " ||
                lastName == "" ||
                lastName == " " ||
                lastName == "  " ||
                arrivalDate == "" ||
                arrivalDate == " " ||
                arrivalDate == "  " ||
                airlineInitials == "" ||
                airlineInitials == " " ||
                airlineInitials == "  " ||
                flightNumber == "" ||
                flightNumber == " " ||
                flightNumber == "  " ||
                totalPax == "0" ||
                totalPax == "" ||
                totalPax == " " ||
                totalPax == "  "
            ){
                var invalidFields = "Invalid fields: ";
                if (firstName == "" ||
                    firstName == " " ||
                    firstName == "  ")
                {
                    $("#nameRepresent").css("box-shadow", "inset 0 1px 1px rgb(255, 0, 0)");
                    $("#nameRepresent").css("webkit-box-shadow", "inset 0 1px 1px rgb(255, 0, 0)");
                    invalidFields += "firstName,"
                }
                if(lastName == "" ||
                    lastName == " " ||
                    lastName == "  ")
                {

                    $("#lastname").css("box-shadow", "inset 0 1px 1px rgb(255, 0, 0)");
                    $("#lastname").css("webkit-box-shadow", "inset 0 1px 1px rgb(255, 0, 0)");
                    invalidFields += "lastName,"
                }
                if (arrivalDate == "" ||
                    arrivalDate == " " ||
                    arrivalDate == "  ")
                {

                    $("#arrivalDate").css("box-shadow", "inset 0 1px 1px rgb(255, 0, 0)");
                    $("#arrivalDate").css("webkit-box-shadow", "inset 0 1px 1px rgb(255, 0, 0)");
                    invalidFields += "arrivalDate,"
                }
                if (airlineInitials == "" ||
                    airlineInitials == " " ||
                    airlineInitials == "  ")
                {

                    $("#airline-selector").css("box-shadow", "inset 0 1px 1px rgb(255, 0, 0)");
                    $("#airline-selector").css("webkit-box-shadow", "inset 0 1px 1px rgb(255, 0, 0)");
                    invalidFields += "airlineInitials,"
                }
                if (flightNumber == "" ||
                    flightNumber == " " ||
                    flightNumber == "  ")
                {

                    $("#flight-selector").css("box-shadow", "inset 0 1px 1px rgb(255, 0, 0)");
                    $("#flight-selector").css("webkit-box-shadow", "inset 0 1px 1px rgb(255, 0, 0)");
                    invalidFields += "flightNumber,"
                }
                if (totalPax == "0" ||
                    totalPax == "" ||
                    totalPax == " " ||
                    totalPax == "  ")
                {

                    $("#total-pax").css("box-shadow", "inset 0 1px 1px rgb(255, 0, 0)");
                    $("#total-pax").css("webkit-box-shadow", "inset 0 1px 1px rgb(255, 0, 0)");
                    invalidFields += "totalPax,"
                }
                console.log(invalidFields);
            } else {
                $("body").prepend("<section id='confirmation-curtain'></section>");
                $("#confirmation-curtain").prepend("<section class='confirmation-background'>" +

                    "</section>");

                var summerScheduleString = Array.from($("#flight-selector").children())[flightSelected].attributes["summer-schedule"].value == "true"? "Sí":"No";

                $("#confirmation-curtain").prepend(
                    "<section id='confirmation-window'>" +
                    "<div id='confirmation-header'><img src='../images/logo-canatur-color.png' width='auto' height='40em' style='float: left; '/><div><label style='line-height: 40px; margin-left: 1em;color:#03a952;font-weight: 600;'>Confirmación de reservación</label></div><div style='flex:auto; display:flex; flex-direction:column;float: right;'><h style='text-align: right;'>Fecha y hora actual:</h><label style='text-align: right;' class='reservation-document-field'>4 de noviembre 2017</label></div></div>" +
                    "<div id='confirmation-container'>" +
                    "   <div class='confirmation-container-column'></div>" +
                    "       <div class='confirmation-container-row'><h>Pasajero representante:</h><label class = 'reservation-document-field'>"+ firstName + " " +lastName+"</label><h>Total de pasajeros: </h><label class = 'reservation-document-field'>" + totalPax + "</label></div> " +
                    "       <div class='confirmation-container-row'><h>Aerolínea:</h><label class = 'reservation-document-field'>"+ Array.from($("#airline-selector").children())[airlineSelected].text +"</label><h>Vuelo:</h><label class = 'reservation-document-field'>"+ flightNumber + "</label><h>Fecha de llegada:</h><label class = 'reservation-document-field' style='font-size:xx-large; font-weight: 200;'>" + arrivalDate + "</label><h>Hora:</h><label class = 'reservation-document-field' style='font-size:xx-large;font-weight: 200; text-align: center'>" + arrivalTime + "</label></div> " +
                    "       <div class='confirmation-container-row'  style='text-align: right;font-weight:100;'><h style='flex:auto;'>Horario de verano:</h><label class = 'reservation-document-field' style='flex:none;'>" + summerScheduleString + "</label></div> " +
                    "       <div class='confirmation-container-row'><h>Información de pasajeros:</h><label class = 'reservation-document-field'>"+ passengersNamesString +"</label></div> " +
                    "       <div class='confirmation-container-row'><h>Comentarios:</h><label class = 'reservation-document-field'>" + annotations + "</label></div> " +
                    "   </div>" +
                    "<div id='confirmation-footer'>" +
                    "   <button class='btn' id='btn-cancelar-reservacion'>Cancelar</button>" +
                    "   <button class='btn btn-default' id='btn-salvar-reservacion' style='margin-top: 0; margin-bottom: 0;padding-left: 2em;padding-right: 2em;'>Proceder</button>" +
                    "</div>" +
                    "</section>"
                );
                $("#top-menu").css("filter","blur(5px)");
                $(".column-left").css("filter","blur(5px)");
                $(".column-right").css("filter","blur(5px)");
                $("footer").css("filter","blur(5px)");

                $(".confirmation-background").click(function () {
                        hideConfirmationWindow(false);
                    }
                );

                $("#btn-cancelar-reservacion").click(function () {
                    hideConfirmationWindow(false);
                });

                $("#btn-salvar-reservacion").click(function () {

                    //Creación del JSON para el envío
                    var reservation = "{";
                    reservation += "\"nameRepresent\":" + "\"" + firstName + "\"\,\n";
                    reservation += "\"lastname\":" + "\"" + lastName + "\"\,\n";
                    reservation += "\"arrivalDate\":" + "\"" + arrivalDate + "\"\,\n";
                    reservation += "\"initials_fk\":" + "\"" + airlineInitials + "\"\,\n";
                    reservation += "\"flightNumber\":" + "\"" + flightNumber + "\"\,\n";
                    reservation += "\"total-pax\":" + "\"" + totalPax + "\"\,\n";
                    reservation += "\"arrival-time\":" + "\"" + arrivalTime + "\"\,\n";
                    reservation += "\"annotations\":" + "\"" + annotations + "~" + passengersNamesStringReal + "\"\n}";

                    $("#confirmation-window").empty();
                    $("#confirmation-window").css("width","");
                    $("#confirmation-window").css("height","");
                    $("#confirmation-window").css("min-height","");
                    $("#confirmation-window").css("min-width","");
                    $("#confirmation-window").append("<div style='position: absolute; top:50%;left:50%;transform: translate(-50%,-50%);'>" +
                        "<svg xmlns:svg=\"http://www.w3.org/2000/svg\" xmlns=\"http://www.w3.org/2000/svg\" xmlns:xlink=\"http://www.w3.org/1999/xlink\" version=\"1.0\" width=\"64px\" height=\"64px\" viewBox=\"0 0 128 128\" xml:space=\"preserve\"><rect x=\"0\" y=\"0\" width=\"100%\" height=\"100%\" fill=\"#FFFFFF\" /><path fill=\"#43b05c\" fill-opacity=\"1\" id=\"ball1\" class=\"cls-1\" d=\"M67.712,108.82a10.121,10.121,0,1,1-1.26,14.258A10.121,10.121,0,0,1,67.712,108.82Z\"><animateTransform attributeName=\"transform\" type=\"rotate\" values=\"0 64 64;4 64 64;0 64 64;0 64 64;0 64 64;0 64 64;0 64 64;0 64 64;0 64 64;0 64 64;0 64 64;0 64 64;0 64 64;\" dur=\"1800ms\" repeatCount=\"indefinite\"></animateTransform></path><path fill=\"#43b05c\" fill-opacity=\"1\" id=\"ball2\" class=\"cls-1\" d=\"M51.864,106.715a10.125,10.125,0,1,1-8.031,11.855A10.125,10.125,0,0,1,51.864,106.715Z\"><animateTransform attributeName=\"transform\" type=\"rotate\" values=\"0 64 64;10 64 64;0 64 64;0 64 64;0 64 64;0 64 64;0 64 64;0 64 64;0 64 64;0 64 64;0 64 64;0 64 64;0 64 64;\" dur=\"1800ms\" repeatCount=\"indefinite\"></animateTransform></path><path fill=\"#43b05c\" fill-opacity=\"1\" id=\"ball3\" class=\"cls-1\" d=\"M33.649,97.646a10.121,10.121,0,1,1-11.872,8A10.121,10.121,0,0,1,33.649,97.646Z\"><animateTransform attributeName=\"transform\" type=\"rotate\" values=\"0 64 64;20 64 64;40 64 64;65 64 64;85 64 64;100 64 64;120 64 64;140 64 64;160 64 64;185 64 64;215 64 64;255 64 64;300 64 64;\" dur=\"1800ms\" repeatCount=\"indefinite\"></animateTransform></path></svg>" +
                        "</div>");
                    /* //Antiguo extractor de datos de los pasajeros
                    var passangersInfomation = "{\n\"passengers\":[\n";
                    var passangers = Array.from($("#passengers-container").children());
                    for (let i = 0; i < passangers.length; i++){
                        let input = passangers[i].children;
                        passangersInfomation += "\""+ input[1].value + " " + input[2].value + "\"";
                        if (i < passangers.length-1)
                            passangersInfomation += ",\n";
                    }
                    passangersInfomation += "]\n}"
                    */

                    $.ajax({
                        url: 'nuevareservacion',
                        type: 'POST',
                        data: JSON.parse(reservation),
                        success: function (data) {
                            if (data == "Succesfull"){
                                $("#confirmation-window").empty();
                                $("#confirmation-window").css("width","fit-content");
                                $("#confirmation-window").css("height","fit-content");
                                $("#confirmation-window").css("min-width","fit-content");
                                $("#confirmation-window").css("min-height","fit-content");
                                $("#confirmation-window").css("padding","2em");
                                $("#confirmation-window").append("<div style='position: relative;display: flex; flex-direction: column; text-align: center;'>" +
                                    "<svg version=\"1.1\" id=\"Layer_1\" xmlns=\"http://www.w3.org/2000/svg\" xmlns:xlink=\"http://www.w3.org/1999/xlink\" x=\"0px\" y=\"0px\"\n" +
                                    "\t viewBox=\"0 0 426.667 426.667\" style=\"enable-background:new 0 0 426.667 426.667;\" xml:space=\"preserve\" width='10em' height='10em'>\n" +
                                    "<path style=\"fill:#43b05c;\" d=\"M213.333,0C95.518,0,0,95.514,0,213.333s95.518,213.333,213.333,213.333\n" +
                                    "\tc117.828,0,213.333-95.514,213.333-213.333S331.157,0,213.333,0z M174.199,322.918l-93.935-93.931l31.309-31.309l62.626,62.622\n" +
                                    "\tl140.894-140.898l31.309,31.309L174.199,322.918z\"/>\n" +
                                    "</svg>" +
                                    "<label style='color:#43b05c; margin-top: 1em;'>¡Reservación exitosa!</label>"+
                                    "</div>");
                                $("#nameRepresent").val('');
                                $("#lastname").val('');
                                $("#arrivalDate").val('');
                                $("#total-pax").val('0');
                                $(".tags-input").val('');
                                setTimeout(function () {
                                    hideConfirmationWindow(false);
                                },5000);
                            } else {

                                $("#confirmation-window").empty();
                                $("#confirmation-window").css("width","fit-content");
                                $("#confirmation-window").css("height","fit-content");
                                $("#confirmation-window").css("min-width","fit-content");
                                $("#confirmation-window").css("min-height","fit-content");
                                $("#confirmation-window").css("padding","2em");
                                $("#confirmation-window").append("<div style='position: relative; display: flex;flex-direction: column;text-align: center;'>" +
                                    "<svg xmlns=\"http://www.w3.org/2000/svg\" xmlns:xlink=\"http://www.w3.org/1999/xlink\" version=\"1.1\" id=\"Capa_1\" x=\"0px\" y=\"0px\" viewBox=\"0 0 483.537 483.537\" style=\"enable-background:new 0 0 483.537 483.537; width: 100%\" xml:space=\"preserve\" width=\"10em\" height=\"10em\">\n" +
                                    "<g>\n" +
                                    "\t<g>\n" +
                                    "\t\t<g>\n" +
                                    "\t\t\t<path d=\"M479.963,425.047L269.051,29.854c-5.259-9.88-15.565-16.081-26.782-16.081h-0.03     c-11.217,0-21.492,6.171-26.782,16.051L3.603,425.016c-5.046,9.485-4.773,20.854,0.699,29.974     c5.502,9.15,15.413,14.774,26.083,14.774H453.12c10.701,0,20.58-5.594,26.083-14.774     C484.705,445.84,484.979,434.471,479.963,425.047z M242.239,408.965c-16.781,0-30.399-13.619-30.399-30.399     c0-16.78,13.619-30.399,30.399-30.399c16.75,0,30.399,13.619,30.399,30.399C272.638,395.346,259.02,408.965,242.239,408.965z      M272.669,287.854c0,16.811-13.649,30.399-30.399,30.399c-16.781,0-30.399-13.589-30.399-30.399V166.256     c0-16.781,13.619-30.399,30.399-30.399c16.75,0,30.399,13.619,30.399,30.399V287.854z\" fill=\"#FFDA44\"/>\n" +
                                    "\t\t</g>\n" +
                                    "\t</g>\n" +
                                    "\t\n" +
                                    "</g>\n" +
                                    "</svg>" +
                                    "<label style='color:#FFDA44;margin-top: 1em;'>Reservación fallida</label>"+
                                    "<label style='color:grey;font-weight: 100;'>Por favor, comuníquese con soporte técnico</label>"+
                                    "</div>");
                                console.log(data);

                            }


                        },
                        error: function (ts) {
                            $("#confirmation-window").empty();
                            $("#confirmation-window").css("width","fit-content");
                            $("#confirmation-window").css("height","fit-content");
                            $("#confirmation-window").css("min-width","fit-content");
                            $("#confirmation-window").css("min-height","fit-content");
                            $("#confirmation-window").append("<div style='position: relative; display: flex;flex-direction: column;text-align: center;'>" +
                                "<svg xmlns=\"http://www.w3.org/2000/svg\" xmlns:xlink=\"http://www.w3.org/1999/xlink\" version=\"1.1\" id=\"Capa_1\" x=\"0px\" y=\"0px\" viewBox=\"0 0 483.537 483.537\" style=\"enable-background:new 0 0 483.537 483.537; width: 100%\" xml:space=\"preserve\" width=\"10em\" height=\"10em\">\n" +
                                "<g>\n" +
                                "\t<g>\n" +
                                "\t\t<g>\n" +
                                "\t\t\t<path d=\"M479.963,425.047L269.051,29.854c-5.259-9.88-15.565-16.081-26.782-16.081h-0.03     c-11.217,0-21.492,6.171-26.782,16.051L3.603,425.016c-5.046,9.485-4.773,20.854,0.699,29.974     c5.502,9.15,15.413,14.774,26.083,14.774H453.12c10.701,0,20.58-5.594,26.083-14.774     C484.705,445.84,484.979,434.471,479.963,425.047z M242.239,408.965c-16.781,0-30.399-13.619-30.399-30.399     c0-16.78,13.619-30.399,30.399-30.399c16.75,0,30.399,13.619,30.399,30.399C272.638,395.346,259.02,408.965,242.239,408.965z      M272.669,287.854c0,16.811-13.649,30.399-30.399,30.399c-16.781,0-30.399-13.589-30.399-30.399V166.256     c0-16.781,13.619-30.399,30.399-30.399c16.75,0,30.399,13.619,30.399,30.399V287.854z\" fill=\"#FFDA44\"/>\n" +
                                "\t\t</g>\n" +
                                "\t</g>\n" +
                                "\t\n" +
                                "</g>\n" +
                                "</svg>" +
                                "<label style='color:#FFDA44;margin-top: 1em;'>Reservación fallida</label>"+
                                "<label style='color:grey;font-weight: 100;'>Por favor, comuníquese con soporte técnico</label>"+
                                "</div>");
                            console.log(ts.responseText);

                        }
                    })

                });
            }

            //Extracción de todos los datos
            /*
            var username = "";
            $.ajax({
                url: "/partner/obtener-username",
                type: "GET",
                success: function (data) {



                },
                error:function (data) {
                    hideConfirmationWindow(false);
                }
            });
            */
        }

        $("#btn-salvar").click(function () {
            showConfirmationWindow();
        });

        function updateTags(){
            $(".favorite-tags").empty();
            var a = $('#id-partner').find("option:selected").val();
            $.ajax({
                url: 'obtenerfavoritos',
                type: 'GET',
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

        $("#id-partner").change(function () {
            updateTags();
        });

        updateTags();

    })