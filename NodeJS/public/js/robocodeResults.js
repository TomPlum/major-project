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
            renderStandardDeviationAndVariance(results);
            calculateAvgRounds(results);
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
    el.css("display", "block");
    $("#results-overview").css("visibility", "hidden");
}


function stopResultsLoading() {
    $(".results-loading").html("").css("display", "none");
    $("#results-overview").css("visibility", "visible");
}

function renderResultsOverview(data) {
    //Total Number of Robocode Battles
    const noOfBattles = $(".noOfBattles .value");
    noOfBattles.html(formatLargeNumber(data.length));

    //Total Number of Rounds
    const noOfrounds = $(".noOfRounds .value");
    noOfrounds.html(formatLargeNumber(calculateTotalNumberOfRounds(data)));

    //Total Number of Robots
    const noOfRobots = $(".noOfRobots .value");
    noOfRobots.html(formatLargeNumber(calculateTotalNumberOfRobots(data)));

    //Total No of Turns
    const noOfTurns = $(".noOfTurns .value");
    noOfTurns.html(formatLargeNumber(calculateTotalNoOfTurns(data)));

    //Total Battle Time
    const battleTime = $(".battleTime .value");
    battleTime.html(calculateTotalBattleTime(data));

    //Total Score
    const totalScore = $(".totalScore .value");
    totalScore.html(formatLargeNumber(calculateTotalScore(data)));

    //Total Bullet Damage
    const totalBulletDamage = $(".totalBulletDamage .value");
    totalBulletDamage.html(formatLargeNumber(calculateTotalBulletDamage(data)));

    //Total Ram Damage
    const totalRamDamage = $(".totalRamDamage .value");
    totalRamDamage.html(formatLargeNumber(calculateTotalRamDamage(data)));
}

function calculateTotalNumberOfRobots(data) {
    let robots = 0;
    for (let i = 0; i < data.length; i++) {
        for (let j = 0; j < data[i].results.length; j++) {
            robots++;
        }
    }
    return robots;
}

function calculateTotalNumberOfRounds(data) {
    let rounds = 0;
    for (let i = 0; i < data.length; i++) {
        rounds += data[i].rules.no_of_rounds;
    }
    return rounds;
}

function calculateTotalRamDamage(data) {
    let ram = 0;
    for (let i = 0; i < data.length; i++) {
        for (let j = 0; j < data[i].results.length; j++) {
            ram += data[i].results[j].ram_damage;
        }
    }
    return ram;
}

function calculateTotalBulletDamage(data) {
    let dmg = 0;
    for (let i = 0; i < data.length; i++) {
        for (let j = 0; j < data[i].results.length; j++) {
            dmg += data[i].results[j].bullet_damage;
        }

    }
    return dmg;
}

function calculateTotalScore(data) {
    let score = 0;
    for (let i = 0; i < data.length; i++) {
        for (let j = 0; j < data[i].results.length; j++) {
            score += data[i].results[j].score;
        }
    }
    return score;
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
    let hours = ~~(minutes / 60);
    let remainingMinutes = ~~(minutes % 60);
    let remainingSeconds = ~~(seconds % 60);

    return hours + "h " + remainingMinutes + "m " + remainingSeconds + "s";
}