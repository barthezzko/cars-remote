function Utils(){
	
	var gridSize = -1;
	var predefinedCases = ['5,5:RFLFRFLF', '6,6:FFLFFLFFLFF', '5,5:FLFLFFRFFF'];
	var latest = {};
	var directions = [];
	
	this.loadConfig = function(){
		$.ajax({
			url:'/config',
			dataType:'json',
			data: {},
			success: function(data){
				gridSize = data.gridSize;
				init();			
				$('#topRightComment').text('Top-right corner has coordinates ('+(gridSize-1)+','+(gridSize-1)+')');
			},
			error: function(){
				alert('Error loading config from server');
			}
		})
	}
	
	var init = function(){
		console.log("Loading, gridSize=" + gridSize);
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
					//$('#resultComment').html(JSON.stringify(data));
					var className = 'panel '
					$('#'+latest.x+'_'+latest.y).html('<span class="glyphicon glyphicon-remove"/>');
					$('table#resultTable tbody tr td').removeClass('success');
					$('#resultComment .panel-heading').html(data.responseType);
					var body_html = '';
					if (data.responseType == 'SUCCESS'){
						var pl = data.payload;
						className +='panel-success';
						$('#'+pl.x+'_'+pl.y).html('<span class="'+decodePL(pl.direction)+'" aria-hidden="true"></span>');
						$('#'+pl.x+'_'+pl.y).parent().addClass('success');
						latest.y = pl.y;
						latest.x = pl.x;
						body_html+='<table class="table table-condensed table-bordered table-striped"><tbody><tr><th class="col-md-6">Key</th><th>Value</th></tr>';
						for (k in data.payload){
							body_html += '<tr><th>' + k +'</th><td>'+data.payload[k]+'</td></tr>';
						}
						body_html += '</tbody></table>'
					} else {
						className +='panel-danger';
						body_html += data.payload;
					}
					$('#resultComment .panel-body').html(body_html);
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
		for (i=0;i<gridSize;i++){
			table_html += '<tr>'; 
			for (k=0;k<gridSize;k++){
				table_html += '<td class="col-xs-01 text-center"><a href="#" data-toggle="tooltip" title="Cell ('+k+','+(gridSize-i-1)+')" id="' + k + '_' + (gridSize-i-1)+'"><span class="glyphicon glyphicon-remove"/></a></td>';
			}
			table_html += '</tr>';
		}
		$('table#resultTable tbody').append(table_html);
		$(document).ready(function(){
		    $('[data-toggle="tooltip"]').tooltip(); 
		});
	}
	
}

utils = new Utils();
$(document).ready(function(){
	utils.loadConfig();	
});