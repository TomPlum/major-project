$(document).ready(() => {
   //Bind Radio Button Events
   $("#include-sentry").on("click", includeTwitterSentry);
   $("#exclude-sentry").on("click", excludeTwitterSentry);
   $("#include-all").on("click", includeAllRecords);
});

//Setting The Dimensions Of The Canvas
const margin = {top: 20, right: 20, bottom: 90, left: 50},
    width = 1000 - margin.left - margin.right,
    height = 400 - margin.top - margin.bottom;

//Setting X & Y Ranges
let x = d3.scaleLinear().range([0, width]);
let y = d3.scaleLinear().range([height, 0]);

//Define The Axes
let xAxis = d3.axisBottom().scale(x);
let yAxis = d3.axisLeft().scale(y);

let svg = d3.select('#simple-linear-regression-graph').append("svg").attr("class", "svg-element")
    .attr("viewBox", "0 0 " + (width + margin.left + margin.right) + " " + (height + margin.top + margin.bottom))
    .attr("width", "100%").attr("height", height + margin.top + margin.bottom)
    .append("g").attr("transform", "translate(" + margin.left + "," + margin.top + ")");

let valueline = d3.line().x(function(d) {return x(d._id);}).y(function(d) {return y(d.percentage);});

let scatterPlotData = [];
let rawResultData;
let dynamicScatterPlotData;

function updateSimpleLinearRegression() {
    console.log("Dynamic Data Length: " + dynamicScatterPlotData.length);

    //Re-Define Domains
    x.domain(d3.extent(dynamicScatterPlotData, function(d) {return d.x}));
    y.domain(d3.extent(dynamicScatterPlotData, function(d) {return d.y}));

    //Select All Circles, Bind New Data
    let circles = svg.selectAll(".point").data(dynamicScatterPlotData);

    //Remove Redundant Circles
    circles.exit().remove();

    //Add New Circles
    circles.enter().append("circle")
        .attr("class", "point")
        .attr("r", 6)
        .attr("cy", function(d){ return y(d.y); })
        .attr("cx", function(d){ return x(d.x); });

    console.log("Number of Circles: " + $(".point").length + "\n");

    //Transition Remaining Dataset Circles To New Positions
    circles.transition().duration(1000).delay(function(d, i) {
        return i / dynamicScatterPlotData.length * 500;
    }).attr("cx", function(d) { return x(d.x); }).attr("cy", function(d) { return y(d.y); });

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
    updateSimpleLinearRegression();
}

function includeAllRecords() {
    //Copy Original Raw Data Into Dynamic Variable
    dynamicScatterPlotData = rawResultData.slice(0);

    //Convert Into Graph Format To Be Updated
    dynamicScatterPlotData = convertRawToGraphFormat(dynamicScatterPlotData);
    updateSimpleLinearRegression();
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
    updateSimpleLinearRegression();
}

function convertRawToGraphFormat(data) {
    let result = [];
    for (let i = 0; i < data.length; i++) {
        result.push({
            _id: data[i]._id,
            x: parseInt(data[i].results[0].score), //First one is the winner as in desc order
            y: parseInt(data[i].no_of_turns)
        });
    }
    return result;
}

function renderSimpleLinearRegression(data) {
    console.log("Called renderSimpleLinearRegression()");
    //Store In Raw
    rawResultData = data;

    //Build Data Structure
    scatterPlotData = convertRawToGraphFormat(data);

    //Store In Dynamic
    dynamicScatterPlotData = scatterPlotData;

    //Define Domains
    x.domain(d3.extent(scatterPlotData, function(d) {return d.x}));
    y.domain(d3.extent(scatterPlotData, function(d) {return d.y}));

    // see below for an explanation of the calcLinear function
    let lg = calcLinear(scatterPlotData, "x", "y", d3.min(scatterPlotData, function(d){ return d.x}), d3.min(scatterPlotData, function(d){ return d.x}));

    svg.append("line")
        .attr("class", "regression")
        .attr("x1", x(lg.ptA.x))
        .attr("y1", y(lg.ptA.y))
        .attr("x2", x(lg.ptB.x))
        .attr("y2", y(lg.ptB.y));

    svg.append("g")
        .attr("class", "x-axis")
        .attr("transform", "translate(0," + height + ")")
        .call(xAxis);

    svg.append("g")
        .attr("class", "y-axis")
        .call(yAxis);

    svg.selectAll(".point")
        .data(scatterPlotData)
        .enter().append("circle")
        .attr("class", "point")
        .attr("r", 6)
        .attr("cy", function(d){ return y(d.y); })
        .attr("cx", function(d){ return x(d.x); });



    function calcLinear(data, x, y, minX, minY){
        /////////
        //SLOPE//
        /////////

        // Let n = the number of data points
        let n = data.length;

        // Get just the points
        let pts = [];
        data.forEach(function(d,i){
            let obj = {};
            obj.x = d[x];
            obj.y = d[y];
            obj.mult = obj.x*obj.y;
            pts.push(obj);
        });

        // Let a equal n times the summation of all x-values multiplied by their corresponding y-values
        // Let b equal the sum of all x-values times the sum of all y-values
        // Let c equal n times the sum of all squared x-values
        // Let d equal the squared sum of all x-values
        let sum = 0;
        let xSum = 0;
        let ySum = 0;
        let sumSq = 0;
        pts.forEach(function(pt){
            sum = sum + pt.mult;
            xSum = xSum + pt.x;
            ySum = ySum + pt.y;
            sumSq = sumSq + (pt.x * pt.x);
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
        document.getElementsByClassName("equation")[0].innerHTML = "y = " + m + "x + " + b;
        document.getElementsByClassName("equation")[1].innerHTML = "x = ( y - " + b + " ) / " + m;

        // return an object of two points
        // each point is an object with an x and y coordinate
        return {
            ptA : {
                x: minX,
                y: m * minX + b
            },
            ptB : {
                y: minY,
                x: (minY - b) / m
            }
        }

    }

}

