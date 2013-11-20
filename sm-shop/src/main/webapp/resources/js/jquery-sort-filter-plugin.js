(function($) {
  $.fn.sorted = function(customOptions) {
	alert('0');
    var options = {
      reversed: false,
      by: function(a) { return a.text(); }
    };
    alert('1');
    $.extend(options, customOptions);
    $data = $(this);
    alert('2');
    arr = $data.get();
    alert('3');
    arr.sort(function(a, b) {
      alert(a + b);
      var valA = options.by($(a));
      var valB = options.by($(b));
      alert(valA);
      alert(valB);
      if (options.reversed) {
        return (valA < valB) ? 1 : (valA > valB) ? -1 : 0;				
      } else {		
        return (valA < valB) ? -1 : (valA > valB) ? 1 : 0;	
      }
    });
    return $(arr);
  };
})(jQuery);