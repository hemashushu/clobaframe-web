/* 
 * page-box.js 
 */

var PageBox = React.createClass({
	getInitialState: function() {
		var options = $.extend({}, this.props.pageOptions);
		return {pages: [], options:options };
	},
	componentDidMount: function() {
		this.loadPages();
	},
	loadPages:function(){
		var me = this;
		$.ajax({
			url: me.props.url,
			dataType: 'json',
			cache: false
		}).done(function(data, textStatus, jqXHR){
			me.setState({pages:data});
		}).fail(function(jqXHR, textStatus, errorThrown){
			console.error(this.props.url, textStatus, errorThrown.toString());
		});
	},
	handlePageSubmit: function(page) {
		var pages = this.state.pages;
		var me = this;
		$.ajax({
			url: me.props.url,
			contentType: 'application/json; charset=UTF-8',
			dataType: 'json', // expect return data type
			type: 'POST',
			data: JSON.stringify(page)
		}).done(function(data, textStatus, jqXHR) {
			var newPages = pages.concat([data]);
			me.setState({pages: newPages});
			
		}).fail(function(jqXHR, textStatus, errorThrown){
			console.error(this.props.url, textStatus, errorThrown.toString());
		});
	},
	handleLanguageChange: function(url) {
		var me = this;
		$.getJSON(url).done(function(data, textStatus, jqXHR){
			if (data.locale) {
				me.loadPages();

				// update form
				var options = me.state.options;
				var newOptions = $.extend({}, options, {locale:data.locale});
				me.setState({options: newOptions});
			}else{
				console.error(data);
			}
		}).fail(function(jqXHR, textStatus, errorThrown){
			console.error(this.props.url, textStatus, errorThrown.toString());
		});
		
	},
	render: function(){
		return (
			<div className="page-box">
				<LanguageSwitch onLauguageChange={this.handleLanguageChange}/>
				<h1>Pages</h1>
				<PageList pages={this.state.pages}/>
				<PageForm options={this.state.options} onPageSubmit={this.handlePageSubmit}/>
			</div>
		);
	}
});

var LanguageSwitch = React.createClass({
	handleClick:function(e){
		e.preventDefault();
		var url = $(e.currentTarget).attr('href');
		this.props.onLauguageChange(url);
	},
	render: function() {
		return (
			<div className="text-right locale-switch">
				<h4>Change Language</h4>
				<ul className="list-inline">
					<li><a onClick={this.handleClick} href="/setlanguage?locale=en">English</a></li>
					<li><a onClick={this.handleClick} href="/setlanguage?locale=ja">Japanese</a></li>
					<li><a onClick={this.handleClick} href="/setlanguage?locale=zh_CN">Simplified Chinese</a></li>
				</ul>
			</div>	
		);
	}
	
});

var PageList = React.createClass({
	render: function() {
		var pageNodes = this.props.pages.map(function (page) {
			return (
				<Page page={page} key={page.pageKey.name + ',' + page.pageKey.locale}>
				</Page>
			);
		});
		
		return (
			<div className="page-list">
			{pageNodes}
			</div>
		);
	}
});

var Page = React.createClass({
	render: function() {
		var page = this.props.page;
		var pageUrl = co.objectUrl.page(page);
		var summary = page.content.left(200, true);

		return (
			<div className="page">
				<h3><a href={pageUrl}>{page.title}</a></h3>
				<div className="action">
					<ul className="list-inline">
						<li>
							<button className="btn btn-default edit">
								<i className="fa fa-edit"></i>
							</button>
						</li>
						<li>
							<button className="btn btn-default delete">
								<i className="fa fa-remove"></i>
							</button>
						</li>
					</ul>
				</div>
				<ul className="list-inline text-muted meta">
					<li>locale: <em>{page.pageKey.locale}</em></li>
					<li>revision: <em>{page.revision}</em></li>
				</ul>
				<div className="content">
					{summary}
				</div>
			</div>
		);
	}
});

var PageForm = React.createClass({
	handleSubmit: function(e){
		e.preventDefault();
		var name = React.findDOMNode(this.refs.name).value.trim();
		var title = React.findDOMNode(this.refs.title).value.trim();
		var urlName = React.findDOMNode(this.refs.urlName).value.trim();
		var content = React.findDOMNode(this.refs.content).value.trim();
		var locale = React.findDOMNode(this.refs.locale).value.trim();
		
		if (!name || !title || !content) {
		  return;
		}
		
		//send request to the server
		this.props.onPageSubmit({
			name: name, title: title, 
			urlname: urlName, content: content, 
			locale: locale});
		
		React.findDOMNode(this.refs.name).value = '';
		React.findDOMNode(this.refs.title).value = '';
		React.findDOMNode(this.refs.urlName).value = '';
		React.findDOMNode(this.refs.content).value = '';
		React.findDOMNode(this.refs.locale).value = '';
		return;
	},
	render: function() {
		return (
			<div className="page-form">
				<h2>Create new page</h2>
				<form onSubmit={this.handleSubmit}>
					<div className="form-group">
						<label htmlFor="txtName">Name</label>
						<input className="form-control" type="text" 
							id="txtName"
							placeholder="Only a-z,0-9 and '-' are allowed." 
							ref="name"/>
					</div>
					<div className="form-group">
						<label htmlFor="txtTitle">Title</label>
						<input className="form-control" type="text"
							id="txtTitle"
							ref="title"/>
					</div>
					<div className="form-group">
						<label htmlFor="txtUrlName">Url</label>
						<input className="form-control" type="text" 
							id="txtUrlName"
							placeholder="Optional. e.g. 'mypage', 'help/quickstart', only a-z,0-9,'-' and '/' are allowed." ref="urlName"/>
					</div>
					<div className="form-group">
						<label htmlFor="txtContent">Content</label>
						<textarea className="form-control" id="txtContent"
							rows="4"
							ref="content"></textarea>
					</div>
					<div className="form-group">
						<label htmlFor="txtLocale">Locale</label>
						<input className="form-control" type="text" 
							id="txtLocale"
							placeholder="The country and language code, e.g. en, ja, zh_CN"
							defaultValue={this.props.options.locale}
							ref="locale"/>
					</div>
					<button className="btn btn-info" type="submit">Post</button>
				</form>
			</div>
		);
	}
});

React.render(
  <PageBox url="rest/page" pageOptions={pageOptions}/>,
  document.getElementById('page-box')
);