(function () {
    "use strict";

    var app = angular.module('App');

    app.factory('httpRequestInterceptor', function ($q, $injector, LOGIN_STATE) {
        return {
            'responseError': function (rejection) {
                var $state = $injector.get('$state');
                if ($state.current.name !== 'login' && rejection.status === 403) {
                    var AuthService = $injector.get('AuthService');
                    AuthService.onError($state.current.name);
                    $state.go(LOGIN_STATE);
                }
                return $q.reject(rejection);
            }
        };
    });
})();

