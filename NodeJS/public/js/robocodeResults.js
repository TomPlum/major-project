$(document).ready(() => {
    startResultsLoading();
    $.ajax({
        url: "/get-robocode-results",
        type: "POST",
        async: true,
        success: function(results) {
            //results = generatePseudoRandomData();
            stopResultsLoading();
            renderResultsOverview(results);
            renderStatisticsBreakdown(results);
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

function renderStatisticsBreakdown(data) {
    const rounds = calculateRoundsBreakdown(data);

    //1 Round
    $("#oneRounds").html(rounds[1]);

    //3 Rounds
    $("#threeRounds").html(rounds[3]);

    //5 Rounds
    $("#fiveRounds").html(rounds[5]);

    //8 Rounds
    $("#eightRounds").html(rounds[8]);

    //10 Rounds
    $("#tenRounds").html(rounds[10]);

    const turns = calculateTurnsBreakdown(data);

    //Min
    $("#turnsMin").html(turns.min);
    //Avg
    $("#turnsAvg").html(formatLargeNumber(turns.avg));
    //Max
    $("#turnsMax").html(turns.max);

    const time = calculateTimeBreakdown(data);

    //Min
    $("#timeMin").html(formatTime(time.min));
    //Avg
    $("#timeAvg").html(formatTime(time.avg));
    //Max
    $("#timeMax").html(formatTime(time.max));
}

function calculateTurnsBreakdown(data) {
    let roundsMin = 9999;
    let roundsMax = 0;
    let roundsAvg = 0;

    for (let i = 0; i < data.length; i++) {
        let rounds = data[i].no_of_turns;
        if (rounds < roundsMin) {
            roundsMin = rounds;
        }
        if (rounds > roundsMax) {
            roundsMax = rounds;
        }
        roundsAvg += rounds;
    }

    roundsAvg = roundsAvg / data.length;
    return {
        min: roundsMin,
        avg: roundsAvg.toFixed(0),
        max: roundsMax
    }
}

function calculateTimeBreakdown(data) {
    let timeMin = 9999;
    let timeMax = 0;
    let timeAvg = 0;

    for (let i = 0; i < data.length; i++) {
        let time = data[i].real_time;
        if (time < timeMin) {
            timeMin = time;
        }
        if (time > timeMax) {
            timeMax = time;
        }
        timeAvg += time;
    }

    timeAvg = timeAvg / data.length;
    
    return {
        min: timeMin,
        avg: timeAvg.toFixed(1),
        max: timeMax
    }
}

function formatTime(milliseconds) {
    let seconds = milliseconds / 1000;
    let minutes = ~~(seconds / 60);
    let hours = ~~(minutes / 60);
    let remainingMinutes = ~~(minutes % 60);
    let remainingSeconds = ~~(seconds % 60);

    return hours + "h " + remainingMinutes + "m " + remainingSeconds + "s";
}

function calculateRoundsBreakdown(data) {
    let rounds = {};
    for (let i = 0; i < data.length; i++) {
        if (!rounds.hasOwnProperty(data[i].rules.no_of_rounds)) {
            rounds[data[i].rules.no_of_rounds] = 1;
        } else {
            rounds[data[i].rules.no_of_rounds]++;
        }
    }
    return rounds;
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
    let minutes = ~~(seconds / 60);
    let hours = ~~(minutes / 60);
    let remainingMinutes = ~~(minutes % 60);
    let remainingSeconds = ~~(seconds % 60);

    return hours + "h " + remainingMinutes + "m " + remainingSeconds + "s";
}