			<div class="row-fluid" id="checkout">
				<div class="span12">


				

		<div class="span8">

					<div class="box">
						<span class="box-title">
							<p><font color="#FF8C00">Billing information</font></p>
						</span>

						
			<div class="row-fluid">

				<div class="span4">
  				   <div class="control-group"> 
					<label>First</label>
    					<div class="controls"> 
      					<input type="text" class="input-medium" id="first"> 
    					</div> 
  				   </div> 
				</div>


				<div class="span4">
  				   <div class="control-group"> 
					<label>Last</label>
    					<div class="controls"> 
      					<input type="text" class="input-medium" id="last"> 
    					</div> 
  				   </div> 
				</div>

			</div>



			<div class="row-fluid">

				<div class="span4">
  				   <div class="control-group"> 
					<label>Email</label>
    					<div class="controls"> 
      					<input type="text" class="input-medium" id="email"> 
    					</div> 
  				   </div> 
				</div>


				<div class="span4">
  				   <div class="control-group"> 
					<label>Company</label>
    					<div class="controls"> 
      					<input type="text" class="input-medium" id="company"> 
    					</div> 
  				   </div> 
				</div>

			</div>


			<div class="row-fluid">



  			<div class="control-group"> 
				<label>Address</label>
    				<div class="controls"> 
      					<input type="text" class="input-xlarge" id="address1">
    				</div> 
  			</div> 

			</div>

			<div class="row-fluid">


			<div class="span4">
  			<div class="control-group"> 
				<label>City</label>
    				<div class="controls"> 
      					<input type="text" class="input-medium" id="city">
    				</div> 
  			</div>
			</div>



			<div class="span4">
  			<div class="control-group"> 
				<label>State / Province</label>
    				<div class="controls"> 
      					<input type="text" class="input-medium" id="state">
    				</div> 
  			</div>
			</div>


			<div class="span4">
  			<div class="control-group"> 
				<label>Postal code</label>
    				<div class="controls"> 
      					<input type="text" class="input-small" id="postalcode">
    				</div> 
  			</div>
			</div>

			</div>
			<div class="row-fluid">

	  			<div class="control-group"> 
					<label>Country</label>
	    				<div class="controls"> 
	      					<input type="text" class="input-large" id="country">
	    				</div> 
	  			</div>
	
	  			<div class="control-group"> 
					<label>Phone</label>
	    				<div class="controls"> 
	      					<input type="text" class="input-large" id="phone">
	    				</div> 
	  			</div>


				<label class="checkbox" checked> <input type="checkbox" id="useAddress"> Ship to this address</label>
			</div>


			


		</div>

		<div class="span8">

					<div class="box">
						<span class="box-title">
							<p><font color="#FF8C00">Shipping information</font></p>
						</span>

						
			<div class="row-fluid">

				<div class="span4">
  				   <div class="control-group"> 
					<label>First</label>
    					<div class="controls"> 
      					<input type="text" class="input-medium" id="first"> 
    					</div> 
  				   </div> 
				</div>


				<div class="span4">
  				   <div class="control-group"> 
					<label>Last</label>
    					<div class="controls"> 
      					<input type="text" class="input-medium" id="last"> 
    					</div> 
  				   </div> 
				</div>

			</div>



			<div class="row-fluid">



  			<div class="control-group"> 
				<label>Address</label>
    				<div class="controls"> 
      					<input type="text" class="input-xlarge" id="address1">
    				</div> 
  			</div> 

			</div>

			<div class="row-fluid">


			<div class="span4">
  			<div class="control-group"> 
				<label>City</label>
    				<div class="controls"> 
      					<input type="text" class="input-medium" id="city">
    				</div> 
  			</div>
			</div>



			<div class="span4">
  			<div class="control-group"> 
				<label>State / Province</label>
    				<div class="controls"> 
      					<input type="text" class="input-medium" id="state">
    				</div> 
  			</div>
			</div>


			<div class="span4">
  			<div class="control-group"> 
				<label>Postal code</label>
    				<div class="controls"> 
      					<input type="text" class="input-small" id="postalcode">
    				</div> 
  			</div>
			</div>

			</div>
			<div class="row-fluid">

	  			<div class="control-group"> 
					<label>Country</label>
	    				<div class="controls"> 
	      					<input type="text" class="input-large" id="country">
	    				</div> 
	  			</div>
	
	  			<div class="control-group"> 
					<label>Phone</label>
	    				<div class="controls"> 
	      					<input type="text" class="input-large" id="phone">
	    				</div> 
	  			</div>


			</div>


			


		</div>




					<br/>

					<div class="box">
						<span class="box-title">
						<p>Payment</p>
						</span>

						<!--<ul class="nav nav-tabs">
							<li id="pp-tab"  class="active"><a href="#" id="pp-link" data-toggle="tab">PPAL</a></li>
							<li id="cc-tab"  class="active"><a href="#" id="cc-link" data-toggle="tab">CC</a></li>
						</ul>-->


    <div class="tabbable"> 
    	<ul class="nav nav-tabs">
    		<li class="active"><a href="#tab1" data-toggle="tab">Pay</a></li>
    		<li><a href="#tab2" data-toggle="tab">Credit</a></li>
            <li><a href="#tab3" data-toggle="tab">Check</a></li>
      </ul>
    	<div class="tab-content">
    		<div class="tab-pane active" id="tab1">
    			<p>Pay<p>
    		</div>
    		<div class="tab-pane" id="tab2">


<div class="control-group">
	<label class="control-label">Card Holder's Name</label>
	<div class="controls">
		<input type="text" class="input-large" pattern="\w+ \w+.*" title="Fill your first and last name" required>
	</div>
</div>
<div class="control-group">
	<label class="control-label">Card Number</label>
	<div class="controls">
		<div class="row-fluid">
			<input type="text" class="input-large" autocomplete="off"  required>
		</div>
	</div>
</div>
<div class="control-group">
	<label class="control-label">Card Expiry Date</label>
	<div class="controls">
		<div class="row-fluid">
		<div class="span3">
			<select class="input-medium">
				<option>January</option>
				<option>...</option>
				<option>December</option>
			</select>
		</div>
		<div class="span3">
			<select class="input-small">
				<option>2013</option>
				<option>...</option>
				<option>2015</option>
			</select>
		</div>
	</div>
	</div>
</div>
<div class="control-group">
	<label class="control-label">Card CVV</label>
	<div class="controls">
	<div class="row-fluid">
		<div class="span3">
			<input type="text" class="input-small" autocomplete="off" maxlength="3" pattern="\d{3}" title="Three digits at back of your card" required>
		</div>
	<div class="span8">
	<!-- screenshot may be here -->
	</div>
</div>
</div>
</div>






    		</div>
    		<div class="tab-pane" id="tab3">
    			<p>Check<p>
    		</div>
       </div>
    </div>
					</div>

				</div>



				<div class="span4">


					<div class="box">
						<span class="box-title">
						<p><font color="#FF8C00">Order Summary</font></p>
						</span>



		<table class="table table-condensed">
			<thead> 
				<tr> 
					<th width="55%">Item</th> 
					<th width="15%">Quantity</th> 
					<th width="15%">Price</th>
					<th width="15%">Total</th>  
				</tr> 
			</thead> 

			<tbody> 
				<tr> 
					<td>Vertical (default)</td> 
					<td >2</td> 
					<td><strong>$49.99</strong></td> 
					<td><strong>$99.98</strong></td> 
				</tr>

				<tr class="subt"> 
					<td colspan="3"><font color="red">[Vertical item $6.00]</font></td> 
					<td><font color="red"><strong>($6.00)</strong></font></td> 
				</tr> 

				<tr class="subt"> 
					<td colspan="3"><strong>Sub-total</strong></td> 
					<td><strong>$99.98</strong></td> 
				</tr> 

			</tbody> 
		</table>


<div class="total-box">
<span style="float:right">
<font class="total-box-label">
Total 
<font class="total-box-price">$99.98</font>
</font>
</span>
</div>

		<div class="form-actions">
			<div class="pull-right"> 
			<button type="submit" class="btn btn-success">Submit order</button>
			</div>
		</div> 


					</div>


				</div>

			    </div>

			</div>