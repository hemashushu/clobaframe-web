/* 
 * page-box.js 
 */

var PageBox = React.createClass({
	getInitialState: function() {
		return {pages: []};
	},
	componentDidMount: function() {
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
	render: function(){
		return (
			<div className="page-box">
				<h1>Pages</h1>
				<PageList pages={this.state.pages}/>
				<PageForm />
			</div>
		);
	}
});

var PageList = React.createClass({
	render: function() {
		var pageNodes = this.props.pages.map(function (page) {
			return (
				<Page page={page}>
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
		return (
			<div className="page">
				<h3>{this.props.page.title}</h3>
				<p className="text-muted">{this.props.page.revision}</p>
				<div className="content">
					{this.props.page.content}
				</div>
			</div>
		);
	}
});

var PageForm = React.createClass({
	handleSubmit: function(e){
		e.preventDefault();
		
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
							placeholder="Only a-z,0-9,- are allowed." 
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
							placeholder="Optional, only a-z,0-9,- are allowed." ref="urlName"/>
					</div>
					<div className="form-group">
						<label htmlFor="txtContent">Content</label>
						<textarea className="form-control" id="txtContent"
							ref="content"></textarea>
					</div>
					<button className="btn btn-info" type="submit">Post</button>
				</form>
			</div>
		);
	}
});

React.render(
  <PageBox url="rest/page" />,
  document.getElementById('page-box')
);