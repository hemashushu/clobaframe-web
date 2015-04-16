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
			
			// test upload
			$('.test-upload input[type="submit"]').click(function(event){
				event.preventDefault();
				
				var inputFileBox = $('.test-upload input[type="file"]');
				var inputFileBoxDom = inputFileBox.get(0);
				
				if (inputFileBoxDom.files.length === 0) {
					return;
				}
				
				var file = inputFileBoxDom.files[0];
				var type = file.type; // file.name, file.size, file.lastModifiedDate
				var fileReader = new FileReader();
				
				// to slicing a file.
				// see: http://www.html5rocks.com/en/tutorials/file/dndfiles/
				//var blob = file.slice(startingByte, endindByte);
				//reader.readAsBinaryString(blob);
				
				var success = function(data){
					methods.showUpload(data);
				};
				
				var fail = function(status, data){
					console.log(status);
					console.log(data);
				};
				
				var title = $('.test-upload input[name="title"]').val();
				var description = $('.test-upload input[name="description"]').val();
				var url = '/upload?title=' + encodeURIComponent(title) +
						'&description=' + encodeURIComponent(description);
				
				fileReader.onload = function(event){
				   methods.upload(url, this.result, type, success, fail);
				};

				fileReader.readAsBinaryString(file);

			});
			
			methods.showUploads();
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
		},
		
		upload:function(url, data, type, success, fail, progress) {

			/*
			 * var file = inputFileBoxDom.files[0];
			 * var fileReader = new FileReader();
			 * fileReader.onload = function(event){
			 * 	restHttp.upload(url, this.result);
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
			$.ajax(url, {
				method:'POST',
				//url:url,
				contentType: type, // 'application/oct-stream', // overwrite the XML http request default header.
				processData: false, // !importance.
				data:arrayBuffer
			}).done(function(data, textStatus, jqXHR){
				if (success !== undefined){
					success(data);
				}
			}).fail(function(jqXHR, textStatus){
				if (fail !== undefined) {
					fail(jqXHR.status, methods.parseResponseJson(jqXHR));
				}
			});
		},
		
		parseResponseJson:function(jqXHR) {
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
		
		deleteUpload:function(id){
			$.ajax('/upload/' + id, {
				method:'DELETE'
			}).done(function(){
				$('.test-upload .photos .photo[data-id="' + id + '"]').remove();
			}).fail(function(jqXHR, textStatus){
				//
			});
		},
		
		showUploads:function(){
			$.getJSON('/upload', null, function(data){
				$.each(data, function(idx, item){
					methods.showUpload(item);
				});
			});
		},
		
		showUpload:function(item){
			var itemEle = $('<li class="photo"><img>' +
					'<strong class="title"></strong>' +
					'<em class="description"></em>' +
					'<button class="edit">edit</button>' +
					'<button class="delete">delete</button></li>');
			itemEle.attr('data-id', item.id);
			itemEle.find('img').attr('src', '/upload/' + item.id + "?data");
			itemEle.find('img').width(320);
			itemEle.find('strong').text(item.title);
			itemEle.find('em').text(item.description);
			itemEle.find('button.delete').on('click', function(){
				methods.deleteUpload(item.id);
			});
			$('.test-upload .photos').prepend(itemEle);
		}
	};
	
	$(function(){
		methods.init();
	});
	
})(this);

