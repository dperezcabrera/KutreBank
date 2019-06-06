(function () {
    "use strict";

    var app = angular.module('App');

    app.component('login.component', {
        templateUrl: '/app/components/login/login.component.html',
        controllerAs: 'ctrl',
        controller: function (AuthService, $state, toastr, HOME_STATE) {
            var ctrl = this;
            ctrl.credentials = {};
            ctrl.state = 'login';
            ctrl.signupData = {};

            ctrl.$onInit = function () {
                ctrl.state = 'login';
                if (!AuthService.isAuthenticated()) {
                    ctrl.state = 'waiting';
                    AuthService.obtainProfile().then(function () {
                        ctrl.state = 'login';
                        $state.go(HOME_STATE);
                    }, function () {
                        ctrl.state = 'login';
                    });
                }
            };

            ctrl.login = function () {
                if (ctrl.credentials.login && ctrl.credentials.password) {
                    ctrl.state = 'waiting';
                    AuthService.login({username: ctrl.credentials.login, password: ctrl.credentials.password}).then(function () {
                        $state.go(HOME_STATE);
                    }, function () {
                        ctrl.state = 'login';
                        ctrl.credentials.password = "";
                        toastr.error("Usuario o contraseña no valido", "Error");
                    });
                }
            };

            ctrl.signUp = function () {
                if (!ctrl.signupData.password || ctrl.signupData.password !== ctrl.signupData.password2) {
                    ctrl.signupData.error = "Las contraseñas no son iguales";
                    toastr.error("Las contraseñas no son iguales", "Error");
                } else {
                    ctrl.state = 'waiting';
                    AuthService.signUp({username: ctrl.signupData.username, password: ctrl.signupData.password, code: ctrl.signupData.code}).then(function (response) {
                        ctrl.state = 'login';
                        toastr.info(response.data.description, "Información");
                    }, function (error) {
                        ctrl.signupData.error = error.data.description;
                        toastr.error(error.data.description, "Error");
                        ctrl.state = 'signup';
                    });
                }
            };
        }});
})();