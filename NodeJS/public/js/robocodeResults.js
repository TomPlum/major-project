$(document).ready(() => {
   $.ajax({
       url: "/get-robocode-results",
       type: "POST",
       async: true,
       success: function(results) {
           console.log(results);
       },
       error: function(err) {
           console.log(err);
       }
   });
});