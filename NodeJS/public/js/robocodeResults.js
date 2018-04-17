$(document).ready(() => {
    startResultsLoading();
    $.ajax({
        url: "/get-robocode-results",
        type: "POST",
        async: true,
        success: function(results) {
            stopResultsLoading();
            renderResultsOverview(results);
            renderSimpleLinearRegression(results);
            console.log(results);
        },
        error: function(err) {
            console.log(err);
        }
    });
});

function startResultsLoading() {
    let el = $(".results-loading");
    el.html("<i class='fa fa-lg fa-fw fa-3x fa-pulse fa-spinner'></i><p>Loading Robocode Results...</p>");
    el.css("display", "inline");
    $("#results-overview").css("visibility", "hidden");
}


function stopResultsLoading() {
    $(".results-loading").html("").css("display", "none");
    $("#results-overview").css("visibility", "visible");
}

function renderResultsOverview(data) {
    //Total Number of Robocode Battles
    const noOfBattles = $(".noOfBattles .value");
    console.log(data);
    noOfBattles.html(data.length);

    //Total Number of Robots
    const noOfRobots = $(".noOfRobots .value");
    noOfRobots.html(data.length * 3);

    //Total No of Turns
    const noOfTurns = $(".noOfTurns .value");
    noOfTurns.html(calculateTotalNoOfTurns(data));

    //Total Battle Time
    const battleTime = $(".battleTime .value");
    battleTime.html(calculateTotalBattleTime(data));

}

function calculateTotalNoOfTurns(data) {
    let turns = 0;
    for (let i = 0; i < data.length; i++) {
        turns += data[i].no_of_turns;
    }
    return turns;
}

function calculateTotalBattleTime(data) {
    let milliseconds = 0;
    for (let i = 0; i < data.length; i++) {
        milliseconds += data[i].real_time;
    }

    let seconds = milliseconds / 1000;
    console.log(seconds);
    let minutes = ~~(seconds / 60);
    let remainingSeconds = ~~(seconds % 60);

    return minutes + "m " + remainingSeconds + "s";
}