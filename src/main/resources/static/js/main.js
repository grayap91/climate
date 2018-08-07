$(document).ready(function () {
    console.log("Username from previous page ", getParameterByName('user'));
    console.log("Username from previous page ", getParameterByName('game'));

    var userId = getParameterByName('user')
    var gameId = getParameterByName('game')
    var round = getParameterByName('round')
    //insert a timeout button here maybe
    $("#search-form").submit(function (event) {

        //stop submit the form, we will post it manually.
            event.preventDefault();

        fire_ajax_submit(userId, gameId, round);

    });

});

function fire_ajax_submit(userId, gameId, round) {

    var bids = {}
    bids["bid1"] = $("#bid1").val();
    bids["bid2"] = $("#bid2").val();
    bids["bid3"] = $("#bid3").val();
    bids['userId'] = userId
    bids['gameId'] = gameId
    bids['round'] = round

    console.log("SUCCESS : ", bids["bid1"]);
    console.log("SUCCESS : ", bids["bid2"]);
    console.log("SUCCESS : ", bids["bid3"]);

    $("#btn-search").prop("disabled", true);

    $.ajax({
        type: "POST",
        contentType: "application/json",
        url: "/api/bids",
        data: JSON.stringify(bids),
        dataType: 'json',
        cache: false,
        timeout: 600000,
        success: function (data) {

            var json = "<h4>Allocated</h4><pre>"
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

function getParameterByName(name, url) {
    if (!url) url = window.location.href;
    name = name.replace(/[\[\]]/g, "\\$&");
    var regex = new RegExp("[?&]" + name + "(=([^&#]*)|&|#|$)"),
        results = regex.exec(url);
    if (!results) return null;
    if (!results[2]) return '';
    return decodeURIComponent(results[2].replace(/\+/g, " "));
}