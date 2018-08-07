$(document).ready(function () {
        var gameId = getParameterByName('game')
        var name = getParameterByName('user')
        wait_on_game(gameId, name)
    });

function wait_on_game(gameId, name) {


    $.ajax({
        type: "POST",
        contentType: "application/json",
        url: "/api/game",
        data: JSON.stringify(gameId),
        dataType: 'json',
        cache: false,
        timeout: 600000,
        success: function (data) {
            window.location = '/ajax?user=' + name+'&game=' +gameId+'&round=1';

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

function getParameterByName(name, url) {
    if (!url) url = window.location.href;
    name = name.replace(/[\[\]]/g, "\\$&");
    var regex = new RegExp("[?&]" + name + "(=([^&#]*)|&|#|$)"),
        results = regex.exec(url);
    if (!results) return null;
    if (!results[2]) return '';
    return decodeURIComponent(results[2].replace(/\+/g, " "));
}