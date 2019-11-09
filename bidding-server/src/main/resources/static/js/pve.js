require(['jquery'], function ($) {
    function resolveRoundsCountdown(data) {
        let color;
        if (data.roundsLeft > 2) {
            color = 'green';
        } else {
            color = 'red';
        }
        $("#roundsLeft").css('color', color).text(data.roundsLeft);
    }

    function checkIfGameHasEnded(data) {
        if (!data.status.isActive) {
            switch (data.status) {
                case 'BLUE_WON':
                    window.alert('VICTORY! \n' +
                        '\nAcquired ' + data.acquiredAmount +
                        'QUs out of ' + [[${blue.conditions.winnableQuantity}]] + '\n' +
                        'Money left: ' + data.balance + '\n' +
                        '\nOpponent lost with ' + data.opponentAcquiredAmount + ' QUs and ' +
                        data.opponentBalance + ' money.' +
                        '\n\nYou will now be redirected to the menu.');
                    window.location.replace('/');
                    break;
                case 'RED_WON':
                    window.alert('DEFEAT! \n' +
                        '\nAcquired ' + data.acquiredAmount +
                        'QUs out of ' + [[${blue.conditions.winnableQuantity}]] + '\n' +
                        'Money left: ' + data.balance + '\n' +
                        '\nOpponent won with ' + data.opponentAcquiredAmount + ' QUs and ' +
                        data.opponentBalance + ' money.' +
                        '\n\nYou will now be redirected to the menu.');
                    window.location.replace('/');
                    break;
                case 'DRAW':
                    window.alert('DRAW! \n' +
                        '\nBoth acquired ' + data.acquiredAmount +
                        'QUs out of ' + [[${blue.conditions.winnableQuantity}]] + '\n' +
                        'Both left with ' + data.balnce + ' of money.' +
                        '\nYou will now be redirected to the menu.');
                    window.location.replace('/');
                    break;
                case 'ENDED_PREMATURELY':
                    window.alert('Something went wrong. \n\nYou will now be redirected to the menu.');
                    window.location.replace('/');
                    break;
                case 'MATCHMAKING':
                    window.alert('Waiting for another side.');
                    break;
            }
        }
    }

    function placeBid() {
        let bid = parseInt($("#bid").val(), 10);
        let balance = parseInt($("#balance").text(), 10);
        if (bid > balance) {
            window.alert("You can't bet more money than you possess");
            upperLimit = balance;
            $("#bid").val(0);
            return;
        }
        if (bid < 0) {
            window.alert("Bid must be positive number");
            $("#bid").val(0);
            return;
        }
        let mapping = '/vs_bot/' + [[${gameId}]] + '/' + bid;
        $.get(mapping, function (data) {
            if (data) {
                $("#balance").text(data.balance);
                $("#acquiredAmount").text(data.acquiredAmount);
                if (data.biddingRound) {
                    let lastOwn = data.biddingRound.ownBid;
                    let lastOpponents = data.biddingRound.opponentBid;
                    let entry = "<p>Your bid: " + lastOwn + "&nbsp; Opponents bid: " + lastOpponents + "</p>";
                    $("#history").append(entry);
                }
                $("#bid").val(0);
                resolveRoundsCountdown(data);
                checkIfGameHasEnded(data);
            }
        });
    }

    $("#bidBtn").click(function () {
        checkIfGameHasEnded([[${state}]]);
        placeBid();
    });

    $(document).ready(function () {
        let state = [[${state}]];
        checkIfGameHasEnded(state);
        $("#bid").val(0);
        $(".mask").inputmask('numeric', {rightAlign: false, min: 0, max: upperLimit});
        resolveRoundsCountdown(state);
    });
});