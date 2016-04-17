CKEDITOR.plugins.add( 'inlinesave',
{
	init: function( editor )
	{
		editor.addCommand( 'inlinesave',
			{
				exec : function( editor )
				{
					var config = editor.config.inlinesave, 
					    postData = {};
					
					if (typeof config == "undefined") { // Give useful error message if user doesn't define config.inlinesave
						throw new Error("CKEditor inlinesave: You must define config.inlinesave in your configuration file. See http://ckeditor.com/addon/inlinesave");
					}

					if (typeof config.onSave == "function") {
						config.onSave(editor);				// Allow showing "loading" spinner
					}
					
					// Clone postData object from config and add editabledata and editorID properties
					CKEDITOR.tools.extend(postData, config.postData || {}, true);  // Clone config.postData to prevent changing the config.
					postData.editabledata = editor.getData();
					postData.editorID = editor.container.getId();
				}
			});
		editor.ui.addButton( 'Inlinesave',
		{
			label: 'Save',
			command: 'inlinesave',
			icon: this.path + 'images/inlinesave.png'
		} );
	}
} );
