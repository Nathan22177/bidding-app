let name;
require(['jquery'], function ($) {
    function startPvpGame() {
        if (name) {
            window.location.replace('/start_new_game_vs_another_player/' + name);
        } else {
            name = window.prompt('Enter your pvp name.');
            window.location.replace('/start_new_game_vs_another_player/' + name);
        }
    }

    $("#newGamePvp").click(function () {
        startPvpGame();
    });


    $(document).ready(function () {
        name = window.prompt('Enter your name.');
    });
});