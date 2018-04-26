function calculateVariance() {

}

function getMinMaxAvg(results) {
    let min = 999999;
    let max = 0;
    let avg = 0;

    for (let i = 0; i < results.length; i++) {
        let score = results[i].results[0].score;

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

    return {
        min: min,
        avg: (avg / results.length).toFixed(1),
        max: max
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

    //Render Standard Deviation
    const sd = calculateStandardDeviation(results);
    $("#standardDeviation").html(sd.standardDeviation);
}