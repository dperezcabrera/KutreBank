(function () {
    "use strict";

    var app = angular.module('App');

    function hasAnyRole(credentials, $location, roles) {
        if (credentials.authenticatedUser()) {
            if (credentials.hasAnyRole(roles)) {
                return true;
            } else {
                $location.path("/home");
            }
        } else {
            credentials.fromView = $location.path();
            $location.path("/login");
        }
        return false;
    }

    var isAuthenticated = function (credentials, $location) {
        if (credentials.authenticatedUser()) {
            return true;
        } else {
            credentials.fromView = $location.path();
            $location.path("/login");
        }
        return false;
    };

    var isAdmin = function (credentials, $location) {
        return hasAnyRole(credentials, $location, ["Admin"]);
    };

    app.config(function ($routeProvider) {
        $routeProvider.when('/home', {
            templateUrl: '/app/components/home/home.component.html',
            controller: 'HomeCtrl',
            controllerAs: 'vm',
            resolve: {factory: isAuthenticated}
        }).when('/admin', {
            templateUrl: '/app/components/home/home.component.html',
            controller: 'HomeCtrl',
            controllerAs: 'vm',
            resolve: {factory: isAdmin}
        }).when('/login', {
            templateUrl: '/app/components/auth/login.component.html',
            controller: 'LoginCtrl',
            controllerAs: 'vm'
        }).otherwise({
            redirectTo: '/home'
        });
    });
})();

