<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>

<head>

 

    <!-- Framework CSS -->

    <link rel="stylesheet" href="<%=request.getContextPath()%>/resources/css/blueprint/screen.css" type="text/css" media="screen, projection">

    <link rel="stylesheet" href="<%=request.getContextPath()%>/resources/css/blueprint/print.css" type="text/css" media="print">

    <!--[if lt IE 8]><link rel="stylesheet" href="<%=request.getContextPath()%>/resources/css/blueprint/ie.css" type="text/css" media="screen, projection"><![endif]-->

 

      <style type="text/css">

            /**.container {text-align:left;}**/

            .container .label {

                  font-family: Verdana, Arial, Helvetica, sans-serif; 
                  font-size : 160%;

            }
          
          
                  .textfield {

                                    font-family: Verdana, Arial, Helvetica, sans-serif; 
                                    font-size: 18px; 
                                    color: #000000; 
                                    background-color: #FFFFCC; 
                                    padding: 2px; 
                                    height: 30px; 
                                    border: 1px solid #7F9DB9; 

 

            }

 

            	.textarea {

                                    font-family: Verdana, Arial, Helvetica, sans-serif; 
                                    font-size: 18px; 
                                    color: #000000; 
                                    background-color: #FFFFCC; 
                                    padding: 2px; 
                                    height: 120px; 
                                    border: 1px solid #7F9DB9; 



 

            }

            	.select {

                                    font-family: Verdana, Arial, Helvetica, sans-serif; 
                                    font-size: 18px; 
                                    color: #000000; 
                                    background-color: #FFFFCC; 
                                    padding: 2px; 
                                    height: 30px; 
                                    border: 1px solid #7F9DB9; 



 

            }

 

            .medium {
                                    width: 300px; 
            }

 

            .short {
                                    width: 120px; 
            }

      </style>
      
      
      
       <script type="text/javascript" src="<c:url value="/resources/jquery/jquery-1.5.1.min.js" />"></script>
 

            <script type="text/javascript"> 

                        $(document).ready(function(){ 
                                    $("#addTag").click(function() { 

                                                var i = $("#tags > span").size();

                                                i = i +1;

                                                $("#tags").append("<span id=\"tagline-" + i + "\"><input name=\"tags\" type=\"text\" id=\"tags\" class=\"textfield short\"><span class=\"label\">,</span> (<a href=\"#\" class=\"removeTag\" id=\"" + i + "\" >Remove</a>)<br/></span>");

                                                init();

                                    });

                                    init();
                        });

 

                        function init() {

                                    $(".removeTag").click(function() { 
                                                     //var parent = $(this).parent().remove();

                                                var id = $(this).attr("id");

                                                $("#tagline-" + id).remove();

                                                //alert(id);

                                    });

                        }

 

                        function index() {

						var form = $("#informations");
						var query = $("#informations").serializeArray();



						//var json = '';
						json = {};
						$('input:not([type=checkbox]), input[type=checkbox]:selected, text,select,select-one,textarea', form).each( 
							function() {
								var name = $(this).attr('name'); 
								var val = $(this).val(); 
								var type = $(this).attr('type');
								//alert(name + ' ' + val + ' ' + type);
								if(type=='checkbox' || type=='radio' || type=='text' || type=='select' || type=='select-one' || type=='textarea') {


									var obj = new jsonobject(null,name,val);
									if(json[name]!=null) {
										//need to create an array
										o = json[name];
										if(o instanceof Array) {
											//alert('object exist and is array');
											o.push(obj);
										} else {
											//alert('will create an array from object');
											var arr = new Array();
											arr.push(o);//original object
											arr.push(obj);
											json[name]=arr;
										}
									} else {
										json[name]=obj;
									}
								}
							} 
						);

 						var strStr = '';
						//var arrStr = '';
						$.each(json, function(key, value) {

							if(value instanceof jsonobject) {
								var str = '\"' + key + '\":\"' + value.val + '\",';
								//alert(str);
								strStr = strStr + str;
							} else if(value instanceof Array) {
								var arr = '\"' + key + '\":[';
								var content = '';
								var i = 1;
								$.each(value, function() { 
									content = content + '\"' + this.val + '\"';
									if(i<value.length) {
										content = content + ',';
									}
									i++;
								});
								//alert(arr + content + ']');
								content = arr + content + ']';
								strStr = strStr + content + ',';
							}
							
						});
						strStr = strStr.slice(0,strStr.length-1); 
						//alert(strStr);


						var indexable ='{'  + strStr + '}';

						//alert(indexable);

						sendRequest(indexable,'profile_en','profile');

                  }


				function jsonobject(objectname,name,val){ 
					this.objectname=objectname;
					this.name=name 
					this.val=val 
				}
				
				function sendRequest(json,collection,object) {
	




					$.ajax({

						  url: "<%=request.getContextPath()%>/index/product_en/product/",
						  cache: false,
						  type:"POST",
						  dataType:"json",
						  data:json,
						  contentType:"application/json;charset=UTF-8",
						  success: function(data) {
							  alert('success indexing data');
							  if(data.success) {
								  alert(data.success);
							  }
							  if(data.exception) {
								  alert(data.exception);
							  }
						  },
						  faillure: function() {alert('fail');}
					});

					
					
				}


 

      </script>

 

</head>

 

<body>

 

 

<div class="container">
     
     
      
      <div class="span-20 last">

 

 

      <form name="informations" id="informations">


	  <h2>Product : </h2>
      <!--<div class="span-19 prepend-1 last"><span class="label">{</span></span></div>-->

      <div class="span-18 prepend-2 last"><span class="label">{</span></div>

      

      <div class="span-20 prepend-4 last">

 

      <p>

      <table>
      
            <tr>

                  <td><span class="label">"id" : </span></td><td><input name="id" type="text" class="textfield medium"><span class="label">,</span></td>

            </tr>

            <tr>

                  <td><span class="label">"product name" : </span></td><td><input name="name" type="text" class="textfield medium"><span class="label">,</span></td>

            </tr>
            
            <tr>

                  <td><span class="label">"category" : </span></td><td><input name="category" type="text" class="textfield medium"><span class="label">,</span></td>

            </tr>

            <tr>

                  <td><span class="label">"price" : </span></td><td><input name="price" type="text" class="textfield short"><span class="label">,</span></td>

            </tr>
            
            <tr>

                  <td><span class="label">"date available" : </span></td><td><input name="availability" type="text" class="textfield short"><span class="label">,</span></td>

            </tr>
            

            <tr>

                  <td><span class="label">"available" : </span></td><td><select name="available" class="select short"><option value="true">yes</option><option value="false">no</option><span class="label">,</span></td>

            </tr>

            <tr>

                  <td><span class="label">"lang" : </span></td><td><select name="lang" class="select short"><option value="en">english</option><option value="fr">french</option><span class="label">,</span></td>

            </tr>

            <tr>

                  <td><span class="label">"description" : </span></td><td><textarea name="description" class="textarea" id="description"></textarea><span class="label">,</span></td>

            </tr>

            <tr>

                  <td><span class="label">"tags" : </span><br/>&nbsp;&nbsp;&nbsp;<span style="font-size:11px;"><a href="#" id="addTag">Add tag</a></span></td><td><span class="label">[</span></td>

            </tr>

      </table>

      </p>

 

      </div>
      
      
      <div class="span-15 prepend-10 last">

            <p>

 

                        <span id="tags">

                              <span id="tagline-1"><input name="tags" type="text" id="tags" class="textfield short"><span class="label">,</span> (<a href="#" class="removeTag" id="1">Remove</a>)<br/></span>

                        </span>

 

            </p>

      </div>

 

 

      <div class="span-11 prepend-8 last">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<span class="label">]</span></div>

 

 

      <div class="span-18 prepend-2 last"><span class="label">}</span></div>

      <!-- <div class="span-19 prepend-1 last"><span class="label">}</span></div>-->

 

      <div class="span-5 prepend-14 last"><input type="button" value="Submit" onClick="javascript:index();"></div>

 

      </form>

 

</div>

</div>
</body>
</html>