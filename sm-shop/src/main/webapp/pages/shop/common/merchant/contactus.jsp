<%
response.setCharacterEncoding("UTF-8");
response.setHeader("Cache-Control","no-cache");
response.setHeader("Pragma","no-cache");
response.setDateHeader ("Expires", -1);
%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="/WEB-INF/shopizer-tags.tld" prefix="sm" %> 
 
<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>


<div class="row show-grid">
<!-- START LEFT-SIDE CONTACT FORM AREA -->
                            <div class="contact-form span8">
                                <div id="result">

                                </div>
                                <form action="send-form-email.php" method="POST" id="validForm" name="contactForm">
                                    <div class="control-group">
                                        <label for="inputName" class="control-label">NAME<sup>*</sup></label>
                                        <div class="controls">
                                            <input type="text" placeholder="Name" id="inputName" class="span4" name="name">
                                            <span class="help-inline" style="display: none;">Please fill your name</span>
                                        </div>
                                    </div>
                                    <div class="control-group">
                                        <label for="inputEmail" class="control-label">EMAIL<sup>*</sup></label>
                                        <div class="controls">
                                            <input type="text" placeholder="Email" id="inputEmail" class="span4" name="email">
                                            <span class="help-inline" style="display: none;">Please correct your email</span>
                                        </div>
                                    </div>
                                    <div class="control-group">
                                        <label class="control-label" for="textarea">COMMENT<sup>*</sup></label>
                                        <div class="controls">
                                            <textarea id="textarea" class="span8" rows="4" name="comment"></textarea>
                                            <span class="help-inline" style="display: none;">Please write a comment</span>
                                        </div>
                                    </div>
                                    <!--?php
                                            require_once('config.php');
                                            require_once('recaptchalib.php');
                                            if($use_recaptcha === true){
                                                echo recaptcha_get_html($publickey);
                                            }
                                        ?-->
                                    <div class="control-group form-button-offset">
                                        <input type="submit" value="Send Message" class="btn">
                                    </div>
                                </form>
                            </div>
<!-- END LEFT-SIDE CONTACT FORM AREA -->

<!-- BEGIN RIGHT-SIDE CONTACT FORM AREA -->
                                <div class="contact-info span4">
                                    <h2>bizstrap</h2>
<!-- COMPANY ADDRESS -->                                    
                                    <address>
                                        1234 Main Street,<br>
                                        Anytown,<br>
                                        USA<br>
                                        Phone: 123 456 7890<br>
                                        Fax: +49 123 456 7891<br>
                                        Email: <a href="mailto:hello@example.com">hello@example.com</a><br>
                                        Web: <a href="#">example.com</a>
                                    </address>
<!-- SOCIAL ICONS -->                                                                
                                    <ul class="socials unstyled">
                                        <li><a class="flickr" href="#"></a></li>
                                        <li><a class="twitter" href="#"></a></li>
                                        <li><a class="facebook" href="#"></a></li>
                                        <li><a class="youtube" href="#"></a></li>
                                        <li><a class="dribbble" href="#"></a></li>
                                        <li><a class="pinterest" href="#"></a></li>
                                    </ul>
<!-- HOURS OF OPERATION -->                                    
                                    <h2>Hours of Business Operation</h2>
                                        <table class="table">
                                        <tbody>
                                            <tr>
                                                <td class="small">Monday:</td>
                                                <td class="bold">8am to 6pm</td>
                                            </tr>
                                            <tr>
                                                <td class="small">Tuesday:</td>
                                                <td class="bold">8am to 6pm</td>
                                            </tr>
                                            <tr>
                                                <td class="small">Wednesday:</td>
                                                <td class="bold">8am to 6pm</td>
                                            </tr>
                                            <tr>
                                                <td class="small">Thursday:</td>
                                                <td class="bold">8am to 6pm</td>
                                            </tr>
                                            <tr>
                                                <td class="small">Friday:</td>
                                                <td class="bold">8am to 6pm</td>
                                            </tr>
                                            <tr>
                                                <td class="small">Saturday:</td>
                                                <td>Closed</td>
                                            </tr>
                                            <tr>
                                                <td class="small">Sunday:</td>
                                                <td>Closed</td>
                                            </tr>
                                        </tbody>
                                        </table>
                                </div>
<!-- END RIGHT-SIDE CONTACT FORM AREA -->

<!-- GOOGLE MAP -->                                
<iframe src="http://maps.google.com/maps?f=q&amp;source=s_q&amp;hl=en&amp;geocode=&amp;q=Mockingbird+Station,+Dallas,+TX&amp;aq=1&amp;oq=mockinStation,+Dallas,+TX&amp;sll=32.786144,-96.788897&amp;sspn=0.00929,0.018947&amp;ie=UTF8&amp;hq=&amp;hnear=Mockingbird+Station,+Dallas,+Texas+75206&amp;t=m&amp;ll=32.845774,-96.772385&amp;spn=0.043266,0.061712&amp;z=14&amp;iwloc=A&amp;output=embed" style="width: 100%; height: 600px; border: none;"></iframe><br><small><a style="color:#0000FF;text-align:left" href="http://maps.google.com/maps?f=q&amp;source=embed&amp;hl=en&amp;geocode=&amp;q=Mockingbird+Station,+Dallas,+TX&amp;aq=1&amp;oq=mockinStation,+Dallas,+TX&amp;sll=32.786144,-96.788897&amp;sspn=0.00929,0.018947&amp;ie=UTF8&amp;hq=&amp;hnear=Mockingbird+Station,+Dallas,+Texas+75206&amp;t=m&amp;ll=32.845774,-96.772385&amp;spn=0.043266,0.061712&amp;z=14&amp;iwloc=A">View Larger Map</a></small>
 </div>
