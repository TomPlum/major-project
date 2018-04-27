function getMinMaxAvg(results) {
    //Sort Into Ascending Order (For Quartiles)
    results.sort(function(a, b) {return a.results[0].score - b.results[0].score;});

    let min = 999999;
    let max = 0;
    let avg = 0;
    let q1, q3, q2;
    let test = [];
    for (let i = 0; i < results.length; i++) {
        let score = results[i].results[0].score;
        test.push(score);

        //Max
        if (score > max) {
            max = score;
        }

        //Avg
        avg += score;

        //Min
        if (score < min) {
            min = score;
        }
    }
    console.log(test);
    //Lower Quartile
    q1 = Math.round((1 / 4) * (results.length + 1));
    //Mean
    q2 = Math.round((1 / 2) * (results.length + 1));
    //Upper Quartile
    q3 = Math.round((3 / 4) * (results.length + 1));


    return {
        min: min,
        avg: (avg / results.length).toFixed(1),
        max: max,
        q1: results[q1].results[0].score,
        q2: results[q2].results[0].score,
        q3: results[q3].results[0].score,
        iqr: results[q3].results[0].score - results[q1].results[0].score
    }
}

function calculateStandardDeviation(results) {
    let standardDeviation;
    let mean = 0;
    let n = results.length;
    for (let i = 0; i < results.length; i++) {
        mean += results[i].results[0].score;
    }

    mean = mean / n;

    let squaredDifferences = 0;
    for (let i = 0; i < results.length; i++) {
        let score = results[i].results[0].score;
        squaredDifferences += Math.pow(score - mean, 2);
    }

    let squaredDifferencesMean = squaredDifferences / n;

    standardDeviation = Math.sqrt(squaredDifferencesMean);

    return {
        standardDeviation: standardDeviation.toFixed(1),
        squaredDifferences: squaredDifferences.toFixed(1),
        mean: mean.toFixed(1),
        squaredDifferencesMean: squaredDifferencesMean.toFixed(1)
    }
}

function renderStandardDeviationAndVariance(results) {
    calculateStandardDeviation(results);

    //Render Min, Avg & Max
    const mma = getMinMaxAvg(results);
    $("#scoreMin").html(mma.min);
    $("#scoreAvg").html(mma.avg);
    $("#scoreMax").html(mma.max);
    $("#lowerQuartile").html(mma.q1);
    $("#median").html(mma.q2);
    $("#upperQuartile").html(mma.q3);
    $("#interQuartileRange").html(mma.iqr);

    //Render Standard Deviation
    const sd = calculateStandardDeviation(results);
    $("#standardDeviation").html(sd.standardDeviation);
    $("#squaredDifferences").html(formatLargeNumber(sd.squaredDifferences));
    $("#squaredDifferencesMean").html(formatLargeNumber(sd.squaredDifferencesMean));
    $("#variance").html(formatLargeNumber(sd.squaredDifferencesMean));
}