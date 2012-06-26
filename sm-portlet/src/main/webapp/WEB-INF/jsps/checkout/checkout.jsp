<%@include file="../include.jsp"%>

<portlet:renderURL var="showProductURL">
	<portlet:param name="action" value="showProductPage" />
</portlet:renderURL>

<div class="sm">

<div class="row">

<form>

 

      <div class="span4">

           

      <p><span class="badge badge-warning">1</span> <font color="#FF8C00">Billing information</font></p>

 

      <div class="row">

      <div class="span2">

 

                  <div class="control-group">

                        <label>First</label>

                        <div class="controls">

                                    <input type="text" class="input-small" id="first">

                        </div>

                  </div>

                  <div class="control-group">

                        <label>Company</label>

                        <div class="controls">

                                    <input type="text" class="input-small" id="company">

                        </div>

                  </div>

      </div>

 

      <div class="span2">

 

                  <div class="control-group">

                        <label>Last</label>

                        <div class="controls">

                                    <input type="text" class="input-small" id="last">

                                    <!--<p class="help-block">Supporting help text</p>-->

                        </div>

                  </div>

                  <div class="control-group">

                        <label>Email</label>

                        <div class="controls">

                                    <input type="text" class="input-small" id="email">

     

                        </div>

                  </div>

      </div>

 

      <div class="span4">

 

                  <div class="control-group">

                        <label>Address</label>

                        <div class="controls">

                                    <input type="text" class="input-xlarge" id="address1">

                                    <input type="text" class="input-xlarge" id="address2"> 

                        </div>

                  </div>

 

                  <div class="control-group">

                        <label>City</label>

                        <div class="controls">

                                    <input type="text" class="input-large" id="city">

                        </div>

                  </div>

 

                  <div class="control-group">

                        <label>State / Province</label>

                        <div class="controls">

                                    <input type="text" class="input-large" id="state">

                        </div>

                  </div>

 

                  <div class="control-group">

                        <label>Country</label>

                        <div class="controls">

                                    <input type="text" class="input-large" id="country">

                        </div>

                  </div>

 

                  <div class="control-group">

                        <label>Postal code</label>

                        <div class="controls">

                                    <input type="text" class="input-large" id="postal code">

                        </div>

                  </div>

 

                  <div class="control-group">

                        <label>Phone</label>

                        <div class="controls">

                                    <input type="text" class="input-large" id="phone">

                        </div>

                  </div>

 

                        <label class="checkbox"> <input type="checkbox" id="useAddress"> Ship to this address</label>

 

      </div>

      </div>

 

      </div>

      <div class="span4">

   

        

                     <p><span class="badge badge-warning">2</span> <font color="#FF8C00">Shipping method</font></p>

     

                     <div class="control-group">

                        <label class="control-label">Shipping options</label>

                        <div class="controls">

                              <label class="radio">

                                    <input type="radio" name="optionsRadios" id="optionsRadios1" value="option1" checked>

                                    Shipping fast to Canada - $9.99

                              </label>

                              <label class="radio">

                                    <input type="radio" name="optionsRadios" id="optionsRadios2" value="option2">

                                    Shipping slow to Canada - $2.99

                              </label>

                        </div>

                   </div>

                     <p><span class="badge badge-warning">3</span> <font color="#FF8C00">Payment methods</font></p>

     

                     <div class="control-group">

                        <label class="control-label">Shipping options</label>

                        <div class="controls">

                              <label class="radio">

                                    <input type="radio" name="optionsRadios" id="optionsRadios1" value="option1" checked>

                                    Shipping fast to Canada - $9.99

                              </label>

                              <label class="radio">

                                    <input type="radio" name="optionsRadios" id="optionsRadios2" value="option2">

                                    Shipping slow to Canada - $2.99

                              </label>

                        </div>

                   </div>

 

      </div>

 

      <div class="span4">

   

            <p><font color="#FF8C00">Order Summary</font></p>

 

 

            <table class="table table-striped table-bordered table-condensed">

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

 

                        <tr class="subt">

                              <td colspan="3"><h3>Grand total</h3></td>

                              <td><h3>$99.98</h3></td>

                        </tr>

 

                  </tbody>

            </table>

 

 

            <div class="form-actions">

                  <div class="pull-right">

                  <button type="submit" class="btn btn-success">Submit order</button>

                  </div>

            </div>

 

 

      </div>

</form>

</div>

</div>
