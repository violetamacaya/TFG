charts= Class.create( {

    initialize : function(type,array) {
    		this.type=type;
    	
    	
    	  // Load the Visualization API and the piechart package.
          google.load('visualization', '1.0', {'packages':['corechart']});

          // Set a callback to run when the Google Visualization API is loaded.
           google.setOnLoadCallback(drawChart);

          // Callback that creates and populates a data table,
          // instantiates the pie chart, passes in the data and
          // draws it.
          function drawChart() {

            // Create the data table.
            var data = new google.visualization.DataTable();
            if(type=="MAY")
            {
            	data.addColumn('string', 'Option');
                data.addColumn('number', 'Votos');
            }
            if(type=="KEM")
            {
            	data.addColumn('string', 'Kemeny Option');
                data.addColumn('number', 'Votos');
            }
            if(type=="BOR")
            {
            	data.addColumn('string', 'Option');
                data.addColumn('number', 'Votos');
            }
            if(type=="RANGE")
            {
            	data.addColumn('string', 'Option');
                data.addColumn('number', 'Votos');
            }
            if(type=="APPROVAL")
            {
            	data.addColumn('string', 'Option');
                data.addColumn('number', 'Votos');
            }
            if(type=="BRAMS")
            {
            	data.addColumn('string', 'Option');
                data.addColumn('number', 'Votos');
            }
            if(type=="ACUMULATIVO")
            {
            	data.addColumn('string', 'Option');
                data.addColumn('number', 'Votos');
            }       
            if(type=="MAYORITARIO")
            {
            	data.addColumn('string', 'Option');
                data.addColumn('number', 'Votos');
            }    
            if(type=="CONDORCET")
            {
            	data.addColumn('string', 'Option');
                data.addColumn('number', 'Votos');
            }    
            if(type=="BLACK")
            {
            	data.addColumn('string', 'Option');
                data.addColumn('number', 'Votos');
            }    
            if(type=="DODGSON")
            {
            	data.addColumn('string', 'Option');
                data.addColumn('number', 'Votos');
            }    
            if(type=="COPELAND")
            {
            	data.addColumn('string', 'Option');
                data.addColumn('number', 'Votos');
            }    
            if(type=="SCHULZE")
            {
            	data.addColumn('string', 'Option');
                data.addColumn('number', 'Votos');
            }    
            if(type=="SMALL")
            {
            	data.addColumn('string', 'Option');
                data.addColumn('number', 'Votos');
            }   
            if(type=="MEJOR_PEOR")
            {
            	data.addColumn('string', 'Option');
                data.addColumn('number', 'Votos');
            }   
            
            var firstItem=true;
          
            array.forEach(function(obj)
            {
	            if(firstItem==true)
	           	{
	           		firstItem=false;
	           	}
	            else
	            {
	            	data.addRow([obj.option,obj.value])
	            }
            });
            
            // Set chart options
            var options = {'title':array[0].title,
                           'width':1000,
                           'height':600
                           ,'is3D':true
                           };

            // Instantiate and draw our chart, passing in some options.
            var chart;
            if(type=="MAY")
            {
            	chart = new google.visualization.PieChart(document.getElementById('chart_div'));
            }
            if(type=="KEM")
            {
            	chart= new google.visualization.BarChart(document.getElementById('chart_div'));
            }
            if(type=="BOR")
            {
            	chart = new google.visualization.PieChart(document.getElementById('chart_div'));
            }
            if(type=="RANGE")
            {
            	chart = new google.visualization.PieChart(document.getElementById('chart_div'));
            }
            if(type=="APPROVAL")
            {
            	chart = new google.visualization.PieChart(document.getElementById('chart_div'));
            } 
            if(type=="BRAMS")
            {
            	chart = new google.visualization.PieChart(document.getElementById('chart_div'));
            }

            if(type=="ACUMULATIVO")
            {
            	chart= new google.visualization.ColumnChart(document.getElementById('chart_div'));
            }   
            if(type=="MAYORITARIO")
            {
            	chart= new google.visualization.ColumnChart(document.getElementById('chart_div'));
            }   
            if(type=="CONDORCET")
            {
            	chart= new google.visualization.PieChart(document.getElementById('chart_div'));
            }
            if(type=="BLACK")
            {
            	chart= new google.visualization.PieChart(document.getElementById('chart_div'));
            }
            if(type=="DODGSON")
            {
            	chart= new google.visualization.PieChart(document.getElementById('chart_div'));
            }
            if(type=="COPELAND")
            {
            	chart= new google.visualization.PieChart(document.getElementById('chart_div'));
            }
            if(type=="SCHULZE")
            {
            	chart= new google.visualization.PieChart(document.getElementById('chart_div'));
            }
            if(type=="SMALL")
            {
            	chart= new google.visualization.PieChart(document.getElementById('chart_div'));
            }
            if(type=="MEJOR_PEOR")
            {
            	chart= new google.visualization.ColumnChart(document.getElementById('chart_div'));
            }
            
            chart.draw(data, options);
          }
    }
    

} )

Tapestry.Initializer.charts_may = function(spec) {
    var array=JSON.parse(spec);
	new charts("MAY",array);
}


Tapestry.Initializer.charts_kem = function(spec) {
	var array=JSON.parse(spec);
	new charts("KEM",array);
    
}

Tapestry.Initializer.charts_bor = function(spec) {
	
	var array=JSON.parse(spec);
	new charts("BOR",array);
}
Tapestry.Initializer.charts_range = function(spec) {
	
	var array=JSON.parse(spec);
	new charts("RANGE",array);
}
Tapestry.Initializer.charts_approval = function(spec) {
	
	var array=JSON.parse(spec);
	new charts("APPROVAL",array);
}
Tapestry.Initializer.charts_brams = function(spec) {
	
	var array=JSON.parse(spec);
	new charts("BRAMS",array);
}
Tapestry.Initializer.charts_votoAcumulativo = function(spec) {
	
	var array=JSON.parse(spec);
	new charts("ACUMULATIVO",array);
}
Tapestry.Initializer.charts_juicioMayoritario = function(spec) {
	
	var array=JSON.parse(spec);
	new charts("MAYORITARIO",array);
}
Tapestry.Initializer.charts_condorcet = function(spec) {
	
	var array=JSON.parse(spec);
	new charts("CONDORCET",array);
}
Tapestry.Initializer.charts_black = function(spec) {
	
	var array=JSON.parse(spec);
	new charts("BLACK",array);
}
Tapestry.Initializer.charts_dodgson = function(spec) {
	
	var array=JSON.parse(spec);
	new charts("DODGSON",array);
}
Tapestry.Initializer.charts_copeland = function(spec) {
	
	var array=JSON.parse(spec);
	new charts("COPELAND",array);
}
Tapestry.Initializer.charts_schulze = function(spec) {
	
	var array=JSON.parse(spec);
	new charts("SCHULZE",array);
}
Tapestry.Initializer.charts_small = function(spec) {
	
	var array=JSON.parse(spec);
	new charts("SMALL",array);
}
Tapestry.Initializer.charts_mejorPeor = function(spec) {
	
	var array=JSON.parse(spec);
	new charts("MEJOR_PEOR",array);
}