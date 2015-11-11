$.noConflict();
jQuery( document ).ready(function( $ ) {

	$(document).ready(function() {
		$('#decreaseSize').click(function() { 

			$('*').each(function(){
				var k =  parseInt($(this).css('font-size')); 
				//var redSize = ((k*90)/100) ; 
				var newSize = k-2 + "px";
				$(this).css({'font-size':newSize});  

			});
		});

		$('#increaseSize').click(function() { 

			$('*').each(function(){
				var k =  parseInt($(this).css('font-size')); 
				//var redSize = ((k*110)/100) ;
				var newSize = k+2 + "px";
				$(this).css({'font-size':newSize});  

			});
		});
	});

});