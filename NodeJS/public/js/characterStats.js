$(document).ready(() => {
    renderTwitterUsers();
    $("#twitterUsers").change();
    if ($.browser.mozilla) $("form").attr("autocomplete", "off");

   $.ajax({
       url: "/character-analysis",
       type: "POST",
       async: true,
       success: function(data) {
           stopLoadingAnimation();
           updatePieCharts(data);
           animatePieCharts();
           renderBarChart(data);
       },
       error: function(err) {
          console.log(err);
       }
   });
});

function renderBarChart(data) {
    //Setting The Dimensions Of The Canvas
    let margin = {top: 20, right: 20, bottom: 25, left: 50},
        width = 1000 - margin.left - margin.right,
        height = 400 - margin.top - margin.bottom;

    //Setting X & Y Ranges
    let x = d3.scaleBand().range([1, width], 0.1, 0).paddingInner(0.15).paddingOuter(0.15);
    let y = d3.scaleLinear().range([height, 0]);

    //Set X & Y Domains
    x.domain(data.map(function(d) {return d.letter;}));
    y.domain([0, d3.max(data, function(d) {return d.percentage;})]).nice();

    //Define The Axes
    let xAxis = d3.axisBottom().scale(x);
    let yAxis = d3.axisLeft().scale(y).ticks(6).tickFormat(d => {return d + "%"});

    //Add The SVG Element
    let svg = d3.select("#characterFrequencyBarChart")
        .append("svg").attr("class", "svg-element")
        .attr("viewBox", "0 0 " + (width + margin.left + margin.right) + " " + (height + margin.top + margin.bottom))
        .attr("width", "100%")
        .attr("height", height + margin.top + margin.bottom)
        .append("g").attr("transform", "translate(" + margin.left + "," + margin.top + ")");

    //Adding The Bars
    let bars = svg.selectAll("bar").data(data).enter().append("rect").attr("class", "bar")
        .attr("x", function(d) {return x(d.letter);})
        .attr("width", x.bandwidth())
        .attr("y", height - 1)
        .attr("height", 0)
        .style("fill", "#1fcdff");

    bars.transition().duration(3500).delay(200)
        .attr("y", function(d) {return y(d.percentage);})
        .attr("height", function(d) {return height - y(d.percentage);})
        .style("fill", "#1fcdff");

    //Adding X-Axis
    svg.append("g").attr("class", "x-axis")
        .attr("transform", "translate(0," + height + ")")
        .call(xAxis);

    svg.select(".x-axis").selectAll("text")
        .style("text-anchor", "middle")
        .attr("font-family", 'Roboto').attr("font-size", "1.3em")
        .attr("font-weight", "500");

    //Adding Y-Axis
    svg.append("g").attr("class", "y-axis").call(yAxis)
        .append("text")
        .attr("transform", "rotate(-90)")
        .attr("x", 0 - (height / 2)).attr("y", 0 - margin.left + 10)
        .style("text-anchor", "middle")
        .style("font-family", 'Roboto')
        .attr("font-weight", "500")
        .text("Frequency Percentage");

    //Bind Floating Information Div
    bars.on("mouseover", function(d) {
        $(document).bind('mousemove', function (e) {
            //Add CSS
            $("#cfbcFloatingDiv").css({
                left: e.pageX + 15,
                top: e.pageY + 25,
                "visibility": "visible"
            });
        });

        $("#cfbcFloatingDiv").html("<p class='letter'>" + d.letter + "</p><p class='floating-text'>" + d.percentage.toFixed(2) + "%</p>")
    });

    bars.on("mouseout", function() {
        $(document).unbind("mousemove");

        let floating = $("#cfbcFloatingDiv");
        //Remove CSS
        floating.css("visibility", "hidden");

        //Remove HTML From Floating Div
        floating.html("");
    });

    function sortAscending() {
        let x0 = x.domain(data.sort(function(a, b) {return a.percentage - b.percentage;}).map(function(d){return d.letter;})).copy();
        svg.selectAll(".bar").sort(function(a,b){return x0(a.letter) - x0(b.letter);});
        let transition = svg.transition().duration(500);
        let delay = function(d, i) { return i * 50;};
        transition.selectAll(".bar").delay(delay).attr("x", function(d){return x0(d.letter);});
        transition.select(".x-axis").call(xAxis).selectAll("g").delay(delay);
    }

    function sortDescending() {
        let x0 = x.domain(data.sort(function(a, b) {return b.percentage - a.percentage;}).map(function(d){return d.letter;})).copy();
        svg.selectAll(".bar").sort(function(a,b){return x0(a.letter) - x0(b.letter);});
        let transition = svg.transition().duration(500);
        let delay = function(d, i) { return i * 50;};
        transition.selectAll(".bar").delay(delay).attr("x", function(d){return x0(d.letter);});
        transition.select(".x-axis").call(xAxis).selectAll("g").delay(delay);
    }

    function sortAlphabetical() {
        let x0 = x.domain(data.sort(function(a, b) {return d3.ascending(a.letter, b.letter);}).map(function(d){return d.letter;})).copy();
        svg.selectAll(".bar").sort(function(a,b){return d3.ascending(a.letter, b.letter);});
        let transition = svg.transition().duration(500);
        let delay = function(d, i) { return i * 50;};
        transition.selectAll(".bar").delay(delay).attr("x", function(d){return x0(d.letter);});
        transition.select(".x-axis").call(xAxis).selectAll("g").delay(delay);
    }

    //Bind On-Click Events
    $("#sort-alpha").on("click", sortAlphabetical);
    $("#sort-asc").on("click", sortAscending);
    $("#sort-desc").on("click", sortDescending);
}

function renderTwitterUsers() {
    let twitterUsers = $("#twitterUsers");
    for (let i = 0; i < users.length; i++) {
        twitterUsers.append($("<option>", {
            value: users[i].username,
            text: formatUsername(users[i].username.toString())
        }));
    }

    function findUser(name) {
        for (let i = 0; i < users.length; i++) {
            if (users[i].username === name) {
                return users[i];
            }
        }
    }

    twitterUsers.change(() => {
        let selectedUser = $("#twitterUsers").val();
        let userObject = findUser(selectedUser);
        $("#username").html(formatUsername(userObject.username));
        $("#screenName").html("<a href='" + userObject.url + "'> @" + userObject.screen_name + "</a>");
        $("#stats").html("<i class='fa fa-twitter'></i><p title='Tweets'>" + formatLargeNumber(userObject.statuses) + "</p>" + "<i class='fa fa-fw fa-user'></i>" + formatLargeNumber(userObject.followers));
        $("#profile-image").html("<img src='" + userObject.profile_https + "' title='" + userObject.username + "' alt='" + userObject.username + " Profile Picture'/>");

        startCharacterLoading(formatUsername(userObject.username));
        $.ajax({
            /*
            xhr: function () {
                let xhr = new window.XMLHttpRequest();
                xhr.upload.addEventListener("progress", function (evt) {
                    if (evt.lengthComputable) {
                        let percentComplete = evt.loaded / evt.total;
                        console.log(percentComplete);
                        $('.progress').css({
                            width: percentComplete * 100 + '%'
                        });
                        if (percentComplete === 1) {
                            $('.progress').addClass('hide');
                        }
                    }
                }, false);
                xhr.addEventListener("progress", function (evt) {
                    if (evt.lengthComputable) {
                        let percentComplete = evt.loaded / evt.total;
                        console.log(percentComplete);
                        $('.progress').css({
                            width: percentComplete * 100 + '%'
                        });
                    }
                }, false);
                return xhr;
            },
            */
            url: "/calculate-user-stats",
            type: "POST",
            async: true,
            data: {username: userObject.username},
            success: function(data) {
                stopCharacterLoading();
                renderUserStats(data);
            },
            error: function(err) {
                console.log(err);
            }
        });
    });
}

function renderUserStats(data) {
    //Hyperlinks
    $('.tweet-percentage').data('easyPieChart').update(calculatePercentage(data.links, data.userTweetCount));

    $(".longest-tweet p").text(data.longestTweet);
    $(".longest-tweet .progress-bar").css("width", calculatePercentage(data.longestTweet, 280) + "%");

    $(".shortest-tweet p").text(data.shortestTweet);
    $(".shortest-tweet .progress-bar").css("width", calculatePercentage(data.shortestTweet, 280) + "%");

    $(".average-tweet p").text(data.averageTweet);
    $(".average-tweet .progress-bar").css("width", calculatePercentage(data.averageTweet, 280) + "%");
}

function calculatePercentage(variable, divisor) {
    return (variable / divisor) * 100;
}


function formatUsername(name) {
    if (name.length <= 3) {
        return name.toUpperCase();
    }
    let arr = name.split(" ");
    let formattedName = "";
    for (let i = 0; i < arr.length; i++) {
        formattedName += arr[i].substring(0, 1).toUpperCase();
        formattedName += arr[i].substring(1, arr[i].length).toLowerCase();
        if (i !== arr.length - 1) {
            formattedName += " ";
        }
    }
    return formattedName;
}

function formatLargeNumber(x) {
    return x.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ",");
}

function updatePieCharts(alpha) {
    for (let i = 0; i < alpha.length; i++) {
        const el = eval($("." + alpha[i].letter.toLowerCase()));
        el.attr("data-percent", parseFloat(alpha[i].percentage).toFixed(2));
    }
}

function stopLoadingAnimation() {
    $(".loading").html("");
    $(".characterStatsPie, .characterStatsBar").css("display", "inherit");
}

function startCharacterLoading(username) {
    let el = $(".character-loading");
    el.html("<i class='fa fa-lg fa-fw fa-3x fa-pulse fa-spinner'></i><p>Analysing " + username + "'s Data...</p>");
    el.css("display", "inline");
    $(".selectedTwitterUserAnalysis").css("display", "none");
}

function stopCharacterLoading() {
    $(".character-loading").css("display", "none");
    $(".selectedTwitterUserAnalysis").css("display", "inline");
}

function animatePieCharts() {
    let chart = $('.chart'),
        chartNr = $('.chart-content'),
        chartParent = chart.parent();

    function centerChartsNr() {
        chartNr.css({
            top: (chart.height() - chartNr.outerHeight()) / 2
        });
    }

    if (chart.length) {
        centerChartsNr();
        $(window).resize(centerChartsNr);

        chartParent.each(function () {
            $(this).find('.chart').easyPieChart({
                barColor: '#2f2f2f',
                trackColor: '#dcdcdc',
                lineCap: false,
                lineWidth: '3',
                size: '72',
                scaleColor: false,
                animate: 1500,
                onStep: function (from, to, percent) {
                    $(this.el).find('.percent').text(percent.toFixed(1));
                }
            });
        });
    }
}