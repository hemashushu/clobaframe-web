/* 
 * index.js
 */

(function(window){
	var $ = window.jQuery;
	var co = window.co;
	
	var methods = {
		init: function(){
			// handle theme change
			$('.theme-switch ul li a').click(function(event){
				event.preventDefault();
				var url = $(this).attr('href');
				
				co.rest.get(url, function(data){
					// remove old theme resource
					$('head link[data-source="theme"]').remove();
					
					for(var idx in data) {
						var header = $(data[idx]);
						$('head').append(header);
					}
				});
			});
			
			// handle markdown
			$('.markdown input[type="hidden"]').each(function(){
				var textEle = $(this);
				var text = textEle.val();
				var markdown = window.marked(text, {sanitize: true});
				var nodes = $.parseHTML(markdown); // convert html text into DOM

				var markdownEle = textEle.parent();
				markdownEle.html('');
				markdownEle.append(nodes);
			});
		}
	};
	
	$(function(){
		methods.init();
	});
	
})(this);

