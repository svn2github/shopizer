    /**
     * registration functionality for storefront
     */
$(document).ready(function() {
$("#hidden_registration_zones").hide();
$("#registration_country").change(function() {
			getZones($(this).val());
  })
});
function getZones(countryCode){
	$("#registration_zones option").remove(); 
	var url=getContextPath() + '/shop/customer/getZonesByCountry.html';
	var data='countryCode=' + countryCode;
	$.getJSON(url,data,function(responseObj) {
		
		if(responseObj.response.status == 0 || responseObj.response.status ==9999){
			
			$("#registration_zones option").remove();
			$("#registration_zones option:first").val("-1").text("Please select a  state");
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
			
		}
		else{
			$('#registration_zones').hide();  
			$('#hidden_registration_zones').show();
		}
		
		
	});
	
	/*$.ajax({
	  type: 'GET',
	  url: getContextPath() + '/shop/customer/getZonesByCountry.html', 
	 // url: '<c:url value="/shop/customer/getZonesByCountry.html"/>',
	  data: 'countryCode=' + countryCode,
	  dataType: 'json',
	  success: function(responseObj){
           console.log(responseObj.response.status);
			
		  var status = isc.XMLTools.selectObjects(response, "/response/status");
			if(status==0 || status ==9999) {} else {
				
			}

	  
	  },
	  error: function(xhr, textStatus, errorThrown) {
	  	alert('error ' + errorThrown);
	  }
	  
	});*/
}	