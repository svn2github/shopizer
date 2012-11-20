<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ page session="false" %>				
				





<div class="tabbable">

					<jsp:include page="/common/adminTabs.jsp" />
  					
  					 <div class="tab-content">

    					<div class="tab-pane active" id="catalogue-section">



								<div class="sm-ui-component">	
								
								
				<h3>
						Order ID 123578
					
				</h3>	
				<br/>


			<br/>
	
			<div class="span4">
			
			<h6>Customer address</h6>
			<address>
				<strong>Twitter, Inc.</strong><br>
				795 Folsom Ave, Suite 600<br>
				San Francisco, CA 94107<br>
				<abbr title="Phone">P:</abbr> (123) 456-7890
			</address>
			
			
			<br/>

			<dl class="dl-horizontal">
				<dt>Telephone</dt>
				<dd>444-656-8765</dd>

				<dt>Email</dt>
				<dd>test@test.com</dd>


				<dt>Date purchased</dt>
				<dd>2012-06-06</dd>
				
				<dt>Payment method</dt>
				<dd>Master Card</dd>
				
				<dt>Shipping method</dt>
				<dd>Canada Post</dd>
			</dl>
			

			
			</div>
			
			<div class="span4">
			
			<h6>Shipping address</h6>
			<address>
				<strong>Twitter, Inc.</strong><br>
				795 Folsom Ave, Suite 600<br>
				San Francisco, CA 94107<br>
				<abbr title="Phone">P:</abbr> (123) 456-7890
			</address>

			

			
			<br/>

		  <h6>Billing address</h6>
		  <address>
				<strong>Twitter, Inc.</strong><br>
				795 Folsom Ave, Suite 600<br>
				San Francisco, CA 94107<br>
				<abbr title="Phone">P:</abbr> (123) 456-7890
			</address>

			
			</div>
		
			<div class="span8">
			<div class="form-actions">
              <button class="btn btn-medium btn-primary" type="button">Edit</button>
              <button class="btn btn-medium btn-danger" type="button">Apply refund</button>
      </div>
      </div>
      
      <br/>
      
      <div class="span8">
      <table class="table table-bordered table-striped"> 
			<thead> 
				<tr> 
					<th colspan="2" width="55%">Item</th> 
					<th colspan="1" width="15%">Quantity</th> 
					<th width="15%">Price</th>
					<th width="15%">Total</th>  
				</tr> 
			</thead> 
			<tbody> 
				<tr> 
					<td width="10%">image</td>
					<td>Vertical (default)</td> 
					<td >2</td> 
					<td><strong>$49.99</strong></td> 
					<td><strong>$99.98</strong></td> 
				</tr> 

				<tr> 
					<td width="10%">image</td>
					<td>Item 2 this is just a test
						<br/>
						<ul>
							<li><i>color - red</i></li>
							<li><i>size - large</i></li>
						</ul>
					</td> 
					<td >1</td> 
					<td><strong>$19.99</strong></td> 
					<td><strong>$19.99</strong></td> 
				</tr> 

				<tr class="subt"> 
					<td colspan="2">&nbsp;</td> 
					<td colspan="2"><font color="red">[Vertical item $6.00]</font></td> 
					<td><font color="red"><strong>($6.00)</strong></font></td> 
				</tr> 

				<tr class="subt"> 
					<td colspan="2">&nbsp;</td> 
					<td colspan="2"><strong>Sub-total</strong></td> 
					<td><strong>$104.00</strong></td> 
				</tr> 

				<tr class="subt"> 
					<td colspan="2">&nbsp;</td> 
					<td colspan="2"><strong>Grand total</strong></td> 
					<td><strong>$104.00</strong></td> 
				</tr> 


			</tbody> 
		</table>
    </div>
    
    <div class="span8">
    
    
    					<div class="control-group">
                       <label>Status</label>
                       <div class="controls">
                                 <select>
                                 		<option>Processing</option>
                                 		<option>Delivered</option>
                                 </select>
                        </div>
              </div>
           
              <div class="control-group">
                       <label>History</label>
                       <div class="controls">
													<dl class="dl-horizontal">
														<dd>- We have received you order</dd>
														<dd>- One question, is it possible to send me the item in white color ?</dd>
														<dd>- We will sure do !</dd>
													</dl>
										   </div>
              </div>
              
     					<div class="control-group">
                       <label>Status</label>
                       <div class="controls">
                                 <textarea rows="3"></textarea>
                        </div>
              </div>
              
              <div class="form-actions">
              		<button class="btn btn-medium btn-primary" type="button">Save</button>
      				</div>
              
              <br/>
              
              
              <ul class="nav nav-pills">
								<li><a href="#">Send email invoice</a></li>
								<li class="disabled"><a href="#">Print packing slip</a></li>
							</ul> 
    
    
    </div>
    
    

			</div>


		</div>      			     