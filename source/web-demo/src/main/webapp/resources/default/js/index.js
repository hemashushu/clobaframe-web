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
	
//	var methods = {
//		init:function(){
//			$('.test-i18n .action a').click(function(event){
//				event.preventDefault();
//				var url = $(this).attr('href');
//				$.getJSON(url, null, function(){
//					// refresh page
//					window.location.reload();
//				});
//			});
//			
//			// test script i18n message
//			var message = methods.getMessage('test.text1');
//			$('.test-script-i18n .text').text(message);
//			
//			// test view object
//			var viewObject = window.viewObject;
//			var viewObjectText = '{id:"' + viewObject.id + '", name:"' + viewObject.name + '"}';
//			$('.test-view-object .text').text(viewObjectText);
//			
//			// test upload
//			$('.test-rest input[type="submit"]').click(function(event){
//				event.preventDefault();
//				methods.createNote();
//			});
//			
//			methods.showNotes();
//		},
//		
//		getMessage:function(code){
//			var message = null;
//			
//			if (window.i18nMessageLocal){
//				message = window.i18nMessageLocal[code];
//			}
//			
//			if (!message && window.i18nMessageDefault){
//				message = window.i18nMessageDefault[code];
//			}
//			
//			return message;
//		},
//		
//		createNote:function(){
//			var inputFileBox = $('.test-rest input[type="file"]');
//			var inputFileBoxDom = inputFileBox.get(0);
//
//			if (inputFileBoxDom.files.length === 0) {
//				return;
//			}
//
//			var file = inputFileBoxDom.files[0];
//			var type = file.type; // file.name, file.size, file.lastModifiedDate
//			var fileReader = new FileReader();
//
//			// to slicing a file.
//			// see: http://www.html5rocks.com/en/tutorials/file/dndfiles/
//			//var blob = file.slice(startingByte, endindByte);
//			//reader.readAsBinaryString(blob);
//
//			var titleBox = $('.test-rest input[name="title"]');
//			var descriptionBox = $('.test-rest input[name="description"]');
//			
//			var success = function(data){
//				methods.showNote(data);
//				titleBox.val('');
//				descriptionBox.val('');
//				inputFileBox.val('');
//			};
//
//			var fail = function(status, data){
//				console.log(status);
//				console.log(data);
//			};
//
//			var title = titleBox.val();
//			var description = descriptionBox.val();
//			var url = '/note?title=' + encodeURIComponent(title) +
//					'&description=' + encodeURIComponent(description);
//
//			fileReader.onload = function(event){
//			   methods.postNote(url, this.result, type, success, fail);
//			};
//
//			fileReader.readAsBinaryString(file);
//		},
//		
//		postNote:function(url, data, type, success, fail, progress) {
//
//			/*
//			 * var file = inputFileBoxDom.files[0];
//			 * var fileReader = new FileReader();
//			 * fileReader.onload = function(event){
//			 * 	restHttp.upload(url, this.result);
//			 * };
//			 *
//			 * fileReader.readAsBinaryString(file);
//			 *
//			 */
//
//			// force browser does not convert into UTF-8
//			var arrayBuffer = new ArrayBuffer(data.length);
//			var writer = new Uint8Array(arrayBuffer);
//			for (var i = 0, len = data.length; i < len; i++) {
//				writer[i] = data.charCodeAt(i);
//			}
//
//			// upload photo data.
//			$.ajax(url, {
//				method:'POST',
//				//url:url,
//				contentType: type, // 'application/oct-stream', // overwrite the XML http request default header.
//				processData: false, // !importance.
//				data:arrayBuffer
//			}).done(function(data, textStatus, jqXHR){
//				if (success !== undefined){
//					success(data);
//				}
//			}).fail(function(jqXHR, textStatus){
//				if (fail !== undefined) {
//					fail(jqXHR.status, methods.parseResponseJson(jqXHR));
//				}
//			});
//		},
//		
//		parseResponseJson:function(jqXHR) {
//			var contentType = jqXHR.getResponseHeader('Content-Type');
//			if (contentType !== null && contentType.indexOf('application/json') >= 0){
//				if (jqXHR.responseJSON){
//					return jqXHR.responseJSON;
//				}else{
//					return $.parseJSON(jqXHR.responseText);
//				}
//			}else{
//				return null;
//			}
//		},
//		
//		showNotes:function(){
//			$.getJSON('/note', null, function(data){
//				$.each(data, function(idx, item){
//					methods.showNote(item);
//				});
//			});
//		},
//		
//		showNote:function(item){
//			var photoUrl = '/note/' + item.id + "/photo?data";
//			var itemEle = $('<li class="note"><a href="' + photoUrl + '"><img src="' + photoUrl + '"></a>' +
//					'<div class="title"></div>' +
//					'<div class="description"></div>' +
//					'<div class="action">' +
//					'<button class="edit">edit</button>' +
//					'<button class="delete">delete</button>' +
//					'</div></li>');
//			itemEle.attr('data-id', item.id);
//			itemEle.find('.title').text(item.title);
//			itemEle.find('.description').text(item.description);
//			
//			itemEle.find('button.delete').on('click', function(){
//				methods.deleteNote(item.id);
//			});
//			
//			itemEle.find('button.edit').on('click', function(){
//				methods.editNote(item.id);
//			});
//			
//			$('.test-rest .notes').prepend(itemEle);
//		},
//		
//		deleteNote:function(id){
//			$.ajax('/note/' + id, {
//				method:'DELETE'
//			}).done(function(){
//				$('.test-rest .notes .note[data-id="' + id + '"]').remove();
//			}).fail(function(jqXHR, textStatus){
//				//
//			});
//		},
//		
//		editNote:function(id){
//			var item = $('.test-rest .notes .note[data-id="' + id + '"]');
//			var editor = $('<div class="editor">' +
//					'<div><input type="text" name="new-title"></div>' +
//					'<div><input type="text" name="new-description"></div>' +
//					'<button class="update">update</button>' +
//					'<button class="cancel">cancel</button>' +
//					'</div>');
//			
//			$.getJSON('/note/' + id, null, function(data){
//				editor.find('input[name="new-title"]').val(data.title);
//				editor.find('input[name="new-description"]').val(data.description);
//				
//				methods.toggleEditor(item, true);
//				item.append(editor);
//				
//				item.find('button.cancel').on('click', function(){
//					editor.remove();
//					methods.toggleEditor(item, false);
//				});
//				
//				item.find('button.update').on('click', function(){
//					methods.postUpdate(item);
//				});
//			});
//		},
//		
//		postUpdate:function(item) {
//			var editor = item.find('.editor');
//			var title = editor.find('input[name="new-title"]').val();
//			var description = editor.find('input[name="new-description"]').val();
//			
//			var url = '/note/' + item.data('id');
//			
//			$.ajax(url, {
//				method:'PUT',
//				// the application/x-www-form-urlencoded way
//				//data: {title:title, description:description}
//				
//				// the JSON way
//				contentType: 'application/json; charset=utf-8',
//				data:JSON.stringify({title:title, description:description})
//			}).done(function(data, textStatus, jqXHR){
//				item.find('.title').text(data.title);
//				item.find('.description').text(data.description);
//				methods.toggleEditor(item, false);
//				editor.remove();
//			}).fail(function(jqXHR, textStatus){
//				console.log(jqXHR.status);
//				console.log(methods.parseResponseJson(jqXHR));
//			});
//			
//		},
//		
//		toggleEditor:function(item, editMode){
//			if (editMode) {
//				item.find('.title').hide();
//				item.find('.description').hide();
//				item.find('.action').hide();
//			}else{
//				item.find('.title').show();
//				item.find('.description').show();
//				item.find('.action').show();
//			}
//		}
//	};
	
})(this);

