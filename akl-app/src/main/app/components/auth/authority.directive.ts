angular.module('app')
.directive('hasAnyRole', Principal => {
    return {
        restrict: 'A',
        link: (scope, element, attrs: any) => {
            let setVisible = () => {
                    element.removeClass('hidden');
                },
                setHidden = () => {
                    element.addClass('hidden');
                },
                defineVisibility = reset => {
                    let result;
                    if (reset) {
                        setVisible();
                    }

                    result = Principal.isInAnyRole(roles);
                    if (result) {
                        setVisible();
                    } else {
                        setHidden();
                    }
                },
                roles = attrs.hasAnyRole.replace(/\s+/g, '').split(',');

            if (roles.length > 0) {
                defineVisibility(true);
            }
        }
    };
})
.directive('hasRole', Principal => {
    return {
        restrict: 'A',
        link: (scope, element, attrs: any) => {
            const
                setVisible = () => {
                    element.removeClass('hidden');
                },
                setHidden = () => {
                    element.addClass('hidden');
                },
                defineVisibility = reset => {

                    if (reset) {
                        setVisible();
                    }

                    Principal.isInRole(role)
                        .then(result => {
                            if (result) {
                                setVisible();
                            } else {
                                setHidden();
                            }
                        });
                },
                role = attrs.hasRole.replace(/\s+/g, '');

            if (role.length > 0) {
                defineVisibility(true);
            }
        }
    };
});
