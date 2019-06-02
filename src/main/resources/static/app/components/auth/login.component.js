(function () {
    "use strict";

    var app = angular.module('App');

    app.controller('LoginCtrl', function (credentials, $routeParams, $location, Login) {
        var vm = this;
        vm.credentials = {};

        vm.init = function () {
            if ($routeParams.param === 'error' || $routeParams.param === 'logout') {
                credentials.user = {};
                vm.state = 'login';
            } else if (credentials.authenticatedUser()) {
                $location.path(credentials.fromView);
            } else {
                vm.state = 'waiting';
                credentials.user = {};

                Login.get().$promise.then(loginOk, function () {
                    vm.state = 'login';
                });
            }
        };

        vm.login = function () {
            if (vm.credentials.login && vm.credentials.password) {
                vm.state = 'waiting';
                Login.save({username: vm.credentials.login, password: vm.credentials.password}).$promise.then(loginOk, function () {
                    vm.state = 'login';
                    credentials.logout();
                    vm.credentials.password = "";
                    vm.credentials.error = "Usuario o contraseña no validos";
                });
            }
        };

        function loginOk(user) {
            vm.credentials.password = "";
            credentials.registerUser(user);
            var next = credentials.fromView;
            credentials.fromView = "/home";
            $location.path(next);
        }
        
        vm.init();
    });
})();