/*!
 * misc.js
 *
 * Some functions depends on jQuery, check the function comments for details.
 */

/**
 * Common objects.
 * Objects and variables.
 *
 * @param {type} window
 * @returns {undefined}
 */
(function(window){

	/**
	 * Defines the top object 'co' for store most object and functions.
	 */
	var methods = {
		
		/**
		 * Merge source object's properties into the target object.
		 * @param {type} target
		 * @returns {unresolved}
		 */
		extend:function(target) {
			if (arguments.length <= 1) {
				return target;
			}

			for (var argIdx = 1; argIdx < arguments.length; argIdx++){
				var argument = arguments[argIdx];
				if (argument === undefined || argument === null) {
					continue;
				}

				// copy all properties (overwrite)
				for(var name in argument) {
					target[name] = argument[name];
				}
			}

			return target;
		} //,

		/**
		 * Store the page scripts, the function 'init' will be invoke in each page request.
		 *
		 * pages.pageName = {
		 *		init:function(){...},
		 *		otherMethod:function(){...}
		 *		};
		 *
		 * There is a script at the end of page body:
		 * pages.pageName.init();
		 * @type
		 */
//		pages:{
//			//pageName:{
//			//	init:function(){},
//			//	other_func:function(){}
//			//}
//		},

		/**
		 * Global scripts, such as the global 'init' function.
		 */
//		global:{
//			init:function(){}
//		},
		
		/**
		 * Page options, i.e. some values from backend to script (frontend):
		 * {
		 *		"someSwitch":false,
		 *		"authenticated":true, ...
		 * };
		 *
		 * @type type
		 */
//		pageOptions:{}

	};

	var exports = {
		co: methods
	};
	
	// export to the window object.
	methods.extend(window, exports);

})(this);

/**
 * Collection.
 *
 * @param {type} window
 * @returns {undefined}
 */
(function(window){

	var methods = {

		/**
		 * Check the DOM objects that exists in the jQuery object whether equals.
		 *
		 * e.g.
		 * var left = $('div.one');
		 * var right = $('div.two');
		 * var result = jqEquals(left, right); // result will be false.
		 *
		 * null & null = true.
		 * null & element(s) = false.
		 * same element(s) = true.
		 * more elements & less elements = false.
		 * no element & no element = false.
		 *
		 * @param {type} left
		 * @param {type} right
		 * @returns {Boolean}
		 */
		jQueryObjectEquals : function(left, right) {
			if (left === null && right === null) {
				return true;
			}

			if (left === null || right === null) {
				return false;
			}

			if (left.length !== right.length) {
				return false;
			}

			if (left.length === 0){
				return false;
			}

			for (var idx=0; idx < left.length; idx++) {
				if (left[idx] !== right[idx]) {
					return false;
				}
			}

			return true;
		},

		defaultEquals : function(left, right){
			return left === right;
		},

		/**
		 * Object set collection.
		 * For adding none-duplicated objects.
		 *
		 * @param {type} objectEqualsFunc
		 * @returns {undefined}
		 */
		set : function (objectEqualsFunc) {
			var items = [];
			var equals = (objectEqualsFunc === undefined ? methods.defaultEquals : objectEqualsFunc);

			var indexOf = function(obj){
				for(var idx in items){
					if (equals(items[idx], obj)){
						return idx;
					}
				}
				return -1;
			};

			this.size = function() {
				return items.length;
			};

			this.isEmpty = function() {
				return items.length === 0;
			};

			this.getAll = function() {
				return items;
			};

			this.add = function(obj){
				if (!this.contains(obj)){
					items.push(obj);
				}
				return this;
			};

			/**
			 * Remove the specify object.
			 *
			 * @param {type} obj
			 * @returns {undefined}
			 */
			this.remove = function(obj){
				var idx = indexOf(obj);
				if (idx >=0){
					items.splice(idx, 1);
				}
			};

			this.contains = function(obj){
				return (indexOf(obj) !== -1);
			};

			this.clear = function(){
				items.splice(0, items.length);
			};
		},

		/**
		 * Object map collection.
		 *
		 * @param {type} objectKeyEqualsFunc
		 * @returns {undefined}
		 */
		map : function (objectKeyEqualsFunc) {

			/*
			 * items contains a set of object: {key:item_key, value:item_value}
			 * @type Array
			 */
			var items = [];
			var equals = (objectKeyEqualsFunc === undefined ? methods.defaultEquals : objectKeyEqualsFunc);

			var indexOfKey = function(key){
				for(var idx in items){
					var item = items[idx];
					if (equals(item.key, key)){
						return idx;
					}
				}
				return -1;
			};

			this.size = function() {
				return items.length;
			};

			this.isEmpty = function() {
				return items.length === 0;
			};

			/**
			 * return the object {key:item_key, value:item_value} array.
			 * @returns {type.map.items}
			 */
			this.getAll = function() {
				return items;
			};

			/**
			 * Get value by key.
			 *
			 * @param {type} key
			 * @returns {unresolved}
			 */
			this.get = function(key) {
				var idx = indexOfKey(key);
				if (idx >=0){
					return items[idx].value;
				}else{
					return null;
				}
			};

			/**
			 * If the key has exists, the new value will replace
			 * the old one.
			 *
			 * @param {type} key
			 * @param {type} value
			 * @returns {_L52.co.map}
			 */
			this.put = function(key, value){
				var idx = indexOfKey(key);
				if (idx >= 0){
					// replace the old value.
					items[idx].value = value;
				}else{
					var item = {key:key, value:value};
					items.push(item);
				}
				return this;
			};

			this.remove = function(key){
				var idx = indexOfKey(key);
				if (idx >=0){
					items.splice(idx, 1);
				}
			};

			this.containsKey = function(key){
				return (indexOfKey(key) !== -1);
			};

			this.clear = function(){
				items.splice(0, items.length);
			};
		},

		defaultComparator : function(left, right) {
			if (left === undefined && right === undefined) {
				return 0;
			}else if (right === undefined) {
				return 1;
			}else if (left === undefined) {
				return -1;
			}

			if (left === right) {
				return 0;
			}else if (left > right) {
				return 1;
			}else {
				return -1;
			}
		},

		/**
		 * Collection query.
		 * to filter, order an object collection.
		 *
		 * Consider these object array:
		 * var array = [{id:1, name:'c', score:70},
		 *				{id:2, name:'b', score:50},
		 *				{id:3, name:'a', score:65},
		 *				{id:4, name:'d', score:80}];
		 *
		 * execute the query:
		 * var r = new co.query(array)
		 *	.whereGreaterThan('score', 60)
		 *	.orderBy('name')
		 *	.select(['id','score'])
		 *	.list();
		 *
		 * the result will be:
		 * [{id:3, score:65},{id:1, score:70},{id:4, score:80}]
		 *
		 * @param {type} array
		 * @returns {undefined}
		 */
		query : function(array){
			var items = array;
			var predicates = [];
			var comparators = [];
			var selects = [];
			var limits = 0;

			this.where = function(filterFunc) {
				/* The filterFunc must be a function.
				 *
				 * filterFunc = function(value){
				 * if (value is ok)
				 * 	return true;
				 * else
				 * 	return false;
				 * }
				 *
				 */

				predicates.push(filterFunc);
				return this;
			};

			this.whereEquals = function(name, val){
				return this.where(function(e){
					return (e[name] === val);
				});
			};

			this.whereGreaterThan = function(name, val){
				return this.where(function(e){
					return (e[name] > val);
				});
			};

			this.whereLessThan = function(name, val){
				return this.where(function(e){
					return (e[name] < val);
				});
			};

			this.order = function(compareFunc) {
				/* The compareFunc must be a function.
				 *
				 * compareByAscendingOrderFunc = function(left, right){
				 * if (left > right) {
				 *	return 1;
				 * }else if (left < right) {
				 *	return -1;
				 * } else {
				 *  return 0;
				 * }
				 */
				comparators.push(compareFunc);
				return this;
			};

			this.orderBy = function(name) {
				return this.order(function(left, right){
					if (left[name] > right[name]) {
						return 1;
					}else if (left[name] < right[name]){
						return -1;
					}else {
						return 0;
					}
				});
			};

			this.orderByDesc = function(name) {
				return this.order(function(left, right){
					if (left[name] > right[name]) {
						return -1;
					}else if (left[name] < right[name]){
						return 1;
					}else {
						return 0;
					}
				});
			};

			this.limit = function(value) {
				limits = value;
				return this;
			};

			this.select = function(names){
				/*
				 * keys must be the key array.
				 * @type Array
				 */
				for(var idx in names){
					selects.push(names[idx]);
				}
				return this;
			};

			this.list = function() {
				// combine predicates
				if (predicates.length > 0) {
					var filter = function(value) {
						for (var idx in predicates) {
							if (!predicates[idx](value)){
								return false;
							}
						}
						return true;
					};

					var filtered = [];
					for (var idx in items){
						if (filter(items[idx])){
							filtered.push(items[idx]);
						}
					}

					items = filtered;
				}

				// combine comparators
				if (comparators.length > 0) {
					var compare = function(left, right) {
						for(var idx = 0; idx < comparators.length; idx++){
							var result = comparators[idx](left, right);
							if (result !== 0) {
								return result;
							}
						}
						return 0;
					};

					var copy = [];
					for(var idx in items){
						copy.push(items[idx]);
					}

					copy.sort(compare);
					items = copy;
				}

				// pick selected properties
				if (selects.length > 0) {
					var selected = [];
					for (var idx in items) {
						var item = items[idx];
						var subItem = {};
						for (var subIdx in selects){
							var name = selects[subIdx];
							subItem[name] = item[name];
						}
						selected.push(subItem);
					}

					items = selected;
				}

				// limit items
				if (limits > 0){
					var limited = [];
					var actual = (limits > items.length ? items.length : limits);
					for (var idx=0; idx<actual; idx++){
						limited.push(items[idx]);
					}

					items = limited;
				}

				return items;
			};

			this.first = function(){
				var result = this.list();
				return (result.length > 0 ? result[0] : null);
			};
		}
	};

	// extend co object.
	var co = window.co;
	co.extend(co, methods);

})(this);


/**
 * Extend Functions
 *
 * @param {type} window
 * @returns {undefined}
 */
(function(window){

	/**
	 * html escape
	 * @returns {String.prototype.escapeHtml@call;replace|String.prototype.escapeHtml.content}
	 */
	String.prototype.escapeHtml = function(){
			var content = this.replace(/&/g, '&amp;');
			content = content.replace(/>/g, '&gt;');
			content = content.replace(/</g, '&lt;');
			content = content.replace(/"/g, '&quot;');
			content = content.replace(/\n/g, '<br/>'); // TODO:: replace with <p>...</p>
			return content;
		};

	String.prototype.unescapeHtml = function(){
			var content = this.replace(/<br\/>/ig, '\n');
			content = content.replace(/<br>/ig, '\n');
			content = content.replace(/&lt;/g, '<');
			content = content.replace(/&gt;/g, '>');
			content = content.replace(/&quot;/g, '"');
			content = content.replace(/&amp;/g, '&');
			return content;
		};

	String.prototype.left = function(length, showDash) {
		if (this.length <= length) {
			return this;
		}
		
		var part = this.substring(0, length);
		if (showDash !== undefined && showDash === true) {
			part += "...";
		}
		return part;
	};
		
	var padZero = function(number){
		var r = String(number);
		if ( r.length === 1 ) {
			r = '0' + r;
		}
		return r;
	};

	var Date = window.Date;
	if (Date.prototype.toISOString === undefined){
		/**
		 * ie-fix (6,7,8) add Date.toISOString() function to 'Date' prototype
		 * Output as 'YYYY-MM-DDTHH:mm:ss.sssZ'
		 * @returns {String}
		 */
		Date.prototype.toISOString = function(){
			var date = this;
			return date.getUTCFullYear()+'-'
				+ padZero(date.getUTCMonth()+1)+'-'
				+ padZero(date.getUTCDate())+'T'
				+ padZero(date.getUTCHours())+':'
				+ padZero(date.getUTCMinutes())+':'
				+ padZero(date.getUTCSeconds())+'Z';
		};
	};
})(this);

/**
 * I18n message resolver.
 *
 * The global variable 'i18nMessageDefault' and 'i18nMessageLocal' defines
 * all messages text, in the form as: {codename1:'text1', codename2:'text2'}.
 *
 * Get text by code:
 * considers message defines as
 * {'test.text1':'hello world!'}
 *
 * var text = co.message('test.text1');
 *
 * Get text by code and parameters:
 * considers message defines as
 * {'test.text2':'hello {0}, now is {1}.'}
 *
 * var text = co.message('test.text2',['Dude', new Date()]);
 *
 * Depends on jQuery.
 *
 * @param {type} window
 * @returns {undefined}
 */
(function(window){
	var messages = null;
	var messagesLocal = null;

	var $ = window.jQuery;

	var methods = {

		/**
		 * Invoke this function when document ready.
		 *
		 * @param {type} defaults
		 * @param {type} local
		 * @returns {undefined}
		 */
		setSource : function(defaults, local){
			messages = (defaults !== undefined ?
						defaults :
						(window.i18nMessageDefault !== undefined ? window.i18nMessageDefault : null));

			messagesLocal = (local !== undefined ?
						local :
						(window.i18nMessageLocal !== undefined ? window.i18nMessageLocal : null));
		},

		message : function(code, params, defaultText){
			var content = null;

			if (messagesLocal === null){
				content = messages[code];
			}else{
				content = messagesLocal[code];

				// get the failback text (default language text)
				if (content === undefined){
					content = messages[code];
				}
			}

			if (content === undefined || content === null){
				// no text define in the default/locale message list.
				return (defaultText === undefined ? '[' + code + ']' : defaultText);
			}

			// replace placeholders
			if (params !== undefined){
				for(var idx in params){
					var regexp = new RegExp('\\{' + idx + '(,\\w+)*\\}','g');
					content = content.replace(regexp, params[idx]);
				}
			}

			return content;
		}
	};

	methods.setSource();

	// export to co.
	var co = window.co;
	co.extend(co, methods);

})(this);

/**
 * REST support.
 *
 * Depends on jQuery.
 *
 * @param {type} window
 * @returns {undefined}
 */
(function(window){

	var $ = window.jQuery;

	var methods = {
		url:function() {
			/*
			 * host is the api server url, such as 'http://some.com/',
			 * it's allowed include the api base path, exam. 'http://some.com/api/'
			 * the url must ends with symbol '/'.
			 */
			var host = '/';
			var version = null; // the api version, default is null.
			var path = ''; // path must not start with symbol '/'.
			var clientId = null; // see: http://oauth.net/2/
			var accessToken = null; // the authentication token
			var params = {}; // the query parameters

			this.host = function(value) {
				host = value;
				return this;
			};

			this.version = function(value) {
				version = value;
				return this;
			};

			this.path = function(value) {
				path = value;
				return this;
			};

			this.clientId = function(value) {
				clientId = value;
				return this;
			};

			this.accessToken = function(value) {
				accessToken = value;
				return this;
			};

			this.param = function(name, value) {
				params[name] = value;
				return this;
			};

			/**
			 *
			 * @param {type} value an object, exam. {name1=value1, name2=value2}
			 */
			this.params = function(value) {
				params = value;
				return this;
			};

			this.build = function(){
				var builder = [];
				builder.push(host);

				if (version !== null) {
					builder.push(version);
					builder.push('/');
				}

				builder.push(path);

//				if (params === undefined || params === null) {
//					params = {};
//				}

				if (clientId !== null) {
					params['client_id'] = clientId;
				}

				if (accessToken !== null) {
					params['access_token'] = accessToken;
				}

				var queryBuilder = [];

				for(var name in params) {
					var paramBuilder = [];
					paramBuilder.push(name);

					var value = params[name];
					if (value !== undefined && value !== null) {
						paramBuilder.push('=');
						paramBuilder.push(encodeURIComponent(value));
					}

					queryBuilder.push(paramBuilder.join(''));
				}

				if (queryBuilder.length > 0) {
					builder.push("?");
					builder.push(queryBuilder.join('&'));
				}

				return builder.join('');
			};
		},

		/**
		 * REST http client.
		 *
		 * @type type
		 */
		rest:{

			/**
			 *
			 * @param {type} data is a JSON object.
			 * @returns {undefined}
			 */
			defaultDoneFunc:function(data){
			},

			/**
			 *
			 * @param {type} statusCode is the HTTP response status code, such as 404, 401.
			 * @param {type} result is the JSON object for descript error, or NULL in some endpoints.
			 *  result = {code:string, message:string, data:{key1:value1, key2:value2...}}
			 * @returns {undefined}
			 */
			defaultFailFunc:function(statusCode, result){
			},

			parseResultObject:function(jqXHR) {
				var contentType = jqXHR.getResponseHeader('Content-Type');
				if (contentType !== null && contentType.indexOf('application/json') >= 0){
					if (jqXHR.responseJSON){
						return jqXHR.responseJSON;
					}else{
						return $.parseJSON(jqXHR.responseText);
					}
				}else{
					return null;
				}
			},

			get:function(url, done, fail) {
				$.ajax({
					type:'GET',
					url:url
				}).done(function(data, textStatus, jqXHR){
					if (done !== undefined){
						done(data);
					}
				}).fail(function(jqXHR, textStatus){
					if (fail !== undefined) {
						fail(jqXHR.status, methods.rest.parseResultObject(jqXHR));
					}
				});
			},

			post:function(url, form, done, fail){
				$.ajax({
					type:'POST',
					url:url,
					data:(form === undefined || form === null) ? {}:form
				}).done(function(data, textStatus, jqXHR){
					if (done !== undefined){
						done(data);
					}
				}).fail(function(jqXHR, textStatus){
					if (fail !== undefined) {
						fail(jqXHR.status, methods.rest.parseResultObject(jqXHR));
					}
				});
			},

			put:function(url, form, done, fail) {
				$.ajax({
					type:'PUT',
					url:url,
					data:(form === undefined || form === null) ? {}:form
				}).done(function(data, textStatus, jqXHR){
					if (done !== undefined){
						done(data);
					}
				}).fail(function(jqXHR, textStatus){
					if (fail !== undefined) {
						fail(jqXHR.status, methods.rest.parseResultObject(jqXHR));
					}
				});
			},

			del:function(url, done, fail){
				$.ajax({
					type:'DELETE',
					url:url
				}).done(function(data, textStatus, jqXHR){
					if (done !== undefined){
						done(data);
					}
				}).fail(function(jqXHR, textStatus){
					if (fail !== undefined) {
						fail(jqXHR.status, methods.rest.parseResultObject(jqXHR));
					}
				});
			},

			/**
			 * 
			 * @param {type} url
			 * @param {type} data the result of FileReader.readAsBinaryString.
			 * @param {type} progress
			 * @param {type} done
			 * @param {type} fail
			 * @returns {undefined}
			 */
			upload:function(url, data, progress, done, fail) {

				/*
				 * var file = inputFileBoxDom.files[0];
				 * var fileReader = new FileReader();
				 * fileReader.onload = function(event){
				 * 	co.rest.upload(url, this.result);
				 * };
				 *
				 * fileReader.readAsBinaryString(file);
				 *
				 */

				// force browser does not convert into UTF-8
				var arrayBuffer = new ArrayBuffer(data.length);
				var writer = new Uint8Array(arrayBuffer);
				for (var i = 0, len = data.length; i < len; i++) {
					writer[i] = data.charCodeAt(i);
				}

				// upload photo data.
				$.ajax({
					type:'PUT',
					url:url,
					//contentType: 'application/oct-stream', // overwrite the XML http request default header.
					//mimeType: 'application/oct-stream',
					processData: false, // !importance.
					data:arrayBuffer
				}).done(function(data, textStatus, jqXHR){
					if (done !== undefined){
						// it seems no jqXHR.responseJSON.
						//var object = methods.co.rest.parseResultObject(jqXHR);
						done(data);
					}
				}).fail(function(jqXHR, textStatus){
					if (fail !== undefined) {
						fail(jqXHR.status, methods.co.rest.parseResultObject(jqXHR));
					}
				});
			}
		}
	};

	// extend co object.
	var co = window.co;
	co.extend(co, methods);

}(this));
