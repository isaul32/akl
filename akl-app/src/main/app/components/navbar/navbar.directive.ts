angular.module('app')
.directive('activeMenu', ($translate, $locale) => {
    return {
        restrict: 'A',
        link: (scope, element, attrs: any) => {
            let language = attrs.activeMenu;

            scope.$watch(() => {
                return $translate.use();
            }, selectedLanguage => {
                if (language === selectedLanguage) {
                    element.addClass('active');
                } else {
                    element.removeClass('active');
                }
            });
        }
    };
})
.directive('activeLink', location => {
    return {
        restrict: 'A',
        link: (scope, element, attrs: any) => {
            let clazz = attrs.activeLink;
            let path = attrs.href;
            path = path.substring(1); // hack because path does bot return including hashbang
            scope.location = location;
            scope.$watch('location.path()', newPath => {
                if (path === newPath) {
                    element.addClass(clazz);
                } else {
                    element.removeClass(clazz);
                }
            });
        }
    };
});
