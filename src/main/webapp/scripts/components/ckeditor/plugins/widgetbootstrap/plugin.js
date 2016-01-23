// Init default alert classes

CKEDITOR.config.widgetbootstrapAlert_alertTypes = {
    'alert': 'Alert',
    'info': 'Info',
    'warning': 'Warning',
    'success': 'Success'
};


CKEDITOR.plugins.add( 'widgetbootstrap', {
    requires: 'widget',

    icons: 'widgetbootstrapLeftCol,widgetbootstrapRightCol,widgetbootstrapTwoCol,widgetbootstrapThreeCol,widgetbootstrapAlert',

    /*defaults : {
        name: 'accordion',
        count: 3,
        activePanel: 1,
        multiExpand: false
    },*/

    init: function( editor ) {

        // Configurable settings
        //var allowedWidget = editor.config.widgetbootstrap_allowedWidget != undefined ? editor.config.widgetbootstrap_allowedFull :
        //    'p h2 h3 h4 h5 h6 span br ul ol li strong em img[!src,alt,width,height]';
        var allowedFull = editor.config.widgetbootstrap_allowedFull != undefined ? editor.config.widgetbootstrap_allowedFull :
            'p a div span h2 h3 h4 h5 h6 section article iframe object embed strong b i em cite pre blockquote small sub sup code ul ol li dl dt dd table thead tbody th tr td img caption mediawrapper br[href,src,target,width,height,colspan,span,alt,name,title,class,id,data-options]{text-align,float,margin}(*);'
        //var allowedText = editor.config.widgetbootstrap_allowedText != undefined ? editor.config.widgetbootstrap_allowedFull :
        //    'p span br ul ol li strong em';


        allowedWidget = allowedFull;
        //allowedText = allowedWidget;

        var showButtons = editor.config.widgetbootstrapShowButtons != undefined ? editor.config.widgetbootstrapShowButtons : true;

        // Define the widgets
        editor.widgets.add( 'widgetbootstrapLeftCol', {

            button: showButtons ? 'Add left column box' : undefined,

            template:
                '<div class="row two-col-left">' +
                    '<div class="col-md-3 col-sidebar"><p>Content</p></div>' +
                    '<div class="col-md-9 col-main"><p>Content</p></div>' +
                '</div>',

            editables: {
                col1: {
                    selector: '.col-sidebar',
                    allowedContent: allowedWidget
                },
                col2: {
                    selector: '.col-main',
                    allowedContent: allowedWidget
                }
            },

            allowedContent: allowedFull,

            upcast: function( element ) {
                return element.name == 'div' && element.hasClass( 'two-col-right-left' );
            }

        } );

        editor.widgets.add( 'widgetbootstrapRightCol', {

            button: showButtons ? 'Add right column box' : undefined,

            template:
                '<div class="row two-col-right">' +
                    '<div class="col-md-9 col-main"><p>Content</p></div>' +
                    '<div class="col-md-3 col-sidebar"><p>Content</p></div>' +
                '</div>',

            editables: {
                col1: {
                    selector: '.col-sidebar',
                    allowedContent: allowedWidget
                },
                col2: {
                    selector: '.col-main',
                    allowedContent: allowedWidget
                }
            },

            allowedContent: allowedFull,

            upcast: function( element ) {
                return element.name == 'div' && element.hasClass( 'two-col-right' );
            }

        } );

        editor.widgets.add( 'widgetbootstrapTwoCol', {

            button: showButtons ? 'Add two column box' : undefined,

            template:
                '<div class="row two-col">' +
                    '<div class="col-md-6 col-1"><p>Content</p></div>' +
                    '<div class="col-md-6 col-2"><p>Content</p></div>' +
                '</div>',

            editables: {
                col1: {
                    selector: '.col-1',
                    allowedContent: allowedWidget
                },
                col2: {
                    selector: '.col-2',
                    allowedContent: allowedWidget
                }
            },

            allowedContent: allowedFull,

            upcast: function( element ) {
                return element.name == 'div' && element.hasClass( 'two-col' );
            }

        } );

        editor.widgets.add( 'widgetbootstrapThreeCol', {

            button: showButtons ? 'Add three column box' : undefined,

            template:
                '<div class="row three-col">' +
                    '<div class="col-md-4 col-1"><p>Content</p></div>' +
                    '<div class="col-md-4 col-2"><p>Content</p></div>' +
                    '<div class="col-md-4 col-3"><p>Content</p></div>' +
                '</div>',

            editables: {
                col1: {
                    selector: '.col-1',
                    allowedContent: allowedWidget
                },
                col2: {
                    selector: '.col-2',
                    allowedContent: allowedWidget
                },
                col3: {
                    selector: '.col-3',
                    allowedContent: allowedWidget
                }
            },

            allowedContent: allowedFull,

            upcast: function( element ) {
                return element.name == 'div' && element.hasClass( 'three-col' );
            }

        } );

        // Append the widget's styles when in the CKEditor edit page,
        // added for better user experience.
        // Assign or append the widget's styles depending on the existing setup.
        if (typeof editor.config.contentsCss == 'object') {
            editor.config.contentsCss.push(CKEDITOR.getUrl(this.path + 'contents.css'));
        }

        else {
            editor.config.contentsCss = [editor.config.contentsCss, CKEDITOR.getUrl(this.path + 'contents.css')];
        }

    }


} );
