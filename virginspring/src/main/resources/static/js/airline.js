jQuery(document).ready(function () {

    $("#summer-schedule-manager").click(function () {
        $("#container-page").attr("src","horarios/modificar");
    });

    var airlineSelected = false;
    var airlineObjectSelected;
    var tempSelectedInitials;

    $("#flight-selector").hide();
    $(".airline-element-lookflights").click(function () {
        //VARIABLES
        airlineSelected = true;
        airlineObjectSelected = $(this);

        var airlineName = airlineObjectSelected.siblings().text();

        var airlineInitials = airlineObjectSelected.siblings().attr("value");


        /*Lo que se muestra*/
        $("#airlines-selector").css("width","5%");
        $("#content-viewer").css("width","80%");
        $("#flight-selector").show();
        $("#container-page").attr("src","vuelo-defecto/");

        $.ajax({
            url: '/admin/obtener-vuelos',
            type: 'GET',
            data: {
                airlineInitials: airlineInitials,
            },
            success: function (data) {
                //Muestra los vuelos
                $('#flights-section').empty();
                if (data.length != 0){
                    for (var i = 0; i < data.length; i++) {
                        //var newOption = $('<option value=' + data[i].arrivalHour + '>' + data[i].flightNumber + '</option>');
                        var state;
                        if (data[i].state)
                            state = " checked ";
                        else
                            state = " ";
                        var newOption = $('<div class="flight-element">\n' +
                            '                    <div class="flight-element-name">'+ data[i].flightNumber +'</div>\n' +
                            '                    <div class="flight-element-available"> <input type="checkbox" onclick="return false;" value="'+ data[i].flightNumber + '"' + state +'/></div>\n' +
                            '                </div>');
                        $('#flights-section').append(newOption);


                        $('#flights-section').css("display", "");
                        $('#flights-section').css("align-items", "");
                    }
                    $(".flight-element-name").click(function () {
                        var flightNumber = $(this).text();
                        var initials = tempSelectedInitials;
                        $("#container-page").attr("src","vuelo-seleccionado?idVuelo="+ flightNumber +"&initials="+airlineInitials);
                    });

                }else {
                    //Centra los elementos horizontalmente
                    $('#flights-section').css("display", "flex");
                    $('#flights-section').css("align-items", "center");

                    //Muestra texto
                    $('#flights-section').append("<div style='text-align: center; width: 100%'> No hay vuelos disponibles </div>");
                }
            },
            error: function () {
                console.log('Error occured');

            }
        });

        /*Lo que cambia*/
        $("#airlines-header").empty();
        //Agrega un sombrero a la columna de aerolineas
        $("#airlines-header").append("<svg version=\"1.1\" color='white' width='2em' height='2em' id=\"Capa_1\" xmlns=\"http://www.w3.org/2000/svg\" xmlns:xlink=\"http://www.w3.org/1999/xlink\" x=\"0px\" y=\"0px\"\n" +
            "\t viewBox=\"0 0 611.999 611.999\" style=\"enable-background:new 0 0 611.999 611.999;\" xml:space=\"preserve\">\n" +
            "<g>\n" +
            "\t<path fill='#FFF' d=\"M611.347,198.507c-1.614-5.139-5.992-8.922-11.31-9.772l-208.551-33.428c-56.38-9.038-114.644-9.033-171.018,0.003\n" +
            "\t\tL11.964,188.733c-5.29,0.847-9.652,4.596-11.287,9.701c-1.634,5.102-0.259,10.69,3.559,14.45l89.285,88.02v72.422\n" +
            "\t\tc0,0.043,0.011,0.08,0.011,0.122c0.006,0.81,0.097,1.62,0.244,2.424c0.028,0.165,0.051,0.333,0.088,0.495\n" +
            "\t\tc0.142,0.642,0.315,1.282,0.549,1.91c0.04,0.108,0.111,0.296,0.216,0.551c0.009,0.02,0.017,0.04,0.026,0.06\n" +
            "\t\tc3.379,8.243,39.4,84.581,211.347,84.581c171.95,0,207.968-76.338,211.35-84.581c0.009-0.02,0.017-0.04,0.026-0.06\n" +
            "\t\tc0.105-0.256,0.176-0.441,0.216-0.551c0.233-0.625,0.406-1.265,0.549-1.91c0.037-0.162,0.057-0.33,0.088-0.495\n" +
            "\t\tc0.148-0.804,0.236-1.614,0.244-2.424c0-0.043,0.011-0.08,0.011-0.122v-74.272l89.177-86.065\n" +
            "\t\tC611.529,209.251,612.959,203.64,611.347,198.507z M195.71,204.914h63.466c4.965,0,9.326,2.555,11.867,6.415\n" +
            "\t\tc7.362-12.111,20.644-20.246,35.824-20.246c14.663,0,27.576,7.58,35.074,19.012c2.606-3.141,6.489-5.181,10.892-5.181h63.463\n" +
            "\t\tc7.85,0,14.211,6.361,14.211,14.211c0,7.85-6.361,14.211-14.211,14.211h-17.58c0.009,0.196,0.06,0.381,0.06,0.577\n" +
            "\t\tc0,7.85-6.361,14.211-14.211,14.211H371.44c-0.622,7.273-6.654,13.001-14.089,13.001h-4.519c-4.4,0-8.285-2.044-10.892-5.182\n" +
            "\t\tc-7.498,11.435-20.411,19.012-35.074,19.012c-15.181,0-28.46-8.135-35.824-20.246c-2.541,3.86-6.898,6.415-11.867,6.415h-4.522\n" +
            "\t\tc-7.435,0-13.467-5.727-14.089-13.001h-13.123c-7.85,0-14.211-6.361-14.211-14.211c0-0.199,0.051-0.381,0.06-0.577h-17.58\n" +
            "\t\tc-7.85,0-14.211-6.361-14.211-14.211C181.498,211.275,187.86,204.914,195.71,204.914z M490.058,359.114H121.944v-50.925h368.114\n" +
            "\t\tV359.114z M293.348,233.021c0-7.452,6.063-13.515,13.515-13.515s13.515,6.063,13.515,13.515s-6.063,13.515-13.515,13.515\n" +
            "\t\tS293.348,240.474,293.348,233.021z\"/>\n" +
            "</g>\n" +
            "\n" +
            "</svg>");

        //Coloca funcionalidad a la barra vertical para volver atras
        $("#airlines-selector").css("cursor", "pointer");
        $("#airlines-selector").on({click:function () {
            if(!airlineSelected){
                airlineSelected=true;
                $("#airlines-selector").off('click');

                $("#airlines-header").empty();
                $("#airlines-header").append("Aerolíneas");

                $("#airlines-selector").css("width","15%");
                $("#content-viewer").css("width","85%");
                $("#flight-selector").hide();
                $("#container-page").attr("src","vuelo-defecto/");
                $("#airlines-selector").css("cursor", "default");

                $("#airlines-header").css("display","");
                $("#airlines-header").css("justify-content","");
                $("#airlines-header").css("align-items","");
                $("#airlines-filter-input").show();


                $("#airlines-section").css("background-color","white");
                $("#airlines-section").css("box-shadow","");

                $(".airline-element-name").off('mouseenter mouseleave');

                $(".airline-element-name").hover(function () {
                    $(this).css("background-color","grey");
                    $(this).css("color","white");
                },function () {
                    $(this).css("background-color","transparent");
                    $(this).css("color","grey");
                });
                $(".airline-element").css("background","white");
                $(".airline-element-name").css("color","grey");
                $(".airline-element-name").css("padding-left", "1em");
                $(".airline-element-name").css("flex", "auto");
                $(".airline-element").css("display","");
                $(".airline-element").css("justify-content","");
                $(".airline-element").css("align-items","");
                $(".airline-element").css("border-bottom","1px lightgrey solid");
                $(".airline-element-lookflights").css("color","grey");
                $(".airline-element-lookflights").show();

                $(".airline-element-selected").click(function () {
                    airlineSelected = true;
                    airlineObjectSelected = $(this);
                    var airlineName = airlineObjectSelected.siblings().text();
                    var airlineInitials = airlineObjectSelected.siblings().attr("value");
                    $("#container-page").attr("src","aerolinea-selecionada?nameAirline="+airlineName);
                });

                $(".airline-element-name").click(function () {

                    airlineSelected = true;
                    airlineObjectSelected = $(this);
                    var airlineInitials = airlineObjectSelected.attr("value");
                    tempSelectedInitials=airlineInitials;
                    $("#container-page").attr("src","aerolinea-selecionada?airlineInitials="+airlineInitials);
                });

                //Cambio de nombre
                var name = airlineObjectSelected.siblings().text();
                var initials = airlineObjectSelected.siblings().attr("value");
                airlineObjectSelected.siblings().text(initials);
                airlineObjectSelected.siblings().attr("value",name)

                $("#container-page").attr("src","aerolinea-defecto/");


            }
            airlineSelected=false;
        }});


        //Coloca el sombrero bien
        $("#airlines-header").css("display","flex");
        $("#airlines-header").css("justify-content","center");
        $("#airlines-header").css("align-items","center");
        $("#airlines-filter-input").hide();
        $("#airlines-section").css("background-color","lightgrey");
        $("#airlines-section").css("box-shadow","inset 0px 0px 10px 0px rgba(0,0,0,0.75)");

        //Cambio de nombre
        var name = $(this).siblings().text();
        var initials = $(this).siblings().attr("value");
        $(this).siblings().text(initials);
        $(this).siblings().attr("value",name);
        $(".airline-element-name").css("padding-left", "");
        $(".airline-element").css("background","transparent")
        $(".airline-element").css("border-bottom","0px")
        $(".airline-element-name").css("flex", "");
        $(".airline-element-name").off('mouseenter mouseleave click');
        $(".airline-element-name").hover(function () {
            $(this).css("background-color","trasparent");
            $(this).css("color",$(this).css("color"));
        },function () {
            $(this).css("background-color","transparent");
            $(this).css("color",$(this).css("color"));
        });
        $(".airline-element-lookflights").hide();

        $(this).parent().css("background","green");
       // $(this).parent().css("z-");
        $(this).siblings().css("color","white");
        $(this).parent().css("display","flex");
        $(this).parent().css("justify-content","center");
        $(this).parent().css("align-items","center");



    });

    $(".airline-element-name").off('mouseenter mouseleave');
    $(".airline-element-name").hover(function () {
        $(this).css("background-color","grey");
        $(this).css("color","white");
    },function () {
        $(this).css("background-color","transparent");
        $(this).css("color","grey");
    });


    $("#airlines-footer").click(function () {
        $("#container-page").attr("src","/admin/agregar-aerolinea/");
    });

    $("#summer-schedule-button").click(function () {
        $("#container-page").attr("src","/admin/horarios/modificar");
    });

    $("#flights-footer").click(function () {
        $("#container-page").attr("src","/admin/vuelos/agregar?initials="+ airlineObjectSelected.siblings().attr("value"));
    });

    $(".airline-element-selected").click(function () {
        airlineSelected = true;
        airlineObjectSelected = $(this);
        var airlineName = airlineObjectSelected.siblings().text();
        var airlineInitials = airlineObjectSelected.siblings().attr("value");
        $("#container-page").attr("src","aerolinea-selecionada?nameAirline="+airlineName);
    });

    $(".airline-element-name").click(function () {

        airlineSelected = true;
        airlineObjectSelected = $(this);
        var airlineInitials = airlineObjectSelected.attr("value");
        tempSelectedInitials=airlineInitials;
        $("#container-page").attr("src","aerolinea-selecionada?airlineInitials="+airlineInitials);
    });

    $(".delete-airline-button").click(function () {

        airlineSelected = true;
        airlineObjectSelected = $(this);
        airlineInitials=tempSelectedInitials;
        var airlineInitials = airlineObjectSelected.attr("value");
        $("#container-page").attr("src","aerolinea-selecionada?airlineInitials="+airlineInitials);
    });

})

function searchAirlines() {
    // Declare variables
    var input, filter, airlinesSection, airlineElements, airlineName, i;
    input = document.getElementById('airlines-filter-input');
    filter = input.value.toUpperCase();
    airlinesSection = document.getElementById("airlines-section");
    airlineElements = airlinesSection.getElementsByClassName('airline-element');

    // Loop through all list items, and hide those who don't match the search query
    for (i = 0; i < airlineElements.length; i++) {
        airlineName = airlineElements[i].getElementsByClassName("airline-element-name");
        if (airlineName[0].innerHTML.toUpperCase().indexOf(filter) > -1) {
            airlineElements[i].style.display = "";
        } else {
            airlineElements[i].style.display = "none";
        }
    }
}

function searchFlights() {
    // Declare variables
    var input, filter, flightSection, flightElements, flightName, i;
    input = document.getElementById('flights-filter-input');
    filter = input.value.toUpperCase();
    flightSection = document.getElementById("flights-section");
    flightElements = flightSection.getElementsByClassName('flight-element');

    // Loop through all list items, and hide those who don't match the search query
    for (i = 0; i < flightElements.length; i++) {
        flightName = flightElements[i].getElementsByClassName("flight-element-name");
        if (flightName[0].innerHTML.toUpperCase().indexOf(filter) > -1) {
            flightElements[i].style.display = "";
        } else {
            flightElements[i].style.display = "none";
        }
    }
}


