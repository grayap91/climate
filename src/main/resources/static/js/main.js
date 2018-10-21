$(document).ready(function () {
    var userId = getParameterByName('user')
    var gameId = getParameterByName('game')
    var round = getParameterByName('round')

    if(round >=2)
    { getUserHistory(userId, gameId, round) }

    var timeout = 100000
    setTimeout(function(){
       fire_ajax_submit(userId, gameId, round, 0, 0, 0);
     }, timeout);

    var totalSeconds = 0
    var secondsLabel = document.getElementById("seconds");
    setInterval(function(){
    ++totalSeconds
    secondsLabel.innerHTML = (timeout/1000)-totalSeconds
    }, 1000);


    //200 seconds to submit bids
    get_values(userId,gameId,round)
    //insert a timeout button here maybe
    $("#search-form").submit(function (event) {

        //stop submit the form, we will post it manually.
            event.preventDefault();
            bid1 = $("#bid1").val();
            bid2 = $("#bid2").val();
            bid3 = $("#bid3").val();

        fire_ajax_submit(userId, gameId, round, bid1, bid2, bid3);

    });

});



function getUserHistory(userId, gameId, round)  {
             var histReq = {}
             histReq['userId'] = userId
             histReq['gameId'] = gameId
             histReq['round'] = round
             $.ajax({
                     type: "POST",
                     contentType: "application/json",
                     url: "/api/history",
                     data: JSON.stringify(histReq),
                     dataType: 'json',
                     cache: false,
                     timeout: 600000,
                     success: function (data) {
                               list = data['list']
                               var table = document.getElementById('firsttable')
                               for(r=1;r<round;r++)
                               {
                               var row = table.insertRow(r)
                               for(i=0;i<5;i++)
                               {
                                   row.insertCell(i)
                               }
                               hist = list[r-1]
                               console.log('data is '+hist)
                               allocation = hist['allocation']
                               price = hist['price']
                               bids = hist['bids']
                               profit = hist['profit']
                               row.cells[0].innerHTML = r
                               row.cells[1].innerHTML = allocation
                               row.cells[2].innerHTML = price.join()
                               row.cells[3].innerHTML = bids.join()
                               row.cells[4].innerHTML = profit
                               }

                    },
                     error: function (e) {
                         console.log(e)
                     }
                 });
        //really only needed from round 2 onwards
        }

function get_values(userId, gameId, round)   {
             var histReq = {}
             histReq['userId'] = userId
             histReq['gameId'] = gameId
             histReq['round'] = round
    $.ajax({
            type: "POST",
            contentType: "application/json",
            url: "/api/value",
            data: JSON.stringify(histReq),
            dataType: 'json',
            cache: false,
            timeout: 600000,
            success: function (data) {

                var index = []
                for (var i =1;i<=data['values'].length;i++)
                {
                index.push(i)
                }

                TESTER = document.getElementById('tester');
                	Plotly.plot( TESTER, [{
                	x: index,
                	y: data['values'],
                	type : 'bar'}], {
                	margin: { t: 0 } } );

                console.log("SUCCESS : ", data['values']);
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


function fire_ajax_submit(userId, gameId, round, bid1, bid2, bid3) {

    var bids = {}
    bids["bid1"] = bid1
    bids["bid2"] = bid2
    bids["bid3"] = bid3
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
            if(round < 10)
            {
                round++
                window.location = '/ajax?user=' + userId+'&game=' +gameId+'&round='+round;
            }
            else
            {
                window.location = '/finish?user=' + userId+'&game=' +gameId+'&round='+round;
            }

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