/* 
 * common.js
 */

/**
 * Object URL builder
 * 
 * @param {type} window
 * @returns {undefined}
 */
(function(window){
	
	var methods = {
		objectUrl:{
			page:function(page, locale, revision){
				var url = new co.url();
				if (page.urlName && page.urlName.length > 0) {
					url.path(page.urlName);
				}else{
					url.path('page/' + page.pageKey.name);
				}
				
				if (locale) {
					url.param('locale', locale);
				}
				
				if (revision) {
					url.param('revision', revision);
				}
				return url.build();
			}
		}
	};
	
	// extend co object.
	var co = window.co;
	co.extend(co, methods);
	
})(this);
