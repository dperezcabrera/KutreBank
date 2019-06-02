(function () {
    "use strict";

    var app = angular.module('App');

    app.factory('httpRequestInterceptor', function ($q, $location, credentials) {
        return {
            'responseError': function (rejection) {
                if (rejection.data && rejection.status === 403) {
                    $location.path('/login/error');
                    credentials.logout();
                    return {};
                }
                return $q.reject(rejection);
            }
        };
    });
})();

