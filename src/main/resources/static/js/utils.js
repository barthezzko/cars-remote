function Utils(){
	
	var gridSize = 15;
	var predefinedCases = ['5,5:RFLFRFLF', '6,6:FFLFFLFFLFF', '5,5:FLFLFFRFFF'];
	
	this.init = function(){
		console.log("Loaded");
		$('#sendCommandsBtn').click(function(){
			var input = $('#commandsTextarea').val();
			$.ajax({
				url:'calc',
				type: 'POST',
				data: {'instruction':input},
				success: function(data){
					$('#resultComment').html(JSON.stringify(data));
					var className = 'alert '
					if (data.responseType == 'SUCCESS'){
						className +='alert-success';
					} else {
						className +='alert-danger';
					}
					$('#resultComment').removeClass().addClass(className);
					console.log(data);
				},
				dataType: 'json',
				error: function(){
					alert('Unexpected server error happend. Are you sure server is still working?');
				}
			});
			console.log('waiting...')
		});
		for (k in predefinedCases){
			$('#predefined').append('[<a href="#" onclick="utils.predef('+k+');"><code>' + predefinedCases[k] + '</code></a>] ');	
		}
		redrawTable();
	}
	this.predef= function(i){
		$('#commandsTextarea').val(predefinedCases[i]);
	}
	
	var redrawTable = function(){
		var table_html = '';
		for (i=0;i<gridSize;i++){
			table_html += '<tr>'; 
			for (k=0;k<gridSize;k++){
				table_html += '<td class="text-center" id="' + i + '_' + k+'">.</td>';
			}
			table_html += '</tr>';
		}
		$('table#resultTable').append(table_html);
		for (i=0;i<gridSize;i++){
			$('#'+i+'_'+i).addClass('danger bold');
		}
		$('table#resultTable tr td').click(function(){
			console.log($(this).attr('id'));
		});
		
	}
	
}

utils = new Utils();
$(document).ready(function(){
	utils.init();	
});