$(document).ready(() => {
    startResultsLoading();
    $.ajax({
        url: "/get-robocode-results",
        type: "POST",
        async: true,
        success: function(results) {
            stopResultsLoading();
            renderResultsOverview(results);
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
    $("#results-overview").css("display", "none");
}


function stopResultsLoading() {
    $(".results-loading").html("");
    $("#results-overview").css("display", "inherit");
}

function renderResultsOverview(data) {
    //Total Number of Robocode Battles
    const noOfBattles = $(".noOfBattles .value");
    noOfBattles.html(data[.length);

    //Total Number of Robots
    const noOfRobots = $(".noOfRobots .value");
    f
}