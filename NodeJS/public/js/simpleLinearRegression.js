$(document).ready(() => {
    //Bind Radio Button Events
    $("#include-sentry").on("click", includeTwitterSentry);
    $("#exclude-sentry").on("click", excludeTwitterSentry);
    $("#include-all").on("click", includeAllRecords);

    //Bind Point Radius Slider
    $("#pointRadius").val("6").html("6px");
    $("#pointRadiusSlider input").on("input", () => {
        let radius = $("#pointRadiusSlider input").val();
        changePointRadius(radius);
        $("#pointRadius").html(radius + "px");
    });

    //Bind Number of Rounds Checkbox Events
    $("input[name='numberOfRounds']").on("change", () => {
        let checkedBoxes = $("input[name='numberOfRounds']:checked");
        let roundsToDisplay = [];
        for (let i = 0; i < checkedBoxes.length; i++) {
            roundsToDisplay.push(checkedBoxes[i].value);
        }
        includeNumberOfRounds(roundsToDisplay);
    });

    //Initialise RangeSlider Plugin
    $('input[type="range"]').rangeslider({
        polyfill : false,

        // Default CSS classes
        rangeClass: 'rangeslider',
        disabledClass: 'rangeslider--disabled',
        horizontalClass: 'rangeslider--horizontal',
        verticalClass: 'rangeslider--vertical',
        fillClass: 'rangeslider__fill',
        handleClass: 'rangeslider__handle',
    });
});

//Setting The Dimensions Of The Canvas
const margin = {top: 20, right: 20, bottom: 50, left: 60},
    svgWidth = 1000 - margin.left - margin.right,
    svgHeight = 400 - margin.top - margin.bottom;

//Setting X & Y Ranges
let x = d3.scaleLinear().range([0, svgWidth]);
let y = d3.scaleLinear().range([svgHeight, 0]);

//Define The Axes
let xAxis = d3.axisBottom().scale(x);
let yAxis = d3.axisLeft().scale(y);

let svg = d3.select('#simple-linear-regression-graph').append("svg").attr("class", "svg-element")
    .attr("viewBox", "0 0 " + (svgWidth + margin.left + margin.right) + " " + (svgHeight + margin.top + margin.bottom))
    .attr("width", "100%").attr("height", svgHeight + margin.top + margin.bottom)
    .append("g").attr("transform", "translate(" + margin.left + "," + margin.top + ")");

let scatterPlotData = [];
let rawResultData;
let dynamicScatterPlotData;

function changePointRadius(radius) {
    d3.selectAll("circle").transition().duration(500).attr("r", radius);
}

function includeNumberOfRounds(rounds) {
    //Remove Rounds That Should Not Be Included
    for (let i = 0; i < dynamicScatterPlotData.length; i++) {
        if (!rounds.includes(dynamicScatterPlotData[i].rules.rounds)) {
            dynamicScatterPlotData.splice(0, i);
        }
    }

    //Update Graph
    updateSimpleLinearRegression($("#pointRadiusSlider").val());
}


function updateSimpleLinearRegression(radius) {
    console.log("Dynamic Data Length (Inside Update): " + dynamicScatterPlotData.length);
    console.log("Radius: " + radius);
    //Re-Define Domains
    x.domain(d3.extent(dynamicScatterPlotData, function(d) {return d.x}));
    y.domain(d3.extent(dynamicScatterPlotData, function(d) {return d.y}));

    //Select All Circles, Bind New Data
    let circles = svg.selectAll(".point").data(dynamicScatterPlotData);

    //Remove Redundant Circles
    circles.exit().transition().duration(1000).attr("r", 0).remove();

    //Add New Circles
    let newCircles = circles.enter().append("circle")
        .attr("class", "point")
        .attr("cy", function(d){ return y(d.y); })
        .attr("cx", function(d){ return x(d.x); })
        .attr("r", 0) //Initial 0 (Animate To Radius Below)
        .on("mouseover", (d, i) => {
            const floating = $("#simpleLinearRegressionTooltip");

            //Bind Mouse Move Event
            $(document).bind('mousemove', function(e) {
                floating.css({
                    left: e.pageX + 15,
                    top: e.pageY + 25,
                    display: "initial"
                });
            });

            //Add HTML
            floating.html(
                "<div class='container floatingContainer'>" +
                "<div class=''>" +
                "<h1 class='battle-number'>Battle " + i + "</h1>" +
                "<hr class='floatingRule'>" +
                "</div>" +
                "<div class=''>" +
                "<h1 class='floatingData'>" + dateToString(d.date) + "</h1>" +
                "</div>" +
                "<div class=''>" +
                "<h1 class='floatingData'>" + d.turns + " Turns</h1>" +
                "</div>" +
                "<div>" +
                "<h1 class='floatingData'>" + d.score + " Score</h1>" +
                "</div>" +
                "<div>" +
                "<h1 class='floatingData'>" + d.rules.no_of_rounds + " Rounds</h1>" +
                "</div>" +
                "</div>"

            );
        })
        .on("mouseout", () => {
            const floating = $("#simpleLinearRegressionTooltip");

            $(document).unbind("mousemove");

            //Remove CSS
            floating.css("display", "none");

            //Remove HTML From Floating Div
            floating.html("");
        });

    //Animate Radius (Fade's New Circles In)
    newCircles.transition().duration(1000).attr("r", radius);

    console.log("Number of Circles: " + $(".point").length + "\n");

    //Transition Remaining Dataset Circles To New Positions
    circles.transition().duration(1000).delay(function(d, i) {
        return i / dynamicScatterPlotData.length * 500;
    }).attr("cx", function(d) { return x(d.x); }).attr("cy", function(d) { return y(d.y); });

    //Re-Calculate & Adjust Regression Line
    let lg = calcLinear(dynamicScatterPlotData, "x", "y", d3.min(dynamicScatterPlotData, function(d){ return d.x}), d3.min(dynamicScatterPlotData, function(d){ return d.y}));
    d3.select(".regression").transition().duration(1000)
        .attr("x1", x(lg.ptA.x))
        .attr("y1", y(lg.ptA.y))
        .attr("x2", x(lg.ptB.x))
        .attr("y2", y(lg.ptB.y));

    //Update Axes
    svg.select(".x-axis").transition().duration(1000).call(xAxis);
    svg.select(".y-axis").transition().duration(1000).call(yAxis);
}

function includeTwitterSentry() {
    //Copy Original Raw Data Into Dynamic Variable
    dynamicScatterPlotData = rawResultData.slice(0);

    //Remove Results With No TwitterSentry
    for (let i = 0; i < dynamicScatterPlotData.length; i++) {
        if (dynamicScatterPlotData[i].results.length !== 3) {
            dynamicScatterPlotData.splice(i, 1);
        }
    }

    //Convert Into Graph Format To Be Updated
    dynamicScatterPlotData = convertRawToGraphFormat(dynamicScatterPlotData);
    updateSimpleLinearRegression($("#pointRadiusSlider").val());
    updatePointTimeSliderMaxValue();
    updatePointTimeSliderDate();
}

function includeAllRecords() {
    //Copy Original Raw Data Into Dynamic Variable
    dynamicScatterPlotData = rawResultData.slice(0);

    //Convert Into Graph Format To Be Updated
    dynamicScatterPlotData = convertRawToGraphFormat(dynamicScatterPlotData);
    updateSimpleLinearRegression($("#pointRadiusSlider").val());
    updatePointTimeSliderMaxValue();
    updatePointTimeSliderDate();
}

function excludeTwitterSentry() {
    //Copy Original Raw Data Into Dynamic Variable
    dynamicScatterPlotData = rawResultData.slice(0);

    //Remove Results With TwitterSentry
    for (let i = 0; i < dynamicScatterPlotData.length; i++) {
        if (dynamicScatterPlotData[i].results.length === 3) {
            dynamicScatterPlotData.splice(i, 1);
        }
    }

    //Convert Into Graph Format To Be Updated
    dynamicScatterPlotData = convertRawToGraphFormat(dynamicScatterPlotData);
    updateSimpleLinearRegression($("#pointRadiusSlider").val());
    updatePointTimeSliderMaxValue();
    updatePointTimeSliderDate();
}

function convertRawToGraphFormat(data) {
    let result = [];
    for (let i = 0; i < data.length; i++) {
        result.push({
            _id: data[i]._id,
            date: data[i].date,
            score: data[i].results[0].score,
            turns: data[i].no_of_turns,
            rules: data[i].rules,
            x: parseInt(data[i].results[0].score), //First one is the winner as in desc order
            y: parseInt(data[i].no_of_turns)
        });
    }
    return result;
}

function calculateAvgRounds(data) {
    let avg = 0;
    for (let i = 0; i < data.length; i++) {
        avg += data[i].results[0].firsts + data[i].results[0].seconds + data[i].results[0].thirds;
    }
    $("#noOfRounds").html(~~(avg / data.length));
}

function dateToString(obj) {
    let date = new Date(obj);
    let YYYY = date.getFullYear();
    let MM = date.getMonth() < 10 ? "0" + date.getMonth() : date.getMonth();
    let DD = date.getDate() < 10 ? "0" + date.getDate() : date.getDate();
    return YYYY + "/" + MM + "/" + DD;
}

function timeToString(obj) {
    let date = new Date(obj);
    let HH = date.getHours() < 10 ? "0" + date.getHours() : date.getHours();
    let MM = date.getMinutes() < 10 ? "0" + date.getMinutes() : date.getMinutes();
    let SS = date.getSeconds() < 10 ? "0" + date.getSeconds() : date.getSeconds();
    return HH + ":" + MM + ":" + SS;
}

function renderSimpleLinearRegression(data) {
    //Store In Raw For Reference
    rawResultData = data.slice(0);

    //Build Data Structure
    scatterPlotData = convertRawToGraphFormat(data);

    //Store In Dynamic
    dynamicScatterPlotData = scatterPlotData.slice(0);

    //Define Domains
    x.domain(d3.extent(scatterPlotData, function(d) {return d.x}));
    y.domain(d3.extent(scatterPlotData, function(d) {return d.y}));

    //Create Regression Line
    /*
    let regressionData = create_data(scatterPlotData);
    regressionData.forEach(function(d) {
        d.x = +d.x;
        d.y = +d.y;
        d.yhat = +d.yhat;
    });
    let regressionLine = d3.line().x(function(d){return x(d.x);}).y(function(d){return y(d.yhat);});*/
    let lg = calcLinear(scatterPlotData, "x", "y", d3.min(scatterPlotData, function(d){ return d.x}), d3.min(scatterPlotData, function(d){ return d.y}));

    //Add Regression Line
    // noinspection JSSuspiciousNameCombination
    //svg.append("path").datum(regressionData).attr("d", regressionLine).attr("class", "regression");
    svg.append("line")
        .attr("class", "regression")
        .attr("x1", x(Math.abs(lg.ptA.x)))
        .attr("y1", y(Math.abs(lg.ptA.y)))
        .attr("x2", x(Math.abs(lg.ptB.x)))
        .attr("y2", y(Math.abs(lg.ptB.y)));

    //Add X-Axis
    svg.append("g")
        .attr("class", "x-axis")
        .attr("transform", "translate(0," + svgHeight + ")")
        .call(xAxis);

    //Add Y-Axis
    svg.append("g")
        .attr("class", "y-axis")
        .call(yAxis);

    //Add Scatter Points
    svg.selectAll(".point")
        .data(scatterPlotData)
        .enter().append("circle")
        .attr("class", "point")
        .attr("r", 6)
        .attr("cy", function(d){ return y(d.y); })
        .attr("cx", function(d){ return x(d.x); })
        .on("mouseover", (d, i) => {
            const floating = $("#simpleLinearRegressionTooltip");

            //Bind Mouse Move Event
            $(document).bind('mousemove', function(e) {
                floating.css({
                    left: e.pageX + 15,
                    top: e.pageY + 25,
                    display: "initial"
                });
            });

            //Add HTML
            floating.html(
                "<div class='container floatingContainer'>" +
                "<div class=''>" +
                "<h1 class='battle-number'>Battle " + i + "</h1>" +
                "<hr class='floatingRule'>" +
                "</div>" +
                "<div class=''>" +
                "<h1 class='floatingData'>" + dateToString(d.date) + "</h1>" +
                "</div>" +
                "<div class=''>" +
                "<h1 class='floatingData'>" + d.turns + " Turns</h1>" +
                "</div>" +
                "<div>" +
                "<h1 class='floatingData'>" + d.score + " Score</h1>" +
                "</div>" +
                "<div>" +
                "<h1 class='floatingData'>" + d.rules.no_of_rounds + " Rounds</h1>" +
                "</div>" +
                "</div>"

            );
        })
        .on("mouseout", () => {
            const floating = $("#simpleLinearRegressionTooltip");

            $(document).unbind("mousemove");

            //Remove CSS
            floating.css("display", "none");

            //Remove HTML From Floating Div
            floating.html("");
        });

    //Add Axis Labels
    svg.append("text").attr("x", (svgWidth / 2) - 20).attr("y", svgHeight + 45).attr("class", "axis-label").text("Score");
    svg.append("text").attr("x", 0 - (svgHeight / 2)).attr("y", 0 - margin.left - 15).attr("text-anchor", "middle")
        .attr("transform", "rotate(-90)").attr("class", "axis-label").text("Number of Turns");


    //After we've rendered the Scatter Plot. Bind the slider event as the dataset is now defined
    //Bind Point Timeline Slider
    const pointTimeLineSlider = $("#pointTimelineSlider");
    updatePointTimeSliderMaxValue();
    updatePointTimeSliderDate();
    pointTimeLineSlider.on("change", () => {
        let sliderValue = pointTimeLineSlider.val();
        console.log("Slider Value: " + sliderValue);
        console.log("Dynamic Data Length (Before): " + dynamicScatterPlotData.length);

        //Sort the dynamicScatterPlotData variable
        dynamicScatterPlotData.sort((a, b) => {return a.date - b.date;});

        //Conform To The Border Sentry Filter Selection
        const filterBorderSentry = $("input[name='filter-linear-reg']").val();
        switch(filterBorderSentry) {
            case "All":
                //Do Nothing
                break;
            case "Exclude":
                //Remove Results With TwitterSentry
                for (let i = 0; i < dynamicScatterPlotData.length; i++) {
                    if (dynamicScatterPlotData[i].results.length === 3) {
                        dynamicScatterPlotData.splice(i, 1);
                    }
                }
                break;
            case "Include":
                //Remove Results Without TwitterSentry
                for (let i = 0; i < dynamicScatterPlotData.length; i++) {
                    if (dynamicScatterPlotData[i].results.length !== 3) {
                        dynamicScatterPlotData.splice(i, 1);
                    }
                }
                break;
            default:
                alert("No Value For 'Filter Border Sentry'.");
                break;
        }

        //Slice The Array (From The Start --> Selected Slider Length)
        dynamicScatterPlotData = dynamicScatterPlotData.slice(0, sliderValue);

        //Update Scatter Plot (With Currently Selected Radius)
        updateSimpleLinearRegression($("#pointRadiusSlider input").val());

        //Copy Original Raw Data Into Dynamic Variable
        dynamicScatterPlotData = convertRawToGraphFormat(rawResultData.slice(0));
    });

    //Update Date Text (On-Input)
    pointTimeLineSlider.on("input", updatePointTimeSliderDate);
}

function updatePointTimeSliderDate() {
    let sortedData = dynamicScatterPlotData.sort((a, b) => {return a.date - b.date;}).slice(0);
    let earliestDate = sortedData[0].date;
    let sliderDate = sortedData[$("#pointTimelineSlider").val() - 1].date;
    $("#pointTimelineSliderDate").html(dateToString(earliestDate) + " " + timeToString(earliestDate) + " - " + dateToString(sliderDate) + " " + timeToString(sliderDate) + " (Displaying " + $("#pointTimelineSlider").val() + "/" + sortedData.length + " Battles)");
}

function updatePointTimeSliderMaxValue() {
    const pointTimeLineSlider = $("#pointTimelineSlider");
    pointTimeLineSlider.prop("max", dynamicScatterPlotData.length).val(dynamicScatterPlotData.length);
    pointTimeLineSlider.rangeslider('update', true);
}

function calcLinear(data, x, y, minX, minY){
    /////////
    //SLOPE//
    /////////

    // Let n = the number of data points
    let n = data.length;

    // Get just the points
    let pts = [];
    data.forEach(function(d){
        let obj = {};
        obj.x = d[x];
        obj.y = d[y];
        obj.mult = obj.x * obj.y;
        pts.push(obj);
    });
    console.log(pts);

    // Let a equal n times the summation of all x-values multiplied by their corresponding y-values
    // Let b equal the sum of all x-values times the sum of all y-values
    // Let c equal n times the sum of all squared x-values
    // Let d equal the squared sum of all x-values
    let sum = 0;
    let xSum = 0;
    let ySum = 0;
    let sumSq = 0;
    pts.forEach(function(pt){
        sum += pt.mult;
        xSum += pt.x;
        ySum += pt.y;
        sumSq += (pt.x * pt.x);
    });
    let a = sum * n;
    let b = xSum * ySum;
    let c = sumSq * n;
    let d = xSum * xSum;

    // Plug the values that you calculated for a, b, c, and d into the following equation to calculate the slope
    // slope = m = (a - b) / (c - d)
    let m = (a - b) / (c - d);

    /////////////
    //INTERCEPT//
    /////////////

    // Let e equal the sum of all y-values
    let e = ySum;

    // Let f equal the slope times the sum of all x-values
    let f = m * xSum;

    // Plug the values you have calculated for e and f into the following equation for the y-intercept
    // y-intercept = b = (e - f) / n
    b = (e - f) / n;

    // Print the equation below the chart
    let interceptOperator = (b < 0) ? "-" : "+";
    document.getElementsByClassName("equation")[0].innerHTML = "y = " + m.toFixed(2) + "x " + interceptOperator + " " + Math.abs(b.toFixed(2)); //Y = mX + B
    document.getElementsByClassName("equation")[1].innerHTML = "x = ( y - " + b.toFixed(2) + " ) / " + m.toFixed(2);

    // return an object of two points
    // each point is an object with an x and y coordinate
    console.log("m: " + m + ", minX: " + minX + ", b: " + b);
    console.log("Regression Line Coords: (" + minX + ", " + Math.abs((m * minX) + b) + "), (" + (minY - b) / m + ", " + minY + ")");
    return {
        ptA : {
            x: minX,
            y: (m * minX) + b
        },
        ptB : {
            x: (minY - b) / m,
            y: minY
        }
    }
}

function create_data(data) {
    let x = data.map(function(d){return d.x});
    let y = data.map(function(d){return d.y});
    let n = data.length;

    let xSum = 0;
    let ySum = 0;
    let xSumSquared = 0;
    let xySum = 0;
    for (let i = 0; i < x.length; i++) {
        xSum += x[i];
        ySum += y[i];
        xySum += (x[i] * y[i]);
        xSumSquared += (x[i] * x[i]);
    }
    let slope = ((n * xySum) - (xSum * ySum)) / ((n * xSumSquared) - Math.pow(xSum, 2));
    let intercept = (ySum - slope * (xSum)) / n;
    console.log("y = " + slope.toFixed(2) + "x + " + intercept.toFixed(2));

    let x_mean = 0;
    let y_mean = 0;
    let term1 = 0;
    let term2 = 0;
    // calculate mean x and y
    x_mean /= n;
    y_mean /= n;
    // calculate coefficients
    let xr = 0;
    let yr = 0;
    for (let i = 0; i < x.length; i++) {
        xr = x[i] - x_mean;
        yr = y[i] - y_mean;
        term1 += xr * yr;
        term2 += xr * xr;
    }
    let b1 = term1 / term2;
    let b0 = y_mean - (b1 * x_mean);
    // perform regression
    let yhat = [];
    // fit line using coeffs
    for (let i = 0; i < x.length; i++) {
        yhat.push(b0 + (x[i] * b1));
    }
    let data2 = [];
    for (let i = 0; i < y.length; i++) {
        data2.push({
            "yhat": yhat[i],
            "y": y[i],
            "x": x[i]
        })
    }
    console.log(data2);
    //Clear Arrays For Garbage Collection
    yhat = null;

    return (data2);
}

function generatePseudoRandomData() {
    const scoreFloor = 0;
    const scoreCeiling = 1019;
    const turnsFloor = 220;
    const turnsCeiling = 37605;
    const timeFloor = 9;
    const timeCeiling = 737000;
    const battles = 1381;
    let data = [];

    for (let i = 0; i < battles; i++) {
        data.push({
            real_time: Math.floor((Math.random() * timeCeiling) + timeFloor),
            no_of_turns: Math.floor((Math.random() * turnsCeiling) + turnsFloor),
            results: [{
                name: "controller.CompetitorOne*",
                score: Math.floor((Math.random() * scoreCeiling) + scoreFloor),
                bullet_damage: 0,
                bullet_damage_bonus: 0,
                firsts: 1,
                seconds: 0,
                thirds: 0,
                last_survivor_bonus: 20,
                ram_damage: 0,
                ram_damage_bonus: 0,
                rank: 1,
                survival: 50
            }],
            rules: {
                no_of_rounds: Math.floor((Math.random() * 10) + 1),
                battlefield_h: 1000,
                battlefield_w: 1000,
                sentry_border_size: 100,
                gun_cooling_rate: 0.1,
                inactivity_time: 2000,
                hide_enemy_names: false
            }
        });
    }

    return data;
}