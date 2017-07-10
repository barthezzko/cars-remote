function Utils(){
	
	var gridSize = 15;
	var predefinedCases = ['5,5:RFLFRFLF', '6,6:FFLFFLFFLFF', '5,5:FLFLFFRFFF'];
	var latest = {};
	var directions = [];
	
	this.init = function(){
		console.log("Loaded");
		directions['NORTH'] = 'up';
		directions['SOUTH'] = 'down';
		directions['WEST'] = 'left';
		directions['EAST'] = 'right';
		
		$('#sendCommandsBtn').click(function(){
			var input = $('#commandsTextarea').val();
			$.ajax({
				url:'calc',
				type: 'POST',
				data: {'instruction':input},
				success: function(data){
					$('#resultComment').html(JSON.stringify(data));
					var className = 'alert '
					$('#'+latest.x+'_'+latest.y).html('.');
					$('table#resultTable tbody tr td').removeClass('success');
					if (data.responseType == 'SUCCESS'){
						var pl = data.payload;
						className +='alert-success';
						$('#'+pl.x+'_'+pl.y).html('<span class="'+decodePL(pl.direction)+'" aria-hidden="true"></span><br/>[' + pl.x+','+ pl.y +']');
						$('#'+pl.x+'_'+pl.y).addClass('success');
						latest.x = pl.x;
						latest.y = pl.y;
					} else {
						className +='alert-danger';
					}
					$('#resultComment').removeClass().addClass(className);
					
				},
				dataType: 'json',
				error: function(){
					alert('Unexpected server error happend. Are you sure server is still working?');
				}
			});
			console.log('waiting...')
		});
		for (k in predefinedCases){
			$('#predefined').append('[<a href="javascript:void(0);" onclick="utils.predef('+k+');"><code>' + predefinedCases[k] + '</code></a>] ');	
		}
		redrawTable();
	}
	this.predef= function(i){
		$('#commandsTextarea').val(predefinedCases[i]);
	}
	var decodePL = function(direction){
		var clName = 'glyphicon glyphicon-arrow-' + directions[direction];
		console.log(clName);
		return clName;
	}
	var redrawTable = function(){
		var table_html = '';
		for (i=1;i<=gridSize;i++){
			table_html += '<tr>'; 
			for (k=1;k<=gridSize;k++){
				table_html += '<td class="col-xs-01 text-center" id="' + i + '_' + k+'">.</td>';
			}
			table_html += '</tr>';
		}
		$('table#resultTable tbody').append(table_html);
	}
	
}

utils = new Utils();
$(document).ready(function(){
	utils.init();	
});