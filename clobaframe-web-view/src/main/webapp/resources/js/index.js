/* 
 * index.js
 */

(function(window){
	var $ = window.jQuery;
	
	var methods = {
		init:function(){
			// test script i18n message
			var message = methods.getMessage('test.text1');
			$('.test-script-i18n .text').text(message);
			
			// test view object
			var viewObject = window.viewObject;
			var viewObjectText = '{id:"' + viewObject.id + '", name:"' + viewObject.name + '"}';
			$('.test-view-object .text').text(viewObjectText);
		},
		
		getMessage:function(code){
			var message = null;
			
			if (window.i18nMessageLocal){
				message = window.i18nMessageLocal[code];
			}
			
			if (!message && window.i18nMessageDefault){
				message = window.i18nMessageDefault[code];
			}
			
			return message;
		}
	};
	
	$(function(){
		methods.init();
	});
	
})(this);

