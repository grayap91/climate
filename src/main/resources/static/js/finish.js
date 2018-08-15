$(document).ready(function () {
    var userId = getParameterByName('user')
    var gameId = getParameterByName('game')
    var round = getParameterByName('round')

    getUserHistory(userId, gameId, round)


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
                               for(i=0;i<4;i++)
                               {
                                   row.insertCell(i)
                               }
                               hist = list[r-1]
                               console.log('data is '+hist)
                               allocation = hist['allocation']
                               price = hist['price']
                               bids = hist['bids']
                               row.cells[0].innerHTML = r
                               row.cells[1].innerHTML = allocation
                               row.cells[2].innerHTML = price.join()
                               row.cells[3].innerHTML = bids.join()
                               }

                    },
                     error: function (e) {
                         console.log(e)
                     }
                 });
        //really only needed from round 2 onwards
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