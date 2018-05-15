jQuery(document).ready(function () {
    $("#country").on({
        change:function () {
            var region = $("#country").val();
            if(region != ""){
                $.ajax({
                    url: "obtener-horarios-verano",
                    type: 'POST',
                    crossDomain: true,
                    data: {"region": region},
                    success:function (data) {
                        if (data.length == 2) {
                            $("#initialSummerDay").val(data[0]);
                            $("#lastSummerDay").val(data[1]);
                        } else {
                            console.log("ERROR: " + data);
                        }
                    },
                    error:function (data) {
                        console.log("ERROR: " + data);
                    }
                })
            }
        }
    });
});