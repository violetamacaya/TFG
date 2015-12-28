$.noConflict();
jQuery( document ).ready(function( $ ) {

	$(document).ready(function() {
		var cookieSize = getCookie("elesolFSize");
		if(cookieSize != undefined){
			$('body').css({'font-size':cookieSize});  
		}
		$('#decreaseSize').click(function() { 
			var bodySize = ($("body").css('font-size')); 
			var k = parseInt(bodySize) * 72 / 96;
			if(k>8){
				var newSize = k-1 + "pt";
				$('body').css({'font-size':newSize});  
				setCookie("elesolFSize", newSize, 1);
			}


		});

		$('#increaseSize').click(function() { 
			var bodySize = ($("body").css('font-size')); 
			var k = parseInt(bodySize) * 72 / 96;
			if (k<16){
				var newSize = k+1 + "pt";
				$('body').css({'font-size':newSize});  
				setCookie("elesolFSize", newSize, 1);
			}
		});
		$('#restoreSize').click(function() { 
				var newSize = 12 + "pt";
				$('body').css({'font-size':newSize});  
				setCookie("elesolFSize", newSize, 1);
		});
	});

});

function setCookie(cname, cvalue, exdays) {
    var d = new Date();
    d.setTime(d.getTime() + (exdays*24*60*60*1000));
    var expires = "expires="+d.toUTCString();
    document.cookie = cname + "=" + cvalue + "; " + expires;
}

function getCookie(cname) {
    var name = cname + "=";
    var ca = document.cookie.split(';');
    for(var i=0; i<ca.length; i++) {
        var c = ca[i];
        while (c.charAt(0)==' ') c = c.substring(1);
        if (c.indexOf(name) == 0) return c.substring(name.length,c.length);
    }
    return "";
}