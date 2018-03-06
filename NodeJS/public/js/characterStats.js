$(document).ready(() => {
   $.ajax({
       url: "/character-analysis",
       type: "POST",
       async: true,
       success: function(data) {
           stopLoadingAnimation();
           updatePieCharts(data);
           animatePieCharts();
       },
       error: function(err) {
          console.log(err);
       }
   });
});

function updatePieCharts(alpha) {
    //const a = $(".a");
    //a.attr("data-percent", alpha[0].percentage);

    for (let i = 0; i < alpha.length; i++) {
        const el = eval($("." + alpha[i].letter.toLowerCase()));
        el.attr("data-percent", parseFloat(alpha[i].percentage).toFixed(2));
    }
}

function stopLoadingAnimation() {
    $(".loading").html("");
    $(".characterStatsPie").css("display", "inherit");
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
            $(this).onScreen({
                doIn: function () {
                    $(this).find('.chart').easyPieChart({
                        barColor: '#2f2f2f',
                        trackColor: '#dcdcdc',
                        lineCap: false,
                        lineWidth: '3',
                        size: '72',
                        scaleColor: false,
                        animate: 1500,
                        onStep: function (from, to, percent) {
                            $(this.el).find('.percent').text(percent.toFixed(2));
                        }
                    });
                },
            });
        });
    }
}