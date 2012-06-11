<%@include file="../include.jsp"%>

<portlet:renderURL var="showProductURL">
	<portlet:param name="action" value="showProductPage" />
</portlet:renderURL>

<div class="row">
	<div class="span2">



		<ul class="nav nav-list">
			<li class="nav-header">Books</li>
			<li><a href="#">Business books</a></li>
			<li class="active"><a href="#">Computer books</a></li>
			<li><a href="#">Novell books </a></li>
			<li><a href="#">Comics books </a></li>
		</ul>





	</div>




	<div class="span7">
		<h2>Browse books</h2>
		<p>With a bit of extra markup, it's possible to add any kind of
			HTML content like headings, paragraphs, or buttons into thumbnails.</p>
		<ul class="thumbnails">

			<li class="span2">
				<div class="thumbnail">
					<img alt="" src="http://placehold.it/260x180">
					<div class="caption">
						<h5>Thumbnail label</h5>
						<p>Cras justo odio, dapibus ac facilisis in, egestas eget
							quam. Donec id elit non mi porta gravida at eget metus. Nullam id
							dolor id nibh ultricies vehicula ut id elit.</p>
						<p>
							<a class="btn btn-primary" href="#">Cart</a> <a class="btn"
								href="#">Details</a>
						</p>
					</div>
				</div>
			</li>
			<li class="span2">
				<div class="thumbnail">
					<img alt="" src="http://placehold.it/260x180">
					<div class="caption">
						<h5>Thumbnail label</h5>
						<p>Cras justo odio, dapibus ac facilisis in, egestas eget
							quam. Donec id elit non mi porta gravida at eget metus. Nullam id
							dolor id nibh ultricies vehicula ut id elit.</p>
						<p>
							<a class="btn btn-primary" href="#">Cart</a> <a class="btn"
								href="#">Details</a>
						</p>
					</div>
				</div>
			</li>


			<li class="span2">
				<div class="thumbnail">
					<img alt="" src="http://placehold.it/260x180">
					<div class="caption">
						<h5>Thumbnail label</h5>
						<p>Cras justo odio, dapibus ac facilisis in, egestas eget
							quam. Donec id elit non mi porta gravida at eget metus. Nullam id
							dolor id nibh ultricies vehicula ut id elit.</p>
						<p>
							<a class="btn btn-primary" href="#">Cart</a> <a class="btn"
								href="#">Details</a>
						</p>
					</div>
				</div>
			</li>


		</ul>
	</div>
</div>