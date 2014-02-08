    /**
     * registration functionality for storefront
     */
$(document).ready(function() {
	getZones($('#registration_country').val());
	$("#hidden_registration_zones").hide();
	$("#registration_country").change(function() {
			getZones($(this).val());
	})
});
function getZones(countryCode){
	$("#registration_zones option").remove(); 
	var url=getContextPath() + '/shop/reference/provinces.html';
	var data='countryCode=' + countryCode + '&lang=' + getLanguageCode();
	
	$.ajax({
		  type: 'POST',
		  url: url,
		  data: data,
		  dataType: 'json',
		  success: function(responseObj){

			  if((responseObj.response.status == 0 || responseObj.response.status ==9999) && responseObj.response.data){
					$("#registration_zones option").remove();
					$('#registration_zones').show();  
					$('#hidden_registration_zones').hide();
					$.each(responseObj.response.data,function(index, value){
						
						var zone = document.getElementById("registration_zones");
						var option=new Option(value.name,value.code);
						try{
							zone.add(option);
						}
						catch(e){
							zone.appendChild(option);
						}
					});
			
			  } else {
				  $('#registration_zones').hide();  
				  $('#hidden_registration_zones').show();
			  }
		      },
			    error: function(xhr, textStatus, errorThrown) {
			  	alert('error ' + errorThrown);
			  }
		
		
	});

}	