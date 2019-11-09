socket = new WebSocket("ws://" + location.hostname + ":" + location.port + "/pvp/" + state.gameId + "/" + state.ownName);

require(['jquery'], function ($) {
    socket.onmessage = function (message) {
        let data = JSON.parse(message.data);
        state = data.state;
        switch (data.type) {
            case 'PLAYER_JOINED' :
                startGame(data.state);
                break;
            case 'BID' :
                updateGameInfo(data.state);
                break;
            case 'PLAYER_LEFT':
                resolveSuddenEnd();
                break;
            case 'WAITING_FOR_OPPONENT' :
                waitingForOpponent();
                break;
            default :
                window.alert('Unknown type found: ' + data.type);
        }
    };

    function startGame(state) {
        $('#waiting').hide();
        $('#opponentName').text(state.opponentsName);
        $('#interface').show();
    }

    function resolveSuddenEnd() {
        window.alert('Opponent left the game. Walkover! \n\nYou will now be redirected to the menu.');
        window.location.replace('/');
    }

    function waitingForOpponent() {
        $('#interface').hide();
        $('#waiting').show();
    }

    function updateGameInfo(data) {
        if (data.active) {
            switch (data.status) {
                case 'WAITING_FOR_BIDS' :
                    updateAndEnableInterface(state);
                    break;
                case 'WAITING_FOR_BLUE' :
                    if (side.toString() === 'RED')
                        disableInterface();
                    break;
                case 'WAITING_FOR_RED' :
                    if (side.toString() === 'BLUE')
                        disableInterface();
                    break;
                default:
                    window.alert('Something went wrong.1231231212 \n\nYou will now be redirected to the menu. Status: ' + data.status);
                    window.location.replace('/');
                    break;
            }
        }
    }

    function updateAndEnableInterface(state) {
        if (state) {
            $("#balance").text(state.balance);
            $("#acquiredAmount").text(state.acquiredAmount);
            $("#bid").val('');
            if (state.biddingRound) {
                let lastOwn = state.biddingRound.ownBid;
                let lastOpponents = state.biddingRound.opponentBid;
                let entry = "<p>Your bid: " + lastOwn + "&nbsp; Opponents bid: " + lastOpponents + "</p>";
                $("#history").append(entry);
            }
            $("#bid").val(0);
            resolveRoundsCountdown(state);
            checkIfGameHasEnded(state);
            $('#waiting').hide();
            $('#opponentName').text(state.opponentsName);
            $('#interface').show();
        }
    }

    function resolveRoundsCountdown(data) {
        let color;
        if (data.roundsLeft > 2) {
            color = 'green';
        } else {
            color = 'red';
        }
        $("#roundsLeft").css('color', color).text(data.roundsLeft);
    }

    function resolveWin(data) {
        window.alert('VICTORY! \n' +
            '\nAcquired ' + data.acquiredAmount +
            'QUs out of ' + winnableQU + '\n' +
            'Money left: ' + data.balance + '\n' +
            '\nOpponent lost with ' + data.opponentAcquiredAmount + ' QUs and ' +
            data.opponentBalance + ' money.' +
            '\n\nYou will now be redirected to the menu.');
        window.location.replace('/');
    }

    function resolveDefeat(data) {
        window.alert('DEFEAT! \n' +
            '\nAcquired ' + data.acquiredAmount +
            'QUs out of ' + winnableQU + '\n' +
            'Money left: ' + data.balance + '\n' +
            '\nOpponent won with ' + data.opponentAcquiredAmount + ' QUs and ' +
            data.opponentBalance + ' money.' +
            '\n\nYou will now be redirected to the menu.');
        window.location.replace('/');
    }

    function resolveDraw() {
        window.alert('DRAW! \n' +
            '\nBoth acquired ' + data.acquiredAmount +
            'QUs out of ' + winnableQU + '\n' +
            'Both left with ' + data.balnce + ' of money.' +
            '\nYou will now be redirected to the menu.');
        window.location.replace('/');
    }

    function checkIfGameHasEnded(data) {
        state = data;
        if (!data.active) {
            switch (data.status) {
                case 'BLUE_WON':
                    if (side.toString() === 'BLUE')
                        resolveWin(data);
                    else
                        resolveDefeat(data);
                    break;
                case 'RED_WON':
                    if (side.toString() === 'RED')
                        resolveWin(data);
                    else
                        resolveDefeat(data);
                    break;
                case 'DRAW':
                    resolveDraw(data);
                    break;
                case 'MATCHMAKING' :
                    $('#interface').hide();
                    $('#waiting').show();
                    break;
                case 'ENDED_PREMATURELY':
                default:
                    window.alert('Something went wrong.asdasasd \n\nYou will now be redirected to the menu. Status:' + data.status);
                    window.location.replace('/');
                    break;
            }
        }
    }

    function placeBid() {

        let bid = parseInt($("#bid").val(), 10);
        $("#bid").val(0);
        let balance = parseInt($("#balance").text(), 10);
        if (bid > balance) {
            window.alert("You can't bet more money than you possess");
            return;
        }
        if (bid < 0) {
            window.alert("Bid must be positive number");
            return;
        }
        let message = {};
        message.gameId = state.gameId;
        message.bid = bid;
        message.side = state.side;
        socket.send(message);
    }

    $("#bidBtn").click(function () {
        checkIfGameHasEnded($.get('/updateGameState/' + state.gameId + '/' + side));
        placeBid();
    });

    $(document).ready(function () {
        checkIfGameHasEnded($.get('/updateGameState/' + state.gameId + '/' + side));
        $("#bid").val(0);
        $(".mask").inputmask('numeric', {rightAlign: false, min: 0, max: upperLimit});
        resolveRoundsCountdown(state);
    });
});