$(document).ready(function () {
    $("#search-form").submit(function (event) {

        event.preventDefault();
        var name = $("#userId").val()
        register_user(name)
    });
    });

function register_user(name) {


    $.ajax({
        type: "POST",
        contentType: "application/json",
        url: "/api/register",
        data: JSON.stringify(name),
        dataType: 'json',
        cache: false,
        timeout: 600000,
        success: function (data) {
            if(data['registered'])
            {
                var gameId = data['gameId']
                //window.location = '/ajax?user=' + name+'&game=' +gameId+'&round=1';
                window.location = '/wait?user=' + name+'&game=' +gameId;
                //can change the round thing to something else later
            }
            var json = "<h4>Ajax Response</h4><pre>"
                + JSON.stringify(data, null, 4) + "</pre>";
            $('#feedback').html(json);

            console.log("SUCCESS : ", data);
            $("#btn-search").prop("disabled", false);

        },
        error: function (e) {

            var json = "<h4>Ajax Response</h4><pre>"
                + e.responseText + "</pre>";
            $('#feedback').html(json);

            console.log("ERROR : ", e);
            $("#btn-search").prop("disabled", false);

        }
    });

}